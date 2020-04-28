package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

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

