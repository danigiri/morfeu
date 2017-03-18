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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cat.calidos.morfeu.model.Document;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DocumentModuleTest {

// TODO: move to integration testing
//@Test
//public void testInjection() throws Exception {
//	
//	String uri = "http://localhost:3000/test-resources/documents/document1.json";
//	Document document = DaggerDocumentComponent.builder()
//						.URIModule(new URIModule(uri))
//						.build()
//						.produce()
//						.get();
//	System.err.println(document);
//}

@Test
public void testParseDocument() throws Exception {
	
	Document document = parseLocation("test-resources/documents/document1.json");
	
	assertEquals("Document 1", document.getName());
	assertEquals("First document", document.getDesc());
	assertEquals("xml", document.getType());

	URI modelURI = new URI("http://localhost:3000/test-resources/models/test-model.xsd");
	URI contentURI = new URI("http://localhost:3000/test-resources/documents/document1.xml");
	assertEquals(modelURI, document.getModelURI());
	assertEquals(contentURI, document.getContentURI());
	
}


@Test(expected = JsonParseException.class)
public void testMalformedDocument() throws Exception {
	parseLocation("test-resources/documents/malformed-document.json");
}


@Test(expected = JsonMappingException.class)
public void testInvalidDocument() throws Exception {
	parseLocation("test-resources/documents/invalid-document.json");
}


private Document parseLocation(String location) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {

	URI uri = new URI(location);
	InputStream stream = this.getClass().getClassLoader().getResourceAsStream(location);
	ObjectMapper mapper = ParserModule.produceJSONObjectMapper();
	Document document = DocumentModule.parseDocument(uri, stream, mapper);

	return document;

}

}
