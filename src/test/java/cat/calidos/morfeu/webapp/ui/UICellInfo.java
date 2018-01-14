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

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UICellInfo extends UIWidget<UICellInfo>{

UICellInfo() {
	super($(".cell-data"));
}

public static void shouldNotBeVisible() {
	$(".cell-data").shouldNotBe(visible);
}


public String header() {
	return element.$(".cell-data-header").text();
}


public String desc() {
	return element.$(".cell-data-model-desc").text();
}


public String URI() {
	return element.$(".cell-data-model-uri").text();
}


public boolean isFromCell() {
	return element.$(".cell-data-source").getText().equals("CELL");
}


public boolean isFromModel() {
	return !isFromCell();
}


public List<UIAttributeInfo> attributes() {
	return element.$$(".attribute-info").stream().map( e -> new UIAttributeInfo(e)).collect(Collectors.toList());
}


// we have * at the end of compulsory
public UIAttributeInfo attribute(String name) {
	return attributes().stream().filter(a -> a.name().equals(name)).findAny().get();
}

}
