/*
 *    Copyright 2018 Daniel Giribet
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

package cat.calidos.morfeu.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class APIContentTransformIntTest extends APITezt {


@Test
public void testDocument1YAMLTransform() throws Exception {

	String model = pathPrefix+"models/test-model.xsd";
	InputStream content = fetchRemoteInputStreamFrom("content/"+pathPrefix+"transform/document1.yaml?model="+model);
	assertNotNull(content);

	JsonNode doc = parseJson(content);
	assertEquals("Wrong document schema", 0, doc.get("schema").asInt());
	assertTrue("/children is not an array and it should be", doc.get("children").isArray());

	JsonNode test = doc.get("children").get(0);				// /test(0)
	assertEquals("/children/test(0) has a wrong name", "test", test.get("name").asText());

	JsonNode testInternalAttributes = test.get("internalAttributes");
	assertNotNull(testInternalAttributes);
	assertTrue("/children/test(0)@internalAttributes should be an array", testInternalAttributes.isArray());
	assertEquals("/children/test(0)@internalAttributes should have two elems", 2, testInternalAttributes.size());

	JsonNode testAttributes = test.get("attributes");
	assertNotNull(testAttributes);
	assertTrue("/children/test(0)@attributes should be an array", testAttributes.isArray());
	assertEquals("/children/test(0)@attributes should have zero elems", 0, testAttributes.size());

	JsonNode row = test.get("children").get(0);				// /test(0)/row(0)
	assertNotNull(row);
	assertEquals("/children/test(0)/row(0) has a wrong name", "row", row.get("name").asText());

}

}
