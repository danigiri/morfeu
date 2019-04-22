// CONTENT SAVER PARSER INT TEST . JAVA

package cat.calidos.morfeu.model.injection;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

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


@AfterClass
public static void teardownClass() throws InterruptedException {

	Thread.sleep(5000); // wait for reboot of jetty env

}



@Before
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


@Test
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
	assertTrue("'Content saver parser' did not validate a valid XML string", validator.isValid());

	Composite<Cell> rootCells = contentComponent.content().get();
	assertNotNull(rootCells);

	assertEquals("Wrong size of content root from 'content saver parser' parsed XML string", 1, rootCells.size());
	Cell test = rootCells.child(0).asComplex().children().child("test(0)"); // skip virtual root
	assertEquals("Wrong root node name from 'content saver parser' parsed XML string", "test", test.getName());

}


@Test
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
		assertTrue("Wrong exception message parsing of 'content saver parser'", e.getMessage().contains("notvalid"));
	}

}


@Test
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
	assertTrue("Saver component did not create a file", savedFile.exists());

	try {
		String writtenContent = FileUtils.readFileToString(savedFile, Config.DEFAULT_CHARSET);
		assertEquals("Content saved to file is not the same as input", content, writtenContent);
	} finally {
		if (savedFile.exists()) {
			savedFile.delete();
		}
	}

}


@Test
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
	File savedFile = new File(outputPath);
	assertTrue("Saver component did not create a file", savedFile.exists());
	savedFile.deleteOnExit();

	String writtenContent = FileUtils.readFileToString(savedFile, Config.DEFAULT_CHARSET);
	//System.err.println(writtenContent);

	YAMLMapper mapper = new YAMLMapper();
	checkContent(writtenContent, mapper);

}



@Test
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
	File savedFile = new File(outputPath);
	assertTrue("Saver component did not create a file", savedFile.exists());
	savedFile.deleteOnExit();

	String writtenContent = FileUtils.readFileToString(savedFile, Config.DEFAULT_CHARSET);
	//System.err.println(writtenContent);

	ObjectMapper mapper = new ObjectMapper();
	checkContent(writtenContent, mapper);

}


private String temporaryOutputFilePath() {

	return tmpPath+"/filesaver-test-"+System.currentTimeMillis()+".txt";
}



private void checkContent(String writtenContent, ObjectMapper mapper) throws IOException {

	JsonNode node = mapper.readTree(writtenContent);
	assertNotNull(node);
	assertTrue(node.isObject());
	assertTrue(node.has("rows"));

	JsonNode rows = node.get("rows");			//rows
	assertNotNull(rows);
	assertTrue(rows.isArray());
	assertEquals(1, rows.size());

	JsonNode cols = rows.get(0).get("cols");
	assertNotNull(cols);
	assertTrue(cols.isArray());

	JsonNode col0Size = cols.get(0).get("size");
	assertNotNull(col0Size);
	assertEquals(4, col0Size.asInt());

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
