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
import java.util.Collection;
import java.util.List;

import javax.inject.Named;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Locator;

import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
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
public static  Attributes<CellModel> attributesOf(XSElementDecl elem, 
												  Type t,
												  Lazy<Collection<? extends XSAttributeUse>> attributesProducer) {

	if (t.isSimple()) {
		return new OrderedMap<CellModel>(0);
	}
	Collection<? extends XSAttributeUse> rawAttributes = attributesProducer.get();
	Attributes<CellModel> attributes = new OrderedMap<CellModel>(rawAttributes.size());

	rawAttributes.forEach(a -> {
								XSAttributeDecl attributeDecl = a.getDecl();
								attributes.addAttribute(attributeDecl.getName(), cellModelFrom(attributeDecl));
	});

	return attributes;

}


@Provides
public static Collection<? extends XSAttributeUse> rawAttributes(XSElementDecl elem) {

	XSComplexType complexType = elem.getType().asComplexType();
	
	return complexType.getAttributeUses();
	
}


@Provides
public static Composite<CellModel> childrenOf(Type t) {
	
	return null;
}



@Provides
public static URI getDefaultURI(XSElementDecl elem) throws RuntimeException {
	
	Locator locator = elem.getLocator();
	try {
	
		return new URI(locator.getSystemId());
		
	} catch (URISyntaxException e) {
		log.error("What the heck, URI '{}' of element '{}' is not valid ", locator.getSystemId(), elem.getName());
		throw new RuntimeException("Somehow we failed to create URI of element "+elem.getName(), e);
	}

}


@Provides @Named("TypeDefaultName")
public static String getDefaultTypeName(XSElementDecl elem) {
	return elem.getName()+DEFAULT_TYPE_POSTFIX;
}


public static List<CellModel> attributes(XSElementDecl elem) {
	return null;
}


private static CellModel cellModelFrom(XSAttributeDecl xsAttributeDecl) {
	CONTINUE HERE
	// TODO Auto-generated method stub
	return null;
}


}
