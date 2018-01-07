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

import java.util.List;

import org.junit.Before;
import org.junit.Test;

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


@Before
public void setup() {	

	open(appBaseURL);
	UIDocument doc = UICatalogues.openCatalogues().clickOn(0).clickOnDocumentNamed("Document 1");
	model = doc.model();
	content = doc.content();

}


@Test
public void newAfterMouseActivationOfCellModel() {

	model.shouldAppear();
	content.shouldBeVisible();

	UICellModelEntry testModel = model.rootCellModels().get(0);					// TEST
	UICellModelEntry rowModel = testModel.child("row").child("row").child("col");	// TEST->ROW->COL
	assertNotNull(rowModel);


	UICell targetCol = content.rootCells().get(0).child("row(0)").child("col(0)");
	assertEquals("Before creating a new cell, we should only have one child", 1, targetCol.children().size());
	
	UIDropArea targetDropArea = targetCol.dropArea(0);
	assertFalse("Target area should not be active before rollover", targetDropArea.isActive());

	rowModel.hover();
	assertTrue("row cell model is not highlighted after rollover", rowModel.isHighlighted());
	assertTrue("Target area should be selected on rollover", targetDropArea.isActive());
	targetDropArea.selectThroughKeyboard();
	
}



}
