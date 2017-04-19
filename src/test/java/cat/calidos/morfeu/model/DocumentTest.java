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

package cat.calidos.morfeu.model;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.problems.ValidationException;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DocumentTest {

@Test
public void testSetModelURI() throws Exception {
	
	String site = "http://foo.com";
	String path = "/whatever.json";
	String relative = "/relative.xsd";
	Document doc = createDocumentWithModel(site, path, relative);
	
	// url should be "http://foo.com/relative.xsd" as we want to ensure we reach the server
	URI expectedAbsoluteModelURI = new URI(site+relative);
	assertEquals(expectedAbsoluteModelURI, doc.getModelURI());
	
	
	site = "http://foo.com:8080";
	doc = createDocumentWithModel(site, path, relative);

	expectedAbsoluteModelURI = new URI(site+relative);
	assertEquals(expectedAbsoluteModelURI, doc.getModelURI());
	
	
	// for file paths we don't make them absolute
	site = "file://";
	doc = createDocumentWithModel(site, path, relative);

	expectedAbsoluteModelURI = new URI(relative);
	assertEquals(expectedAbsoluteModelURI, doc.getModelURI());
	
	
	String absoluteModel = "http://bar.com/absolute.xsd";
	doc = createDocumentWithModel(site, path, absoluteModel);

	expectedAbsoluteModelURI = new URI(absoluteModel);
	assertEquals(expectedAbsoluteModelURI, doc.getModelURI());
	
}

@Test(expected=ValidationException.class)
public void testEmptyValidator() throws Exception {
	
	String site = "http://foo.com";
	String path = "/whatever.json";
	String relative = "/relative.xsd";
	Document doc = createDocumentWithModel(site, path, relative);

	doc.validate();

}



private Document createDocumentWithModel(String site, String path, String model) throws URISyntaxException {

	URI uri = new URI(site+path);
	URI modelURI = new URI(model);
	Document doc = new Document(uri);
	doc.setModelURI(modelURI);

	return doc;

}

}
