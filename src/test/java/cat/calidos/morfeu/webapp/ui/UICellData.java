// UI CELL DATA . JAVA

package cat.calidos.morfeu.webapp.ui;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.Keys;

import com.codeborne.selenide.SelenideElement;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UICellData extends UIWidget<UICellData>{


UICellData() {
	super($(".cell-data"));
}


public UICellData(SelenideElement e) {
	super(e);
}


public static void shouldNotBeVisible() {
	$(".cell-data").shouldNotBe(visible);
}


public String header() {
	return element.$(".cell-header").text();
}


public String desc() {
	return element.$(".cell-header-desc").text();
}


public String URI() {
	return element.$(".cell-header-uri").text();
}


/** @return Does the data come from a cell? (always true if it's an editor), false if from a model */
public boolean isFromCell() {
	return class_().contains("cell-data-source-cell") || isFromEditor();
}


public boolean isFromModel() {
	return !isFromCell();
}


public boolean isFromEditor() {
	return element.attr("id").contains("editor");
}


public List<UIAttributeData> attributes() {
	return attributesOfClass(".attribute-data");
}


// we have * at the end of compulsory
public UIAttributeData attribute(String name) {
	return attributes().stream().filter(a -> a.name().equals(name)).findAny().get();
}


public List<UIAttributeData> notPresentAttributes() {

	if (!isFromEditor()) {
		throw new UnsupportedOperationException("Can't access not-present attributes in info view");
	}

	return attributesOfClass(".attribute-not-present");

}


public Optional<UIAttributeData> notPresentAttribute(String name) {
	return notPresentAttributes().stream().filter(a -> a.name().equals(name)).findAny();
}



public Optional<String> value() {
	return Optional.ofNullable(element.$(".cell-data-value").getValue());
}


private List<UIAttributeData> attributesOfClass(String class_) {
	return element.$$(class_).stream().map( e -> new UIAttributeData(e, this)).collect(Collectors.toList());
}


}

/*
 *    Copyright 2019 Daniel Giribet
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
