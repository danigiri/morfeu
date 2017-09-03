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

import cat.calidos.morfeu.webapp.ui.UICatalogue;
import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICellModelEntry;
import cat.calidos.morfeu.webapp.ui.UIModel;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelUITest extends UITezt {


@Before
public void setup() {	
	open(appBaseURL);
}


//@Test
public void modelTest() {
	
	UICatalogues catalogues = UICatalogues.openCatalogues();
	catalogues.shouldAppear();
	UIModel.shouldNotBeVisible();
	
	UICatalogue catalogue = catalogues.clickOn(0);
	catalogue.shouldAppear();
	UIModel.shouldNotBeVisible();
	
	UIModel model = catalogue.clickOnDocumentNamed("Document 1")
								.getModel();
	model.shouldAppear();
	
	assertEquals("Model: test-model.xsd", model.name());
	assertEquals("Description of test model", model.desc());
	
}


//@Test
public void modelDisappearsOnClickingOtherCatalogue() {	
	
	UICatalogues catalogues = UICatalogues.openCatalogues().shouldAppear();
	UICatalogue catalogue = catalogues.clickOn(0);

	UIModel model = catalogue.clickOnDocumentNamed("Document 1").getModel();
	model.shouldAppear();
	
	// click on a different catalogue model should disappear
	catalogues.clickOn(1);
	model.shouldDisappear();
	
	catalogue = catalogues.clickOn(0);
	UIModel.shouldNotBeVisible();
	
}


//@Test
public void documentWithNonValidModelIsSelected() {	
	
	UICatalogues catalogues = UICatalogues.openCatalogues().shouldAppear();
	UICatalogue catalogue = catalogues.clickOn(0).shouldAppear();

	UIModel model = catalogue.clickOnDocumentNamed("Document 1").getModel();
	model.shouldAppear();
	
	// click on a different document model should change (for instance an problematic doc)
	catalogue.clickOnDocumentNamed("Document with non-valid model").getModel();
	model.shouldDisappear();
	
}


@Test
public void testCellModels() {

	UIModel model = UICatalogues.openCatalogues()
				 					.shouldAppear()
				 					.clickOn(0)
				 					.clickOnDocumentNamed("Document 1")
				 					.getModel()
				 					.shouldAppear();

	List<UICellModelEntry> rootCellModels = model.rootCellModels();
	assertEquals(1, rootCellModels.size());
	
	UICellModelEntry testModelEntry = rootCellModels.get(0);				// TEST
	assertEquals("test", testModelEntry.name());
	assertEquals("Root cell-model desc", testModelEntry.desc());
	
	//TODO: add cell model children testing
	
	testModelEntry.check(cm -> cm.isExpanded(), "Model is not expanded by default")
	.clickOnArrow()
	.check(cm -> cm.isCollapsed(), "Model should collapse when clicked")
	.clickOnArrow();	// let's expand again so we can find the children =)
	
	List<UICellModelEntry> testModelChildren = testModelEntry.children();
	assertEquals(1, testModelChildren.size());
	
	UICellModelEntry rowModel = testModelChildren.get(0);					// TEST/ROW
	assertEquals("row", rowModel.name());
	
	List<UICellModelEntry> rowChildren = rowModel.children();
	assertEquals(1, rowChildren.size());

	UICellModelEntry colModel = rowChildren.get(0);							// TEST/ROW/COL
	assertEquals("col", colModel.name());
	
	List<UICellModelEntry> colChildren = colModel.children();				// TEST/ROW/COL/*
	assertEquals(3, colChildren.size());

}

}
