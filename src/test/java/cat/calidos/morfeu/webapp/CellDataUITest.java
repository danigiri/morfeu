// CELL DATA UI TEST . JAVA

package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.webapp.ui.UIAttributeData;
import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UICellData;
import cat.calidos.morfeu.webapp.ui.UICellModelEntry;
import cat.calidos.morfeu.webapp.ui.UIContent;
import cat.calidos.morfeu.webapp.ui.UIModel;

/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellDataUITest extends UITezt {

private UICell test;


@BeforeEach
public void setup() {

	open(appBaseURL);
	UIContent content = UICatalogues.openCatalogues().shouldAppear().clickOn(0).clickOnDocumentNamed("Document 1")
			.content();
	content.shouldBeVisible();
	test = content.rootCells().get(0);

}


@Test
public void checkCellData() {


	// target/test-classes/test-resources/documents/document1.xml/test(0)/row(0)/col(0)/data(0)
	UICell data = test.child("row(0)").child("col(0)").child("data(0)");
	assertNotNull(data);

	UICellData.shouldNotBeVisible();

	data.hover();
	UICellData cellData = data.cellInfo();
	assertNotNull(cellData);
	assertFalse(cellData.isFromModel(), "cell data from hovering on a cell should not come from the model");
	assertTrue(cellData.isFromCell(), "cell data from hovering on a cell should come from the cell");
	assertFalse(cellData.isFromEditor(), "cell data from hovering should not be an editor");

	String header = cellData.header();
	assertTrue(header.contains("data"), "Bad information cell, we did not get 'data' in the cell info header");
	assertTrue(header.contains("[0..∞]"), "Bad information cell, should have 0 to ∞ as the cardinality of 'data'");

	String desc = cellData.desc();
	String expectedDesc = "Globally provided description of 'data'";
	assertTrue(desc.contains(expectedDesc), "Bad information cell, does not have the correct description");

	String uri = cellData.URI();
	String expectedURI = "target/test-classes/test-resources/documents/document1.xml/test(0)/row(0)/col(0)/data(0)";
	assertTrue(uri.endsWith(expectedURI), "Bad information cell, does not have the correct uri");

	// now we test the attributes :)
	List<UIAttributeData> attributes = cellData.attributes();
	assertNotNull(attributes);
	assertEquals(2, attributes.size(), "Wrong number of attributes of 'data',");

	UIAttributeData textAttribute = cellData.attribute("text");
	assertEquals("text", textAttribute.name(), "Wrong name of attribute 'data@text'");
	assertTrue(textAttribute.isOptional(), "Attribute data@text should not be mandatory");
	assertEquals("blahblah", textAttribute.value());

	UIAttributeData numberAttribute = cellData.attribute("number");
	assertEquals("number", numberAttribute.name(), "Wrong name of attribute 'data@text'");
	assertTrue(numberAttribute.isMandatory(), "Attribute data@number should be mandatory");
	assertEquals("42", numberAttribute.value());

}


@Test
public void checkCellDataMissingAttributes() {

	// this cell only has 'number' attribute and no 'text'
	// target/test-classes/test-resources/documents/document1.xml/test(0)/row(0)/col(1)/row(0)/col(0)/data(0)
	UICell data = test.child("row(0)").child("col(1)").child("row(0)").child("col(0)").child("data(0)");
	assertNotNull(data);

	UICellData.shouldNotBeVisible();

	data.hover();
	UICellData cellData = data.cellInfo().shouldAppear();
	assertNotNull(cellData);

	List<UIAttributeData> attributes = cellData.attributes();
	assertNotNull(attributes);
	assertEquals(1, attributes.size(), "Wrong number of attributes of 'data',");

	UIAttributeData numberAttribute = cellData.attribute("number");
	assertEquals("number", numberAttribute.name(), "Wrong name of attribute 'data@text'");
	assertTrue(numberAttribute.isMandatory(), "Attribute data@number should be mandatory");
	assertEquals("42", numberAttribute.value());

}


@Test
public void checkCellModelData() {

	UIModel model = UICatalogues.openCatalogues().shouldAppear().clickOn(0).clickOnDocumentNamed("Document 1").model()
			.shouldAppear();

	List<UICellModelEntry> rootCellModels = model.rootCellModels();
	UICellModelEntry testModelEntry = rootCellModels.get(0); // TEST

	UICellData.shouldNotBeVisible();
	testModelEntry.hover();
	UICellData testData = testModelEntry.cellInfo().shouldAppear();
	assertTrue(testData.isFromModel(), "cell data from hovering on the model should come from the model");
	assertFalse(testData.isFromCell(), "cell data from hovering on the model should come from the model");

	String header = testData.header();
	assertTrue(header.contains("test"), "Bad information from model, we did not get 'test' in the cell info header");
	assertTrue(header.contains("[1..1]"), "Bad information from model, should have 1..1 as the cardinality of 'test'");

	String desc = testData.desc();
	String expectedDesc = "Root cell-model desc";
	assertTrue(desc.contains(expectedDesc), "Bad information from model, does not have the correct description");

	List<UIAttributeData> attributes = testData.attributes();
	assertNotNull(attributes);
	assertEquals(1, attributes.size(), "Wrong number of attributes of 'test', should be 1");

	UIAttributeData textAttribute = testData.attribute("text");
	assertEquals("text", textAttribute.name(), "Wrong name of attribute 'test@text'");
	assertFalse(textAttribute.isMandatory(), "Attribute test@text should not be mandatory");

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
