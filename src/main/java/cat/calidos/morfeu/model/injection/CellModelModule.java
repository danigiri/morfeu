/*
 *    Copyright 2017 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package cat.calidos.morfeu.model.injection;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.Locator;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSType;

import cat.calidos.morfeu.model.Attributes;
import cat.calidos.morfeu.model.BasicCellModel;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.CellModelWeakReference;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Type;
import cat.calidos.morfeu.utils.OrderedMap;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dagger.producers.Producer;
import dagger.producers.Produces;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class CellModelModule {

private static final String NODE_SEPARATOR = "/";
private static final String ATTRIBUTE_SEPARATOR = "@";
private static final String DEFAULT_TYPE_POSTFIX = "-type";
protected final static Logger log = LoggerFactory.getLogger(CellModelModule.class);


@Provides
public static CellModel provideCellModel(Type t,
										 @Named("Processed") Set<Type> processed,
										 Provider<BasicCellModel> providerCell,
										 Provider<ComplexCellModel> providerComplexCell,
										 Provider<CellModelWeakReference> providerRefCell) {

	CellModel cellModel;
	if (processed.stream().anyMatch(pt -> pt.getName().equals(t.getName()))) {
		cellModel = providerRefCell.get();
	} else {
		if (t.isGlobal()) {
			processed.add(t);	// in essence, our recursive algorithm is post-processing, once we do the recursive
		}						// calls to get cell model children, they will know this global type is done
		if (t.isSimple()) {
			cellModel = providerCell.get();
		} else {
			cellModel = providerComplexCell.get();
		}
	}
	
	return cellModel;
}


@Provides
public static BasicCellModel buildCellModelFrom(@Named("name") String name, 
										   @Named("desc") String desc,
										   Type t, 
										   @Named("presentation") String presentation,
										   URI u) {
	// TODO: add cell description from metadata
	return new BasicCellModel(u, name, desc, t, presentation);

}


@Provides
public static ComplexCellModel buildComplexCellModelFrom(@Named("name") String name,
														 @Named("desc") String desc, 
														 Type t,
														 @Named("presentation") String presentation,
														 Attributes<CellModel> attributes, 
														 Composite<CellModel> children,
														 URI u) {
		
	return new ComplexCellModel(u, name, desc, t, presentation, attributes, children);
	
}


@Provides
public static CellModelWeakReference buildReferenceCellModelFrom(@Named("name") String name, 
															     @Named("desc") String desc,
															     Type t, 
															     @Named("presentation") String presentation,
															     URI u) {
	return new CellModelWeakReference(u, name, desc, t, presentation);
}


@Provides @Named("desc")
public static String descriptionFromSchemaAnnotation(XSElementDecl elem, XSType type) {

	// note that we prioritise the element annotation if any, if not we default to the XSType one
	// TODO: move to getter internal logic? (to keep coherence with presentation field)
	String desc = "";
	XSAnnotation annotation = (elem.getAnnotation()!=null) ? elem.getAnnotation() : type.getAnnotation();
	desc = DaggerModelMetadataComponent.builder().from(annotation).named("mf:desc").build().value();
	
	return desc;
}


@Provides
public static Type getTypeFrom(XSType type, @Named("TypeDefaultName") String defaultName) {
	
	return DaggerTypeComponent.builder()
								.withDefaultName(defaultName)
								.withXSType(type)
								.build()
								.type();
			
}


@Provides
public static Attributes<CellModel> attributesOf(XSElementDecl elem, 
												  Type t,
												  Lazy<Collection<? extends XSAttributeUse>> attributesProducer,
												  URI u) {

	if (t.isSimple()) {
		return new OrderedMap<CellModel>(0);
	}
	Collection<? extends XSAttributeUse> rawAttributes = attributesProducer.get();
	return attributesFrom(rawAttributes, u);

}


@Provides
public static Collection<? extends XSAttributeUse> rawAttributes(XSElementDecl elem) {

	XSComplexType complexType = elem.getType().asComplexType();
	
	return complexType.getAttributeUses();
	
}

@Provides @Named("Processed")
public static Set<Type> processed(@Nullable Set<Type> processed) {

	if (processed==null) {
		return new HashSet<Type>();
	} else {
		return processed;
	} 

}



@Provides
public static Composite<CellModel> childrenOf(XSElementDecl elem, Type t, URI u, @Named("Processed") Set<Type> processed) {
	
	// Magic happens here: 
	// BASE CASES:
	//	if we are a simple type we are at a leaf
	//	if we are an empty type we do not have children
	// RECURSIVE CASE:
	//	we go through all the children and we add them
	if (t.isSimple()) {
		return new OrderedMap<CellModel>(0);						// base case, simple type sanity check
	}

	Composite<CellModel> children = new OrderedMap<CellModel>();
	
	XSComplexType complexType = elem.getType().asComplexType();
	XSContentType contentType = complexType.getContentType();
	if (contentType.asEmpty()!=null) {
		return new OrderedMap<CellModel>(0);						// base case, no children, we return
	}
	
	//System.err.println("TYPE:"+t);
	XSTerm termType = contentType.asParticle().getTerm();			// recursive case, go through all children
	LinkedList<XSTerm> termTypes = new LinkedList<XSTerm>();		// this is a list of all the terms left to process
	termTypes.add(termType);
	while (!termTypes.isEmpty()) {
		termType = termTypes.removeFirst();
		
		if (termType.isModelGroup()) {
			XSModelGroup typeModelGroup = termType.asModelGroup();
			typeModelGroup.iterator().forEachRemaining(m -> termTypes.addFirst(m.getTerm())); //FIXME: this is reversed!!!
			//System.err.print("\t["+typeModelGroup.getSize()+"]");
		} else {
			
			XSElementDecl child = termType.asElementDecl();
			CellModel childCellModel = DaggerCellModelComponent.builder()
												.withElement(child)
												.withParentURI(u)
												.havingProcessed(processed)
												.build()
												.cellModel();
				children.addChild(childCellModel.getName(), childCellModel);
			}
	}
	
	return children;
	
}


@Provides
public Locator locatorFrom(XSElementDecl elem) {
	return elem.getLocator();
}


@Provides @Named("name")
public String nameFrom(XSElementDecl elem) {
	return elem.getName();
}

@Provides @Named("URIString")
public String getURIString(@Named("ParentURI") URI parentURI, @Named("name") String name) {
	return parentURI+NODE_SEPARATOR+name;
}


@Provides
public static URI getURIFrom(@Named("URIString") String uri, @Named("name") String name) throws RuntimeException {
	
	try {
		
		return new URI(uri);
		
	} catch (URISyntaxException e) {
		log.error("What the heck, URI '{}' of element '{}' is not valid ", uri, name);
		throw new RuntimeException("Somehow we failed to create URI of element "+name, e);
	}
	
}


@Provides @Named("TypeDefaultName")
public static String getDefaultTypeName(XSElementDecl elem) {
	return elem.getName()+DEFAULT_TYPE_POSTFIX;
}


@Provides
public static XSType type(XSElementDecl elem) {
	return elem.getType();
}


@Provides @Named("presentation")
public static String presentation(XSElementDecl elem) {
		return  DaggerModelMetadataComponent.builder()
			.from(elem.getAnnotation())
			.named(ModelMetadataComponent.PRESENTATION_FIELD)
			.build()
			.value();
}

private static CellModel cellModelFrom(XSAttributeDecl xsAttributeDecl, URI nodeURI) {

	String name = xsAttributeDecl.getName();
	URI attributeURI = getURIFrom(nodeURI.toString()+ATTRIBUTE_SEPARATOR+name, name);
	
	Type type = DaggerTypeComponent.builder()
									.withDefaultName(name)
									.withXSType(xsAttributeDecl.getType())
									.build()
									.type();
		
	// attributes have the presentation of the corresponding type
	return new BasicCellModel(attributeURI, name, "DESC GOES HERE", type, ModelMetadataComponent.UNDEFINED_VALUE);

}


private static Attributes<CellModel> attributesFrom(Collection<? extends XSAttributeUse> rawAttributes, 
													URI nodeURI) {

	Attributes<CellModel> attributes = new OrderedMap<CellModel>(rawAttributes.size());

	rawAttributes.forEach(a -> {
								XSAttributeDecl attributeDecl = a.getDecl();
								CellModel cellModel = cellModelFrom(attributeDecl, nodeURI);
								attributes.addAttribute(attributeDecl.getName(), cellModel);
	});

	return attributes;
}



//private static URI getDefaultURIFrom(Locator locator, @Nullable String uriPrefix, String name) throws RuntimeException {
//
//
//
//
//}


}
