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

package cat.calidos.morfeu.webapp.ui;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UICatalogues {

private static final String CATALOGUE_LIST = "#catalogue-list";
private static final String CATALOGUE_LIST_ENTRY = ".catalogue-list-entry";

private UICatalogues() {}

public static UICatalogues openCatalogues() {

	return new UICatalogues();

}


public UICatalogues shouldAppear() {
	
	$(CATALOGUE_LIST).should(appear);

	return this;

}


public UICatalogues shouldBeVisible() {

	$(CATALOGUE_LIST).shouldBe(visible);
	
	return this;
}


public List<UICatalogueEntry> allCatalogueEntries() {
	return $$(CATALOGUE_LIST_ENTRY).stream().map(e -> new UICatalogueEntry(e)).collect(Collectors.toList());
}


// convenience method
public UICatalogue clickOn(int i) {

	List<UICatalogueEntry> catalogueEntries = this.allCatalogueEntries();
	int count = catalogueEntries.size();
	if (i>=count) { 
		throw new IndexOutOfBoundsException("Could not click on catalogue entry "+i+" as there are only "+count);
	}
	catalogueEntries.get(i).click();

	return UICatalogue.openCatalogue();

}

}
