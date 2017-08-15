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
import static com.codeborne.selenide.Selenide.*;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UIModel {


public UIModel shouldAppear() {

	$("#model-info").should(appear);

	return this;

}

public void shouldDisappear() {
	$("#model-info").should(disappear);
}


public void shouldBeVisible() {
	$("#model-info").shouldBe(visible);
}


public static void shouldNotBeVisible() {
	$("#model-info").shouldNotBe(visible);
}


public String name() {
	return $("#model-name").getText();
}


public String desc() {
	return $("#model-desc").getText();
}


public List<UICellModelEntry> rootCellModels() {
	return $$(".cell-model-level-1").stream().map( e -> new UICellModelEntry(e) ).collect(Collectors.toList());
}

}
