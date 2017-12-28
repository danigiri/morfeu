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

import com.codeborne.selenide.SelenideElement;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UIAttributeInfo {

private SelenideElement element;

public UIAttributeInfo(SelenideElement e) {
	this.element = e;
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


private String name_() {

	return element.$(".attribute-info-name").text();
}


public String value() {
	return element.$(".attribute-info-value").text();
}


public boolean hasValue() {
	return element.attr("class").contains("list-group-item-secondary");	// this is 
}

}
