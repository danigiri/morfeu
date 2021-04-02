// ARROWS UI TEST . JAVA

package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.webapp.ui.UIArrow;
import cat.calidos.morfeu.webapp.ui.UILinks;

public class LinksUITest extends UITezt {


@Test @DisplayName("Test show one arrow")
public void testShowArrow() {

	open(appBaseURL+"test/arrows/arrow");

	UILinks links = new UILinks("test");
	assertAll("test arrows",
		() -> assertNotNull(links),
		() -> assertEquals(1, links.size())
	);

	UIArrow arrow = links.get(0);
	assertAll("check created arrow",
		() -> assertNotNull(arrow),
		() -> assertEquals(0, arrow.start().getX()),
		() -> assertEquals(0, arrow.start().getY()),
		() -> assertEquals(128, arrow.end().getX()),
		() -> assertEquals(128, arrow.end().getY())
	);

}


}

/*
 *	  Copyright 2021 Daniel Giribet
 *
 *	 Licensed under the Apache License, Version 2.0 (the "License");
 *	 you may not use this file except in compliance with the License.
 *	 You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 *	 Unless required by applicable law or agreed to in writing, software
 *	 distributed under the License is distributed on an "AS IS" BASIS,
 *	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	 See the License for the specific language governing permissions and
 *	 limitations under the License.
 */
