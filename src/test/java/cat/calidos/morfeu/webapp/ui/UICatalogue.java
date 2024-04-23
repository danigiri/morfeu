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

import java.util.Optional;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UICatalogue {

private UICatalogue() {}

public static UICatalogue openCatalogue() {
	return new UICatalogue();
}

public static void shouldNotBeVisible() {

	$("#catalogue").shouldNotBe(visible);
	$("#document-list").shouldNotBe(visible);

}


public UICatalogue shouldAppear() {

	$("#catalogue").should(appear);
	$("#document-list").should(appear);

	return this;
	
}


public String getName() {
	return $("#catalogue-name").getText();
} 


public String getDesc() {
	return $("#catalogue-desc").getText();
}


public UIDocument clickOnDocument(int i) {
	
	ElementsCollection documentEntries = getDocuments();
	int count = documentEntries.size();
	if (i>=count) { 
		throw new IndexOutOfBoundsException("Could not click on doc entry "+i+" as there are only "+count);
	}
	documentEntries.get(i).click();
	
	return currentlySelectedDocument();
	
}


public UIDocument clickOnDocumentNamed(String name) {
	
	ElementsCollection documentEntries = getDocuments();
	Optional<SelenideElement> foundDocument = documentEntries.asFixedIterable().stream()
																.filter(d -> d.getText().trim().equals(name))
																.findFirst();
	foundDocument.orElseThrow(() -> new IndexOutOfBoundsException("Could not find document named '"+name+"'")).click();
	
	return currentlySelectedDocument();
	
}


private UIDocument currentlySelectedDocument() {

	UIDocument document = new UIDocument();
	document.shouldBeVisible();
	
	return document;
	
}


public ElementsCollection getDocuments() {
	return $$(".document-list-entry");
}

}
