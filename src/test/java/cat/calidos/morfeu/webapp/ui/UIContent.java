/*
 *    Copyright 2017 Daniel Giribet
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

package cat.calidos.morfeu.webapp.ui;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UIContent {


public UIContent() {}


public UIContent shouldBeVisible() {
	
	$("#content").shouldBe(visible);
	
	return this;
	
}


public static void shouldNotBeVisible() {
	$("#content").shouldNotBe(visible);
}


public void shouldDisappear() {
	$("#content").should(disappear);
}


public List<UICell> rootCells() {
	return $$(".cell-level-1").stream().map(e -> new UICell(e, 1) ).collect(Collectors.toList());
}

}
