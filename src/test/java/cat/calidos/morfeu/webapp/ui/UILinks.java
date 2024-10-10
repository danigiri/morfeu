// UI LINKS . JAVA

package cat.calidos.morfeu.webapp.ui;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;


/** UI testing widget for the arrows svg container component */
public class UILinks extends UIWidget<UILinks> {

private static final String	CX					= "cx";
private static final String	CY					= "cy";
private static final String	CIRCLE				= "circle";
private static final String	POLYGON				= "poly";				// arrow parsing constants
private static final String	TRANSFORM			= "transform";
private static final String	SPACE				= " ";
private static final int	TRANSLATE_LENGTH	= "translate(".length();
private static final String	COMMA				= ",";

private String	id;
private int		size	= 0;

public UILinks(String id) {

	super($(By.id(id)));

	this.id = id;
	this.size = element.$$(By.id(id + CIRCLE)).size();

}


public UILinks(SelenideElement e) {
	super(e);
}


public List<UIArrow> arrows() {

	AtomicInteger i = new AtomicInteger(-1);
	By circle = By.id(id + CIRCLE);

	return element.$$(circle)
			.asFixedIterable()
			.stream()
			.map(e -> new UIArrow(this, i.incrementAndGet()))
			.collect(Collectors.toList());

}


public UIArrow get(int index) {
	return checkBounds(index).arrows().get(index);
}


public int size() {
	return size;
}


public Point start(int index) {

	checkBounds(index);

	SelenideElement circle = element.$$(By.id(id + CIRCLE)).get(index);
	int x = Integer.parseInt(circle.attr(CX));
	int y = Integer.parseInt(circle.attr(CY));

	return new Point(x, y);

}


public Point end(int index) {

	checkBounds(index);

	SelenideElement arrow = element.$$(By.id(id + POLYGON)).get(index);
	// parse the transform that moves the arrow polygon to the destination
	String translate = arrow.attr(TRANSFORM).split(SPACE)[0].substring(TRANSLATE_LENGTH);
	String[] coords = translate.split(COMMA);
	int x = Integer.parseInt(coords[0]);
	int y = Integer.parseInt(coords[1].substring(0, coords[1].length() - 1));

	return new Point(x, y);

}


private UILinks checkBounds(int index) {

	if (index >= size) {
		throw new ArrayIndexOutOfBoundsException("We do not have an arrow at index " + index);
	}

	return this;

}

}

/*
 * Copyright 2021 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
