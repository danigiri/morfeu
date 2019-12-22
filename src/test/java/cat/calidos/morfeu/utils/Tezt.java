// TEZT . JAVA

package cat.calidos.morfeu.utils;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Tezt {

public static final String DEFAULT_TMP_FOLDER_NAME = "integration-tests-tmp";
protected static final String DEFAULT_URL = "http://localhost:8980";


public String tempDirectoryPath() {
	return defineSystemVariable("TMP_FOLDER", "./target/"+DEFAULT_TMP_FOLDER_NAME);
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


protected static String defineSystemVariable(String systemProperty, String defaultValue) {

	String value = System.getProperty(systemProperty);
	if (value==null) {
		value = defaultValue;
		System.setProperty(systemProperty, defaultValue);
	} else {
		System.err.println("Using "+systemProperty+"="+value+" [ENV]");
	}

	return value;

}


/** First look at java system properties, then into env vars (env overrides java system props), if no value is found
*	then use the default value 
* 	@return final value
*////////////////////////////////////////////////////////////////////////////////
protected String getConfigurationVariable(String name, String defaultValue) {

	String value = System.getProperty(name);
	value = (value==null || System.getenv(name)!=null) ? System.getenv(name) : value;
	value = value==null ? defaultValue : value; 

	return value;

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
