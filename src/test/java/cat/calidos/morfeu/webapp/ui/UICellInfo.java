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
public class UICellInfo {

public static void shouldNotBeVisible() {
	$("#cell-info").shouldNotBe(visible);
}


public static void shouldAppear() {
	$("#cell-info").should(appear);
}


public String header() {
	return $("#cell-info-header").text();
}


public String desc() {
	return $("#cell-info-model-desc").text();
}


public String URI() {
	return $("#cell-info-model-uri").text();
}


public boolean isFromCell() {
	return $("#cell-info-source").getText().equals("CELL");
}


public boolean isFromModel() {
	return !isFromCell();
}


public List<UIAttributeInfo> attributes() {
	return $$(".attribute-info").stream().map( e -> new UIAttributeInfo(e)).collect(Collectors.toList());
}

// we have * at the end of compulsory
public UIAttributeInfo attribute(String name) {
	return attributes().stream().filter(a -> a.name().equals(name)).findAny().get();
}

}
