// API CONTENT TRANSFORM INT . TEST

package cat.calidos.morfeu.api;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class APIContentTransformIntTest extends APITezt {

@Test
public void testDocument1YAMLTransform() throws Exception {

	String model = pathPrefix + "models/test-model.xsd";
	InputStream content = fetchRemoteInputStreamFrom(
			"content/" + pathPrefix + "transform/document1.yaml?model=" + model);
	// content = printInputStream(content);

	assertNotNull(content);

	JsonNode root = parseJson(content); // <root node>
	assertNotNull(root);
	assertEquals(0, root.get("schema").asInt(), "Wrong document schema");
	assertEquals("", root.get("name").asText(), "Root node has a wrong name");
	assertTrue(root.get("children").isArray(), "/children is not an array and it should be");

	JsonNode test = root.get("children").get(0); // /test(0)
	assertEquals("test", test.get("name").asText(), "/children/test(0) has a wrong name");

	JsonNode testInternalAttributes = test.get("internalAttributes");
	assertNotNull(testInternalAttributes);
	assertTrue(
			testInternalAttributes.isArray(),
			"/children/test(0)@internalAttributes should be an array");
	assertEquals(
			2,
			testInternalAttributes.size(),
			"/children/test(0)@internalAttributes should have two elems");

	JsonNode testAttributes = test.get("attributes");
	assertNotNull(testAttributes);
	assertTrue(testAttributes.isArray(), "/children/test(0)@attributes should be an array");
	assertEquals(0, testAttributes.size(), "/children/test(0)@attributes should have zero elems");

	JsonNode row = test.get("children").get(0); // /test(0)/row(0)
	assertNotNull(row);
	assertEquals("row", row.get("name").asText(), "/children/test(0)/row(0) has a wrong name");

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
