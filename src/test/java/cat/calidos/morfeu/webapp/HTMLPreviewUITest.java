// HTML PREVIEW UI TEST . JAVA

package cat.calidos.morfeu.webapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
public class HTMLPreviewUITest extends UITezt {


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
