package cat.calidos.morfeu.cli;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.utils.Pair;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;


public class MorfeuCLIIntTest extends ModelTezt {

@Test @DisplayName("Content parse test")
public void parseContent() throws Exception {

	String prefix = testAwareFullPathFrom(".");
	String path = "test-resources/documents/document1.xml";
	String model = "test-resources/models/test-model.xsd";

	var args = new String[] { "-q", "--model", model, "--prefix", prefix, MorfeuCLI.PARSE, path };
	Pair<Integer, String> result = MorfeuCLI.mainImpl(args);

	assertEquals(0, result.getLeft());

	// the printer in its wisdom appends the classname at the end of the dump
	String outputStr = result.getRight().toString().replaceAll("java\\.io\\.PrintStream@.*$", "");
	// System.out.println(outputStr);
	JsonNode json = DaggerJSONParserComponent.builder().from(outputStr).build().json().get();
	assertAll(
			"basic structure of the json output",
			() -> assertNotNull(json),
			() -> assertEquals(0, json.get("schema").asInt()),
			() -> assertEquals(path, json.get("URI").asText()));

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
