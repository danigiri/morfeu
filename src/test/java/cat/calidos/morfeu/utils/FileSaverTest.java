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

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class FileSaverTest extends Tezt {

private String path;


@Before
public void setup() throws Exception {

	String tmp = setupTempDirectory();
	System.err.println("Using '"+tmp+"' as temporary test folder");
	path = tmp+"/filesaver-test.txt";
	File file = new File(path);
	if (file.exists() && !file.isDirectory()) {
		file.delete();
	}

}


@Test
public void testSave() throws Exception {

	FileSaver saver = new FileSaver(path, "foo");
	saver.save();
	
}

}
