// DOCUMENT INT TEST . JAVA

package cat.calidos.morfeu.model.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.utils.MorfeuUtils;


public class DocumentIntTest extends ModelTezt {

@Test
public void testProduceCompleteDocument() throws Exception {

	// This feels painful to write and using Dagger for complete integration black box tests seems
	// fair
	// Document document = parseLocation("test-resources/documents/document1.json");
	// ModelModule.parseModel(new URI(document.getModelURI()), parserProducer);
	// when(modelComponentProvider.get().builder().model().get()).thenReturn(...);
	// DocumentModule.produceDocument(document, modelComponentProvider);

	Document doc = produceDocumentFromPath("test-resources/documents/document1.json");
	assertNotNull(doc);

	DocumentTest.testDocument1(doc);

	Composite<Cell> content = doc.asComplex().children();
	assertNotNull(content);

}


@Test
public void testNonValidContentDocument() throws Exception {

	try {

		produceDocumentFromPath("test-resources/documents/document-with-nonvalid-content.json");

	} catch (ExecutionException e) {

		Throwable rootCause = MorfeuUtils.findRootCauseFrom(e);
		assertEquals(ValidationException.class, rootCause.getClass());
		assertTrue(rootCause.getMessage().contains("Invalid content"));

	}
}


@Test
public void testNotFoundModelDocument() throws Exception {

	try {

		System.out.println("Please ignore next FileNotFoundException, it is expected");
		produceDocumentFromPath("test-resources/documents/document-with-notfound-model.json");

	} catch (ExecutionException e) {

		Throwable rootCause = MorfeuUtils.findRootCauseFrom(e);
		assertEquals(FetchingException.class, rootCause.getClass());

	}
}


@Test
public void testNonValidModelDocument() throws Exception {

	try {

		System.err.println(
				"Please ignore next ParsingException, it is expected as the model is not valid");
		produceDocumentFromPath("test-resources/documents/document-with-nonvalid-model.json");

	} catch (ExecutionException e) {

		Throwable rootCause = MorfeuUtils.findRootCauseFrom(e);
		assertEquals(ParsingException.class, rootCause.getClass());
		assertTrue(rootCause.getMessage().contains("undefined"));

	}

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
