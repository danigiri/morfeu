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

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class APIContentIntTest extends APITezt {


@Test
public void testContent() throws Exception {

	// http://localhost:8080/morfeu/dyn/content/target/test-classes/test-resources/documents/document1.xml?model=
	String model = pathPrefix+"models/test-model.xsd";
	InputStream content = fetchRemoteInputStreamFrom("content/"+pathPrefix+"documents/document1.xml?model="+model);
	assertNotNull(content);

	JsonNode doc = parseJson(content);
	assertEquals("Wrong document schema", 0, doc.get("schema").asInt());
	assertTrue("/children is not an array and it should be", doc.get("children").isArray());
	assertEquals("/children/test(0) has a wrong name", "test", doc.get("children").get(0).get("name").asText());

	//  target/test-classes/test-resources/documents/document1.xml/test(0)/row(0)/col(0)/data(0)

	JsonNode data0 = doc.get("children").get(0).get("children").get(0).get("children").get(0).get("children").get(0);
	assertNotNull(data0);
	assertTrue(data0.has("attributes"));
	
	JsonNode number = data0.get("attributes").get(0);
	assertEquals("number", number.get("name").asText());
	assertEquals(42, number.get("value").asInt());
	
}

}
