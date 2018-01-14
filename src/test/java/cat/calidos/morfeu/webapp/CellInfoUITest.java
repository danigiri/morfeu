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

package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cat.calidos.morfeu.webapp.ui.UIAttributeInfo;
import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UICellInfo;
import cat.calidos.morfeu.webapp.ui.UICellModelEntry;
import cat.calidos.morfeu.webapp.ui.UIContent;
import cat.calidos.morfeu.webapp.ui.UIModel;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellInfoUITest extends UITezt {

private UICell test;


@Before
public void setup() {
	
	open(appBaseURL);
	UIContent content = UICatalogues.openCatalogues()
			.shouldAppear()
			.clickOn(0)
			.clickOnDocumentNamed("Document 1")
			.content();
	content.shouldBeVisible();
	test = content.rootCells().get(0);
	
}


@Test
public void checkCelInfo() {


	// target/test-classes/test-resources/documents/document1.xml/test(0)/row(0)/col(0)/data(0)
	UICell data = test.child("row(0)").child("col(0)").child("data(0)");
	assertNotNull(data);

	UICellInfo.shouldNotBeVisible();

	data.hover();
	UICellInfo dataInfo = data.cellInfo();
	assertNotNull(dataInfo);
	assertFalse("cell info from hovering on a cell should come from the cell", dataInfo.isFromModel());
	assertTrue("cell info from hovering on a cell should come from the cell", dataInfo.isFromCell());
	
	String header = dataInfo.header();
	assertTrue("Bad information cell, we did not get 'data' in the cell info header", header.contains("data"));
	assertTrue("Bad information cell, should have 0 to ∞ as the cardinality of 'data'", header.contains("[0..∞]"));

	String desc = dataInfo.desc();
	String expectedDesc = "Globally provided description of 'data'";
	assertTrue("Bad information cell, does not have the correct description", desc.contains(expectedDesc));
	
	String uri = dataInfo.URI();
	String expectedURI = "target/test-classes/test-resources/documents/document1.xml/test(0)/row(0)/col(0)/data(0)";
	assertTrue("Bad information cell, does not have the correct uri", uri.endsWith(expectedURI));

	// now we test the attributes :)
	List<UIAttributeInfo> attributes = dataInfo.attributes();
	assertNotNull(attributes);
	assertEquals("Wrong number of attributes of 'data',", 2, attributes.size());
	
	UIAttributeInfo textAttribute = dataInfo.attribute("text");
	assertEquals("Wrong name of attribut 'data@text'", "text", textAttribute.name());
	assertTrue("Attribute data@text should not be mandatory", textAttribute.isOptional());
	assertEquals("blahblah", textAttribute.value());
	
	UIAttributeInfo numberAttribute = dataInfo.attribute("number");
	assertEquals("Wrong name of attribut 'data@text'", "number", numberAttribute.name());
	assertTrue("Attribute data@number should be mandatory", numberAttribute.isMandatory());
	assertEquals("42", numberAttribute.value());
	
}


@Test
public void checkCellInfoMissingAttributes() {
	
	// this cell only has 'number' attribute and no 'text'
	// target/test-classes/test-resources/documents/document1.xml/test(0)/row(0)/col(1)/row(0)/col(0)/data(0)
	UICell data = test.child("row(0)").child("col(1)").child("row(0)").child("col(0)").child("data(0)");
	assertNotNull(data);
	
	UICellInfo.shouldNotBeVisible();
	
	data.hover();
	UICellInfo dataInfo = data.cellInfo().shouldAppear();
	assertNotNull(dataInfo);
	
	List<UIAttributeInfo> attributes = dataInfo.attributes();
	assertNotNull(attributes);
	assertEquals("Wrong number of attributes of 'data',", 1, attributes.size());
	
	UIAttributeInfo numberAttribute = dataInfo.attribute("number");
	assertEquals("Wrong name of attribut 'data@text'", "number", numberAttribute.name());
	assertTrue("Attribute data@number should be mandatory", numberAttribute.isMandatory());
	assertEquals("42", numberAttribute.value());
	
}


@Test
public void checkCellModelInfo() {

	UIModel model = UICatalogues.openCatalogues()
								.shouldAppear()
								.clickOn(0)
								.clickOnDocumentNamed("Document 1")
								.model()
								.shouldAppear();

	List<UICellModelEntry> rootCellModels = model.rootCellModels();
	UICellModelEntry testModelEntry = rootCellModels.get(0);				// TEST

	UICellInfo.shouldNotBeVisible();
	testModelEntry.hover();
	UICellInfo testInfo = testModelEntry.cellInfo().shouldAppear();
	assertTrue("cell info from hovering on the model should come from the model", testInfo.isFromModel());
	assertFalse("cell info from hovering on the model should come from the model", testInfo.isFromCell());
	
	String header = testInfo.header();
	assertTrue("Bad information from model, we did not get 'test' in the cell info header", header.contains("test"));
	assertTrue("Bad information from model, should have 1..1 as the cardinality of 'test'", header.contains("[1..1]"));
	
	String desc = testInfo.desc();
	String expectedDesc = "Root cell-model desc";
	assertTrue("Bad information from model, does not have the correct description", desc.contains(expectedDesc));
	
	List<UIAttributeInfo> attributes = testInfo.attributes();
	assertNotNull(attributes);
	assertEquals("Wrong number of attributes of 'test', should be 1", 1, attributes.size());

	UIAttributeInfo textAttribute = testInfo.attribute("text");
	assertEquals("Wrong name of attribute 'test@text'", "text", textAttribute.name());
	assertFalse("Attribute test@text should not be mandatory", textAttribute.isMandatory());

}

}
