// CELL EDITING UI TEST . JAVA

package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.webapp.ui.UIAttributeData;
import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UICellData;
import cat.calidos.morfeu.webapp.ui.UICellEditor;
import cat.calidos.morfeu.webapp.ui.UIContent;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellEditingUITest extends UITezt {

private UICell		test;
private UIContent	content;

@BeforeEach
public void setup() {

	open(appBaseURL);
	content = UICatalogues
			.openCatalogues()
			.shouldAppear()
			.clickOn(0)
			.clickOnDocumentNamed("Document 1")
			.content();
	content.shouldBeVisible();
	test = content.rootCells().get(0);

}


@Test
public void editCellAndSave() {

	UICell data = test.child("row(0)").child("col(0)").child("data(0)").select().activate();
	assertNotNull(data);
	UICellEditor.shouldNotBeVisible();
	List<UIAttributeData> attributes = data.cellInfo().attributes();
	checkAttribute(attributes, "text", "blahblah");
	checkAttribute(attributes, "number", "42");

	UICellEditor dataEditor = data.edit().shouldAppear();
	assertNotNull(dataEditor);

	UICellData cellEditorData = dataEditor.cellData();
	assertNotNull(cellEditorData);
	assertTrue(cellEditorData.isFromEditor(), "Editing the cell should show an editor");
	assertTrue(
			cellEditorData.isFromCell(),
			"Editing the cell should show an editor with data coming from the cell");

	attributes = cellEditorData.attributes();
	assertNotNull(attributes);
	assertEquals(2, attributes.size(), "We should be editing two attributes");
	UIAttributeData text = checkAttribute(attributes, "text", "blahblah");
	assertTrue(text.isEditable(), "Attribute text should be editable");

	UIAttributeData number = checkAttribute(attributes, "number", "42");
	assertTrue(number.isEditable(), "Attribute number should be editable");

	// let's modify the values
	text.eraseValueInField();
	text.tabIntoEnterText("foo");
	number.enterTextNext("66");
	dataEditor.clickSave();

	data = test.child("row(0)").child("col(0)").child("data(0)").select().activate(); // refresh
	attributes = data.cellInfo().attributes(); // re-read attributes from the now-modified cell
	text = checkAttribute(attributes, "text", "foo");
	number = checkAttribute(attributes, "number", "66");

}


@Test
public void editCellAndDismiss() {

	UICell data = test.child("row(0)").child("col(0)").child("data(0)").select().activate();
	assertNotNull(data);
	List<UIAttributeData> attributes = data.cellInfo().attributes();
	checkAttribute(attributes, "text", "blahblah");
	checkAttribute(attributes, "number", "42");

	UICellEditor.shouldNotBeVisible();

	UICellEditor dataEditor = data.edit();
	assertNotNull(dataEditor);
	UICellData cellEditorData = dataEditor.shouldAppear().cellData();
	assertNotNull(cellEditorData);

	attributes = cellEditorData.attributes();
	assertNotNull(attributes);
	UIAttributeData text = checkAttribute(attributes, "text", "blahblah");
	UIAttributeData number = checkAttribute(attributes, "number", "42");

	// let's modify the values (this is brittle as it needs to be done in order
	text.eraseValueInField();
	text.tabIntoEnterText("foo");
	number.enterTextNext("66");

	dataEditor.clickDiscard();

	// we refetch it as using the previous instance was problematic
	data = test.child("row(0)").child("col(0)").child("data(0)");
	data.select().activate();
	attributes = data.cellInfo().attributes(); // re-read attributes from the now-modified cell
	text = checkAttribute(attributes, "text", "blahblah"); // boom, magically restored
	number = checkAttribute(attributes, "number", "42");

}


@Test
public void editCellByDoubleClick() {

	UICell data = test.child("row(0)").child("col(0)").child("data(0)").select().activate();
	assertNotNull(data);
	List<UIAttributeData> attributes = data.cellInfo().attributes();
	checkAttribute(attributes, "text", "blahblah");
	checkAttribute(attributes, "number", "42");

	UICellEditor.shouldNotBeVisible();

	UICellEditor dataEditor = data.editByDoubleClicking();
	assertNotNull(dataEditor);
	UICellData cellEditorData = dataEditor.shouldAppear().cellData();
	assertNotNull(cellEditorData);

	dataEditor.clickDiscard();

}


private UIAttributeData checkAttribute(	List<UIAttributeData> attributes,
										String name,
										String expectedValue) {

	Optional<UIAttributeData> attributeOptional = attributes
			.stream()
			.filter(a -> a.name().matches(name))
			.findFirst();
	assertTrue(attributeOptional.isPresent());
	UIAttributeData attribute = attributeOptional.get();
	assertEquals(expectedValue, attribute.value(), "Wrong value of '" + name + "'");

	return attribute;

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
