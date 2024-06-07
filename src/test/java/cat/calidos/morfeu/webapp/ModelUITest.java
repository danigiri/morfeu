// MODEL UI TEST . JAVA

package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.webapp.ui.UICatalogue;
import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICellModelEntry;
import cat.calidos.morfeu.webapp.ui.UIModel;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelUITest extends UITezt {


@BeforeEach
public void setup() {
	open(appBaseURL);
}


@Test
public void modelTest() {

	UICatalogues catalogues = UICatalogues.openCatalogues()
											.shouldAppear();
	UIModel.shouldNotBeVisible();

	UICatalogue catalogue = catalogues.clickOn(0);
	catalogue.shouldAppear();
	UIModel.shouldNotBeVisible();

	UIModel model = catalogue.clickOnDocumentNamed("Document 1").model();
	model.shouldAppear();
	assertEquals("Model at: .../test-model.xsd", model.name());
	assertEquals("Description of test model", model.desc());

}


@Test
public void modelDisappearsOnClickingOtherCatalogue() {	

	UICatalogues catalogues = UICatalogues.openCatalogues().shouldAppear();
	UICatalogue catalogue = catalogues.clickOn(0);

	UIModel model = catalogue.clickOnDocumentNamed("Document 1")
								.model()
								.shouldAppear();

	// click on a different catalogue model should disappear
	catalogues.clickOn(1);
	model.shouldDisappear();

	catalogue = catalogues.clickOn(0);
	UIModel.shouldNotBeVisible();

}


@Test
public void documentWithNonValidModelIsSelected() {	

	UICatalogues catalogues = UICatalogues.openCatalogues().shouldAppear();
	UICatalogue catalogue = catalogues.clickOn(0).shouldAppear();

	UIModel model = catalogue.clickOnDocumentNamed("Document 1").model();
	model.shouldAppear();

	// click on a different document model should change (for instance an problematic doc)
	catalogue.clickOnDocumentNamed("Document with non-valid model");
	model.shouldDisappear();

}


@Test
public void testCellModels() {

	UIModel model = UICatalogues.openCatalogues()
									.shouldAppear()
									.clickOn(0)
									.clickOnDocumentNamed("Document 1")
									.model()
									.shouldAppear();

	List<UICellModelEntry> rootCellModels = model.rootCellModels();
	assertEquals(1, rootCellModels.size());

	UICellModelEntry testModelEntry = rootCellModels.get(0);				// TEST
	assertEquals("test", testModelEntry.name());
	//assertEquals("Root cell-model desc", testModelEntry.desc());
	assertTrue(testModelEntry.thumb().endsWith("assets/images/test-thumb.svg"));

	//TODO: add cell model children testing
	assertTrue(testModelEntry.isExpanded(), "Model is not expanded by default");
	// selenium finds the expand button, but it complains it's not clickable, we disable this for now as it's not core
	// functionality
	//	testModelEntry.clickOnArrow();
	//	assertTrue(testModelEntry.isCollapsed(), "Model should collapse when clicked");
	// testModelEntry.clickOnArrow();	// let's expand again so we can find the children =)

	List<UICellModelEntry> testModelChildren = testModelEntry.children();
	assertEquals(1, testModelChildren.size());

	UICellModelEntry rowModel = testModelChildren.get(0);					// TEST/ROW
	assertEquals("row", rowModel.name());
	assertTrue(rowModel.thumb().endsWith("assets/images/row-thumb.svg"));

	List<UICellModelEntry> rowChildren = rowModel.children();
	assertEquals(1, rowChildren.size());

	UICellModelEntry colModel = rowChildren.get(0);							// TEST/ROW/COL
	assertEquals("col", colModel.name());
	assertTrue(colModel.thumb().endsWith("assets/images/col-thumb.svg"));

	List<UICellModelEntry> colChildren = colModel.children();				// TEST/ROW/COL/*
	int colChildrenSize = colChildren.size();
	int expectedCount = ModelTezt.EXPECTED_COL_CHILDREN_COUNT;
	assertEquals( expectedCount, colChildrenSize, "col model should have 10 children, not "+colChildrenSize);

	assertEquals("stuff", colChildren.get(0).name());
	assertEquals("data", colChildren.get(1).name());
	UICellModelEntry data2 = colChildren.get(2);
	assertEquals("data2", data2.name());
	assertTrue(data2.thumb().endsWith("assets/images/data2-thumb.svg"));

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
