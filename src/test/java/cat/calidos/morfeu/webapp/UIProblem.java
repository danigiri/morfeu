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

package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UIProblem {


public static UIProblem problem() {
	return new UIProblem();
}

public static void shouldNotBeVisible() {
	$("#problem").shouldNotBe(visible);
}

public UIProblem shouldAppear() {
	
	$("#problem").should(appear);
	
	return this;
	
}

public UIProblem shouldDisappear() {

	$("#problem").should(disappear);
	
	return this;
	
}


public String getText() {
	return $("#problem").getText();
} 


}
