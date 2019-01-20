// UI ATTRIBUTE DATA . JAVA

package cat.calidos.morfeu.webapp.ui;

import com.codeborne.selenide.SelenideElement;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UIAttributeData extends UIWidget<UIAttributeData> {

private UICellData parent;


public UIAttributeData(SelenideElement element, UICellData parent) {

	super(element);

	this.parent = parent;
}


/** @return the normalised name, without any UI embellishments or clarifications */
public String name() {

	String name = name_();
	name = name.endsWith(":") ? name.substring(0, name.length()-1) : name;	// the UI has a colon after the name
	name = name.endsWith("*") ? name.substring(0, name.length()-1) : name;	// if the attribute is mandatory

	return name;

}


/** @return is this an optional attribute? */
public boolean isOptional() {
	return !isMandatory();
}


/** @return is this a mandatory attribute? */
public boolean isMandatory() {
	return name_().endsWith("*:");	// non-present attributes cannot be mandatory, just saying
}


/** @return true if this is an editable attribute */
public boolean isEditable() {
	return class_().contains("attribute-data-editor");
}


/** @return true if it's present (as opposed to not there) */
public boolean isPresent() {
	return !isNotPresent();
}


/** @return true it it's not there and can be created */
public boolean isNotPresent() {
	return class_().contains("attribute-not-present");

}


private String name_() {

	return element.$(".attribute-data-name").text();
}


public String value() {
	return isEditable()? element.$(".attribute-data-value").getValue() : element.$(".attribute-data-value").text();
}


public boolean hasValue() {
	return element.$(".attribute-data-value").exists();	// this is brittle?
}


public UIAttributeData enterTextDirect(String value) {

	if (!isEditable()) {
		throw new UnsupportedOperationException("Cannot set the value of a non-editable field");
	}

	int attributeIndex = Integer.parseInt(element.attr("_index"));
	for (int i=0; i<attributeIndex; i++) {	// it seems that we have the cursor on the first data field for some reason
		pressKey("\t");
	}

	// this only works if called in order, selenide is not happy about the setValue so we're hacking it
	pressKey(value);

	//element.$(".attribute-data-value").setValue(value);
	//element.setValue(value);

	return this;

}


public UIAttributeData enterTextNext(String value) {

	if (!isEditable()) {
		throw new UnsupportedOperationException("Cannot set the value of a non-editable field");
	}
	pressKey("\t");
	pressKey(value);

	return this;

}


/** Erase the attribute text in the field, we assume our cursor is in the field  */
public UIAttributeData eraseValueInField() {

	if (!isEditable()) {
		throw new UnsupportedOperationException("Cannot erase the value of a non-editable field");
	}


	int length = value().length();
	for (int i=0;i<length;i++) {
		pressBackspace();
	}

	return this;

}


public UIAttributeData clickOnCreate() {

	if (!isNotPresent()) {
		throw new UnsupportedOperationException("Cannot create an attribute unless it's not present");
	}

	element.$(".attribute-data-add").click();	// we click on the add button

	return parent.attribute(this.name());		// and we now have new element, so we recreate ourselves

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
