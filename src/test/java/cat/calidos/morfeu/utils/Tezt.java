// TEZT . JAVA

package cat.calidos.morfeu.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

import javax.xml.transform.Source;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Tezt {

protected static int systemPropertyPrints = 0;

protected static final String	URL_PROPERTY			= "app-url";
protected static String			DEFAULT_URL				= "http://localhost:3000/";
public static final String		DEFAULT_TMP_FOLDER_NAME	= "integration-tests-tmp";

public String tempDirectoryPath() {
	return defineSystemVariable("TMP_FOLDER", "./target/" + DEFAULT_TMP_FOLDER_NAME);
}


public File setupTempDirectory() throws Exception {

	String tmp = tempDirectoryPath();
	File tmpFolder = new File(tmp);
	if (tmpFolder.exists()) {
		if (tmpFolder.isFile()) {
			fail("Temporary folder destination exists and it's not a directory");
		}
	} else {
		FileUtils.forceMkdir(tmpFolder);
	}

	return tmpFolder;

}


public String testAwareFullPathFrom(String path) {
	return this.getClass().getClassLoader().getResource(path).toString();
}


protected JsonNode parseJson(InputStream content) throws IOException, JsonProcessingException {

	ObjectMapper mapper = new ObjectMapper();
	JsonNode doc = mapper.readTree(content);
	return doc;
}


protected static String defineSystemVariable(	String systemProperty,
												String defaultValue) {

	String value = System.getProperty(systemProperty);
	if (value == null) {
		value = defaultValue;
		System.setProperty(systemProperty, defaultValue);
	} else {
		if (systemPropertyPrints < 10) {
			System.err.println("Using " + systemProperty + "=" + value + " [ENV]");
		} else if (systemPropertyPrints == 10) {
			System.err.println("See previous messages for system property overrides");
		}
		systemPropertyPrints++;
	}

	return value;

}


/**
 * First look at java system properties, then into env vars (env overrides java system props), if no
 * value is found then use the default value
 * 
 * @return final value
 *////////////////////////////////////////////////////////////////////////////////
protected String getConfigurationVariable(	String name,
											String defaultValue) {

	String value = System.getProperty(name);
	value = (value == null || System.getenv(name) != null) ? System.getenv(name) : value;
	value = value == null ? defaultValue : value;

	return value;

}


protected void compareWithXMLFile(	String content,
									String path) {

	Source transformedSource = Input.fromString(content).build();
	File originalFile = new File(path);
	Source originalSource = Input.fromFile(originalFile).build();

	Diff diff = DiffBuilder
			.compare(originalSource)
			.withTest(transformedSource)
			.ignoreComments()
			.ignoreWhitespace()
			.build();

	assertFalse(
			diff.hasDifferences(),
			"Transformed JSON to XML should be the same as original" + diff.toString());

}


protected void compareWithXML(	String content,
								String expected) {

	Source transformedSource = Input.fromString(content).build();
	Source originalSource = Input.fromString(expected).build();

	Diff diff = DiffBuilder
			.compare(originalSource)
			.withTest(transformedSource)
			.ignoreComments()
			.ignoreWhitespace()
			.build();

	assertFalse(
			diff.hasDifferences(),
			"Transformed JSON to XML should be the same as original" + diff.toString());

}


protected InputStream printInputStream(InputStream s) {
	String c;
	try {
		c = IOUtils.toString(s, "UTF-8");
		System.err.println(c);
	} catch (IOException e) {
		e.printStackTrace();
		return null;
	}
	return new ByteArrayInputStream(c.getBytes());
}


protected void sleep(Duration duration) {
	try {
		Thread.sleep(duration);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
}


protected String readFromFile(String contentPath) throws URISyntaxException, IOException {

	String fullContentPath = testAwareFullPathFrom(contentPath);
	var fullContentURI = new URI(fullContentPath);
	String content = IOUtils.toString(fullContentURI, Config.DEFAULT_CHARSET);

	return content;

}

}

/*
 * Copyright 2024 Daniel Giribet
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
