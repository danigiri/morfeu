package cat.calidos.morfeu.webapp;
// ATTRIBUTE DATA EDITOR COMPONENT UI TEST . JAVA

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.webapp.ui.UIAttributeData;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class AttributeDataEditorComponentUITest extends UITezt {


@Test @DisplayName("Boolean attribute editor (true)")
public void testBooleanAttributeTrue() {

	open(appBaseURL+"test/attribute-data-editor-test/boolean-true");

	UIAttributeData bool = new UIAttributeData();
	assertAll("check default as true",
		() -> assertNotNull(bool),
		() -> assertTrue(bool.isBoolean(), "attribute should be a boolean and it is not"),
		() -> assertTrue(bool.asBoolean(), "attribute should have the value 'true'"),
		() -> assertTrue(bool.isEditable(), "attribute should be editable")

	);

	bool.toggle();
	assertTrue(bool.asBoolean(), "attribute should have the value 'true'");

}


@Test @DisplayName("Text attribute editor validation")
public void testColorValidation() {

	open(appBaseURL+"test/attribute-data-editor-test/color-validation");

	UIAttributeData color = new UIAttributeData();
	assertAll("check no validation message at first",
		() -> assertNotNull(color),
		() -> assertEquals("ff1100", color.value(), "attribute should have the right value at first"),
		() -> assertTrue(color.isEditable(), "attribute should be editable"),
		() -> assertTrue(color.validates(), "attribute should validate at first and show no warning")
	);

	// we enter invalid color
	String invalidColor = "ff00X0";
	color.enterTextNext(invalidColor);
	assertAll("now we get a validation message",
		() -> assertEquals(invalidColor, color.value(), "attribute should have the new value"),
		() -> assertFalse(color.validates(), "attribute should not validate now"),
		() -> assertEquals("Should match [0-9a-fA-F]{6}", color.validationWarning())
	);

	color.eraseValueInField();
	assertAll("now we get a validation message",
			() -> assertFalse(color.validates(), "attribute should still not validate"),
			() -> assertEquals("Should match [0-9a-fA-F]{6}", color.validationWarning())
	);

	// we enter valid color
	String validColor = "ff00aa";
	color.tabIntoEnterText(validColor);
	assertAll("finally we are ok",
		() -> assertEquals(validColor, color.value(), "attribute should have the new value"),
		() -> assertTrue(color.validates(), "attribute should not validate now")
	);
	assertThrows(IllegalStateException.class, () -> color.validationWarning());

}



@Test @DisplayName("Text attribute dropdown")
public void testSelectDropdown() {

	open(appBaseURL+"test/attribute-data-editor-test/list");

	UIAttributeData list = new UIAttributeData();
	assertAll("check no validation message at first",
		() -> assertNotNull(list),
		() -> assertEquals("A0", list.value(), "attribute should have the right value at first"),
		() -> assertTrue(list.isList(), "attribute should be a (dropdown) list"),
		() -> assertTrue(list.isEditable(), "attribute should be editable"),
		() -> assertTrue(list.validates(), "attribute should validate at first and show no warning")
	);

	list.select("A1");
	assertEquals("A1", list.value(), "attribute should have the right value at first");

}


@Test @DisplayName("Locator dropdown test")
public void locatorDropdownTest() {

	open(appBaseURL+"test/attribute-data-editor-test/locator");

	UIAttributeData list = new UIAttributeData();
	assertAll("check basics at first",
		() -> assertNotNull(list),
		() -> assertEquals("V0", list.value(), "locator attribute should have the right value at first"),
		() -> assertTrue(list.isList(), "locator attribut should be a (dropdown) list"),
		() -> assertTrue(list.isEditable(), "locator attribute should be editable"),
		() -> assertTrue(list.validates(), "locator attribute should validate at first and show no warning")
	);

	List<String> possibleValues = list.possibleValues();
	assertAll("checking locator possible values",
		() -> assertNotNull(possibleValues),
		() -> assertEquals(3, possibleValues.size()),
		() -> assertTrue(possibleValues.contains("V0")),
		() -> assertTrue(possibleValues.contains("V1")),
		() -> assertTrue(possibleValues.contains("V2"))
	);

}


}

/*
 *    Copyright 2020 Daniel Giribet
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

