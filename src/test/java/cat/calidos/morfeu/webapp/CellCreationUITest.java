// CEL CREATION UI TEST . JAVA

package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UICellModelEntry;
import cat.calidos.morfeu.webapp.ui.UIContent;
import cat.calidos.morfeu.webapp.ui.UIDocument;
import cat.calidos.morfeu.webapp.ui.UIDropArea;
import cat.calidos.morfeu.webapp.ui.UIModel;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellCreationUITest extends UITezt  {


private UIModel model;
private UIContent content;


@BeforeEach
public void setup() {	

	open(appBaseURL);
	UIDocument doc = UICatalogues.openCatalogues().shouldAppear().clickOn(0).clickOnDocumentNamed("Document 1");
	model = doc.model();
	content = doc.content();

}


@Test
public void newAfterMouseActivationOfCellModel() {

	model.shouldAppear();
	content.shouldBeVisible();

	UICellModelEntry testModel = model.rootCellModels().get(0);							// TEST
	UICellModelEntry data2Model = testModel.child("row").child("col").child("data2");	// TEST->ROW->COL->DATA2
	assertNotNull(data2Model);

	UICell targetCol = content.rootCells().get(0).child("row(0)").child("col(0)");
	assertEquals( 1, targetCol.children().size(), "Before creating a new cell, we should only have one child");
	assertNotNull( targetCol.child("data(0)"), "Before creating new cell, target child 0 is data");

	UIDropArea targetDropArea = targetCol.dropArea(0);
	assertFalse( targetDropArea.isActive(), "Target area should not be active before rollover");

	data2Model.hover();
	assertTrue( data2Model.isActive(), "data2 cell model is not highlighted after rollover");
	assertTrue( targetDropArea.isActive(), "Target area should be active on rollover");
	targetDropArea.select();
	assertTrue(targetDropArea.isSelected(), "Target drop area shoud be selected after select");

	// create a new data2 element through the keyboard shortcut
	model.pressKey(UIModel.NEW_CELL_KEY);
	assertEquals( 2, targetCol.children().size(), "After creating a new cell, we should have 2 children");
	assertNotNull(targetCol.child("data2(0)"), "Aftert creating new cell, target child 0 is data2");
	assertNotNull(targetCol.child("data(1)"), "Before creating new cell, target child 1 is data");

}


@Test
public void newAfterKeyboardActivationOfCellModel() {

	model.shouldAppear();
	content.shouldBeVisible();

	UICellModelEntry testModel = model.rootCellModels().get(0);							// TEST
	UICellModelEntry data2Model = testModel.child("row").child("col").child("data2");	// TEST->ROW->COL->DATA2
	assertNotNull(data2Model);

	UICell targetCol = content.rootCells().get(0).child("row(0)").child("col(0)");
	assertEquals(1, targetCol.children().size(), "Before creating a new cell, we should only have one child");
	assertNotNull( targetCol.child("data(0)"), "Before creating new cell, target child 0 is data");

	UIDropArea targetDropArea = targetCol.dropArea(0);
	assertFalse(targetDropArea.isActive(), "Target area should not be active before rollover");

	data2Model = testModel.child("row").child("col").child("data2");
	data2Model.select().activate();
	assertTrue(data2Model.isActive(), "data2 cell model is not highlighted after activation");
	assertTrue(targetDropArea.isActive(), "Target area should be active on after activating cell model");
	targetDropArea.select();
	assertTrue(targetDropArea.isSelected(), "Target drop area shoud be selected after select");

	// create a new data2 element through the keyboard shortcut
	model.pressKey(UIModel.NEW_CELL_KEY);
	assertEquals( 2, targetCol.children().size(), "After creating a new cell, we should have 2 children");
	assertNotNull(targetCol.child("data2(0)"), "After creating new cell, target child 0 is data2");
	assertNotNull(targetCol.child("data(1)"), "Before creating new cell, target child 1 is data");

}


}

/*
 *    Copyright 2024 Daniel Giribet
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
