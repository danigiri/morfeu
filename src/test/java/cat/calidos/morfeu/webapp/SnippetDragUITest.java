// SNIPPET DRAG UI TEST . JAVA

package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;

import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UIDocument;
import cat.calidos.morfeu.webapp.ui.UIDropArea;
import cat.calidos.morfeu.webapp.ui.UISnippetEntry;
import cat.calidos.morfeu.webapp.ui.UISnippetsArea;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SnippetDragUITest extends UITezt {


@Test
public void testDragSnippetCell() { 

	open(appBaseURL);

	UIDocument document = UICatalogues.openCatalogues()
										.shouldAppear()
										.clickOn(0)
										.clickOnDocumentNamed("Document 1");

	// let's look for the snipept we want
	UISnippetsArea snippets = document.snippets();
	snippets.shouldBeVisible();

	UISnippetEntry stuffSnippet = snippets.snippet(0);
	assertNotNull(stuffSnippet);

	assertEquals("Stuff 1", stuffSnippet.name());

	// now we select. activate and drag it to the drop area
	stuffSnippet.select();
	assertTrue("Snippet is not selected after selection", stuffSnippet.isSelected());

	UICell stuff = stuffSnippet.children().get(0);
	assertNotNull(stuff);

	stuffSnippet.pressKey("0");	// this is a hack to select the first cell on the snippet
	assertTrue(stuff.isSelected());

	stuff.activate();
	assertTrue(stuff.isActive());

	UICell targetCol = document.content().rootCells().get(0).child("row(0)").child("col(0)");
	assertEquals("Initially we should have one child", 1, targetCol.children().size());
	UIDropArea targetDropArea = targetCol.dropArea(1);
	targetDropArea.select();
	targetDropArea.dropHere(stuff);

	targetCol = document.content().rootCells().get(0).child("row(0)").child("col(0)");

	UICell stuffFromSnippet = targetCol.child("stuff(1)");
	Optional<String> value = stuffFromSnippet.select().activate().cellInfo().value();
	assertTrue("Snippet created 'stuff' should have a value", value.isPresent());
	assertEquals("Stuff content", value.get());

}


}

/*
 *    Copyright 2019 Daniel Giribet
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

