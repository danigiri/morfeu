// CONTENT SAVER PARSER INT TEST . JAVA

package cat.calidos.morfeu.model.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Validable;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.Saver;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentSaverParserIntTest extends ModelTezt {

private URI modelURI;
private URI modelFetchableURI;
private String content;
private URI contentURI;
private String tmpPath;


@AfterAll
public static void teardownClass() throws InterruptedException {

	Thread.sleep(5000); // wait for reboot of jetty env

}


@BeforeEach
public void setup() throws Exception {

	String modelPath = "test-resources/models/test-model.xsd";
	modelURI = new URI(modelPath);
	modelFetchableURI = new URI("target/test-classes/"+modelPath);

	String contentPath = "test-resources/documents/document1.xml";
	contentURI = new URI(contentPath);
	String fullContentPath = testAwareFullPathFrom(contentPath);
	URI fullContentURI = new URI(fullContentPath);
	content = IOUtils.toString(fullContentURI, Config.DEFAULT_CHARSET);

	tmpPath = setupTempDirectory();

}


@Test @DisplayName("Validate string parsing")
public void testValidateString() throws Exception {

	URI outputURI = new URI("file://"+temporaryOutputFilePath());
	ContentSaverParserComponent contentComponent = DaggerContentSaverParserComponent
													.builder()
													.from(content)
													.to(outputURI)
													.having(contentURI)
													.model(modelURI)
													.withModelFetchedFrom(modelFetchableURI)
													.build();

	Validable validator = contentComponent.validator().get();
	assertNotNull(validator);
	validator.validate();	// this would throw an exception
	assertTrue(validator.isValid(), "'Content saver parser' did not validate a valid XML string");

	Composite<Cell> rootCells = contentComponent.content().get();
	assertNotNull(rootCells);

	assertEquals(1, rootCells.size(), "Wrong size of content root from 'content saver parser' parsed XML string");
	Cell test = rootCells.child(0).asComplex().children().child("test(0)"); // skip virtual root
	assertEquals("test", test.getName(), "Wrong root node name from 'content saver parser' parsed XML string");

}


@Test @DisplayName("Non valid string parsing")
public void testNonValidString() throws Exception {

	URI outputURI = new URI("file://"+temporaryOutputFilePath());
	String contentPath = "test-resources/documents/nonvalid-document.xml";
	String fullContentPath = testAwareFullPathFrom(contentPath);
	URI fullContentURI = new URI(fullContentPath);
	String content = IOUtils.toString(fullContentURI, Config.DEFAULT_CHARSET);

	Validable validator = DaggerContentSaverParserComponent
							.builder()
							.from(content)
							.to(outputURI)
							.having(new URI(contentPath))
							.model(modelURI)
							.withModelFetchedFrom(modelFetchableURI)
							.build()
							.validator()
							.get();

	try {
		System.err.println("Please ignore next ParsingException, it is expected as we are testing non valid str");
		validator.validate();
	} catch (ValidationException e) {
		assertTrue(e.getMessage().contains("notvalid"), "Wrong exception message parsing of 'content saver parser'");
	}

}


@Test @DisplayName("Save to XML")
public void testSaveToXML() throws Exception {

	String outputPath = temporaryOutputFilePath();
	URI outputURI = new URI("file://"+outputPath);

	Saver saver = DaggerContentSaverParserComponent.builder()
													.from(content)
													.to(outputURI)
													.having(contentURI)
													.model(modelURI)
													.withModelFetchedFrom(modelFetchableURI)
													.build()
													.saver()
													.get();
	saver.save();
	File savedFile = new File(outputPath);
	assertTrue(savedFile.exists(), "Saver component did not create a file");

	try {
		String writtenContent = FileUtils.readFileToString(savedFile, Config.DEFAULT_CHARSET);
		assertEquals(content, writtenContent, "Content saved to file is not the same as input");
	} finally {
		if (savedFile.exists()) {
			savedFile.delete();
		}
	}

}


@Test @DisplayName("Save to YAML")
public void testSaveToYAML() throws Exception {

	String outputPath = temporaryOutputFilePath()+".yaml";
	URI outputURI = new URI("file://"+outputPath);

	Saver saver = DaggerContentSaverParserComponent.builder()
													.from(content)
													.to(outputURI)
													.having(contentURI)
													.model(modelURI)
													.withModelFetchedFrom(modelFetchableURI)
													.build()
													.saver()
													.get();
	saver.save();
	File savedFile = checkSavedFile(outputPath);

	String writtenContent = FileUtils.readFileToString(savedFile, Config.DEFAULT_CHARSET);
	//System.err.println(writtenContent);

	YAMLMapper mapper = new YAMLMapper();
	checkContent(writtenContent, mapper);

}


@Test @DisplayName("Save to JSON")
public void testSaveToJSON() throws Exception {

	String outputPath = temporaryOutputFilePath()+".json";
	URI outputURI = new URI("file://"+outputPath);

	Saver saver = DaggerContentSaverParserComponent.builder()
													.from(content)
													.to(outputURI)
													.having(contentURI)
													.model(modelURI)
													.withModelFetchedFrom(modelFetchableURI)
													.build()
													.saver()
													.get();
	saver.save();
	File savedFile = checkSavedFile(outputPath);

	String writtenContent = FileUtils.readFileToString(savedFile, Config.DEFAULT_CHARSET);
	//System.err.println(writtenContent);

	ObjectMapper mapper = new ObjectMapper();
	checkContent(writtenContent, mapper);

}


@Test @DisplayName("Save to filters")
public void testSaveToFilters() throws Exception {

	String outputPath = temporaryOutputFilePath()+".yaml";
	URI outputURI = new URI("file://"+outputPath);

	String filters = "content-to-yaml;replace{\"from\":\"blahblah\", \"to\":\"YEAH\"}";
	Saver saver = DaggerContentSaverParserComponent.builder()
													.from(content)
													.filters(filters)
													.to(outputURI)
													.having(contentURI)
													.model(modelURI)
													.withModelFetchedFrom(modelFetchableURI)
													.build()
													.saver()
													.get();
	saver.save();
	File savedFile = checkSavedFile(outputPath);

	String writtenContent = FileUtils.readFileToString(savedFile, Config.DEFAULT_CHARSET);
	System.err.println(writtenContent);

	YAMLMapper mapper = new YAMLMapper();
	checkContent(writtenContent, mapper);

	assertAll("Check replace was applied",
			() -> assertFalse(writtenContent.contains("blahblah")),
			() -> assertTrue(writtenContent.contains("YEAH"))
	);

}


private String temporaryOutputFilePath() {
	return tmpPath+"/filesaver-test-"+System.currentTimeMillis()+".txt";
}


private void checkContent(String writtenContent, ObjectMapper mapper) throws IOException {

	JsonNode node = mapper.readTree(writtenContent);
	assertAll("check content",
		() -> assertNotNull(node),
		() -> assertTrue(node.isObject()),
		() -> assertTrue(node.has("rows"))
	);

	JsonNode rows = node.get("rows");			//rows
	assertAll("check rows",
		() -> assertNotNull(rows),
		() -> assertTrue(rows.isArray()),
		() -> assertEquals(1, rows.size())
	);

	JsonNode cols = rows.get(0).get("cols");
	assertAll("check cols",
		() -> assertNotNull(cols),
		() -> assertTrue(cols.isArray())
	);

	JsonNode col0Size = cols.get(0).get("size");
	assertAll("check col",
		() -> assertNotNull(col0Size),
		() -> assertEquals(4, col0Size.asInt())
	);

}


private File checkSavedFile(String outputPath) {

	File savedFile = new File(outputPath);
	assertTrue(savedFile.exists(), "Saver component did not create a file");
	savedFile.deleteOnExit();

	return savedFile;

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
