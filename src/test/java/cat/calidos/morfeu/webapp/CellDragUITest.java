// CELL DRAG UI TEST . JAVA

package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UIContent;

/** Manipulate cell position
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellDragUITest extends UITezt {


@Before
public void setup() {
	open(appBaseURL);
}


@Test
public void testDragCell() {
	
	UIContent content = UICatalogues.openCatalogues()
										.shouldAppear()
										.clickOn(0)
										.clickOnDocumentNamed("Document 1")
										.content();
	content.shouldBeVisible();
	
	UICell test = content.rootCells().get(0);

	// source col has one data child
	UICell sourceCol = test.child("row(0)").child("col(0)");
	assertEquals(1, sourceCol.children().size());
	UICell data = sourceCol.child("data(0)");
	assertTrue(data.isCell());
	
	// col(1) here has two data2 children, we'll drop the data into the middle
	UICell targetCol = test.child("row(0)").child("col(1)").child("row(0)").child("col(1)");
	assertEquals(2, targetCol.children().size());
	
	data.dragTo(targetCol.dropArea(1));				// drop in the middle
	assertEquals(3, targetCol.children().size());

	// we check that we effectively put it in the middle
	assertNotNull(targetCol.child("data2(0)"));
	assertNotNull(targetCol.child("data(1)"));
	assertNotNull(targetCol.child("data2(2)"));

	// we also check that the source col has no children anymore
	assertTrue(sourceCol.children().isEmpty());
	
}


@Test
public void testDragCellSoOtherCellsChangeTheirURIs() {
	
	
	UIContent content = UICatalogues.openCatalogues()
										.shouldAppear()
										.clickOn(0)
										.clickOnDocumentNamed("Document 2")
										.content();
	content.shouldBeVisible();
	
	// we drag the first cell to the parent row, to trigger quite a different set of URI changes
	// /test(0)/row(0)
	//			/col(0)	[targetCol]
	//										<-- (here) -----------------------------------------------\
	//				/row(0)	(this will become row(1) as 'data' will come first)						|	
	//					/col(0)	[sourceCol]															|
	//						/data(0) * this goes ------------------------------------------------------/
	//						/data2(1)	(this one will end up with a different uri too, as parent and order changed)
	//
	// Outcome:
	// /test(0)/row(0)
	//			/col(0)	[targetCol]
	//						/data(0) * this goes
	//						/row(1)	[displacedRow]	
	//							/col(0)	[sourceCol]
	//								/data2(0)	
	
	
	UICell targetCol = content.rootCells().get(0).child("row(0)").child("col(0)");
	assertNotNull(targetCol);
	assertEquals("Target column should only have one child (row) at the start", 1, targetCol.children().size());
	
	UICell sourceCol = targetCol.child("row(0)").child("col(0)");
	assertNotNull(sourceCol);
	assertEquals("Source column should have two child (data and data2) at the start", 2, sourceCol.children().size());
	
	UICell data = sourceCol.child("data(0)");
	assertNotNull(data);
	UICell data2 = sourceCol.child("data2(1)");
	assertNotNull(data2);
	
	data.dragTo(targetCol.dropArea(0));
	assertEquals("Target column should have two children after drag", 2, targetCol.children().size());

	data = targetCol.child("data(0)");	// newly dropped data cell
	assertNotNull(data);
	
	UICell displacedRow = targetCol.child("row(1)");
	assertNotNull("Displaced row is now at position (1)", displacedRow);

	sourceCol = displacedRow.child("col(0)"); // we re-read the source col as it now has a different location
	assertEquals("Source column should have 1 child after drag", 1, sourceCol.children().size());
	data2 = sourceCol.child("data2(0)");
	assertNotNull(data2);

}


@Test
public void testDragCellFromLast() {

	// BUG: if we drag from the last position two times , the element gets duplicated instead of dragged
	// Start:
	// data
	// data2
	//
	// Next:
	// <-------\
	// data    |
	// data2 --/
	//
	// Put it back:
	// data2 --\
	// data    |
	// <-------/
	//
	// And again:
	// <-------\
	// data    |
	// data2 --/
	//
	// This ends up with:
	// data2
	// data
	// data2
	// We need to be careful here, as we're moving elements at the end of the children list, so the destination position
	// changes while we're moving the cell, as the children list has one less element while the drag happens
	// In effect, the position is position-1 (the children count while the dragged cell is in 'the air' and an orphan)
	
	UIContent content = UICatalogues.openCatalogues()
										.shouldAppear()
										.clickOn(0)
										.clickOnDocumentNamed("Document 1")
										.content();
	content.shouldBeVisible();

	// source col has two children: data and data2
	UICell col = content.rootCells().get(0).child("row(0)").child("col(1)").child("row(0)").child("col(0)");
	assertEquals(2, col.children().size());
	
	// Next:
	UICell data2 = col.child("data2(1)");
	data2.dragTo(col.dropArea(0));
	assertEquals("Target column should still have two children after drag", 2, col.children().size());

	// Put it back
	data2 = col.child("data2(0)");
	data2.dragTo(col.dropArea(2));
	assertEquals("Target column should still have two children after 2nd drag", 2, col.children().size());

	// And again:
	col = content.rootCells().get(0).child("row(0)").child("col(1)").child("row(0)").child("col(0)");
	data2 = col.child("data2(1)");
	data2.dragTo(col.dropArea(0));
	assertEquals("Target column should still have two children after 3rd drag", 2, col.children().size());

}


}

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

