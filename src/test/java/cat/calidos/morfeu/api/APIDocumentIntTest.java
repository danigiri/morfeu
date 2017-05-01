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

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
	

	String uri = webappPrefix+"documents/test-resources/documents/document1.json";
	URI u = new URI(uri);
	HttpGet request = new HttpGet(u);
	InputStream content = client.execute(request)
			 .getEntity()
			 .getContent();
	assertNotNull(content);
	
	ObjectMapper mapper = new ObjectMapper();
	JsonNode doc = mapper.readTree(content);
	assertEquals("Document 1", doc.get("name").asText());
	assertEquals("First document", doc.get("desc").asText());
	assertEquals("xml", doc.get("kind").asText());
	String expected = webappPrefix+"target/test-classes/test-resources/models/test-model.xsd";
	assertEquals(expected, doc.get("modelURI").asText());
	expected = webappPrefix+"target/test-classes/test-resources/documents/document1.xml";
	assertEquals(expected, doc.get("contentURI").asText());
	assertTrue(doc.get("valid").asBoolean());
	
}


@After
public void deardown() throws IOException {
	client.close();
}

}
