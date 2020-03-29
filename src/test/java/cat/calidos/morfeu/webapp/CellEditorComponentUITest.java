package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.webapp.ui.UIAttributeData;
import cat.calidos.morfeu.webapp.ui.UICellEditor;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellEditorComponentUITest extends UITezt {


@Test @DisplayName("Categories")
public void testReadonlyCells() {

	open(appBaseURL+"test/cell-editor-test/categories-all");
	UICellEditor editor = new UICellEditor();
	editor.shouldBeVisible();

	List<String> categories = editor.categories();
	assertAll("check categories",
		() -> assertNotNull(categories),
		() -> assertEquals(2, categories.size(), "wrong number of categories"),
		() -> assertNotEquals(-1, categories.indexOf("X")),
		() -> assertNotEquals(-1, categories.indexOf("Y"))
	);

	List<UIAttributeData> defaultAttributes = editor.cellData().displayedAttributes();
	assertAll("check categories",
		() -> assertNotNull(defaultAttributes),
		() -> assertEquals(2, defaultAttributes.size(), "wrong number of visible attributes in default category"),
		() -> assertTrue(findAttribute(defaultAttributes, "value0x").isPresent()),
		() -> assertTrue(findAttribute(defaultAttributes, "value1x").isPresent())
	);

	editor.clickOnCategory("Y");
	List<UIAttributeData> yAttributes = editor.cellData().displayedAttributes();
	assertAll("check categories",
		() -> assertNotNull(yAttributes),
		() -> assertEquals(2, yAttributes.size(), "wrong number of visible attributes in default category"),
		() -> assertTrue(findAttribute(yAttributes, "value0y").isPresent()),
		() -> assertTrue(findAttribute(yAttributes, "value1y").isPresent())
	);

	assertThrows(NoSuchElementException.class, () -> editor.clickOnCategory("NOTFOUND"));

}


private Optional<UIAttributeData> findAttribute(List<UIAttributeData> attributes, String name) {
	return attributes.stream().filter(a -> a.name().equals(name)).findAny();
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

