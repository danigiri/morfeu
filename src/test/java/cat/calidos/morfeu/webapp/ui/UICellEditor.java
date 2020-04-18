// UI CELL EDITOR . JAVA

package cat.calidos.morfeu.webapp.ui;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

import com.codeborne.selenide.SelenideElement;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UICellEditor extends UIWidget<UICellEditor> {

private static final String SAVE_BUTTON = "#cell-editor-save-button";

private static final String CATEGORY_LINK = ".cell-editor-category-link";

private Optional<UIContent> content;


public UICellEditor(UIContent content) {

	super($("#cell-editor"));

	this.content = Optional.ofNullable(content);

}


/** Not associated with content */
public UICellEditor() {
	this(null);
}


public static void shouldNotBeVisible() {
	$("#cell-editor").shouldNotBe(visible);
}


public boolean canSave() {
	return $(SAVE_BUTTON).isEnabled();
}


public UICellEditor clickSave() {

	$(SAVE_BUTTON).click();	// can access from global id

	return this;

}


public UICellEditor clickDiscard() {

	$("#cell-editor-discard-button").click();	// can access from global id

	return this;

}


public boolean isRemoveValueVisible() {

	return $("#cell-editor-remove-value-button").exists();

}


public UICellEditor clickRemoveValue() {

	$("#cell-editor-remove-value-button").click();

	return this;

}


public boolean isCreateValueVisible() {

	return $("#cell-editor-create-value-button").exists();

}


public UICellEditor clickCreateValue() {

	$("#cell-editor-create-value-button").click();

	return this;

}


public Optional<String> value() {

		SelenideElement valueElement = element.$(".cell-editor-value");

		return valueElement.exists() ? Optional.of(valueElement.getValue()) : Optional.empty();

}


public UICellEditor enterText(String value) {

	if (isCreateValueVisible()) {
		throw new UnsupportedOperationException("Cannot set the value when text field is not visible");
	}

	// cannot get selenide to select the element (textarea is missing in the setValue method)
	// so we send the keys which works
	// content.pressTAB(); (do not seem to need it in the latest selenium and selenide versions
	int l = value().get().length();
	for (int i=0; i<l; i++) {
		pressBackspace();
	}
	if (content.isPresent()) {
		content.get().pressKey(value);
	} else {	// we are standalone test cell editor and there is no content
		this.pressKey(value);
	}

	return this;

}


public UICellData cellData() {
	return new UICellData(element);	// cell data and cell editor are very similar
}


public void clickOnCategory(String category) {
	$$(CATEGORY_LINK).stream()
						.filter(e -> e.getText().equals(category))
						.findAny()
						.orElseThrow(() -> new NoSuchElementException("Could not click on category "+category))
						.click();
}


public List<String> categories() {
	return $$(CATEGORY_LINK).stream().map(e -> e.getText()).collect(Collectors.toList());
}


/** returns the standalone test instance */
public static UICellEditor testInstance() {
	return new UICellEditor();
}


}

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
