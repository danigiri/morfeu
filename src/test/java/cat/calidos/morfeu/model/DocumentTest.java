package cat.calidos.morfeu.model;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.problems.ValidationException;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DocumentTest {

@Test
public void testEmptyValidator() throws Exception {
	String site = "http://foo.com";
	String path = "/whatever.json";
	URI uri = new URI(site + path);
	URI modelURI = new URI("/relative.xsd");
	URI contentURI = new URI("/content.xsd");
	Document doc = new Document(uri, "name", "desc");
	doc.setModelURI(modelURI);
	doc.setContentURI(contentURI);
	// notice we do not set a validator for this document

	assertThrows(ValidationException.class, () -> doc.validate());
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
