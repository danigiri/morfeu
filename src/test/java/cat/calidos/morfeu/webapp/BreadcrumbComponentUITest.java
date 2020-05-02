package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.webapp.ui.UIBreadcrumb;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class BreadcrumbComponentUITest extends UITezt {


@Test @DisplayName("Show document name only")
public void testDocumentName() {

	open(appBaseURL+"test/breadcrumb-test/display-document");
	UIBreadcrumb breadcrumb = new UIBreadcrumb();
	assertAll("basic checks",
		() -> assertNotNull(breadcrumb),
		() -> assertTrue(breadcrumb.documentName().isPresent()),
		() -> assertEquals("Readonly test doc", breadcrumb.documentName().get())
	);

}


@Test @DisplayName("Show full breadcrumb")
public void testCellBreadcrumb() {

	open(appBaseURL+"test/breadcrumb-test/display-all");
	UIBreadcrumb breadcrumb = new UIBreadcrumb();
	assertAll("basic checks",
		() -> assertNotNull(breadcrumb),
		() -> assertTrue(breadcrumb.documentName().isPresent()),
		() -> assertEquals("Readonly test doc", breadcrumb.documentName().get())
	);

	List<String> elements = breadcrumb.elements();
	assertAll("element checks",
		() -> assertNotNull(elements),
		() -> assertEquals(7, elements.size()),
		() -> assertEquals("Readonly test doc", elements.get(0)),
		() -> assertEquals("test(0)", elements.get(1)),
		() -> assertEquals("row(0)", elements.get(2)),
		() -> assertEquals("col(1)", elements.get(3)),
		() -> assertEquals("row(0)", elements.get(4)),
		() -> assertEquals("col(1)", elements.get(5)),
		() -> assertEquals("data2(1)", elements.get(6))
	);

	assertAll("active checks",
			() -> assertNotNull(breadcrumb),
			() -> assertTrue(breadcrumb.activeName().isPresent()),
			() -> assertEquals("data2(1)", breadcrumb.activeName().get())
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

