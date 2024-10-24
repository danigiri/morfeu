package cat.calidos.morfeu.webapp;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

import cat.calidos.morfeu.webapp.ui.UICatalogues;
import cat.calidos.morfeu.webapp.ui.UICell;
import cat.calidos.morfeu.webapp.ui.UICellData;
import cat.calidos.morfeu.webapp.ui.UICellEditor;
import cat.calidos.morfeu.webapp.ui.UIContent;
import cat.calidos.morfeu.webapp.ui.UIDocument;


/**
 * Testing of document saving
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DocumentSaveUITest extends UITezt {

private File	contentFile;
private File	backupFile;

@AfterAll
public static void teardownClass() throws InterruptedException {

	Thread.sleep(5000); // wait for reboot of jetty env as files have changed

}


@BeforeEach
public void setup() throws Exception {

	String contentPath = "test-resources/documents/document1.xml";
	String fullContentPath = testAwareFullPathFrom(contentPath);
	contentFile = new File(new URL(fullContentPath).getFile());
	String tmpPath = setupTempDirectory().getAbsolutePath() + "/document1.xml.backup";
	backupFile = new File(tmpPath);

	// System.err.println("\t"+contentFile+" ->"+backupFile);
	FileUtils.copyFile(contentFile, backupFile);

}


@AfterEach
public void teardown() throws Exception {

	if (backupFile.exists()) {
		// System.err.println("\t"+backupFile+" ->"+contentFile);
		FileUtils.copyFile(backupFile, contentFile);
	}

}


@Test
public void documentSaveTest() throws Exception {

	open(appBaseURL);
	UIDocument document = UICatalogues
			.openCatalogues()
			.shouldAppear()
			.clickOn(0)
			.clickOnDocumentNamed("Document 1");
	UIContent content = document.content();
	content.shouldBeVisible();

	// now we make a modification and then save
	UICell data = content
			.rootCells()
			.get(0)
			.child("row(0)")
			.child("col(0)")
			.child("data(0)")
			.select()
			.activate();
	assertNotNull(data);

	UICellData cellData = data.cellInfo();
	assertEquals("42", cellData.attribute("number").value());

	UICellEditor cellEditor = data.edit().shouldAppear();
	assertNotNull(cellEditor);
	cellEditor.cellData().attribute("number").tabIntoEnterText("66");
	cellEditor.clickSave();
	assertEquals("66", cellData.attribute("number").value());

	// now we save, reñload and check the content
	document.clickSave();

	Thread.sleep(5000); // wait for reboot of jetty env as files have changed

	// reload the same document and check the change
	document = UICatalogues
			.openCatalogues()
			.shouldAppear()
			.clickOn(0)
			.clickOnDocumentNamed("Document 1");
	content = document.content();
	content.shouldBeVisible();
	data = content
			.rootCells()
			.get(0)
			.child("row(0)")
			.child("col(0)")
			.child("data(0)")
			.select()
			.activate();
	assertNotNull(data);

	cellData = data.cellInfo();
	assertEquals(
			"66",
			cellData.attribute("number").value(),
			"Saving the document does not modify its content");

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
