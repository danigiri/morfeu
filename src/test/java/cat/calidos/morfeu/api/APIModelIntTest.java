// API MODEL INT TEST . JAVA

package cat.calidos.morfeu.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

/** We test the model API and some basic aspects of the JSON structure
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class APIModelIntTest extends APITezt {


@Test
public void testModel() throws Exception {

	// http://localhost:8080/morfeu/dyn/models/target/test-classes/test-resources/models/test-model.xsd
	InputStream content = fetchRemoteInputStreamFrom("models/target/test-classes/test-resources/models/test-model.xsd");
	assertNotNull(content);

	JsonNode model = parseJson(content);
	assertNotNull(model);
	
	assertEquals("Wrong model schema", 0, model.get("schema").asInt());
	assertTrue("/children is not an array and it should be", model.get("children").isArray());
	assertEquals("Wrong model name", "<ROOT>", model.get("name").asText());
	assertEquals("Wrong model desc", "Description of test model", model.get("desc").asText());
	assertEquals("/children/test(0) has a wrong name", "test", model.get("children").get(0).get("name").asText());
	
}


}


/*
 *    Copyright 2019 Daniel Giribet
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

