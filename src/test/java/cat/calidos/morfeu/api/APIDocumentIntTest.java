/*
 *    Copyright 2017 Daniel Giribet
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

package cat.calidos.morfeu.api;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
 
/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class APIDocumentIntTest {

private CloseableHttpClient client;
private String webappPrefix;


@Before
public void tearup() {

	client = HttpClients.createDefault();

	webappPrefix = System.getenv("webapp-prefix");
	if (webappPrefix==null) {		
		webappPrefix = "http://localhost:8080/morfeu/";
	}
	
}


@Test
public void testDocument() throws Exception {
	

	InputStream content = fetchInputStreamFrom("documents/test-resources/documents/document1.json");
	assertNotNull(content);
	
	JsonNode doc = parseJson(content);
	assertEquals("Document 1", doc.get("name").asText());
	assertEquals("First document", doc.get("desc").asText());
	assertEquals("xml", doc.get("kind").asText());
	String modelURI = "target/test-classes/test-resources/models/test-model.xsd";
	assertEquals(modelURI, doc.get("modelURI").asText());
	String expected = webappPrefix+modelURI;
	assertEquals(expected, doc.get("fetchableModelURI").asText());
	expected = webappPrefix+"target/test-classes/test-resources/documents/document1.xml";
	assertEquals(expected, doc.get("contentURI").asText());
	assertTrue(doc.get("valid").asBoolean());
	
}


@Test
public void testNonValidContentDocument() throws Exception {
	
	InputStream content = fetchInputStreamFrom("documents/test-resources/documents/document-with-nonvalid-content.json");
	assertNotNull(content);
	
	JsonNode doc = parseJson(content);
	assertEquals("Document with non-valid content", doc.get("name").asText());

	String expected = webappPrefix+"target/test-classes/test-resources/documents/nonvalid-document.xml";
	assertEquals(expected, doc.get("contentURI").asText());
	assertFalse(doc.get("valid").asBoolean());
	 
	assertTrue(doc.get("problem").asText().contains("Invalid content"));
	
}



@Test
public void testNonValidModelDocument() throws Exception {
	
	InputStream content = fetchInputStreamFrom("documents/test-resources/documents/document-with-nonvalid-model.json");
	assertNotNull(content);
	
	JsonNode doc = parseJson(content);
	assertEquals("Problematic document", doc.get("name").asText());
	assertFalse(doc.get("valid").asBoolean());
	assertTrue(doc.get("problem").asText().contains("Problem parsing model"));
	
}


@Test
public void testNotFoundModelDocument() throws Exception {
	
	InputStream content = fetchInputStreamFrom("documents/test-resources/documents/document-with-notfound-model.json");
	assertNotNull(content);
	
	JsonNode doc = parseJson(content);
	assertEquals("Problematic document", doc.get("name").asText());
	assertFalse(doc.get("valid").asBoolean());
	assertTrue(doc.get("problem").asText().contains("Problem fetching model"));
	
}


@Test
public void testMalformedDocument() throws Exception {
	
	InputStream content = fetchInputStreamFrom("documents/test-resources/documents/malformed-document.json");
	assertNotNull(content);
	
	JsonNode doc = parseJson(content);
	assertEquals("Problematic document", doc.get("name").asText());
	assertFalse(doc.get("valid").asBoolean());
	assertTrue(doc.get("problem").asText().contains("Problem with the json format"));
	
}


@After
public void teardown() throws IOException {
	client.close();
}


private InputStream fetchInputStreamFrom(String location)
		throws URISyntaxException, IOException, UnsupportedOperationException, ClientProtocolException {

	String uri = webappPrefix+location;
	URI u = new URI(uri);
	HttpGet request = new HttpGet(u);
	InputStream content = client.execute(request)
							 .getEntity()
							 .getContent();
	
	return content;
	
}


private JsonNode parseJson(InputStream content) throws IOException, JsonProcessingException {

	ObjectMapper mapper = new ObjectMapper();
	JsonNode doc = mapper.readTree(content);
	return doc;
}

}
