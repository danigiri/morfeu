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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;

import static com.codeborne.selenide.Selenide.open;

import org.junit.Test;

import cat.calidos.morfeu.webapp.ui.UICatalogue;
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
public class ContentUITest extends UITezt {

@Before
public void setup() {
	
	open(appBaseURL);
	
}

//@Test
public void contentTestAppearingAndDisappearing() {

	UIContent.shouldNotBeVisible();
	
	UICatalogues catalogues = UICatalogues.openCatalogues().shouldAppear();
	
	UIContent.shouldNotBeVisible();
	
	UICatalogue catalogue = catalogues.clickOn(0);
	UIContent content = catalogue.clickOnDocumentNamed("Document 1").content();
	
	content.shouldBeVisible();
	
	catalogue.clickOnDocumentNamed("Document with non-valid content");
	
	content.shouldDisappear();
	
}


//@Test
public void contentTest() {

	UIContent content = UICatalogues.openCatalogues()
										.shouldAppear()
										.clickOn(0)
										.clickOnDocumentNamed("Document 1")
										.content();
	content.shouldBeVisible();
	
	List<UICell> rootCells = content.rootCells();		// TEST
	assertNotNull(rootCells);
	assertEquals(1, rootCells.size());
	
	UICell test1 = rootCells.get(0);					// TEST/*
	assertNotNull(test1);
	assertTrue(test1.isWell());

	UICell row2 = test1.children().get(0);				// TEST/ROW
	assertNotNull(row2);
	assertTrue(row2.isRowWell());

	
	List<UICell> cols3 = row2.children();				// TEST/ROW/*
	assertNotNull(cols3);
	assertEquals(2, cols3.size());
	
	UICell col3a = cols3.get(0);						// TEST/ROW/COL
	assertNotNull(col3a);
	assertTrue(col3a.isColumnWell());

	UICell col3b = cols3.get(1);						// TEST/ROW/COL
	assertNotNull(col3b);
	assertTrue(col3b.isColumnWell());

}


@Test
public void relationshipFromContentToModelTest() {

	String document1URI = "target/test-classes/test-resources/documents/document1.xml";
	
	UIDocument document = UICatalogues.openCatalogues()
										.shouldAppear()
										.clickOn(0)
										.clickOnDocumentNamed("Document 1");
	UIContent content = document.content();
	content.shouldBeVisible();
	UICell test = content.rootCells().get(0);

	// /test/row/col/data
	UICell data = test.child("row(0)").child("col(0)").child("data(0)");
	assertTrue(data.isCell());
	assertEquals(document1URI+"/test(0)/row(0)/col(0)/data(0)", data.id());
	
	data.hover();
	assertTrue(data.isHighlighted());
	
	UIModel model = document.model();
	//test/row/col/data
	UICellModelEntry dataModel = model.rootCellModel("test").get("row").get("col").get("data");
	assertTrue(dataModel.isHighlighted());
	
	UICellModelEntry data2Model =  model.rootCellModel("test").get("row").get("col").get("data2");
	assertFalse(data2Model.isHighlighted());
	
	UICell data2 = test.child("row(0)").child("col(1)").child("row(0)").child("col(1)").child("data2(0)");
	assertFalse(data2.isHighlighted());

	data2.hover();
	assertTrue(data2.isHighlighted());
	assertTrue(data2Model.isHighlighted());
	assertFalse(dataModel.isHighlighted());
	
}


@Test
public void relationshipFromModelToContentTest() {
	
	UIDocument document = UICatalogues.openCatalogues()
			.shouldAppear()
			.clickOn(0)
			.clickOnDocumentNamed("Document 1");
	
	UIContent content = document.content();
	content.shouldBeVisible();
	UICell test = content.rootCells().get(0);
	UIModel model = document.model();
	
	UICellModelEntry rowModel = model.rootCellModel("test").get("row").hover();
	assertTrue(rowModel.isHighlighted());
	
	assertTrue(test.dropArea(0).isActive());
	
}


@Test
public void dropAreasTest() {
	
	UIDocument document = UICatalogues.openCatalogues()
										.shouldAppear()
										.clickOn(0)
										.clickOnDocumentNamed("Document 1");
	UIContent content = document.content();
	content.shouldBeVisible();
	UICell test = content.rootCells().get(0);

	// we check that we have two drop areas, first inactive
	UICell col = test.child("row(0)").child("col(0)");
	List<UIDropArea> dropAreas = col.dropAreas();
	assertEquals(2, dropAreas.size());
	assertEquals(0, dropAreas.stream().filter(UIDropArea::isActive).count());
	
	// we hover over the data cell and we activate both drop areas
	UICell data = col.child("data(0)");
	assertTrue(data.isCell());
	data.hover();
	assertTrue(data.isHighlighted());
	assertEquals(dropAreas.size(), dropAreas.stream().filter(UIDropArea::isActive).count());
	
	
	// in this column we have a data2, which means that we can only have one of those on each col
	col = test.child("row(0)").child("col(1)").child("row(0)").child("col(1)");
	dropAreas = col.dropAreas();
	
	data.hover();
	assertEquals(dropAreas.size(), dropAreas.stream().filter(UIDropArea::isActive).count());
	
	UICell data2 = col.child("data2(0)");
	data2.hover();
	assertEquals(0, dropAreas.stream().filter(UIDropArea::isActive).count());
	
}




}
