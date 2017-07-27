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
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Model;
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
	
	CellModel test = cellModelFrom(modelURI, "test");							// TEST
	checkComplexCellModel(test, "test", "test-type");	// default name for anonymous types is <elem>-type
	
	ComplexCellModel testComplex = test.asComplex();
	assertNotNull(testComplex);
	assertEquals(1, testComplex.attributes().size());
	assertEquals(1, testComplex.children().size());
	
	CellModel textAttribute = testComplex.attributes().attribute("text");		// TEST . TEXT
	checkAttribute(textAttribute, "text", "textField");
	
	CellModel row = testComplex.children().child("row");						// TEST -> ROW
	checkComplexCellModel(row, "row", "rowCell");	// not anonymous type

	ComplexCellModel rowComplex = row.asComplex();
	assertNotNull(rowComplex);
	assertEquals(1, rowComplex.attributes().size());
	assertEquals(1, rowComplex.children().size());
	
	CellModel numberAttribute = rowComplex.attributes().attribute("number");	// TEST -> ROW . NUMBER
	assertNotNull(numberAttribute);
	assertEquals("number", numberAttribute.getName());
	assertEquals("numberField", numberAttribute.getType().getName());
	assertTrue(numberAttribute.isSimple());
	
	CellModel col = rowComplex.children().child("col");							// TEST -> ROW -> COL
	checkComplexCellModel(col, "col", "colCell");
	
	ComplexCellModel colComplex = col.asComplex();
	assertNotNull(colComplex);
	assertEquals(0, colComplex.attributes().size());
	assertEquals(2, colComplex.children().size());
	
	CellModel testFromCol = colComplex.children().child("test");				// TEST -> ROW -> COL -> TEST
	checkComplexCellModel(testFromCol, "test", "testCell");
	
	ComplexCellModel testFromColComplex = testFromCol.asComplex();
	assertNotNull(testFromColComplex);
	assertEquals(2, testFromColComplex.attributes().size());
	assertEquals(0, testFromColComplex.children().size());
		
	assertNotNull(colComplex.children().child("test2"));						// TEST -> ROW -> COL -> TEST2
	
}


@Test
public void testAttributesOf() {
	
	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, "test");
//	Map<String, XSElementDecl> elementDecls = schemaSet.getSchema(MODEL_NAMESPACE).getElementDecls();
	Type type = provideElementType(elem);
	Collection<? extends XSAttributeUse> rawAttributes = CellModelModule.rawAttributes(elem);
	doReturn(rawAttributes).when(mockAttributesProducer).get();

	Attributes<CellModel> attributes = CellModelModule.attributesOf(elem, type, mockAttributesProducer);
	assertNotNull(attributes);
	assertEquals(1, attributes.size());
	CellModel attribute = attributes.attribute(0);
	checkAttribute(attribute, "text", "textField");

	attribute = attributes.attribute("text");
	checkAttribute(attribute, "text", "textField");

}


@Test
public void testChildrenOf() {

	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, "test");
	Type type = provideElementType(elem);
	
	Composite<CellModel> children  = CellModelModule.childrenOf(elem, type);
	CellModel row = children.child("row");
	assertEquals("row", row.getName());
	assertEquals(row, children.child(0));
	
}


@Test
public void testGetDefaultTypeName() throws Exception {
	
	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, "test");
	assertEquals("test-type", CellModelModule.getDefaultTypeName(elem));

}


private void checkComplexCellModel(CellModel m, String name, String typeName) {
	
	assertNotNull(m);
	assertEquals(name, m.getName());
	assertEquals(typeName, m.getType().getName());
	assertTrue(m.isComplex());

}


private void checkAttribute(CellModel a, String name, String typeName) {

	assertNotNull(a);
	assertEquals(name, a.getName());
	assertEquals(typeName, a.getType().getName());
	assertTrue(a.isSimple());

}


}
