// DOCUMENT UI TEST . JAVA

package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.webapp.ui.UICatalogue;
import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UIDocument;
import cat.calidos.morfeu.webapp.ui.UIProblem;



/** Testing document load
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DocumentUITest extends UITezt {


@Test
public void documentBasicDataTest() {
	
	open(appBaseURL);

	// click on catalogue list entry and it appears
	UICatalogues catalogues = UICatalogues.openCatalogues()
											.shouldAppear();
	UICatalogue.shouldNotBeVisible();
	UIProblem.shouldNotBeVisible();
	
	UICatalogue catalogue = catalogues.clickOn(0);
	catalogue.shouldAppear();
	
	UIDocument document = catalogue.clickOnDocumentNamed("Document 1");
	String title = document.title();
	assertTrue(title.contains("Document 1"), "'Document 1' is not titled correltly ("+title+")");
	assertTrue( title.contains("xml"), "'Document 1' is not correctly detected as xml");
	assertEquals("First document", document.desc());
	assertTrue(document.isValid());

	
	document = catalogue.clickOnDocumentNamed("Document with non-valid content");
	title = document.title();
	assertTrue( title.contains("Problematic document"), "'non valid doc' is not titled correctly ("+title+")");
	assertTrue( title.contains("Unknown"), "'non valid doc' is not correctly detected as Unknown");
	assertEquals("Unknown", document.desc());
	assertFalse(document.isValid());
	
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