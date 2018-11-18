/*
 *    Copyright 2018 Daniel Giribet
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.Type;
import cat.calidos.morfeu.model.metadata.injection.DaggerModelMetadataComponent;
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
	// default name for anonymous types is <elem>-type
	checkComplexCellModel(test, "test", "Root cell-model desc", "test-type", modelURI+"/test");
	assertEquals("WELL", test.getMetadata().getPresentation());
	assertEquals("test root cell model should be min 1", 1, test.getMinOccurs());
	assertEquals("test root cell model should be max 1", 1, test.getMaxOccurs().getAsInt());
	
	ComplexCellModel testComplex = test.asComplex();
	assertNotNull(testComplex);
	assertEquals(1, testComplex.attributes().size());
	assertEquals(1, testComplex.children().size());
	
	CellModel textAttribute = testComplex.attributes().attribute("text");		// TEST . TEXT
	checkAttribute(textAttribute, "text", "textField", modelURI+"/test@text");
	
	CellModel row = testComplex.children().child("row");						// TEST -> ROW
	checkComplexCellModel(row, "row", "rowCell desc", "rowCell", modelURI+"/test/row");	// getting desc from type
	assertEquals("/test/row cell model should be min 0", 0, row.getMinOccurs());
	assertFalse("/test/row cell model should be unbounded", row.getMaxOccurs().isPresent());

	
	ComplexCellModel rowComplex = row.asComplex();
	assertNotNull(rowComplex);
	assertEquals(1, rowComplex.attributes().size());
	assertEquals(1, rowComplex.children().size());
	
	CellModel numberAttribute = rowComplex.attributes().attribute("number");	// TEST -> ROW . NUMBER
	checkAttribute(numberAttribute, "number","numberField", modelURI+"/test/row@number");
	
	CellModel col = rowComplex.children().child("col");							// TEST -> ROW -> COL
	checkComplexCellModel(col, "col", "Column, can accept content", "colCell", modelURI+"/test/row/col");
	assertEquals("COL-WELL", col.getMetadata().getPresentation());
	
	ComplexCellModel colComplex = col.asComplex();
	assertNotNull(colComplex);
	assertEquals(1, colComplex.attributes().size());
	CellModel sizeAttribute = colComplex.attributes().attribute("size");
	assertTrue(sizeAttribute.isAttribute());
	assertEquals("Size attribute of columns should be compulsory", 1, sizeAttribute.getMinOccurs());
	assertEquals("COL-FIELD", sizeAttribute.getMetadata().getPresentation());
	int childrenCount = colComplex.children().size();
	assertEquals("Column should have 7 children, not "+childrenCount, 7, childrenCount);

	CellModel data = colComplex.children().child("data");						// TEST -> ROW -> COL -> DATA
	String dataDesc = "Globally provided description of 'data'";
	checkComplexCellModel(data, "data", dataDesc, "testCell", modelURI+"/test/row/col/data");
	assertEquals("/test/row/col/data cell model should be min 0", 0, data.getMinOccurs());
	assertFalse("/test/row/col/data cell model should be unbounded", data.getMaxOccurs().isPresent());
	ComplexCellModel dataComplex = data.asComplex();
	String defaultTextAttributeFromGlobal = "Default value for text (from global)";
	assertEquals(defaultTextAttributeFromGlobal, dataComplex.attributes().attribute("text").getDefaultValue().get());
	assertEquals("11", dataComplex.attributes().attribute("number").getDefaultValue().get());	// type default

	CellModel data2 = colComplex.children().child("data2");						// TEST -> ROW -> COL -> DATA2
	String data2Desc = "Globally provided description of 'data2'";
	checkComplexCellModel(data2, "data2", data2Desc, "testCell", modelURI+"/test/row/col/data2");
	assertEquals("/test/row/col/data2 cell model should be min 0", 0, data2.getMinOccurs());
	// model references keep local max counts
	assertEquals("/test/row/col/data2 cell model should be max 2", 2, data2.getMaxOccurs().getAsInt());
	// we only have the type default and nothing from global
	ComplexCellModel data2Complex = data2.asComplex();
	assertEquals("11", data2Complex.attributes().attribute("number").getDefaultValue().get());	// type default
	textAttribute = data2Complex.attributes().attribute("text");
	assertFalse("Should not have default", textAttribute.getDefaultValue().isPresent());

	assertTrue(data.isReference() || data2.isReference());

	CellModel testCell;
	if (data.isReference()) {	// it's undetermined which one will be processed first, both are testCell so any will do
		assertEquals("CELL", data.getMetadata().getPresentation());
		testCell = data.getReference().get();

	} else {
		assertEquals("CELL", data2.getMetadata().getPresentation());
		testCell = data2.getReference().get();		
	}	
	assertNotNull(testCell);
	
	ComplexCellModel testComplexCell = testCell.asComplex();
	assertNotNull(testComplex);
	assertEquals(2, testComplexCell.attributes().size());
	assertEquals(0, testComplexCell.children().size());
	
	CellModel attribute = testComplexCell.attributes().attribute("number");
	assertNotNull(attribute);
	assertEquals("11", attribute.getDefaultValue().get());
	

}


@Test
public void testColAndRowReference() throws Exception {

	CellModel test = cellModelFrom(modelURI, "test");							// TEST
	CellModel row = test.asComplex().children().child("row");					// TEST -> ROW
	CellModel col = row.asComplex().children().child("col");					// TEST -> ROW -> COL
	
	// we check the reference from row to col and so forth
	CellModel rowRef = col.asComplex().children().child("row");				// TEST -> ROW -> COL -> ref(ROW)
	assertTrue(rowRef.isReference());
	assertEquals("Row reference in col does not reference original", row, rowRef.getReference().get());

	// the row reference should have the same children as the original row
	assertTrue(rowRef.isComplex());
	assertEquals(1, rowRef.asComplex().children().size());
	assertNotNull(rowRef.asComplex().children().child("col"));
	
}


@Test
public void testAttributesOf() {
	
	String name = "test";
	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, name);
	// Map<String, XSElementDecl> elementDecls = schemaSet.getSchema(MODEL_NAMESPACE).getElementDecls();
	Type type = provideElementType(elem);

	URI uri = CellModelModule.getURIFrom(modelURI+"/"+name, name);
	HashMap<String, CellModel> globals = new HashMap<String, CellModel>();
	Metadata meta = DaggerModelMetadataComponent.builder().from(elem.getAnnotation()).withParentURI(modelURI).build().value();
	Attributes<CellModel> attributes = CellModelModule.attributesOf(elem, type, uri, meta, globals);
	
	assertNotNull(attributes);
	assertEquals(1, attributes.size());
	CellModel attribute = attributes.attribute(0);
	checkAttribute(attribute, "text", "textField", modelURI+"/test@text");
	assertFalse(attribute.getDefaultValue().isPresent());

	attribute = attributes.attribute("text");
	checkAttribute(attribute, "text", "textField", modelURI+"/test@text");

}


@Test
public void testAttributesDefaultValues() {
	
	String name = "test";
	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, name);
	// Map<String, XSElementDecl> elementDecls = schemaSet.getSchema(MODEL_NAMESPACE).getElementDecls();
	Type type = provideElementType(elem);

	URI uri = CellModelModule.getURIFrom(modelURI+"/"+name, name);
	HashMap<String, CellModel> globals = new HashMap<String, CellModel>();
	
	HashMap<String, String> defaultValues = new HashMap<String, String>(1);
	defaultValues.put("@text", "foo");
	Metadata cellMetadata = new Metadata(null, "desc", "pres", "cellpres", "IMG", "thumb", null, defaultValues, null, null);
	//when(mockCellMetadata.getDefaultValues()).thenReturn(defaultValues);
	
	Attributes<CellModel> attributes = CellModelModule.attributesOf(elem, type, uri, cellMetadata, globals);
	assertNotNull(attributes);
	
	CellModel textAttribute = attributes.attribute("text");
	assertNotNull(textAttribute);
	Optional<String> defaultValue = textAttribute.getDefaultValue();
	assertTrue("We should have a default value in this test", defaultValue.isPresent());
	assertEquals("Wrong default value in this test", "foo", defaultValue.get());
	
}



@Test
public void testChildrenOf() {

	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, "test");
	Type type = provideElementType(elem);
	Map<String,CellModel> globals = new HashMap<String, CellModel>(0);
	Map<URI, Metadata> globalMetadata = new HashMap<URI, Metadata>(0);
	
	Composite<CellModel> children  = CellModelModule.childrenOf(elem, type, modelURI, globals, globalMetadata);
	CellModel row = children.child("row");
	assertEquals("row", row.getName());
	assertEquals(row, children.child(0));
	
}


@Test
public void testGetDefaultTypeName() throws Exception {

	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, "test");
	assertEquals("test-type", CellModelModule.getDefaultTypeName(elem));

}


private void checkComplexCellModel(CellModel test, String name, String desc, String typeName, String uri) {

	assertNotNull(test);
	assertEquals(name, test.getName());
	assertEquals(desc, test.getDesc());
	assertEquals(typeName, test.getType().getName());
	assertEquals(uri, test.getURI().toString());
	assertTrue(test.isComplex());
	assertFalse(test.isSimple());

}


private void checkAttribute(CellModel textAttribute, String name, String typeName, String uri) {

	assertNotNull(textAttribute);
	assertEquals(name, textAttribute.getName());
	assertEquals(typeName, textAttribute.getType().getName());
	assertEquals(uri, textAttribute.getURI().toString());
	assertTrue(textAttribute.isSimple());

}


}
