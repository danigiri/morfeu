// API CONTENT INT TEST . JAVA

package cat.calidos.morfeu.api;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.Document;


/**
 * Testing the API to get content and doing some basic tests on it
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class APIContentIntTest extends APITezt {

@Test
public void testContent() throws Exception {

	// http://localhost:8080/dyn/content/target/test-classes/test-resources/documents/document1.xml?model=
	var model = pathPrefix + "models/test-model.xsd";
	InputStream content = fetchRemoteInputStreamFrom(
			"content/" + pathPrefix + "documents/document1.xml?model=" + model);
	assertNotNull(content);

	JsonNode root = parseJson(content);
	assertNotNull(root);
	assertEquals(0, root.get("schema").asInt(), "Wrong document schema");
	assertEquals(Document.ROOT_NAME, root.get("name").asText());
	assertTrue(root.get("children").isArray(), "/children is not an array and it should be");
	assertEquals(
			"test",
			root.get("children").get(0).get("name").asText(),
			"/children/test(0) has a wrong name");

	// target/test-classes/test-resources/documents/document1.xml/test(0)/row(0)/col(0)/data(0)
	JsonNode data0 = root
			.get("children")
			.get(0)
			.get("children")
			.get(0)
			.get("children")
			.get(0)
			.get("children")
			.get(0);
	assertNotNull(data0);
	assertTrue(data0.has("attributes"));

	JsonNode number = data0.get("attributes").get(0);
	assertEquals("number", number.get("name").asText());
	assertEquals(42, number.get("value").asInt());

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
