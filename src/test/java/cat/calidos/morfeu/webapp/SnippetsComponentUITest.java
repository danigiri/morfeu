package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.webapp.ui.UISnippetEntry;
import cat.calidos.morfeu.webapp.ui.UISnippetsArea;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SnippetsComponentUITest extends UITezt {

@Test @DisplayName("Check snippet categories")
public void testSnippetCategories() {

	open(appBaseURL + "test/snippets-list/display");

	UISnippetsArea snippetsArea = new UISnippetsArea();
	snippetsArea.shouldAppear().shouldBeVisible();
	// we need some time to load the snippets, not ideal but this should give us time
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {}

	List<String> categories = snippetsArea.categories();
	assertAll(
			"checking categories",
			() -> assertNotNull(categories),
			() -> assertEquals(2, categories.size()),
			() -> assertEquals("simple", categories.get(0)),
			() -> assertEquals("complex", categories.get(1)));

	List<UISnippetEntry> simpleSnippets = snippetsArea.snippets();
	assertAll(
			"checking simple",
			() -> assertNotNull(simpleSnippets),
			() -> assertEquals(5, simpleSnippets.size()),
			() -> assertEquals("Stuff 1", simpleSnippets.get(0).name()),
			() -> assertEquals("Data 1", simpleSnippets.get(1).name()),
			() -> assertEquals("Data 2", simpleSnippets.get(2).name()),
			() -> assertEquals("Data 3", simpleSnippets.get(3).name()),
			() -> assertEquals("Data 4", simpleSnippets.get(4).name()));

	snippetsArea.clickOnCategory("complex");
	waitOneSec();
	List<UISnippetEntry> complexSnippets = snippetsArea.snippets();
	assertAll(
			"checking comple",
			() -> assertNotNull(complexSnippets),
			() -> assertEquals(1, complexSnippets.size()),
			() -> assertEquals("Row 6,6", complexSnippets.get(0).name()));

}

}

/*
 * Copyright 2024 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
