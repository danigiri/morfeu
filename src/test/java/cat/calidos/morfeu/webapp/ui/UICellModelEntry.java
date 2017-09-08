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

import java.util.List;
import java.util.stream.Collectors;

import com.codeborne.selenide.SelenideElement;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UICellModelEntry extends UIWidget<UICellModelEntry> {

private static final String THUMB = "src";
private int level;


public UICellModelEntry(SelenideElement e, int level) {

	super(e);
	
	this.level = level;

}


public String name() {
	return element.$(".cell-model-name").getText();
}


public String desc() {
	return element.$(".cell-model-desc").getText();
}


public String thumb() {
	return element.$(".cell-model-thumb").getAttribute(THUMB);
}


public UIWidget<UICellModelEntry> click() {
	
	element.click();
	
	return this;
	
}


public UIWidget<UICellModelEntry> clickOnArrow() {
	
	element.$(".toggle-children").click();
	
	return this;
	
}


public boolean isCollapsed() {
	element.$(".tree-node-collapsed");	// wait for dom updates
	return element.attr("class").contains("tree-node-collapsed");
}

public boolean isExpanded() {
	element.$(".tree-node-expanded");	// wait for dom updates
	return element.attr("class").contains("tree-node-expanded");
}

public List<UICellModelEntry> children() {	
	return element.$$(".tree-node-level-"+(level+1))
					.stream()
					.map(e -> new UICellModelEntry(e, level+1) ).collect(Collectors.toList());
}


}
