package cat.calidos.morfeu.cli;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.utils.Pair;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;


@TestInstance(Lifecycle.PER_CLASS)
public class MorfeuCLIIntTest extends ModelTezt {

private String	prefix;
private String	model;
private String outputPath;

@BeforeAll
public void setup() throws Exception {
	prefix = testAwareFullPathFrom(".");
	model = "test-resources/models/test-model.xsd";
}


@Test @DisplayName("Content parse test")
public void parseContent() throws Exception {

	var path = "test-resources/documents/document1.xml";
	var args = new String[] { "-q", "--model", model, "--prefix", prefix, MorfeuCLI.PARSE, path };
	Pair<Integer, String> result = MorfeuCLI.mainImpl(new MorfeuCLI(), args);

	assertEquals(MorfeuCLI.EX_OK, result.getLeft());
	String outputStr = result.getRight();
	assertNotNull(outputStr);
	// System.out.println(outputStr);

	JsonNode json = DaggerJSONParserComponent.builder().from(outputStr).build().json().get();
	assertAll(
			"basic structure of the content json output",
			() -> assertNotNull(json),
			() -> assertEquals(0, json.get("schema").asInt()),
			() -> assertEquals(path, json.get("URI").asText()));

}


@Test @DisplayName("Content save test")
public void saveContent() throws Exception {

	// using the CLI is a fast way to get the XML in the json representation
	var path0 = "test-resources/documents/document1.xml";
	var args0 = new String[] { "-q", "--model", model, "--prefix", prefix, MorfeuCLI.PARSE, path0 };
	Pair<Integer, String> result0 = MorfeuCLI.mainImpl(new MorfeuCLI(), args0);

	assertEquals(MorfeuCLI.EX_OK, result0.getLeft());
	String outputStr0 = result0.getRight();
	assertNotNull(outputStr0);
	String json0 = DaggerJSONParserComponent.builder().from(outputStr0).build().pretty().get();
	assertNotNull(json0);
	var newIn = new ByteArrayInputStream(json0.getBytes());

	// now we take the json redirect it to STDN
	InputStream previousIn = System.in;
	try {
		System.setIn(newIn);
		outputPath = temporaryOutputFilePathIn(".");
		var args1 = new String[] { "-q", "--model", model, "-p", prefix, MorfeuCLI.SAVE, outputPath };
		Pair<Integer, String> result1 = MorfeuCLI.mainImpl(new MorfeuCLI(), args1);
		assertEquals(MorfeuCLI.EX_OK, result1.getLeft());
		String outputStr1 = result1.getRight();
		assertNotNull(outputStr1);
		// System.out.println(outputStr1);
		JsonNode json1 = DaggerJSONParserComponent.builder().from(outputStr1).build().json().get();
		assertAll(
				"basic structure of the content save operation output",
				() -> assertNotNull(json1),
				() -> assertEquals("Content saved successfully", json1.get("result").asText()));
		String savedContent = readFromFile(outputPath);
		//System.out.println(savedContent);
		assertAll("basic tests of the saved content",
				() -> assertNotNull(savedContent),
				() -> assertTrue(savedContent.contains("blahblah")),
				() -> assertTrue(savedContent.contains("42"))
				);
	} finally {
		System.setIn(previousIn);
	}
}


@AfterEach
public void teardown() {

	File file = new File(prefix + outputPath);
	if (file.exists()) {
		file.delete();
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
