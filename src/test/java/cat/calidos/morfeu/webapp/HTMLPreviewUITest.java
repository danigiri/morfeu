// HTML PREVIEW UI TEST . JAVA

package cat.calidos.morfeu.webapp;

import static org.junit.Assert.*;

import static com.codeborne.selenide.Selenide.open;

import org.junit.Test;

import cat.calidos.morfeu.webapp.ui.HTMLPreview;

/** Load one HTML preview independently and just check the values
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class HTMLPreviewUITest extends UITezt {


@Test
public void testPreview() {

	open(appBaseURL+"preview/identifier 1;color=ff1100");

	HTMLPreview preview = new HTMLPreview();
	preview.shouldAppear();

	assertEquals("identifier 1", preview.title());
	assertEquals("#ff1100", preview.color());

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
