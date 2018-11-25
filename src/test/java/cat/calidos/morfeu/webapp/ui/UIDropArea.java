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

import com.codeborne.selenide.SelenideElement;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UIDropArea {

private static final String CLASS = "class";
private static final String ACTIVE = "drop-area-active";
private static final String SELECTED = "drop-area-selected";


private SelenideElement element;
private UICell parent;
private UIContent content;
private int position;

public UIDropArea(SelenideElement e, UIContent content, UICell parent) {

	this.element = e;
	this.content = content;
	this.parent = parent;
	
}


public boolean isActive() {
	return element.attr(CLASS).contains(ACTIVE);
}


public boolean isSelected() {
	return element.attr(CLASS).contains(SELECTED);
}


public void setPosition(int position) {
	this.position = position;
}


public int position() {
	return this.position;
}


/** @return cell just dropped at new position */
public UICell dropHere(UICell cell) {

	// we will use keyboard shorcuts to select the target destination
	cell.select();
	content.pressKey(UIContent.ACTIVATE);
	
	this.select();
	content.pressKey(UIContent.DRAGNDROP);
	
	// if we have dropped at the end, the position is off by one
	int childrenCount = parent.children().size();
	if (position>=childrenCount) {
		return parent.child(childrenCount-1);
	} else {
		return parent.child(position);
	}
}


public UIDropArea select() {

	// select until the parent, setup drop area selection mode, select this drop area and drag-n-drop!!!
	parent.select();
	content.pressKey(UIContent.DROPAREA_MODE);
	content.pressKey(position+"");
		
	return this;
	
}

}
