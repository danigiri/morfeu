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

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UICatalogues {

public static UICatalogues openCatalogues() {

	return new UICatalogues();

}


public UICatalogues shouldAppear() {
	
	$("#catalogue-list").should(appear);

	return this;

}


public ElementsCollection getCatalogueEntries() {
	return $$(".catalogue-list-entry");
}

public UICatalogue clickOn(int i) {

	ElementsCollection catalogueEntries = this.getCatalogueEntries();
	assertTrue("", catalogueEntries.size()<i);
	catalogueEntries.get(i).click();

	return new UICatalogue();

}


}
