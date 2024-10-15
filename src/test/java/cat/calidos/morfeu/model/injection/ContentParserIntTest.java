// CONTENT PARSER INT TEST . JAVA

package cat.calidos.morfeu.model.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.ComplexCell;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.Validable;
import cat.calidos.morfeu.problems.ValidationException;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentParserIntTest extends ModelTezt {

private static String modelPath = "test-resources/models/test-model.xsd";

@Test @DisplayName("Test validate content")
public void testValidate() throws Exception {

	var contentPath = "test-resources/documents/document1.xml";
	String fullContentPath = testAwareFullPathFrom(contentPath);
	String testAwareModelPath = testAwareFullPathFrom(modelPath);

	Validable validator = DaggerContentParserComponent
			.builder()
			.content(new URI(contentPath))
			.fetchedContentFrom(new URI(fullContentPath))
			.model(new URI(modelPath))
			.withModelFetchedFrom(new URI(testAwareModelPath))
			.build()
			.validator()
			.get();
	validator.validate(); // should not throw exception
	assertTrue(validator.isValid());

}


@Test @DisplayName("Testing invalid content")
public void testNonValidDocument() throws Exception {

	var contentPath = "test-resources/documents/nonvalid-document.xml";
	String fullContentPath = testAwareFullPathFrom(contentPath);
	String testAwareModelPath = testAwareFullPathFrom(modelPath);

	Validable validator = DaggerContentParserComponent
			.builder()
			.content(new URI(contentPath))
			.fetchedContentFrom(new URI(fullContentPath))
			.model(new URI(modelPath))
			.withModelFetchedFrom(new URI(testAwareModelPath))
			.build()
			.validator()
			.get();
	try {
		validator.validate();
	} catch (ValidationException e) {
		assertTrue(e.getMessage().contains("notvalid"));
	}

}


@Test @DisplayName("Testing the resulting cells")
public void testProduceContent() throws Exception {

	var contentPath = "test-resources/documents/document1.xml";
	String fullContentPath = testAwareFullPathFrom(contentPath);
	String testAwareModelPath = testAwareFullPathFrom(modelPath);

	Composite<Cell> content = DaggerContentParserComponent
			.builder()
			.content(new URI(contentPath))
			.fetchedContentFrom(new URI(fullContentPath))
			.model(new URI(modelPath))
			.withModelFetchedFrom(new URI(testAwareModelPath))
			.build()
			.content()
			.get();
	testDocument1Content(content);

}


@Test @DisplayName("Testing JSON input")
public void testProduceJSONContent() throws Exception {

	var contentPath = "test-resources/transform/document1-json-content.json";
	String fullContentPath = testAwareFullPathFrom(contentPath);
	String testAwareModelPath = testAwareFullPathFrom(modelPath);

	Composite<Cell> content = DaggerContentParserComponent
			.builder()
			.content(new URI(contentPath))
			.fetchedContentFrom(new URI(fullContentPath))
			.model(new URI(modelPath))
			.withModelFetchedFrom(new URI(testAwareModelPath))
			.build()
			.content()
			.get();
	testDocument1Content(content);

}


@Test @DisplayName("Testing filtered content")
public void testFilteredContent() throws Exception {

	var contentPath = "test-resources/documents/filtered.xml";
	String fullContentPath = testAwareFullPathFrom(contentPath);
	String testAwareModelPath = testAwareFullPathFrom(modelPath);
	// first we use java encoding, then json encoding, finally regexp pattern
	String f = "replace{\"replacements\":[" + "{\"from\":\"=\\\\s*\\\\{\",\"to\":\"=\\\"{\"},"
			+ "{\"from\":\"}\",\"to\":\"}\\\"\"}" + "]}";

	Composite<Cell> content = DaggerContentParserComponent
			.builder()
			.content(new URI(contentPath))
			.fetchedContentFrom(new URI(fullContentPath))
			.filters(f)
			.model(new URI(modelPath))
			.withModelFetchedFrom(new URI(testAwareModelPath))
			.build()
			.content()
			.get();
	testDocument1Content(content);

	// testing with backreferences for more robust handling

	var f2 = "replace{\"replacements\":{\"from\":\"=\\\\s*\\\\{([^}]*?)}\",\"to\":\"=\\\"{$1}\\\"\"}}";

	Composite<Cell> content2 = DaggerContentParserComponent
			.builder()
			.content(new URI(contentPath))
			.fetchedContentFrom(new URI(fullContentPath))
			.filters(f2)
			.model(new URI(modelPath))
			.withModelFetchedFrom(new URI(testAwareModelPath))
			.build()
			.content()
			.get();
	testDocument1Content(content2);

}


@Test @DisplayName("Test dump invalid content")
public void testDumpInvalidContent() {

}


private void testDocument1Content(Composite<Cell> content) {

	assertNotNull(content);
	assertEquals(1, content.size());

	// empty root node
	Cell rootNode = content.child(0);
	assertNotNull(rootNode);
	assertEquals(Document.ROOT_NAME, rootNode.getName());
	assertTrue(rootNode.isComplex());

	Cell testNode = rootNode.asComplex().children().child("test(0)");
	assertNotNull(testNode);
	assertEquals("test", testNode.getName());
	assertTrue(testNode.isComplex());

	ComplexCell testComplexNode = testNode.asComplex();
	assertNotNull(testComplexNode);
	assertEquals(1, testComplexNode.children().size());
	assertEquals("row", testComplexNode.children().child(0).getName());
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
