package cat.calidos.morfeu.webapp;
// ATTRIBUTE DATA EDITOR COMPONENT UI TEST . JAVA

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.*;

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

	UIAttributeData attribute = new UIAttributeData($(".attribute-data"));
	assertAll("check default as true",
		() -> assertNotNull(attribute),
		() -> assertTrue(attribute.isBoolean(), "attribute should be a boolean and it is not"),
		() -> assertTrue(attribute.asBoolean(), "attribute should have the value 'true'"),
		() -> assertTrue(attribute.isEditable())

	);

	attribute.toggle();
	assertTrue(attribute.asBoolean(), "attribute should have the value 'true'");

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

