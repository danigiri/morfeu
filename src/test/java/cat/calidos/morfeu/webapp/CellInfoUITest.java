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

import org.junit.Before;
import org.junit.Test;

import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UICellInfo;
import cat.calidos.morfeu.webapp.ui.UIContent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellInfoUITest extends UITezt {

@Before
public void setup() {
	open(appBaseURL);
}

@Test
public void checkCelInfo() {
	
	UIContent content = UICatalogues.openCatalogues()
			.shouldAppear()
			.clickOn(0)
			.clickOnDocumentNamed("Document 1")
			.content();
	content.shouldBeVisible();

	// target/test-classes/test-resources/documents/document1.xml/test(0)/row(0)/col(0)/data(0)
	UICell test = content.rootCells().get(0);
	UICell data = test.child("row(0)").child("col(0)").child("data(0)");
	assertNotNull(data);
	
	UICellInfo.shouldNotBeVisible();
	data.hover();
	
	UICellInfo dataInfo = data.cellInfo();
	assertNotNull(dataInfo);
	String header = dataInfo.header();
	assertTrue("Bad information cell, we did not get 'data' in the cell info header", header.contains("data"));
	assertTrue("Bad information cell, should have 0 to ∞ as the cardinality of 'data'", header.contains("[0..∞]"));

	String desc = dataInfo.desc();
	String expectedDesc = "Globally provided description of 'data'";
	assertTrue("Bad information cell, does not have the correct description", desc.contains(expectedDesc));
	
}


//@Test
public void checkCellModelInfo() {}

}
