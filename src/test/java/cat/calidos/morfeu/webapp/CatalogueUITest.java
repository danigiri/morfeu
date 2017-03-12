/*
 *    Copyright 2016 Daniel Giribet
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

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CatalogueUITest {

private static final String URL_PROPERTY = "app-url";
private static final String DEFAULT_URL = "http://localhost:3000";
private static final String BROWSER_PROPERTY = "selenide.browser";
private static final String DEFAULT_BROWSER = "chrome";
private static final String DRIVER_LOCATION_PROPERTY = "webdriver.chrome.driver";
private static final String DEFAULT_DRIVER_LOCATION = "/Applications/chromedriver";

private static String appBaseURL;
private static WebDriver driver;

@BeforeClass
public static void setUpClass() throws Exception {
	

	defineSystemVariable(BROWSER_PROPERTY, DEFAULT_BROWSER);
	defineSystemVariable(DRIVER_LOCATION_PROPERTY, DEFAULT_DRIVER_LOCATION);

	appBaseURL = defineSystemVariable(URL_PROPERTY, DEFAULT_URL);

	driver = new ChromeDriver();
}


@Test
public void catalogueListTest() throws Exception {

	// catalogue list appears and has two entries
	open(appBaseURL);
	$("#catalogue-list").should(appear);
	ElementsCollection catalogueEntries = $$(".catalogue-list-entry");
	catalogueEntries.shouldHaveSize(2);
	assertEquals("Wrong catalogue content", "Catalogue 1", catalogueEntries.get(0).getText());
	assertEquals("Wrong catalogue content", "Catalogue 2", catalogueEntries.get(1).getText());
	
}


@Test
public void catalogueDetailTest() throws Exception {

	// click on catalogue list entry and it appears
	open(appBaseURL);
	$("#catalogue-list").should(appear);
	$("#catalogue").shouldNotBe(visible);
	$("#document-list").shouldNotBe(visible);
	
	ElementsCollection catalogueEntries = $$(".catalogue-list-entry");
	catalogueEntries.get(0).click();
	$("#catalogue").should(appear);
	$("#document-list").should(appear);

	assertEquals("Wrong catalogue selected", "Catalogue 1", $("#catalogue-name").getText());
	assertEquals("Wrong catalogue selected", "First Catalogue", $("#catalogue-desc").getText());
	
	// test listing of documents
	ElementsCollection documentEntries = $$(".document-list-entry");
	documentEntries.shouldHaveSize(3);
	assertEquals("Wrong catalogue content", "empty\nxml", documentEntries.get(0).getText());
	assertEquals("Wrong catalogue content", "table\nxml", documentEntries.get(1).getText());
	assertEquals("Wrong catalogue content", "other\nyaml", documentEntries.get(2).getText());
	
}

@AfterClass
public static void tearDownClass() {
	
	//Close the browser
	if (driver!=null) {
		driver.quit();
	}
	
}


private static String defineSystemVariable(String systemProperty, String defaultValue) {

	String value = System.getProperty(systemProperty);
	if (value==null) {
		value = defaultValue;
		System.setProperty(systemProperty, defaultValue);
	}
	return value;
}

}
