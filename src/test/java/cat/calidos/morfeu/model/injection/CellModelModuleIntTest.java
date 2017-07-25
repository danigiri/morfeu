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

import static org.mockito.Mockito.doReturn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import org.apache.xalan.xsltc.compiler.util.AttributeSetMethodGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchemaSet;

import cat.calidos.morfeu.model.Attributes;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Type;
import dagger.Lazy;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellModelModuleIntTest extends ModelTezt {

@Mock Lazy<Collection<? extends XSAttributeUse>> mockAttributesProducer;

private URI modelURI;
private XSSchemaSet schemaSet;


@Before
public void setup() throws Exception {

	modelURI = new URI("target/test-classes/test-resources/models/test-model.xsd");
	schemaSet = parseSchemaFrom(modelURI);
	
}


@Test
public void testProvideCellModel() throws Exception {
	
	CellModel cellModel = cellModelFrom(modelURI, "test");
	assertNotNull(cellModel);
	assertEquals("test", cellModel.getName());
	assertEquals("test-type", cellModel.getType().getName()); // default name for inner types is <elem>-type
		
}


@Test
public void testAttributesOf() {
	
	XSElementDecl elem = schemaSet.getElementDecl(MODEL_NAMESPACE, "test");
//	Map<String, XSElementDecl> elementDecls = schemaSet.getSchema(MODEL_NAMESPACE).getElementDecls();
	Type type = provideElementType(elem);
	Collection<? extends XSAttributeUse> rawAttributes = CellModelModule.rawAttributes(elem);
	doReturn(rawAttributes).when(mockAttributesProducer).get();

	Attributes<CellModel> attributes = CellModelModule.attributesOf(elem, type, mockAttributesProducer);
	assertNotNull(attributes);
	assertEquals(1, attributes.size());
	CellModel attribute = attributes.attribute(0);
	assertNotNull(attribute);
	assertEquals("text", attribute.getName());
	assertEquals(attribute, attributes.attribute("text"));
	
	Type attributeType = attribute.getType();
	assertNotNull(attributeType);
	assertTrue(attributeType.isSimple());
	assertEquals("textField", attributeType.getName());
		
}


@Test
public void testChildrenOf() {

	XSElementDecl elem = schemaSet.getElementDecl(MODEL_NAMESPACE, "test");
	Type type = provideElementType(elem);
	
	Composite<CellModel> children  = CellModelModule.childrenOf(elem, type);
	CellModel row = children.child("row");
	assertEquals("row", row.getName());

	// CONTINUE HERE HERE HERE TEST THAT THE STRUCTURE HAS BEEN CREATED RIGHT
}


@Test
public void testGetDefaultTypeName() throws Exception {
	
	XSElementDecl elem = schemaSet.getElementDecl(MODEL_NAMESPACE, "test");
	assertEquals("test-type", CellModelModule.getDefaultTypeName(elem));

}


}
