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

import org.junit.Before;
import org.junit.Test;

import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UICellEditor;
import cat.calidos.morfeu.webapp.ui.UIContent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellEditingNewValueUITest extends UITezt {

private UICell test;
private UIContent content;

@Before
public void setup() {

	open(appBaseURL);
	content = UICatalogues.openCatalogues()
			.shouldAppear()
			.clickOn(0)
			.clickOnDocumentNamed("Document 3")
			.content();
	content.shouldBeVisible();
	test = content.rootCells().get(0);

}


@Test
public void removeCellValue() {

	//target/test-classes/test-resources/documents/document3.xml/test(0)/row(0)/col(0)/stuff(0)

	UICell stuff = test.child("row(0)").child("col(0)").child("stuff(0)").select().activate();
	assertNotNull(stuff);

	UICellEditor stuffEditor = stuff.edit().shouldAppear();
	assertNotNull(stuffEditor);
	assertFalse("Should not be able to create a value for this cell",  stuffEditor.isCreateValueVisible());
	assertTrue("Should be able to remove value for this cell",  stuffEditor.isRemoveValueVisible());

}


}
