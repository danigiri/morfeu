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

import static com.codeborne.selenide.Selenide.open;

import org.junit.Test;

import cat.calidos.morfeu.webapp.ui.UICatalogue;
import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UIContent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentUITest extends UITezt {


@Test
public void contentTestAppearingAndDisappearing() {
	
	open(appBaseURL);

	UIContent.shouldNotBeVisible();
	
	UICatalogues catalogues = UICatalogues.openCatalogues().shouldAppear();
	
	UIContent.shouldNotBeVisible();
	
	UICatalogue catalogue = catalogues.clickOn(0);
	UIContent content = catalogue.clickOnDocumentNamed("Document 1").content();
	
	content.shouldBeVisible();
	
	catalogue.clickOnDocumentNamed("Document with non-valid content");
	
	content.shouldDisappear();
	
}


@Test
public void contentTest() {

	open(appBaseURL);

	UIContent content = UICatalogues.openCatalogues()
										.shouldAppear()
										.clickOn(0)
										.clickOnDocumentNamed("Document 1")
										.content();
	
	content.shouldBeVisible();
	
	List<UICell> rootCells = content.rootCells();
	assertNotNull(rootCells);
	assertEquals(1, rootCells.size());
	
}

}
