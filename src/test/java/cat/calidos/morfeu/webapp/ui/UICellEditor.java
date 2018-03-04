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

package cat.calidos.morfeu.webapp.ui;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UICellEditor extends UIWidget<UICellEditor> {

public UICellEditor() {
	super($("#cell-editor .cell-data"));
}


public static void shouldNotBeVisible() {
	$("#cell-editor .cell-data").shouldNotBe(visible);
}


public UICellEditor clickSave() {

	$("#cell-editor-save-button").click();	// can access from global id
	
	return this;

}


public UICellEditor clickDiscard() {

	$("#cell-editor-discard-button").click();	// can access from global id
	
	return this;

}


public UICellData cellData() {
	return new UICellData(element);
}

}
