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
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.SAXParserFactory;

import org.junit.Test;
import org.xml.sax.SAXParseException;

import cat.calidos.morfeu.model.injection.DaggerValidationComponent;
import cat.calidos.morfeu.model.injection.IntT3st;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class XSDValidatorIntTest  extends IntT3st {


@Test
public void testValidate() throws Exception {

	String docPath = uriModuleForPath("test-resources/documents/document1.xml");
	String modelPath = uriModuleForPath("test-resources/models/test-model.xsd");
	Validable validator = DaggerValidationComponent.builder()
							.forDocument(new URI(docPath))
							.withModel(new URI(modelPath))
							.build()
							.validator()
							.get();
	validator.validate();
	
	// should not throw exception
}


@Test
public void testNonValidDocument() throws Exception {

	String docPath = uriModuleForPath("test-resources/documents/nonvalid-document.xml");
	String modelPath = uriModuleForPath("test-resources/models/test-model.xsd");
	Validable validator = DaggerValidationComponent.builder()
							.forDocument(new URI(docPath))
							.withModel(new URI(modelPath))
							.build()
							.validator()
							.get();
	try {
		validator.validate();
	} catch (RuntimeException e) {
		Throwable cause = e.getCause();
		if (cause instanceof SAXParseException) {
			SAXParseException parseException = (SAXParseException)cause;
			assertTrue(parseException.getMessage().contains("notvalid"));
		} else {
			fail("Should throw a parse exception and instead something else failed");
		}
	}

}

}
