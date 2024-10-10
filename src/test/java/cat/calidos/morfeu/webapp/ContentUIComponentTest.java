// CONTENT UI COMPONENT TEST . JAVA

package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;

import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UICellData;
import cat.calidos.morfeu.webapp.ui.UICellEditor;
import cat.calidos.morfeu.webapp.ui.UIContent;


/**
 * Test the content component behaviour
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentUIComponentTest extends UITezt {

private UIContent content;

@BeforeEach
public void setup() {

	open(appBaseURL + "test/content-test/readonly");
	content = new UIContent();
	content.shouldBeVisible();

}


@Test @DisplayName("Readonly cells")
public void testReadonlyCells() {

	// target/test-classes/test-resources/documents/readonly.xml/test(0)/row(1)/col(0)/data(0)
	UICell data0 = content.rootCells().get(0).child("row(1)").child("col(0)").child("data(0)");
	assertAll("basic readonly checks", () -> assertNotNull(data0),
			() -> assertFalse(data0.isReadonly()));

	// target/test-classes/test-resources/documents/readonly.xml/test(0)/row(1)/col(0)/readonly(1)
	UICell readonly1 = content.rootCells()
			.get(0)
			.child("row(1)")
			.child("col(0)")
			.child("readonly(1)");
	readonly1.select().activate();
	assertTrue(readonly1.isActiveReadonly());

}


@Test @DisplayName("Cannot delete cells")
public void testCannotDeleteReadonlyCells() {

	UICell parent = content.rootCells().get(0).child("row(1)").child("col(0)");
	assertEquals(3, parent.children().size());
	UICell readonly2 = parent.child("readonly(2)");
	readonly2.select().activate();
	readonly2.forceRemove();
	UICell readonly2AfterDelete = parent.child("readonly(1)");
	assertNotNull(readonly2AfterDelete);
	readonly2AfterDelete.shouldBeVisible();
	assertAll("could not remove cell", () -> assertTrue(readonly2AfterDelete.isCell()),
			() -> assertEquals(3, parent.children().size()));

}


@Test @DisplayName("Cannot edit cells")
public void testCannotEditReadonlyCells() {

	UICell readonly1 = content.rootCells()
			.get(0)
			.child("row(1)")
			.child("col(0)")
			.child("readonly(1)");
	UICellData.shouldNotBeVisible();
	assertThrows(NoSuchElementException.class, () -> readonly1.edit());
	UICellData.shouldNotBeVisible();

	assertThrows(NoSuchElementException.class, () -> readonly1.editByDoubleClicking());
	UICellData.shouldNotBeVisible();

}


@Test @DisplayName("Cannot delete readonly cell parents")
public void testCannotDeleteReadonlyCellParents() {

	UICell parent = content.rootCells().get(0).child("row(1)");
	parent.select().activate();
	parent.forceRemove();
	parent.shouldBeVisible();

}


@Test @DisplayName("Can edit readonly cell parents")
public void testCanEditReadonlyCellParents() {

	UICell parent = content.rootCells().get(0).child("row(1)");
	parent.select().activate();
	UICellEditor editor = parent.edit();
	editor.shouldAppear();
	assertAll("can edit and see cell data", () -> editor.cellData().isFromEditor());

}

}

/*
 * Copyright 2020 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
