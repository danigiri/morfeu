// HTML PREVIEW UI TEST . JAVA

package cat.calidos.morfeu.webapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

import cat.calidos.morfeu.webapp.ui.HTMLPreview;
import cat.calidos.morfeu.webapp.ui.UIAttributeData;
import cat.calidos.morfeu.webapp.ui.UICellData;
import cat.calidos.morfeu.webapp.ui.UICellEditor;

/** Load one HTML preview independently and just check the values
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class HTMLPreviewUIComponentTest extends UITezt {


@Test @DisplayName("Basic preview content test")
public void testPreview() {

	open(appBaseURL+"preview/identifier 1;color=ff1100");

	HTMLPreview preview = new HTMLPreview();
	preview.shouldAppear();

	assertAll("Basic preview content",
		() -> assertEquals("identifier 1", preview.title()),
		() -> assertEquals("#ff1100", preview.color())
	);

}


@Test @DisplayName("Cell editor changes update preview content test")
public void testPreviewLiveUpdates() {

	open(appBaseURL+"test/cell-editor-test/document5");

	UICellEditor editor = new UICellEditor();
	assertNotNull(editor);
	editor.shouldAppear();

	HTMLPreview preview = new HTMLPreview();
	preview.shouldAppear();
	
	UICellData cellEditorData = editor.cellData();
	assertAll("Basic cell editor stuff",
		() -> assertNotNull(cellEditorData),
		() -> assertTrue(cellEditorData.isFromEditor(), "Editing the cell should show an editor"),
		() -> assertTrue(cellEditorData.isFromCell(), "Editor should show data coming from the cell")
	);

	List<UIAttributeData> attributes = cellEditorData.attributes();
	assertAll("Basic cell editor attributes",
		() -> assertNotNull(attributes),
		() -> assertEquals(2, attributes.size(), "We should be editing two attributes")
	);

	UIAttributeData text = cellEditorData.attribute("text");
	String textExpectedValue = "identifier 1";
	assertAll("Text content is correct",
		() -> assertNotNull(text),
		() -> assertTrue(text.hasValue()),
		() -> assertEquals(textExpectedValue, preview.title()),
		() -> assertEquals(textExpectedValue, text.value())
	);

	UIAttributeData color = cellEditorData.attribute("color");
	String colorExpectedValue = "ff1100";
	assertAll("Color content is correct",
		() -> assertNotNull(color),
		() -> assertTrue(color.hasValue()),
		() -> assertEquals("#"+colorExpectedValue, preview.color()),	// preview adds the hash in the markup
		() -> assertEquals(colorExpectedValue, color.value())
	);

	// now we edit the content and expect the preview to change interactively
	
	text.eraseValueInField();
	String erasedExpectedValue = "";
	UIAttributeData erasedText = editor.cellData().attribute("text");
	assertAll("Interactive erase clears preview value",
		() -> assertNotNull(erasedText),
		() -> assertTrue(erasedText.hasValue()),
		() -> assertEquals(erasedExpectedValue, preview.title()),
		() -> assertEquals(erasedExpectedValue, erasedText.value())
	);
	
	String enteredExpectedValue = "";
	text.enterTextDirect(enteredExpectedValue);
	UIAttributeData enteredText = editor.cellData().attribute("text");
	assertAll("Interactive change sets preview value",
		() -> assertNotNull(enteredText),
		() -> assertTrue(enteredText.hasValue()),
		() -> assertEquals(enteredExpectedValue, preview.title()),
		() -> assertEquals(enteredExpectedValue, enteredText.value())
	);

}


}

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
