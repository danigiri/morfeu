// API DOCUMENT INT TEST . JAVA

package cat.calidos.morfeu.api;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class APIDocumentIntTest extends APITezt {

@Test
public void testDocument() throws Exception {

	InputStream content = fetchRemoteInputStreamFrom(
			"documents/" + pathPrefix + "documents/document1.json");
	assertNotNull(content);

	JsonNode doc = parseJson(content);
	assertEquals("Document 1", doc.get("name").asText());
	assertEquals("First document", doc.get("desc").asText());
	assertEquals("xml", doc.get("kind").asText());

	var modelURI = pathPrefix + "models/test-model.xsd?not=used";
	assertEquals(modelURI, doc.get("modelURI").asText());
	assertTrue(
			doc.get("fetchableModelURI").asText().endsWith(modelURI),
			"Fetchable model uri does not end correctly");

	var contentURI = pathPrefix + "documents/document1.xml";
	assertEquals(contentURI, doc.get("contentURI").asText());
	assertTrue(
			doc.get("fetchableContentURI").asText().endsWith(contentURI),
			"Fetchable content uri not correct");

	assertTrue(doc.get("valid").asBoolean());

}


@Test
public void testNonValidContentDocument() throws Exception {

	InputStream content = fetchRemoteInputStreamFrom(
			"documents/" + pathPrefix + "documents/document-with-nonvalid-content.json");
	assertNotNull(content);

	JsonNode doc = parseJson(content);
	assertEquals("Problematic document", doc.get("name").asText());
	assertTrue(doc.get("problem").asText().contains("One of '{col}' is expected"));
	assertFalse(doc.get("valid").asBoolean());

}


@Test
public void testNonValidModelDocument() throws Exception {

	InputStream content = fetchRemoteInputStreamFrom(
			"documents/" + pathPrefix + "documents/document-with-nonvalid-model.json");
	assertNotNull(content);
	// content = printInputStream(content);

	JsonNode doc = parseJson(content);
	assertEquals("Problematic document", doc.get("name").asText());
	assertFalse(doc.get("valid").asBoolean());
	assertTrue(doc.get("problem").asText().contains("Problem parsing model"));

}


@Test
public void testNotFoundModelDocument() throws Exception {

	InputStream content = fetchRemoteInputStreamFrom(
			"documents/" + pathPrefix + "documents/document-with-notfound-model.json");
	assertNotNull(content);

	JsonNode doc = parseJson(content);
	assertEquals("Problematic document", doc.get("name").asText());
	assertFalse(doc.get("valid").asBoolean());
	assertTrue(doc.get("problem").asText().contains("Problem fetching model"));

}


@Test
public void testMalformedDocument() throws Exception {

	InputStream content = fetchRemoteInputStreamFrom(
			"documents/" + pathPrefix + "documents/malformed-document.json");
	assertNotNull(content);

	JsonNode doc = parseJson(content);
	assertEquals("Problematic document", doc.get("name").asText());
	assertFalse(doc.get("valid").asBoolean());
	assertTrue(doc.get("problem").asText().contains("Problem with the json format"));

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
