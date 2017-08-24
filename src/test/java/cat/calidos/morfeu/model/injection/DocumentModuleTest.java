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

import com.fasterxml.jackson.databind.ObjectMapper;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.Composite;
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


@Test
public void testDocumentPrefix() throws Exception {
	
	URI u = new URI("http://foo.com/well/whatever.json");
	Document doc = new Document(u, "doc", "desc");
	String pref = "http://bar.com";

	URI expected = new URI(pref);
	assertEquals(expected, DocumentModule.documentPrefix(doc, pref));
	
	doc = new Document(u, "doc", "desc");	// empty prefix defined so we guess
	expected = new URI("http://foo.com/well/");
	assertEquals(expected, DocumentModule.documentPrefix(doc, ""));

}


@Test
public void testModelURI() throws Exception {
	
	String site = "http://foo.com/well/";
	URI prefixURI = new URI(site);
	String path = "whatever.json";
	String model = "model.xsd";
	String content = "content.xml";
	Document doc = createDocument(site, path, model, content);
	
	// url should be "http://foo.com/well/model.xsd" as we want to ensure we reach the server
	URI expected = new URI(site+model);
	assertEquals(expected, DocumentModule.fetchableModelURI(prefixURI, doc));
	
	site = "http://foo.com:8080/well/";
	prefixURI = new URI(site);
	doc = createDocument(site, path, model, content);
	
	expected = new URI(site+model);
	assertEquals(expected, DocumentModule.fetchableModelURI(prefixURI, doc));
	
	// for file paths we don't make them absolute as per current contract, so unmodified model URI
	site = "file://tmp/";
	doc = createDocument(site, path, model, content);
	prefixURI = new URI(site);
	
	expected = new URI(model);
	assertEquals(expected, DocumentModule.fetchableModelURI(prefixURI, doc));

	model = "http://bar.com/absolute.xsd";
	site = "http://foo.com/well/";
	doc = createDocument(site, path, model, content);
	prefixURI = new URI(site);

	expected = new URI(model);
	assertEquals(expected, DocumentModule.fetchableModelURI(prefixURI, doc));

}


public static void testDocument1(Document document) throws URISyntaxException {

	assertEquals("Document 1", document.getName());
	assertEquals("First document", document.getDesc());
	assertEquals("xml", document.getKind());

	// FIXME: we should not leak maven structure is possible
	URI modelURI = new URI("target/test-classes/test-resources/models/test-model.xsd");
	URI contentURI = new URI("target/test-classes/test-resources/documents/document1.xml");
	assertEquals(modelURI, document.getModelURI());
	assertEquals(contentURI, document.getContentURI());
	
}


private Document parseRelativeLocation(String location) throws ParsingException, FetchingException, URISyntaxException, MalformedURLException, IOException {

	String absoluteLocation = this.getClass().getClassLoader().getResource(location).toString();
	URI uri = new URI(absoluteLocation);
	InputStream stream = FileUtils.openInputStream(FileUtils.toFile(uri.toURL()));
	ObjectMapper mapper = DocumentParserModule.produceJSONObjectMapper();
	Document document = DocumentModule.parseDocument(uri, stream, mapper);

	return document;

}



private Document createDocument(String site, String path, String model, String content) throws URISyntaxException {

	URI uri = new URI(site+path);
	URI modelURI = new URI(model);
	URI contentURI = new URI(content);
	Document doc = new Document(uri, "doc", "desc");
	doc.setModelURI(modelURI);
	doc.setContentURI(contentURI);
	
	return doc;

}

}
