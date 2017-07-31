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
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Locator;

import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSType;

import cat.calidos.morfeu.model.Attributes;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Type;
import cat.calidos.morfeu.utils.OrderedMap;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dagger.producers.Producer;


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
										 @Named("SimpleInstance") Provider<CellModel> provCellModule,
										 @Named("ComplexInstance") Provider<ComplexCellModel> provComplexCellModule) {

	if(t.isSimple()) {
		
		return provCellModule.get();
	
	} else {

		return provComplexCellModule.get();
		
	}
	
}


@Provides @Named("SimpleInstance")
public static CellModel buildCellModelFrom(XSElementDecl elem, @Named("name") String name, Type t, URI u) {
	// TODO: add cell description from metadata
	return new CellModel(u, name, "DESC GOES HERE", t);

}


@Provides @Named("ComplexInstance")
public static ComplexCellModel buildComplexCellModelFrom(XSElementDecl elem,
														 @Named("name") String name,
														 Type t,  
														 Attributes<CellModel> attributes, 
														 Composite<CellModel> children,
														 URI u) {
		
	return new ComplexCellModel(u, name, "DESC GOES HERE", t, attributes, children);
	
}





@Provides
public static Type getTypeFrom(XSElementDecl elem, @Named("TypeDefaultName") String defaultName) {
	
	return DaggerTypeComponent.builder()
								.withDefaultName(defaultName)
								.withXSType(elem.getType())
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


@Provides
public static Composite<CellModel> childrenOf(XSElementDecl elem, Type t, URI u) {
	
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
	
	System.err.println("TYPE:"+t);
	XSTerm termType = contentType.asParticle().getTerm();			// recursive case, go through all children
	LinkedList<XSTerm> termTypes = new LinkedList<XSTerm>();		// this is a list of all the terms left to process
	termTypes.add(termType);
	while (!termTypes.isEmpty()) {
		termType = termTypes.removeFirst();
		
		if (termType.isModelGroup()) {
			XSModelGroup typeModelGroup = termType.asModelGroup();
			typeModelGroup.iterator().forEachRemaining(m -> termTypes.addFirst(m.getTerm()));
			System.err.print("\t["+typeModelGroup.getSize()+"]");
		} else {
			XSElementDecl child = termType.asElementDecl();
			CellModel childCellModel = DaggerCellModelComponent.builder()
										.withElement(child)
										.withParentURI(u)
										.build()
										.cellModel();
			children.addChild(childCellModel.getName(), childCellModel);
		}
	
	}
	System.err.println();

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



private static CellModel cellModelFrom(XSAttributeDecl xsAttributeDecl, URI nodeURI) {

	String name = xsAttributeDecl.getName();
	URI attributeURI = getURIFrom(nodeURI.toString()+ATTRIBUTE_SEPARATOR+name, name);
	
	Type type = DaggerTypeComponent.builder()
									.withDefaultName(name)
									.withXSType(xsAttributeDecl.getType())
									.build()
									.type();
		
	return new CellModel(attributeURI, name, "DESC GOES HERE", type);

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
