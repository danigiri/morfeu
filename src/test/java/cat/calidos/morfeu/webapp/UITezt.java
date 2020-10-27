// UI TEZT . JAVA

package cat.calidos.morfeu.webapp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import cat.calidos.morfeu.utils.Tezt;
import cat.calidos.morfeu.webapp.ui.UICatalogue;
import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UIProblem;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class UITezt extends Tezt {

protected static final String DRIVER_PROPERTY = "driver";
protected static final String BROWSER_PROPERTY = "selenide.browser";
protected static final String DEFAULT_CHROME = "chrome";
protected static final String DEFAULT_FIREFOX = "firefox";
protected static final String CHROME_LOCATION_PROPERTY = "webdriver.chrome.driver";
protected static final String DEFAULT_CHROME_LOCATION = "/Applications/chromedriver";
protected static final String FIREFOX_LOCATION_PROPERTY = "webdriver.gecko.driver";
protected static final String DEFAULT_FIREFOX_LOCATION = "/usr/local/bin/geckodriver";


protected static String appBaseURL;
protected static WebDriver driver;


@BeforeClass @BeforeAll
public static void setUpClass() throws Exception {

	appBaseURL = defineSystemVariable(URL_PROPERTY, DEFAULT_URL);

	String driverName = defineSystemVariable(DRIVER_PROPERTY, DEFAULT_CHROME);
	if (driverName.equalsIgnoreCase(DEFAULT_CHROME)) {
		defineSystemVariable(BROWSER_PROPERTY, DEFAULT_CHROME);
		defineSystemVariable(CHROME_LOCATION_PROPERTY, DEFAULT_CHROME_LOCATION);
		try {
			WebDriverManager.chromedriver().setup();	// setups the chrome driver
		} catch (Exception e) {
			System.err.println("Failed to setup chromedriver, we're probably offline");
		}
		ChromeOptions chromeOptions = new ChromeOptions();
		//chromeOptions.addArguments("--log-level=3");
		chromeOptions.addArguments("--log-path=/dev/null");	//really?
		chromeOptions.addArguments("--silent");
		driver = new ChromeDriver(chromeOptions);
	} else {
		defineSystemVariable(BROWSER_PROPERTY, DEFAULT_FIREFOX);
		defineSystemVariable(FIREFOX_LOCATION_PROPERTY, DEFAULT_FIREFOX_LOCATION);
		driver = new FirefoxDriver();
	}

}


protected UICatalogue clickOnCatalogue(int i) {

	// TODO: move to a domain-specific subclass

	// click on catalogue list entry and it appears
	UICatalogues catalogues = UICatalogues.openCatalogues().shouldAppear();
	UICatalogue.shouldNotBeVisible();
	UIProblem.shouldNotBeVisible();

	UICatalogue catalogue = catalogues.clickOn(i);
	catalogue.shouldAppear();

	return catalogue;

}


public void waitOneSec() {
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {}
}


@AfterClass @AfterAll
public static void tearDownClass() {

	//Close the browser
	if (driver!=null) {
		driver.quit();
	}

}


}

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
