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

import org.junit.Test;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelUITest extends UITezt {


@Test
public void modelTest() {
	
	open(appBaseURL);

	UICatalogues catalogues = UICatalogues.openCatalogues()
											.shouldAppear();
	UIModel.shouldNotBeVisible();
	
	UICatalogue catalogue = catalogues.clickOn(0);
	catalogue.shouldAppear();
	UIModel.shouldNotBeVisible();
	
	UIModel model = catalogue.clickOnDocumentNamed("Document 1")
								.getModel()
								.shouldAppear();
	
	assertEquals("Model: /test-model.xsd", model.name());
	assertEquals("Description of test model", model.desc());
	
}

}
