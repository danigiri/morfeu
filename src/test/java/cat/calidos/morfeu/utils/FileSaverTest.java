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

package cat.calidos.morfeu.utils;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class FileSaverTest extends Tezt {


@Test
public void testSave() throws Exception {

	String tmp = setupTempDirectory();
	//System.err.println("FileSaverTest::Using '"+tmp+"' as temporary test folder");
	String path = tmp+"/filesaver-test-"+System.currentTimeMillis()+".txt";
	File file = new File(path);
	if (file.exists() && !file.isDirectory()) {
		file.delete();
	}
	file.deleteOnExit();
	String contentWritten = "foo";
	FileSaver saver = new FileSaver(path, contentWritten);
	saver.save();
	File file2 = new File(path);
	String contentRead = FileUtils.readFileToString(file2, Config.DEFAULT_CHARSET);

	assertEquals("Content written to the output file was not as expected", contentWritten, contentRead);

}


@Test
public void testBackup() throws Exception {

	String tmp = setupTempDirectory();
	//System.err.println("FileSaverTest::Using '"+tmp+"' as temporary test folder");
	String path = tmp+"/filesaver-test-"+System.currentTimeMillis()+".txt";

	String contentWritten = "foo";
	FileSaver saver = new FileSaver(path, contentWritten);
	saver.save();
	
	saver = new FileSaver(path, "foo2");
	saver.save();									 // this creates a backup with content 'foo'
	File file2 = new File(path+FileSaver.BACKUP_EXTENSION);
	assertTrue("File saver did not create a backup file", file2.exists());
	
	String contentRead = FileUtils.readFileToString(file2, Config.DEFAULT_CHARSET);
	assertEquals("Backup file did not have original content", contentWritten, contentRead);
	
}

}
