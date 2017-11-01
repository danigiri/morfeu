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

import org.junit.Before;
import org.junit.Test;

import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UIContent;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SelectionUITest extends UITezt {

private UIContent content;


@Before
public void setup() {
	
	open(appBaseURL);
	content = UICatalogues.openCatalogues()	
							.shouldAppear()
							.clickOn(0)
							.clickOnDocumentNamed("Document 1")
							.content();
	content.shouldAppear();
	
}


@Test
public void testClearSelection() {
	
	content.pressKey(UIContent.SELECTION_MODE);
	assertFalse("After selection mode, root cell should not be selected", content.rootCells().get(0).isSelected());
	
	content.pressKey("0");
	assertTrue("After selecting root cell it should be selected", content.rootCells().get(0).isSelected());
	assertFalse("First row should not be selected yet", content.rootCells().get(0).child(0).isSelected());

	content.pressKey("0");
	assertFalse(content.rootCells().get(0).isSelected());
	assertTrue(content.rootCells().get(0).child(0).isSelected());
	
	content.pressKey(UIContent.SELECTION_MODE);
	assertFalse(content.rootCells().get(0).isSelected());
	assertFalse(content.rootCells().get(0).child(0).isSelected());
	
}


@Test
public void testSelectionActivate() {

	content.pressKey(UIContent.SELECTION_MODE);
	content.pressKey("0");
	content.pressKey("0");
	content.pressKey("0");
	UICell root = content.rootCells().get(0);
	UICell data = root.child("row(0)").child("col(0)").child("data(0)");
	assertFalse(data.isSelected());
	assertFalse(data.isActive());

	content.pressKey("0");
	assertTrue(data.isSelected());
	assertFalse(data.isActive());

	content.pressKey(UIContent.ACTIVATE);
	assertFalse(data.isSelected());
	assertTrue(data.isActive());

}


@Test
public void testSelectionClearsActivation() {
	
	// we select and activate, then we select the same cell again, which means that it gets deactivated
	// (no double state is allowed)
	content.pressKey(UIContent.SELECTION_MODE);
	content.pressKey("0");
	content.pressKey("0");
	content.pressKey("0");
	content.pressKey("0");
	content.pressKey("a");
	UICell data = content.rootCells().get(0).child("row(0)").child("col(0)").child("data(0)");
	assertFalse("After activating data cell, it should not be selected anymore", data.isSelected());
	assertTrue("After activating data cell, it should be activated now", data.isActive());
	
	content.pressKey(UIContent.SELECTION_MODE);
	content.pressKey("0");
	content.pressKey("0");
	content.pressKey("0");
	content.pressKey("0");
	assertTrue(data.isSelected());
	assertFalse(data.isActive());
	
}


@Test
public void testSelectionChangesActivation() {
	
	content.pressKey("c");
	content.pressKey("0");
	content.pressKey("0");
	content.pressKey("0");
	content.pressKey("0");
	content.pressKey("a");
	UICell data = content.rootCells().get(0).child("row(0)").child("col(0)").child("data(0)");
	assertFalse(data.isSelected());
	assertTrue(data.isActive());
	
	// select and activate another cell, and check activation is moved to the second cell
	content.pressKey("c");
	content.pressKey("0");
	content.pressKey("0");
	content.pressKey("1");
	content.pressKey("0");
	content.pressKey("0");
	content.pressKey("1");
	UICell data2 = content
				.rootCells().get(0).child("row(0)").child("col(1)").child("row(0)").child("col(0)").child("data2(1)");
	assertFalse(data.isSelected());
	assertTrue(data.isActive());
	assertTrue(data2.isSelected());
	assertFalse(data2.isActive());
	
	content.pressKey("a");
	assertFalse(data.isSelected());
	assertFalse(data.isActive());
	assertFalse(data2.isSelected());
	assertTrue(data2.isActive());
	
}

}
