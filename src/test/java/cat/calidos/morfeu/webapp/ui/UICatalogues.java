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

package cat.calidos.morfeu.webapp.ui;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


public List<UICatalogueEntry> getCatalogueEntries() {
	return $$(".catalogue-list-entry").stream().map( e -> new UICatalogueEntry(e)).collect(Collectors.toList());
}


public UICatalogue clickOn(int i) {

	List<UICatalogueEntry> catalogueEntries = this.getCatalogueEntries();
	int count = catalogueEntries.size();
	assertTrue("Could not click on catalogue entry "+i+" as there are only "+count+" entries", i<count);
	catalogueEntries.get(i).click();

	return new UICatalogue();

}


}
