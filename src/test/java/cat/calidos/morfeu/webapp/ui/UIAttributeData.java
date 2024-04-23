// UI ATTRIBUTE DATA . JAVA

package cat.calidos.morfeu.webapp.ui;

import static com.codeborne.selenide.Selenide.$;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;
import com.google.common.base.Optional;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UIAttributeData extends UIWidget<UIAttributeData> {

private static final String ATTRIBUTE_DATA_VALUE = ".attribute-data-value";
private static final String ATTRIBUTE_VALIDATION_WARNING = ".attribute-data-validation-warning";
private Optional<UICellData> parent;


public UIAttributeData() {
	this($(".attribute-data"));
}


public UIAttributeData(SelenideElement element) {
	this(element, null);
}


public UIAttributeData(SelenideElement element, UICellData parent) {

	super(element);

	this.parent = Optional.fromNullable(parent);

}



/** @return the normalised name, without any UI embellishments or clarifications */
public String name() {

	String name = name_().trim();											// get rid of space
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


public String value() {
	return isEditable() ? element.$(ATTRIBUTE_DATA_VALUE).getValue() : element.$(ATTRIBUTE_DATA_VALUE).innerText();
}


public List<String> possibleValues() {

	if (!isList()) {
		throw new RuntimeException("Tried to get a list of possible valus from a non-list ("+name()+")");
	}

	return element.$$(By.xpath(".//option")).asFixedIterable().stream().map(e -> e.innerText()).collect(Collectors.toList());

}


public boolean hasValue() {
	return element.$(ATTRIBUTE_DATA_VALUE).exists();	// this is brittle?
}


public boolean validates() {
	return !element.$(ATTRIBUTE_VALIDATION_WARNING).exists();
}


public String validationWarning() {

	if (validates()) {
		throw new IllegalStateException("Cannot get a validation warning of a correct attribute, sorry");
	}

	return element.$(ATTRIBUTE_VALIDATION_WARNING).getText();

}


public boolean isBoolean() {
	return class_().contains("attribute-data-boolean");
}


public boolean asBoolean() {
	return isEditable() ? value().equals("on") : Boolean.parseBoolean(value());
}


public boolean isList() {
	return class_().contains("attribute-data-list");
}


public UIAttributeData tabIntoEnterText(String value) {

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

// no tabbing
public UIAttributeData enterText(String value) {

	if (!isEditable()) {
		throw new UnsupportedOperationException("Cannot set the value of a non-editable field");
	}
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


public UIAttributeData toggle() {

	if (!isBoolean()) {
		throw new UnsupportedOperationException("Cannot toggle the value of a non-boolean field");
	}
	if (!isEditable()) {
		throw new UnsupportedOperationException("Cannot toggle the value of a non-editable boolean field");
	}

	element.$(ATTRIBUTE_DATA_VALUE).click();

	return this;

}

public UIAttributeData select(String value) {

	if (!isList()) {
		throw new UnsupportedOperationException("Cannot select the value of a non-dropdown field");
	}
	if (!isEditable()) {
		throw new UnsupportedOperationException("Cannot toggle the value of a non-editable dropdown field");
	}

	element.$(ATTRIBUTE_DATA_VALUE).selectOption(value);

	return this;

}



public UIAttributeData clickOnCreate() {

	if (!isNotPresent()) {
		throw new UnsupportedOperationException("Cannot create an attribute unless it's not present");
	}
	if (!parent.isPresent()) {
		throw new UnsupportedOperationException("We have no parent we cannot click on create");
	}

	element.$(".attribute-data-add").click();	// we click on the add button

	return parent.get().attribute(this.name());		// and we now have new element, so we recreate ourselves

}


private String name_() {

	return element.$(".attribute-data-name").text();
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
