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
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UICellEditor;
import cat.calidos.morfeu.webapp.ui.UIContent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellEditingUITest extends UITezt {

private UICell test;
private UIContent content;

@Before
public void setup() {
	
	open(appBaseURL);
	content = UICatalogues.openCatalogues()
			.shouldAppear()
			.clickOn(0)
			.clickOnDocumentNamed("Document 1")
			.content();
	content.shouldBeVisible();
	test = content.rootCells().get(0);
	
}


@Test
public void editCellAndSave() {
	
	UICell data = test.child("row(0)").child("col(0)").child("data(0)");
	assertNotNull(data);
	data.select();
	UICellEditor.shouldNotBeVisible();
	data.activate();
	UICellEditor.shouldNotBeVisible();

	UICellEditor dataEditor = data.editor().shouldAppear();
	
	
	
	
	
}


@Test
public void editCellAndDismiss() {
	
	
}


}
