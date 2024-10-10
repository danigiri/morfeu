package cat.calidos.morfeu.webapp.ui;

import static com.codeborne.selenide.Selenide.$;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.codeborne.selenide.SelenideElement;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UIBreadcrumb extends UIWidget<UIBreadcrumb> {

public UIBreadcrumb() {
	this($("#breadcrumb_"));
}


public UIBreadcrumb(SelenideElement element) {
	super(element);
}


/** @return all elements of the breadcrumb (includind doc name and active cell name) */
public List<String> elements() {
	return element.$$(".breadcrumb-element")
			.asFixedIterable()
			.stream()
			.map(e -> e.text())
			.collect(Collectors.toList());
}


public Optional<String> documentName() {

	SelenideElement e = element.$("#breadcrumb-document");

	return e.exists() ? Optional.of(e.text()) : Optional.empty();

}


public Optional<String> activeName() {

	SelenideElement e = element.$("#breadcrumb-active-name");

	return e.exists() ? Optional.of(e.text()) : Optional.empty();

}


public boolean isFragment() {
	return element.$("#breadcrumb-fragment-back").exists();
}

}

/*
 * Copyright 2020 Daniel Giribet
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
