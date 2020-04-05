package cat.calidos.morfeu.control;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentGETControlTest extends ModelTezt {


@Test @DisplayName("Content GET test")
public void contentGET() throws Exception {

	String prefix = testAwareFullPathFrom(".");
	String path = "test-resources/documents/document1.xml";
	String model = "test-resources/models/test-model.xsd";
	@SuppressWarnings("unchecked")
	List<Cell> content = (List<Cell>) new ContentGETControl(prefix, path, Optional.empty(), model).process();
	assertNotNull(content);

	Cell test = content.get(0);
	assertAll("test basic content",
		() -> assertNotNull(test),
		() -> assertEquals("test-resources/documents/document1.xml", test.getURI().toString()),
		() -> assertTrue(test.isComplex())
	);

}


@Test @DisplayName("Content GET filtered test")
public void contentGETFiltered() throws Exception {


	String prefix = testAwareFullPathFrom(".");
	String path = "test-resources/documents/filtered.xml";
	String model = "test-resources/models/test-model.xsd";
	String f = "replace{\"replacements\":[" +
						"{\"from\":\"=\\\\s*\\\\{\",\"to\":\"=\\\"{\"},"+
						"{\"from\":\"}\",\"to\":\"}\\\"\"}" +
				"]}";
	Optional<String> filters = Optional.of(f);	// if the filters are not applied we get a different exception
	assertThrows(ValidationException.class, () -> new ContentGETControl(prefix, path, filters, model).process());

	Optional<String> empty = Optional.empty();
	System.err.println("Ignore the next exception, thrown during testing");
	assertThrows(ExecutionException.class, () -> new ContentGETControl(prefix, path, empty, model).process());

}


}

/*
 *    Copyright 2020 Daniel Giribet
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

