package cat.calidos.morfeu.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class FileSaverTest extends Tezt {


@Test
public void testSave() throws Exception {

	String tmp = setupTempDirectory().getAbsolutePath();
	//System.err.println("FileSaverTest::Using '"+tmp+"' as temporary test folder");
	var path = tmp+"/filesaver-test-"+System.currentTimeMillis()+".txt";
	var file = new File(path);
	if (file.exists() && !file.isDirectory()) {
		file.delete();
	}
	file.deleteOnExit();
	var contentWritten = "foo";
	LocalSaver saver = new LocalSaver(path, contentWritten);
	saver.save();
	var file2 = new File(path);
	String contentRead = FileUtils.readFileToString(file2, Config.DEFAULT_CHARSET);

	assertEquals(contentWritten, contentRead, "Content written to the output file was not as expected");

}


@Test
public void testBackup() throws Exception {

	String tmp = setupTempDirectory().getAbsolutePath();
	//System.err.println("FileSaverTest::Using '"+tmp+"' as temporary test folder");
	var path = tmp+"/filesaver-test-"+System.currentTimeMillis()+".txt";

	var contentWritten = "foo";
	LocalSaver saver = new LocalSaver(path, contentWritten);
	saver.save();

	saver = new LocalSaver(path, "foo2");
	saver.save();									 // this creates a backup with content 'foo'
	var file2 = new File(path+LocalSaver.BACKUP_EXTENSION);
	assertTrue(file2.exists(), "File saver did not create a backup file");

	String contentRead = FileUtils.readFileToString(file2, Config.DEFAULT_CHARSET);
	assertEquals(contentWritten, contentRead, "Backup file did not have original content");

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
