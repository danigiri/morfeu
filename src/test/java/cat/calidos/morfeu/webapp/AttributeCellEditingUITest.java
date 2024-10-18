// ATTRIBUTE CELL EDITING UI TEST . JAVA

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
import cat.calidos.morfeu.webapp.ui.UIDocument;


/**
 * Tests adding new attributes on a cell editor, does not bother with saving or discarding as this
 * is tested elsewhere
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class AttributeCellEditingUITest extends UITezt {

private UICell		test;
private UIContent	content;

@BeforeEach
public void setup() {

	open(appBaseURL);
	UIDocument document = UICatalogues
			.openCatalogues()
			.shouldAppear()
			.clickOn(0)
			.clickOnDocumentNamed("Document 2");
	document.shouldBeVisible();
	content = document.content();
	content.shouldBeVisible();
	test = content.rootCells().get(0);

}


@Test
public void addAttributeTest() throws Exception {

	// /test(0)/row(0)/col(0)/row(0)/col(0)/data(0)
	UICell data = test
			.child("row(0)")
			.child("col(0)")
			.child("row(0)")
			.child("col(0)")
			.child("data(0)")
			.select()
			.activate();
	assertNotNull(data);

	List<UIAttributeData> attributes = data.cellInfo().attributes();
	assertEquals(
			1,
			attributes.size(),
			"We should only have a single attribute before adding another");

	UIAttributeData number = attributes.get(0);
	assertEquals("42", number.value());

	// now we add the missing attribute, will get the default value and the total of present
	// attributes will be 2
	UICellEditor dataEditor = data.edit();
	UICellData dataEditorCellData = dataEditor.cellData();
	List<UIAttributeData> nonPresentAttributes = dataEditorCellData.notPresentAttributes();
	assertNotNull(nonPresentAttributes);
	assertEquals(1, nonPresentAttributes.size(), "We should only have one attribute missing");

	Optional<UIAttributeData> notPresentAttribute = dataEditorCellData.notPresentAttribute("text");
	assertTrue(notPresentAttribute.isPresent(), "Text attribute is not present yet"); // a bit of a
																						// contradiction

	UIAttributeData textAttribute = notPresentAttribute.get();
	assertFalse(textAttribute.hasValue());
	assertTrue(textAttribute.isNotPresent());

	textAttribute = textAttribute.clickOnCreate();
	waitOneSec();
	assertTrue(textAttribute.isPresent());
	assertTrue(textAttribute.hasValue());
	assertEquals("Default value for text (from global)", textAttribute.value());

	dataEditorCellData = dataEditor.cellData();
	assertEquals(2, dataEditorCellData.attributes().size(), "We have now two attributes"); // awesome!

	nonPresentAttributes = dataEditorCellData.notPresentAttributes();
	assertEquals(
			0,
			nonPresentAttributes.size(),
			"We do not have any remaining non-present attributes");

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
