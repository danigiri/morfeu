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

package cat.calidos.morfeu.utils;

import static org.junit.Assert.fail;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Tezt {


public String setupTempDirectory() throws Exception {
	
	String defaultTmp = "./target/integration-tests-tmp";
	String tmp = defineSystemVariable("TMP_FOLDER", defaultTmp);
	File tmpFolder = new File(tmp);
	if (tmpFolder.exists()) {
		if (tmpFolder.isFile()) {
			fail("Temporary folder destination exists and it's not a directory");
		}
	} else {
		FileUtils.forceMkdir(tmpFolder);
	}

	return tmpFolder.getAbsolutePath();
	
}


public String testAwareFullPathFrom(String path) {
	return this.getClass().getClassLoader().getResource(path).toString();
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

}
