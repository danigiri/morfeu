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


@Test @DisplayName("Show full breadcrumb")
public void testFullBreadcrumb() {

	open(appBaseURL+"test/breadcrumb-test/display");
	UIBreadcrumb breadcrumb = new UIBreadcrumb().shouldAppear();
	assertAll("basic checks",
		() -> assertNotNull(breadcrumb),
		() -> assertFalse(breadcrumb.isFragment())
	);

	List<String> elements = breadcrumb.elements();
	assertAll("element checks",
		() -> assertNotNull(elements),
		() -> assertEquals(6, elements.size()),
		() -> assertEquals("test(0)", elements.get(0)),
		() -> assertEquals("row(0)", elements.get(1)),
		() -> assertEquals("col(1)", elements.get(2)),
		() -> assertEquals("row(0)", elements.get(3)),
		() -> assertEquals("col(1)", elements.get(4)),
		() -> assertEquals("data2(1)", elements.get(5))
	);

	assertAll("active checks",
			() -> assertNotNull(breadcrumb),
			() -> assertTrue(breadcrumb.activeName().isPresent()),
			() -> assertEquals("data2(1)", breadcrumb.activeName().get())
	);

}

@Test @DisplayName("Show and hide breadcrumb")
public void testShowAndHideBreadcrumb() {

	open(appBaseURL+"test/breadcrumb-test/display-and-hide");
	UIBreadcrumb breadcrumb = new UIBreadcrumb().shouldAppear();
	assertAll("active checks",
			() -> assertNotNull(breadcrumb),
			() -> assertTrue(breadcrumb.activeName().isPresent()),
			() -> assertEquals("data2(1)", breadcrumb.activeName().get())
	);

	waitOneSec();
	assertFalse(breadcrumb.activeName().isPresent());

}



@Test @DisplayName("Show fragment breadcrumb")
public void testFragmentlBreadcrumb() {

	open(appBaseURL+"test/breadcrumb-test/display-fragment");
	UIBreadcrumb breadcrumb = new UIBreadcrumb().shouldAppear();
	assertAll("basic checks",
			() -> assertNotNull(breadcrumb),
			() -> assertTrue(breadcrumb.isFragment())
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

