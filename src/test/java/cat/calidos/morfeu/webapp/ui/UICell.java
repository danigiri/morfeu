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
public class UICell extends UIWidget<UICell> {

private static final String ACTIVE = "cell-active";
private static final String CLASS = "class";
private static final String COL_WELL = "col-well";
private static final String WELL = "well";
private static final String ROW_WELL = "row-well";
private int level;

public UICell(SelenideElement element, int level) {

	super(element);

	this.level = level;

}

public UICell hover() {

	element.hover();
	
	return this;
	
}

public boolean isCell() {
	return element.attr(CLASS).contains("cell-img");
}

public boolean isWell() {
	
	element.$(".well");	// wait for dom updates
	String class_ = element.attr(CLASS);
	
	return class_.contains(WELL) && !class_.contains(ROW_WELL);
	
}


public boolean isRowWell() {
	
	element.$(".row-well");	// wait for dom updates

	return element.attr(CLASS).contains(ROW_WELL);
	
}

public boolean isColumnWell() {
	
	element.$(".col-well");	// wait for dom updates

	return element.attr(CLASS).contains(COL_WELL);
	
}


public List<UICell> children() {
	return element.$$(".cell-level-"+(level+1)).stream().map(e -> new UICell(e, level+1)).collect(Collectors.toList());
}

// clever hack: uris are doc/foo(0)/bar(0), to look for 'bar(0)' we go for "/bar(0)$"
public UICell child(String name) {
	return children().stream().filter(c -> c.id().endsWith("/"+name)).findAny().get();
}

/** id is where we store the cell URI*/
public String id() {
	return element.attr("id");
}


public boolean isHighlighted() {
	return element.attr(CLASS).contains(ACTIVE);
}

}
