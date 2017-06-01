/*
 *    Copyright 2016 Daniel Giribet
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

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CatalogueUITest extends UITezt {

private static final int CATALOGUE_SIZE = 3;

@Test
public void catalogueListTest() throws Exception {

	// catalogue list appears and has three entries
	open(appBaseURL);

	ElementsCollection catalogueEntries = UICatalogues.openCatalogues()
													  .shouldAppear()
													  .getCatalogueEntries();
	assertEquals("Wrong catalogue content", "Catalogue 1", catalogueEntries.get(0).getText());
	assertEquals("Wrong catalogue content", "Catalogue 2", catalogueEntries.get(1).getText());
	assertEquals("Wrong catalogue content", "Catalogue not found", catalogueEntries.get(2).getText());
	catalogueEntries.shouldHaveSize(CATALOGUE_SIZE);
	UIProblem.shouldNotBeVisible();
	
}


@Test
public void catalogueDetailTest() throws Exception {

	open(appBaseURL);

	UICatalogue catalogue = clickOnCatalogue(0);

	assertEquals("Wrong catalogue selected", "Catalogue 1", catalogue.getName());
	assertEquals("Wrong catalogue selected", "First Catalogue", catalogue.getDesc());
	
	// test listing of documents
	ElementsCollection documentEntries = catalogue.getDocuments();
	documentEntries.shouldHaveSize(4);
	assertEquals("Wrong catalogue content", "Document 1\nxml", documentEntries.get(0).getText());
	assertEquals("Wrong catalogue content", "Document with non-valid content\nxml", documentEntries.get(1).getText());
	assertEquals("Wrong catalogue content", "Document with non-valid model\nxml", documentEntries.get(2).getText());
	assertEquals("Wrong catalogue content", "Document with not-found content\nyaml", documentEntries.get(3).getText());
	
}


@Test
public void catalogueDetailErrorTest() {
	
	// notice here we are not using the helper method to open a catalogue given that they don't expect errors
	open(appBaseURL);
	UICatalogues catalogues = UICatalogues.openCatalogues()
			  .shouldAppear();
	UICatalogue.shouldNotBeVisible();
	UIProblem.shouldNotBeVisible();
	
	ElementsCollection catalogueEntries = catalogues.getCatalogueEntries();
	catalogueEntries.get(2).click();

	UIProblem problem = UIProblem.problem().shouldAppear();
	assertTrue(problem.getText().contains("Not Found"));

	catalogueEntries.get(0).click();
	problem.shouldDisappear();
	
}

}
