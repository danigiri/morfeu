package cat.calidos.morfeu.control;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.utils.Config;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentSaveControlTest extends ModelTezt {

@Test @DisplayName("Content save test")
public void contentSave() throws Exception {

	String prefix = testAwareFullPathFrom(".");
	String path = "ctrl-save-output-document1.xml";
	String content = readFromFile("test-resources/transform/document1-as-view.json");

	String modelPath = "test-resources/models/test-model.xsd";
	ContentSaveControl ctrl = new ContentSaveControl(prefix, path, content, Optional.empty(), modelPath);
	ctrl.process();	// this controller will save the document

	String savedContent = readFromFile(path);

	compareWithXMLFile(savedContent, "target/test-classes/test-resources/documents/document1.xml");

}


String readFromFile(String contentPath) throws URISyntaxException, IOException {

	String fullContentPath = testAwareFullPathFrom(contentPath);
	URI fullContentURI = new URI(fullContentPath);
	String content = IOUtils.toString(fullContentURI, Config.DEFAULT_CHARSET);

	return content;

}


}


/*
 *    Copyright 2019 Daniel Giribet
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

