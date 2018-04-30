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

package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UICellEditor;
import cat.calidos.morfeu.webapp.ui.UICellModelEntry;
import cat.calidos.morfeu.webapp.ui.UIContent;
import cat.calidos.morfeu.webapp.ui.UIDocument;
import cat.calidos.morfeu.webapp.ui.UIDropArea;
import cat.calidos.morfeu.webapp.ui.UIModel;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellEditingNewValueUITest extends UITezt {

private UICell test;
private UIContent content;
private UIModel model;

@Before
public void setup() {

	open(appBaseURL);
	UIDocument doc = UICatalogues.openCatalogues()
									.shouldAppear()
									.shouldBeVisible()
									.clickOn(0)
									.clickOnDocumentNamed("Document 3");
	doc.shouldBeVisible();
	content = doc.content().shouldAppear();
	test = content.rootCells().get(0);
	model = doc.model();

}


@Test
public void removeCellValue() {

	//target/test-classes/test-resources/documents/document3.xml/test(0)/row(0)/col(0)/stuff(0)

	UICell stuff = test.child("row(0)").child("col(0)").child("stuff(0)").select().activate();
	assertNotNull(stuff);

	UICellEditor stuffEditor = stuff.edit().shouldAppear();
	assertNotNull(stuffEditor);
	Optional<String> value = stuffEditor.getValue();
	assertTrue(value.isPresent());
	assertEquals("Stuff content", value.get());
	assertFalse("Should not be able to create a value for this cell",  stuffEditor.isCreateValueVisible());
	assertTrue("Should be able to remove value for this cell",  stuffEditor.isRemoveValueVisible());

	stuffEditor.clickRemoveValue();
	value = stuffEditor.getValue();
	assertFalse(value.isPresent());

}


@Test
public void addCellValue() {

	//target/test-classes/test-resources/documents/document3.xml/test(0)/row(0)/col(0)

	// TEST->ROW->COL->STUFF
	UICellModelEntry stuffModel = model.rootCellModels().get(0).child("row").child("col").child("stuff");	
	stuffModel.hover();

	UICell col = test.child("row(0)").child("col(0)");
	assertEquals("We should have four children at the beginning", 4, col.children().size());
	UIDropArea targetDropArea = col.dropArea(0);
	targetDropArea.select();

	// create a new stuff element through the keyboard shortcut
	model.pressKey(UIModel.NEW_CELL);
	assertEquals("We should have 5 children after adding a new 'stuff'", 5, col.children().size());

	UICell stuff = test.child("row(0)").child("col(0)").child("stuff(0)");
	UICellEditor stuffEditor = stuff.select().activate().edit().shouldAppear();
	assertNotNull(stuffEditor);
	Optional<String> value = stuffEditor.getValue();
	assertFalse(value.isPresent());
	assertTrue("Should be able to create a value for new cell",  stuffEditor.isCreateValueVisible());
	assertFalse("Should not be able to remove value for new cell",  stuffEditor.isRemoveValueVisible());

	stuffEditor.clickCreateValue();
	assertFalse("Should not be able to create a value again",  stuffEditor.isCreateValueVisible());
	assertTrue("Should be able to remove value for this cell",  stuffEditor.isRemoveValueVisible());

	stuffEditor.enterText("Foo bar");
	assertEquals("Foo bar", stuffEditor.getValue().get());

	//FIXME: JUST FOUND A BUG, THE SECOND TIME WE LOAD A DOCUMENT, THE CELL SELECTION SHORTCUTS DON'T WORK AS EXPECTED

}

}
