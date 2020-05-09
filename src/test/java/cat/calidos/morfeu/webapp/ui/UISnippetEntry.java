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
import java.util.Optional;
import java.util.stream.Collectors;

import com.codeborne.selenide.SelenideElement;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UISnippetEntry extends UIWidget<UISnippetEntry> {

private UISnippetsArea snippets;


public UISnippetEntry(SelenideElement e, UISnippetsArea snippets) {

	super(e);

	this.snippets = snippets;

}


public String name() {
	return element.$(".snippet-name").getText();
}


public int position() {
	return Integer.valueOf(element.attr("_position"));
}


public boolean isSelected() {
	return element.attr("class").contains("snippet-selected");
}


// we (ab)use the flexible constructor so the content is the snippets list, which is in fact
// a content area
public List<UICell> children() {
	return element.$$(".cell-level-0").stream()
			.map(e -> new UICell(e, new UIContent(snippets.element), Optional.empty(), 0))
			.collect(Collectors.toList());
}


public UISnippetEntry select() {

	snippets.pressKey(UISnippetsArea.SNIPPETS_KEY);
	snippets.pressKey("0");							// at the moment we can only select from the first category
	snippets.pressKey(position()+"");

	return this;

}


public UISnippetEntry activate() {

	if (this.isSelected()) {
		snippets.pressKey(UISnippetsArea.ACTIVATE_KEY);
	}

	return this;

}


}
