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

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.Assert.fail;

import java.util.function.Predicate;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.codeborne.selenide.SelenideElement;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UIWidget<T extends UIWidget<T>> {

protected static final String CLASS = "class";

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


@SuppressWarnings("unchecked")
public T shouldBeVisible() {
	
	element.shouldBe(visible);
		
	return (T)this;
	
}


@SuppressWarnings("unchecked")
public T shouldAppear() {
	
	element.should(appear);
	
	return (T)this;
	
}


public void shouldDisappear() {
	element.should(disappear);

}


@SuppressWarnings("unchecked")
public T hover() {

	element.scrollTo().hover();
	element.hover();
	
	return (T)this;
	
}


@SuppressWarnings("unchecked")
public T pressKey(String k) {
	
	// we are sending the keys this way as it seems to work as it should, selenide or selenium has a lot of trouble here
	WebDriver driver = element.getWrappedDriver();
	Actions actions = new Actions(driver);
	//actions.moveToElement(element.getWrappedElement());
	//actions.click();
	actions.sendKeys(Keys.chord((CharSequence)k));
	actions.build().perform();

	// this keeps failing randomly
	try {
		Thread.sleep(50);
	} catch (InterruptedException e) {}

	return (T)this;
	
}


/**
* @return
*////////////////////////////////////////////////////////////////////////////////
protected String class_() {

	return element.attr(CLASS);
}

}
