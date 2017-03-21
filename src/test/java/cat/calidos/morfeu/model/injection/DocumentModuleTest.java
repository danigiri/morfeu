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
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Provider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cat.calidos.morfeu.model.Document;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DocumentModuleTest {

@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();


@Test
public void testParseDocument() throws Exception {
	
	Document document = parseRelativeLocation("test-resources/documents/document1.json");
	
	testDocument1(document);
	
}


private void testDocument1(Document document) throws URISyntaxException {

	assertEquals("Document 1", document.getName());
	assertEquals("First document", document.getDesc());
	assertEquals("xml", document.getType());

	// FIXME: this is a bit ridiculous and should not leak maven structure
	URI modelURI = new URI("target/test-classes/test-resources/models/test-model.xsd");
	URI contentURI = new URI("target/test-classes/test-resources/documents/document1.xml");
	assertEquals(modelURI, document.getModelURI());
	assertEquals(contentURI, document.getContentURI());
}


@Test(expected = JsonParseException.class)
public void testMalformedDocument() throws Exception {
	parseRelativeLocation("test-resources/documents/malformed-document.json");
}


@Test(expected = JsonMappingException.class)
public void testInvalidDocument() throws Exception {
	parseRelativeLocation("test-resources/documents/nonvalid-document.json");
}

@Test
public void testProduceDocument() throws Exception {

	// I leave this here as justification of using Dagger directly for the test
	// Document document = parseLocation("test-resources/documents/document1.json");	
	// ModelModule.parseModel(new URI(document.getModelURI()), parserProducer);
	// when(modelComponentProvider.get().builder().model().get()).thenReturn(...);
	// DocumentModule.produceDocument(document, modelComponentProvider);

	//System.getenv().keySet().stream().forEach(s->System.err.println(s+":"+System.getenv(s)));
	
	String doc1Path = this.getClass().getClassLoader().getResource("test-resources/documents/document1.json").toString();
	URIModule uriModule = new URIModule(doc1Path);
	DocumentComponent docComponent = DaggerDocumentComponent.builder().URIModule(uriModule).build();
	Document doc = docComponent.produce().get();

	assertNotNull(doc);
	
	testDocument1(doc);
	
}


private Document parseRelativeLocation(String location) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {

	String absoluteLocation = this.getClass().getClassLoader().getResource(location).toString();
	URI uri = new URI(absoluteLocation);
	InputStream stream = FileUtils.openInputStream(FileUtils.toFile(uri.toURL()));
	ObjectMapper mapper = ParserModule.produceJSONObjectMapper();
	Document document = DocumentModule.parseDocument(uri, stream, mapper);

	return document;

}

}
