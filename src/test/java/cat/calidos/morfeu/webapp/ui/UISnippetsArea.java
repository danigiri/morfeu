// UI SNIPPETS AREA . JAVA
package cat.calidos.morfeu.webapp.ui;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.codeborne.selenide.Condition;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UISnippetsArea extends UIWidget<UISnippetsArea> {

private static final String SNIPPET_CATEGORY_POSTFIX = "-snippet-category";

public static final String	SNIPPETS_KEY	= "s";
public static final String	ACTIVATE_KEY	= "a";

public UISnippetsArea() {
	super($("#snippets"));
}


public static void shouldNotBeVisible() {
	$("#snippets").shouldNotBe(visible);
}


public static void shouldFinishLoading() {
	$("#snippets-loading-status").shouldNotBe(visible);
}


public void shouldExpand(String name) {
	String id = "#" + name + SNIPPET_CATEGORY_POSTFIX;
	element.$(id).shouldNotHave(Condition.attribute("collapsed"));
}


public boolean isExpanded(String name) {
	return !isCollapsed(name);
}


public boolean isCollapsed(String name) {
	String id = "#" + name + SNIPPET_CATEGORY_POSTFIX;
	return $(id).$(".snippet-category-name").attr("class").contains("collapsed");
}


public List<String> categories() {
	// looks like the accordion implementation we are using assigns the name of the category as ID,
	// which can create collisions. So we are using names for ease of debugging, but note that the
	// snippet categories have a more collision-resistant id, we'll use the id explicitly in those
	// cases
	return element
			.$$(".snippet-category-name")
			.asFixedIterable()
			.stream()
			.map(e -> e.text())
			.collect(Collectors.toList());
}


public UISnippetsArea clickOnCategory(String name) {

	Optional<String> category = categories().stream().filter(c -> c.equals(c)).findAny();

	if (category.isEmpty()) {
		throw new NoSuchElementException("No snippet category with name " + name);
	}

	String id = "#" + name + SNIPPET_CATEGORY_POSTFIX;
	element.$(id).scrollTo().$(".snippet-category-name").click();

	return this;

}


public List<UISnippetEntry> snippets() {
	return $$(".snippet")
			.asFixedIterable()
			.stream()
			.map(e -> new UISnippetEntry(e, this))
			.collect(Collectors.toList());
}


public UISnippetEntry snippet(int pos) {
	return snippets().get(pos);
}


public Optional<UISnippetEntry> snippet(String name) {
	return snippets().stream().filter(s -> s.name().equals(name)).findAny();
}

}

/*
 * Copyright 2024 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
