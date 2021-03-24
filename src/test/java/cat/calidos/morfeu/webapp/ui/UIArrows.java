package cat.calidos.morfeu.webapp.ui;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;

/** UI testing widgets for the arrows svg container component */
public class UIArrows extends UIWidget<UIArrows> {

private static final String CX = "cx";
private static final String CY = "cy";
private static final String CIRCLE = "circle";

private String id;
private int size = 0;


public UIArrows(String id) {

	super($(By.id(id)));

	this.id = id;
	this.size = element.$$(By.id(id+CIRCLE)).size();

}
    

public UIArrows(SelenideElement e) {
	super(e);
}


public int arrowCount() {
	return size;
}


public List<UIArrow> arrows() {
	
	AtomicInteger i = new AtomicInteger(-1);
	By circle = By.id(id+CIRCLE);

	return element.$$(circle).stream().map(e -> new UIArrow(this, i.incrementAndGet())).collect(Collectors.toList());

}


public Point arrowStart(int index) {

	checkBounds(index);

    
	By circleID = By.id(id+CIRCLE);
	int x = Integer.parseInt(element.$$(circleID).get(index).attr(CX));
	int y = Integer.parseInt(element.$$(circleID).get(index).attr(CY));

	return new Point(x, y);

}


private void checkBounds(int index) {

	if (index>=size) {
		throw new ArrayIndexOutOfBoundsException("We do not have an arrow at index "+index);
	}

}


}

/*
 *	  Copyright 2021 Daniel Giribet
 *
 *	 Licensed under the Apache License, Version 2.0 (the "License");
 *	 you may not use this file except in compliance with the License.
 *	 You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 *	 Unless required by applicable law or agreed to in writing, software
 *	 distributed under the License is distributed on an "AS IS" BASIS,
 *	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	 See the License for the specific language governing permissions and
 *	 limitations under the License.
 */
