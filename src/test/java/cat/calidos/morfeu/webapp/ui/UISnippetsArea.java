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

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UISnippetsArea extends UIWidget<UISnippetsArea> {


public static final String SNIPPETS_KEY = "s";
public static final String ACTIVATE_KEY = "a";


public UISnippetsArea() {
	super($("#snippets"));
}


public static void shouldNotBeVisible() {
	$("#snippets-tab").shouldNotBe(visible);
}


public List<String> categories() {
	return element.$$("div[role=tab]")
			.asFixedIterable().stream()
					.filter(e -> e.attr("class")!=null)
					.filter(e -> e.attr("id").endsWith("-header"))
					.map(e -> e.text())
					.collect(Collectors.toList());
}


public UISnippetsArea clickOnCategory(String name) {

	Optional<String> category = categories().stream().filter(c -> c.equals(c)).findAny();
	if (category.isEmpty()) {
		throw new NoSuchElementException("No snippet category with name "+name);
	}


	element.$("#"+name+"-header").scrollTo().$("button").click();

	return this;

}


public List<UISnippetEntry> snippets() {
	return $$(".snippet").stream().map(e -> new UISnippetEntry(e, this)).collect(Collectors.toList());
}




public UISnippetEntry snippet(int pos) {
	return snippets().get(pos);
}


public Optional<UISnippetEntry> snippet(String name) {
	return snippets().stream().filter(s -> s.name().equals(name)).findAny();
}


}