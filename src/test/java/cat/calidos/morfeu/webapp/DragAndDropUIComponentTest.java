package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UICellEditor;
import cat.calidos.morfeu.webapp.ui.UIContent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DragAndDropUIComponentTest extends UITezt {


//@BeforeEach
//public void setup() {
//}


@Test @DisplayName("Drag and drop stuff test")
public void testDragAndDropStuff() {

	open(appBaseURL+"test/content/dnd-1");
	UIContent content = new UIContent();
	content.shouldBeVisible();

	UICell col = content.rootCells().get(0).child("row(0)").child("col(0)");
	assertNotNull(col);

	UICell stuff0 = col.child("stuff(0)");
	assertNotNull(stuff0);

	stuff0.hover();
	assertAll("active areas",
	//	() -> assertFalse(col.dropArea(0).isActive()),	// cannot check due to unreliable CDK drop values
	//	() -> assertFalse(col.dropArea(1).isActive()),
		() -> assertTrue(col.dropArea(0).isActive()),
		() -> assertTrue(col.dropArea(1).isActive()),
		() -> assertTrue(col.dropArea(2).isActive()),
		() -> assertTrue(col.dropArea(3).isActive()),
		() -> assertFalse(col.dropArea(4).isActive())
	);

	stuff0.dragTo(col.dropArea(2));
	UICell col2 = content.rootCells().get(0).child("row(0)").child("col(0)");
	assertNotNull(col2);
	UICellEditor stuffEditor = col2.child("stuff(0)").hover().edit();
	Optional<String> stuffValue = stuffEditor.value();
	stuffEditor.clickDiscard();
	assertAll("after drop",
		() -> assertTrue(stuffValue.isPresent()),
		() -> assertEquals("Stuff content 2", stuffValue.get())
	);

	col2.child("stuff(0)").hover();	// bug in the activation event sequence
	UICell movedStuff = col2.child("stuff(1)").hover();
	UICellEditor movedStuffEditor = movedStuff.editByDoubleClicking();
	Optional<String> movedStuffValue = movedStuffEditor.value();
	movedStuffEditor.clickDiscard();
	assertAll("after drop",
		() -> assertTrue(movedStuffValue.isPresent()),
		() -> assertEquals("Stuff content", movedStuffValue.get())
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

