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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.NoSuchElementException;

import com.codeborne.selenide.SelenideElement;


/** 
* http://localhost:8080/morfeu/models/target/test-classes/test-resources/models/test-model.xsd
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UICell extends UIWidget<UICell> {

private static final String ACTIVE = "cell-active";
private static final String COL_WELL = "col-well";
private static final String WELL = "well";
private static final String ROW_WELL = "row-well";
private static final String SELECTED = "cell-selected";

private UIContent content;
private Optional<UICell> parent;
private int level;

public UICell(SelenideElement element, UIContent content, Optional<UICell> parent, int level) {

	super(element);

	this.content = content;
	this.parent = parent;
	this.level = level;
	
}

/** id is where we store the cell URI*/
public String id() {
	return element.attr("id");
}

public int level() {
	return this.level;
}

public Optional<UICell> parent() {
	return this.parent;
}


public int position() {

	String URI = id();	// we reuse the fact that at the end of the uri we have the position like 'foo(0)'
	return Integer.parseInt(URI.substring(URI.lastIndexOf("(")+1, URI.length()-1));

}


public String img() {
	return element.attr("src");
}


public List<UIDropArea> dropAreas() {
	
	List<UIDropArea> dropAreas = element.$$(".drop-area").stream()
									.map(e -> new UIDropArea(e, content, this))
									.collect(Collectors.toList());
	int i = 0;
	for (UIDropArea da: dropAreas) {
		da.setPosition(i++);
	}
	
	return dropAreas;

}


public UIDropArea dropArea(int i) {
	return dropAreas().get(i);
}


public List<UICell> children() {
	return element.$$(".cell-level-"+(level+1)).stream()
			.map(e -> new UICell(e, content, Optional.of(this), level+1))
			.collect(Collectors.toList());
}


// clever-ish: uris are doc/foo(0)/bar(0), to look for 'bar(0)' we check for "/bar(0)$"
public UICell child(String name) {
	return children().stream().filter(c -> c.id().endsWith("/"+name)).findAny().get();
}


public UICell child(int pos) {
	return children().get(pos);
}




public UICell dragTo(UIDropArea target) {
	return target.dropHere(this);
}


public UICell click() {

	element.click();
	
	return this;
	
}


public UICell select() {
	
	// first we build a list of the tree nodes we use to reach this destination and we will be selecting them in turn
	content.pressKey(UIContent.SELECTION_MODE);
	LinkedList<UICell> path = new LinkedList<UICell>();
	UICell parentVisitor = this;
	while (parentVisitor!=null) {
		path.push(parentVisitor);	// notice we are inserting, so it has the right order (and not reverse)
		parentVisitor = parentVisitor.parent().orElse(null);
	}
	
	// activate selection mode and then select each in turn
	content.pressKey(UIContent.SELECTION_MODE);
	path.stream().forEachOrdered(c -> content.pressKey(c.position()+""));

	return this;
	
}


public UICell activate() {

	if (this.isSelected()) {
		content.pressKey(UIModel.ACTIVATE_KEY);
	}

	return this;
	
}


public boolean isCell() {
	return class_().contains("cell-img");
}

public boolean isWell() {
	
	element.$(".well");	// wait for dom updates
	String class_ = class_();
	
	return class_.contains(WELL) && !class_.contains(ROW_WELL);
	
}


public boolean isRowWell() {
	
	element.$(".row-well");	// wait for dom updates

	return class_().contains(ROW_WELL);
	
}

public boolean isColumnWell() {
	
	element.$(".col-well");	// wait for dom updates

	return class_().contains(COL_WELL);
	
}


public boolean isActive() {
	//element.waitUntil(cssClass(ACTIVE), 100);
	//return element.shouldHave(cssClass(ACTIVE)).exists();

	// this keeps failing randomly
	try {
		Thread.sleep(50);
	} catch (InterruptedException e) {}
	
	return class_().contains(ACTIVE);
}


public boolean isSelected() {
	return class_().contains(SELECTED);
}


public UICellData cellInfo() {
	
	if (!isActive()) {
		throw new NoSuchElementException("Trying the to get the info of a not active cell");

	}
	
	return new UICellData();	// at the moment there is only one info

}


public UICellEditor edit() {

	if (!isActive()) {
		throw new NoSuchElementException("Trying the to get the editor of a not active cell");

	}
	content.pressKey("e");
	
	return new UICellEditor(content);
	
}


}
