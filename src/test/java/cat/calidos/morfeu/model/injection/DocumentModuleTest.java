/*
 *    Copyright 2016 Daniel Giribet
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

package cat.calidos.morfeu.model.injection;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DocumentModuleTest {

@Rule 
public MockitoRule mockitoRule = MockitoJUnit.rule();



@Test
public void testParseDocument() throws Exception {
	
	Document document = parseRelativeLocation("test-resources/documents/document1.json");
	
	testDocument1(document);

}


@Test(expected = ParsingException.class)
public void testMalformedDocument() throws Exception {
	parseRelativeLocation("test-resources/documents/malformed-document.json");
}


@Test(expected = ParsingException.class)
public void testInvalidDocument() throws Exception {
	parseRelativeLocation("test-resources/documents/nonvalid-document.json");
}


public static void testDocument1(Document document) throws URISyntaxException {

	assertEquals("Document 1", document.getName());
	assertEquals("First document", document.getDesc());
	assertEquals("xml", document.getType());

	// FIXME: this is a bit ridiculous, we should not leak maven structure
	URI modelURI = new URI("target/test-classes/test-resources/models/test-model.xsd");
	URI contentURI = new URI("target/test-classes/test-resources/documents/document1.xml");
	assertEquals(modelURI, document.getModelURI());
	assertEquals(contentURI, document.getContentURI());
	
}


private Document parseRelativeLocation(String location) throws ParsingException, FetchingException, URISyntaxException, MalformedURLException, IOException {

	String absoluteLocation = this.getClass().getClassLoader().getResource(location).toString();
	URI uri = new URI(absoluteLocation);
	InputStream stream = FileUtils.openInputStream(FileUtils.toFile(uri.toURL()));
	ObjectMapper mapper = ParserModule.produceJSONObjectMapper();
	Document document = DocumentModule.parseDocument(uri, stream, mapper);

	return document;

}

}
