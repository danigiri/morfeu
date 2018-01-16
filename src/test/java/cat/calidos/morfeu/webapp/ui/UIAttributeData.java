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
public class UIAttributeData extends UIWidget<UIAttributeData> {

public UIAttributeData(SelenideElement element) {
	super(element);
}


/** @return the normalised name, without any UI embellishments or clarifications
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public String name() {

	String name = name_();
	name = name.endsWith(":") ? name.substring(0, name.length()-1) : name;	// the UI has a colon after the name
	name = name.endsWith("*") ? name.substring(0, name.length()-1) : name;	// if the attribute is mandatory

	return name;

}

public boolean isOptional() {
	return !isMandatory();
}


public boolean isMandatory() {
	return name_().endsWith("*:");
}

public boolean isEditable() {
	return class_().contains("attribute-data-editor");
}

private String name_() {

	return element.$(".attribute-data-name").text();
}


public String value() {
	return isEditable()? element.$(".attribute-data-value").getValue() : element.$(".attribute-data-value").text();
}


public boolean hasValue() {
	return class_().contains("list-group-item-secondary");	// this is brittle?
}


public UIAttributeData enterText(String value) {

	if (!isEditable()) {
		throw new UnsupportedOperationException("Cannot set the value of a non-editable field");
	}
	
	element.$(".attribute-data-value").setValue(value);
	
	return this;
}

}
