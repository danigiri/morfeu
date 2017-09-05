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

import static org.junit.Assert.fail;

import java.util.function.Predicate;

import com.codeborne.selenide.SelenideElement;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UIWidget<T extends UIWidget<T>> {

protected SelenideElement element;

public UIWidget(SelenideElement element) {
	this.element = element;
}

@SuppressWarnings("unchecked")
public T check(Predicate<T> check, String message) {
	
	if (!check.test((T) this)) {
		fail(message);
	}

	return (T)this;
	
}


}
