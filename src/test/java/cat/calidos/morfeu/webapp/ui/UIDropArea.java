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

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.codeborne.selenide.SelenideElement;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UIDropArea {

private static final String CLASS = "class";
private static final String ACTIVE = "drop-area-active";

private SelenideElement element;
private UICell parent;

public UIDropArea(SelenideElement e, UICell parent) {

	this.element = e;
	this.parent = parent;
	
}

public boolean isActive() {
	return element.attr(CLASS).contains(ACTIVE);
}

public int position() {
	return Integer.parseInt(element.parent().attr("position"));
}


public UICell dropHere(UICell uiCell) {

	int pos = position();
	//uiCell.element.dragAndDropTo(element);
	
	WebElement cellWebElement = uiCell.element.toWebElement();
	WebElement dropWebElement = element.getWrappedElement();
	WebDriver driver = element.getWrappedDriver();
	Actions builder = new Actions(driver);
    uiCell.hover();
    
    uiCell.element.dragAndDropTo(element.parent());
//	WebElement ngDropArea = element.parent().getWrappedElement();
//	builder
//    	.moveToElement(cellWebElement, 2, 2)
//    	.pause(1000)
//    	.clickAndHold()
//    	.pause(1000)
////    	.dragAndDrop(cellWebElement, element.getWrappedElement())
////    	.moveByOffset(100, 100)
////    	.moveToElement(ngDropArea, 2, 2)
////    	.pause(1000)
//    	
////    	.moveByOffset(5, 5)
////    	.moveByOffset(-2, -2)
////    	.moveByOffset(2, 2)
////    	.moveByOffset(-2, -2)
////    	.moveByOffset(2, 2)
////    	.moveByOffset(-2, -2)
////    	.moveByOffset(2, 2)
////    	.moveByOffset(-2, -2)
////    	.moveByOffset(2, 2)
//        .release()
//        .build().perform();
	
	//parent.hover();	// give time to the UI to refresh
	
	return parent.child(pos);

}

}
