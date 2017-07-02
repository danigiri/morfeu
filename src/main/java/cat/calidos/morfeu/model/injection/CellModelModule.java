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

private static final String DEFAULT_TYPE_POSTFIX = "-type";
protected final static Logger log = LoggerFactory.getLogger(CellModelModule.class);


@Provides
public static CellModel buildCellModelFrom(URI u, XSElementDecl elem, Type t) {

	return new CellModel(u, elem.getName(), t);

}


@Provides
public static ComplexCellModel buildComplexCellModelFrom(URI u, 
														 XSElementDecl elem, 
														 Type t,  
														 Attributes<CellModel> attributes, 
														 Composite<CellModel> children) {
	
	
	return new ComplexCellModel(u, elem.getName(), t, attributes, children);
	
}


@Provides
public static Type getTypeFrom(XSElementDecl elem) {
	
	return DaggerTypeComponent.builder()
								.withDefaultName(elem.getName())
								.withXSType(elem.getType())
								.build()
								.type();
			
}


@Provides
public static Attributes<CellModel> attributesOf(XSElementDecl elem, 
												  Type t,
												  Lazy<Collection<? extends XSAttributeUse>> attributesProducer) {

	if (t.isSimple()) {
		return new OrderedMap<CellModel>(0);
	}
	Collection<? extends XSAttributeUse> rawAttributes = attributesProducer.get();
	return attributesFrom(rawAttributes);

}


@Provides
public static Collection<? extends XSAttributeUse> rawAttributes(XSElementDecl elem) {

	XSComplexType complexType = elem.getType().asComplexType();
	
	return complexType.getAttributeUses();
	
}


@Provides
public static Composite<CellModel> childrenOf(XSElementDecl elem, Type t) {
	
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
	LinkedList<XSTerm> termTypes = new LinkedList<XSTerm>();
	termTypes.add(termType);
	while (!termTypes.isEmpty()) {
		termType = termTypes.removeFirst();
		
		if (termType.isModelGroup()) {
			XSModelGroup typeModelGroup = termType.asModelGroup();
			typeModelGroup.iterator().forEachRemaining(m -> termTypes.addFirst(m.getTerm()));
			System.err.print("\t["+typeModelGroup.getSize()+"]");
		} else {
			XSElementDecl child = termType.asElementDecl();
			CellModel childCellModel = cellModelFrom(child);
			children.addChild(childCellModel.getName(), childCellModel);
		}
	
	}
	System.err.println();

	return children;
	
}


@Provides
public static URI getDefaultURI(XSElementDecl elem) throws RuntimeException {
	return getDefaultURIFrom(elem.getLocator(), elem.getName());
}


@Provides @Named("TypeDefaultName")
public static String getDefaultTypeName(XSElementDecl elem) {
	return elem.getName()+DEFAULT_TYPE_POSTFIX;
}


private static CellModel cellModelFrom(XSElementDecl elem) {
	
	Type t = getTypeFrom(elem);
	URI u = getDefaultURI(elem);
	if(t.isSimple()) {

		return buildCellModelFrom(u, elem, t);

	} else {
		Attributes<CellModel> attributes = attributesFrom(elem);
		Composite<CellModel> children = childrenOf(elem, t);
		
		return buildComplexCellModelFrom(u, elem, t, attributes, children);
		
	}
	
}


private static CellModel cellModelFrom(XSAttributeDecl xsAttributeDecl) {

	String name = xsAttributeDecl.getName();
	URI uri = getDefaultURIFrom(xsAttributeDecl.getLocator(), name);
	
	Type type = DaggerTypeComponent.builder()
									.withDefaultName(name)
									.withXSType(xsAttributeDecl.getType())
									.build()
									.type();
		
	return new CellModel(uri, name, type);

}


private static Attributes<CellModel> attributesFrom(Collection<? extends XSAttributeUse> rawAttributes) {

	Attributes<CellModel> attributes = new OrderedMap<CellModel>(rawAttributes.size());

	rawAttributes.forEach(a -> {
								XSAttributeDecl attributeDecl = a.getDecl();
								CellModel cellModel = cellModelFrom(attributeDecl);
								attributes.addAttribute(attributeDecl.getName(), cellModel);
	});

	return attributes;
}


private static Attributes<CellModel> attributesFrom(XSElementDecl elem) {
	
	Collection<? extends XSAttributeUse> rawAttributes = rawAttributes(elem);

	return attributesFrom(rawAttributes);

}


private static URI getDefaultURIFrom(Locator locator, String name) throws RuntimeException {
	//TODO: create the correct uri with an aditional parameter
	try {
		
		return new URI(locator.getSystemId());
		
	} catch (URISyntaxException e) {
		log.error("What the heck, URI '{}' of element '{}' is not valid ", locator.getSystemId(), name);
		throw new RuntimeException("Somehow we failed to create URI of element "+name, e);
	}

}


}
