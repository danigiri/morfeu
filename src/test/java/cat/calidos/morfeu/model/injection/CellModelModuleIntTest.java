// CELL MODEL MODULE INT TEST . JAVA

package cat.calidos.morfeu.model.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchemaSet;

import dagger.Lazy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import cat.calidos.morfeu.model.Attributes;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.Type;
import cat.calidos.morfeu.model.metadata.injection.DaggerModelMetadataComponent;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellModelModuleIntTest extends ModelTezt {

@Mock
Lazy<Collection<? extends XSAttributeUse>> mockAttributesProducer;

private URI			modelURI;
private XSSchemaSet	schemaSet;

@BeforeEach
public void setup() throws Exception {

	modelURI = new URI("target/classes/test-resources/models/test-model.xsd");
	schemaSet = parseSchemaFrom(modelURI);

}


@Test
public void testProvideCellModel() throws Exception {

	CellModel test = cellModelFrom(modelURI, "test"); // TEST
	// default name for anonymous types is <elem>-type
	checkComplexCellModel(test, "test", "Root cell-model desc", "test-type", modelURI + "/test");
	assertEquals("WELL", test.getMetadata().getPresentation());
	assertEquals(1, test.getMinOccurs(), "test root cell model should be min 1");
	assertEquals(1, test.getMaxOccurs().getAsInt(), "test root cell model should be max 1");
	assertTrue(test.getMetadata().isReadonly().isEmpty());
	assertTrue(test.getCategory().isEmpty());

	ComplexCellModel testComplex = test.asComplex();
	assertNotNull(testComplex);
	assertEquals(1, testComplex.attributes().size());
	assertEquals(1, testComplex.children().size());
	assertTrue(testComplex.areChildrenOrdered());

	CellModel textAttribute = testComplex.attributes().attribute("text"); // TEST . TEXT
	checkAttribute(textAttribute, "text", "textField", modelURI + "/test@text");

	CellModel row = testComplex.children().child("row"); // TEST -> ROW
	checkComplexCellModel(row, "row", "rowCell desc", "rowCell", modelURI + "/test/row"); // getting
																							// desc
																							// from
																							// type
	assertEquals(0, row.getMinOccurs(), "/test/row cell model should be min 0");
	assertFalse(row.getMaxOccurs().isPresent(), "/test/row cell model should be unbounded");

	ComplexCellModel rowComplex = row.asComplex();
	assertNotNull(rowComplex);
	assertEquals(1, rowComplex.attributes().size());
	assertEquals(1, rowComplex.children().size());
	assertFalse(rowComplex.areChildrenOrdered()); // all children of rows are cols, no strict need
													// to be ordered

	CellModel numberAttribute = rowComplex.attributes().attribute("number"); // TEST -> ROW . NUMBER
	checkAttribute(numberAttribute, "number", "numberField", modelURI + "/test/row@number");

	CellModel col = rowComplex.children().child("col"); // TEST -> ROW -> COL
	checkComplexCellModel(
			col,
			"col",
			"Column, can accept content",
			"colCell",
			modelURI + "/test/row/col");
	assertEquals("COL-WELL", col.getMetadata().getPresentation());

	ComplexCellModel colComplex = col.asComplex();
	assertNotNull(colComplex);
	assertEquals(1, colComplex.attributes().size());
	CellModel sizeAttribute = colComplex.attributes().attribute("size");
	assertTrue(sizeAttribute.isAttribute());
	assertEquals(1, sizeAttribute.getMinOccurs(), "Size attribute of columns should be compulsory");
	assertEquals("COL-FIELD", sizeAttribute.getMetadata().getPresentation());
	int childrenCount = colComplex.children().size();
	assertEquals(
			EXPECTED_COL_CHILDREN_COUNT,
			childrenCount,
			"Column should have 9 children, not " + childrenCount);
	assertTrue(colComplex.areChildrenOrdered());

	CellModel data = colComplex.children().child("data"); // TEST -> ROW -> COL -> DATA
	String dataDesc = "Globally provided description of 'data'";
	checkComplexCellModel(data, "data", dataDesc, "testCell", modelURI + "/test/row/col/data");
	assertEquals(0, data.getMinOccurs(), "/test/row/col/data cell model should be min 0");
	assertFalse(
			data.getMaxOccurs().isPresent(),
			"/test/row/col/data cell model should be unbounded");
	ComplexCellModel dataComplex = data.asComplex();
	String defaultTextAttributeFromGlobal = "Default value for text (from global)";
	assertEquals(
			defaultTextAttributeFromGlobal,
			dataComplex.attributes().attribute("text").getDefaultValue().get());
	assertEquals("11", dataComplex.attributes().attribute("number").getDefaultValue().get()); // type
																								// default
	assertTrue(data.getMetadata().isReadonly().isEmpty());

	CellModel data2 = colComplex.children().child("data2"); // TEST -> ROW -> COL -> DATA2
	String data2Desc = "Globally provided description of 'data2'";
	checkComplexCellModel(data2, "data2", data2Desc, "testCell", modelURI + "/test/row/col/data2");
	assertEquals(0, data2.getMinOccurs(), "/test/row/col/data2 cell model should be min 0");
	// model references keep local max counts
	assertEquals(
			2,
			data2.getMaxOccurs().getAsInt(),
			"/test/row/col/data2 cell model should be max 2");
	// we only have the type default and nothing from global
	ComplexCellModel data2Complex = data2.asComplex();
	assertEquals("11", data2Complex.attributes().attribute("number").getDefaultValue().get()); // type
																								// default
	textAttribute = data2Complex.attributes().attribute("text");
	assertFalse(textAttribute.getDefaultValue().isPresent(), "Should not have default");

	assertTrue(data.isReference() || data2.isReference());

	CellModel testCell;
	if (data.isReference()) { // it's undetermined which one will be processed first, both are
								// testCell so any will do
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

	CellModel test = cellModelFrom(modelURI, "test"); // TEST
	CellModel row = test.asComplex().children().child("row"); // TEST -> ROW
	CellModel col = row.asComplex().children().child("col"); // TEST -> ROW -> COL

	// we check the reference from row to col and so forth
	CellModel rowRef = col.asComplex().children().child("row"); // TEST -> ROW -> COL -> ref(ROW)
	assertTrue(rowRef.isReference());
	assertEquals(
			row,
			rowRef.getReference().get(),
			"Row reference in col does not reference original");

	// the row reference should have the same children as the original row
	assertTrue(rowRef.isComplex());
	assertEquals(1, rowRef.asComplex().children().size());
	assertNotNull(rowRef.asComplex().children().child("col"));

	// we also test that holder well has a row that is also a reference
	CellModel holderWell = col.asComplex().children().child("holderWell"); // TEST -> ROW -> COL ->
																			// holderWell -> row
	assertNotNull(holderWell);
	rowRef = holderWell.asComplex().children().child("row");
	assertNotNull(rowRef);
	assertTrue(rowRef.isReference());
	assertEquals(
			row,
			rowRef.getReference().get(),
			"Row reference in col does not reference original");

}


@Test
public void testAttributesOf() {

	var name = "test";
	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, name);
	// Map<String, XSElementDecl> elementDecls =
	// schemaSet.getSchema(MODEL_NAMESPACE).getElementDecls();
	Type type = provideElementType(elem);

	URI uri = CellModelModule.getURIFrom(modelURI + "/" + name, name);
	HashMap<String, CellModel> globals = new HashMap<String, CellModel>();
	Metadata meta = DaggerModelMetadataComponent
			.builder()
			.from(elem.getAnnotation())
			.withParentURI(modelURI)
			.build()
			.value();
	Attributes<CellModel> attributes = CellModelModule.attributesOf(elem, type, uri, meta, globals);

	assertNotNull(attributes);
	assertEquals(1, attributes.size());
	CellModel attribute = attributes.attribute(0);
	checkAttribute(attribute, "text", "textField", modelURI + "/test@text");
	assertFalse(attribute.getDefaultValue().isPresent());

	attribute = attributes.attribute("text");
	checkAttribute(attribute, "text", "textField", modelURI + "/test@text");

}


@Test
public void testColorRestriction() throws Exception {

	CellModel color = cellModelFrom(modelURI, "test")
			.asComplex()
			.children()
			.child("row")
			.asComplex()
			.children()
			.child("col")
			.asComplex()
			.children()
			.child("data3")
			.asComplex()
			.attributes()
			.attribute("color");
	assertNotNull(color);
	assertTrue(color.getType().getRegex().isPresent());
	assertEquals("[0-9a-fA-F]{6}", color.getType().getRegex().get());

}


@Test
public void testPossibleValues() throws Exception {

	CellModel list = cellModelFrom(modelURI, "test")
			.asComplex()
			.children()
			.child("row")
			.asComplex()
			.children()
			.child("col")
			.asComplex()
			.children()
			.child("types")
			.asComplex()
			.attributes()
			.attribute("list");
	assertNotNull(list);

	Type type = list.getType();
	assertTrue(type.getRegex().isEmpty());

	Set<String> possibleValues = type.getPossibleValues();
	assertEquals(3, possibleValues.size());
	assertTrue(possibleValues.contains("A0"));
	assertTrue(possibleValues.contains("A1"));
	assertTrue(possibleValues.contains("A2"));

}


@Test
public void testAttributesDefaultValues() {

	var name = "test";
	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, name);
	// Map<String, XSElementDecl> elementDecls =
	// schemaSet.getSchema(MODEL_NAMESPACE).getElementDecls();
	Type type = provideElementType(elem);

	URI uri = CellModelModule.getURIFrom(modelURI + "/" + name, name);
	HashMap<String, CellModel> globals = new HashMap<String, CellModel>();

	HashMap<String, String> defaultValues = new HashMap<String, String>(1);
	defaultValues.put("@text", "foo");
	Map<String, Set<String>> categories = new HashMap<String, Set<String>>(0);
	Metadata cellMetadata = new Metadata(null, "desc", "pres", "cellpres", "IMG", "GET", "thumb",
			null, Optional.empty(), "valueLocator", defaultValues, null, null, "X", categories);
	// when(mockCellMetadata.getDefaultValues()).thenReturn(defaultValues);

	Attributes<CellModel> attributes = CellModelModule
			.attributesOf(elem, type, uri, cellMetadata, globals);
	assertNotNull(attributes);

	CellModel textAttribute = attributes.attribute("text");
	assertNotNull(textAttribute);
	Optional<String> defaultValue = textAttribute.getDefaultValue();
	assertTrue(defaultValue.isPresent(), "We should have a default value in this test");
	assertEquals("foo", defaultValue.get(), "Wrong default value in this test");

}


@Test
public void testChildrenOf() {

	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, "test");
	Type type = provideElementType(elem);
	Map<String, CellModel> globals = new HashMap<String, CellModel>(0);
	Map<URI, Metadata> globalMetadata = new HashMap<URI, Metadata>(0);

	Composite<CellModel> children = CellModelModule
			.childrenOf(elem, type, modelURI, globals, globalMetadata);
	CellModel row = children.child("row");
	assertEquals("row", row.getName());
	assertEquals(row, children.child(0));

}


@Test
public void testGetDefaultTypeName() throws Exception {

	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, "test");
	assertEquals("test-type", CellModelModule.getDefaultTypeName(elem));

}


@Test
public void testCategory() throws Exception {

	CellModel test = cellModelFrom(modelURI, "test");
	CellModel col = test.asComplex().children().child("row").asComplex().children().child("col");

	ComplexCellModel categ = col.asComplex().children().child("categ").asComplex();
	assertNotNull(categ);
	assertEquals("X", categ.getCategory().get());
	assertEquals("X", categ.attributes().attribute("value0x").getCategory().get());
	assertEquals("X", categ.attributes().attribute("value1x").getCategory().get());
	assertEquals("Y", categ.attributes().attribute("value0y").getCategory().get());
	assertEquals("Y", categ.attributes().attribute("value1y").getCategory().get());

}


private void checkComplexCellModel(	CellModel test,
									String name,
									String desc,
									String typeName,
									String uri) {

	assertNotNull(test);
	assertEquals(name, test.getName());
	assertEquals(desc, test.getDesc());
	assertEquals(typeName, test.getType().getName());
	assertEquals(uri, test.getURI().toString());
	assertTrue(test.isComplex());
	assertFalse(test.isSimple());

}


private void checkAttribute(CellModel textAttribute,
							String name,
							String typeName,
							String uri) {

	assertNotNull(textAttribute);
	assertEquals(name, textAttribute.getName());
	assertEquals(typeName, textAttribute.getType().getName());
	assertEquals(uri, textAttribute.getURI().toString());
	assertTrue(textAttribute.isSimple());

}

}

/*
 * Copyright 2024 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
