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

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;

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

private WebDriver driver;

public UIWidget(SelenideElement element) {

	this.element = element;
	this.driver = element.getWrappedDriver();
	
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

	//element.scrollTo().hover();
	element.hover();

	return (T)this;
	
}


@SuppressWarnings("unchecked")
public T pressKey(String k) {
	
	// we are sending the keys this way as it seems to work as it should, selenide or selenium has a lot of trouble here

	Actions actions = new Actions(driver);
	//actions.moveToElement(element.getWrappedElement());
	//actions.click();

	// on geckodriver, tab is sent specifically and does not switch fields
	if (k.equals("\t") ) {
		actions.sendKeys(Keys.TAB);
	} else {
		actions.sendKeys(Keys.chord((CharSequence)k));
	}
	actions.build().perform();

	// this keeps failing randomly
	try {
		Thread.sleep(50);
	} catch (InterruptedException e) {}

	return (T)this;
	
}


@SuppressWarnings("unchecked")
public T pressBackspace() {

	WebDriver driver = element.getWrappedDriver();
	Actions actions = new Actions(driver);
	actions.sendKeys(Keys.BACK_SPACE);
	actions.build().perform();

	return (T) this;

}


@SuppressWarnings("unchecked")
public T pressTAB() {

	WebDriver driver = element.getWrappedDriver();
	Actions actions = new Actions(driver);
	actions.sendKeys(Keys.TAB);
	actions.build().perform();

	return (T) this;

}


/**
* @return
*////////////////////////////////////////////////////////////////////////////////
protected String class_() {

	return element.attr(CLASS);
}

}
