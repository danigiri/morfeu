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

