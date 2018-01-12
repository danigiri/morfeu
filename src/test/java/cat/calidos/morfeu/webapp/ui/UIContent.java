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
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UIContent extends UIWidget<UIContent> {


public static final String SELECTION_MODE = "c";
public static final String DROPAREA_MODE = "t";
public static final String ACTIVATE = "a";
public static final String DRAGNDROP = "d";


public UIContent() {
	super($("#content"));
}


public static void shouldNotBeVisible() {
	$("#content").shouldNotBe(visible);
}


public List<UICell> rootCells() {
	return $$(".cell-level-0").stream().map(e -> new UICell(e, this, Optional.empty(), 0) ).collect(Collectors.toList());
}


}
