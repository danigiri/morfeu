package cat.calidos.morfeu.control;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.utils.Config;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@TestInstance(Lifecycle.PER_CLASS)
public class ContentSaveControlTest extends ModelTezt {

private String	prefix;
private String	path;
private String	content;
private String	modelPath;

@BeforeAll
public void setup() throws Exception {

	prefix = testAwareFullPathFrom(".");
	path = "ctrl-save-output-document1.xml";
	content = readFromFile("test-resources/transform/document1-as-view.json");
	modelPath = "test-resources/models/test-model.xsd";

}


@Test @DisplayName("Content save test")
public void contentSave() throws Exception {

	ContentSaveControl ctrl = new ContentSaveControl(prefix, path, content, Optional.empty(),
			modelPath);
	ctrl.process(); // this controller will save the document

	String savedContent = readFromFile(path);

	compareWithXMLFile(savedContent, "target/test-classes/test-resources/documents/document1.xml");

}


@Test @DisplayName("Content save with filter test")
public void contentSaveWithFilter() throws Exception {

	String filter = "map-to-string{\"key\":\"xml\"};"
			+ "replace{\"replacements\":{\"from\":\"blahblah\", \"to\":\"HELLO\"}}";
	ContentSaveControl ctrl = new ContentSaveControl(prefix, path, content, Optional.of(filter),
			modelPath);
	ctrl.process(); // this controller will save the document

	String savedContent = readFromFile(path);
	assertAll(
			"testing filtered save",
			() -> assertNotNull(savedContent),
			() -> assertTrue(savedContent.contains("HELLO")),
			() -> assertFalse(savedContent.contains("blahblah")));

}


@AfterAll
public void teardown() {

	File file = new File(prefix + path);
	if (file.exists()) {
		file.delete();
	}

}


private String readFromFile(String contentPath) throws URISyntaxException, IOException {

	String fullContentPath = testAwareFullPathFrom(contentPath);
	URI fullContentURI = new URI(fullContentPath);
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
