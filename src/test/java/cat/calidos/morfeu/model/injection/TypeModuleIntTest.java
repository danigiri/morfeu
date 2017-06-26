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

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.xml.xsom.ForeignAttributes;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSType;

import cat.calidos.morfeu.model.Type;



/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TypeModuleIntTest extends ModelTezt {

private XSSchemaSet schemaSet;


@Before
public void setup() throws Exception {

	URI modelURI = new URI("target/test-classes/test-resources/models/test-model.xsd");
	schemaSet = parseSchemaFrom(modelURI);
	
}

@Test
public void testRootAnonymousType() throws Exception {

	XSElementDecl elementDecl = schemaSet.getElementDecl(MODEL_NAMESPACE, "test");
	
	XSType xsType = elementDecl.getType();
	Type type = TypeModule.buildType("cell-model-name", xsType);
	assertEquals("cell-model-name", type.getName());
	assertFalse(type.isSimple());
		
	
//	attributeUses.iterator().forEachRemaining(au -> System.err.println(au.getDecl().getName()));
//	complexType.getSubtypes().iterator().forEachRemaining(e->System.err.println(e.getName()));
//	Collection<XSComponent> select = complexType.select("/test/*", new NamespaceContext() {
//	
//	@Override
//	public Iterator getPrefixes(String namespaceURI) {
//		
//		// TODO Auto-generated method stub
//		 ArrayList<String> arrayList = new ArrayList<String>(1);
//		 arrayList.add("");
//		return arrayList.iterator();
//	}
//	
//	
//	@Override
//	public String getPrefix(String namespaceURI) {
//		
//		// TODO Auto-generated method stub
//		return "";
//	}
//	
//	
//	@Override
//	public String getNamespaceURI(String prefix) {
//		
//		// TODO Auto-generated method stub
//		return "";
//	}
//	});
//	select.iterator().forEachRemaining(e->System.err.println(e.toString()));
	
//	complexType.getElementDecls().stream().forEach(e->System.err.println(e.getName()));
//	schemaSet.iterateElementDecls().forEachRemaining(e -> e.);
//	schemaSet.iterateModelGroupDecls().forEachRemaining(gd -> System.err.println(gd.getName()));
	
}


@Test
public void testTextSimpleType() {
	
	XSType xsType = schemaSet.getType(MODEL_NAMESPACE, "textField");
	Type type = TypeModule.buildType("not used default", xsType);
	assertEquals("textField", type.getName());
	assertTrue(type.isSimple());
	assertTrue(type.isContentValid("random string"));
	assertFalse(type.isContentValid(new Object()));
	
//	XSRestrictionSimpleType restriction = xsType.asSimpleType().asRestriction();
//	XSType baseType = xsType.getBaseType();

}


@Test
public void testIntegerSimpleType() {
	
	XSType xsType = schemaSet.getType(MODEL_NAMESPACE, "numberField");
	Type type = TypeModule.buildType("not used default", xsType);
	assertEquals("numberField", type.getName());
	assertTrue(type.isSimple());
	
	validateInteger(type);

}


@Test
public void testColFieldSimpleType() {
	
	XSType xsType = schemaSet.getType(MODEL_NAMESPACE, "colField");
	Type type = TypeModule.buildType("not used", xsType);
	assertEquals("colField", type.getName());
	assertTrue(type.isSimple());
		
	// notice that we are not writing a full validation as XML Schema will do this for us
	validateInteger(type);
	
}


private void validateInteger(Type type) {

	assertFalse(type.isContentValid("random string"));
	assertFalse(type.isContentValid(new Object()));
	assertFalse(type.isContentValid(3.1416));
	assertFalse(type.isContentValid("3.1416"));
	assertTrue(type.isContentValid("0"));
	assertTrue(type.isContentValid("12"));
	assertTrue(type.isContentValid("-7"));
	assertTrue(type.isContentValid(0));
	assertTrue(type.isContentValid(11));
	assertTrue(type.isContentValid(-2));
	
}

}
