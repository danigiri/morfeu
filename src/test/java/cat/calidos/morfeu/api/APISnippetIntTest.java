// API SNIPPET INT TEST . JAVA

package cat.calidos.morfeu.api;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;


/**
 * Testing fetting snippet content trough the API
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class APISnippetIntTest extends APITezt {

@Test
public void testStuffSnippet() throws Exception {

	// http://localhost:8080/morfeu/dyn/snippets/target/test-classes/test-resources/snippets/stuff.xml
	// &model=target/test-classes/test-resources/models/test-model.xsd%3Ffilter=/test/row/col/stuff
	var model = pathPrefix + "models/test-model.xsd%3Ffilter=/test/row/col/stuff";
	var uri = "snippets/" + pathPrefix + "snippets/stuff.xml?model=" + model;
	InputStream content = fetchRemoteInputStreamFrom(uri);
	assertNotNull(content);

	JsonNode stuff = parseJson(content);
	assertEquals(0, stuff.get("schema").asInt(), "Wrong document schema");
	assertTrue(stuff.isContainerNode(), "Stuff snippet should just have the content cell directly");
	assertEquals("stuff", stuff.get("name").asText(), "/stuff has a wrong name");
	assertEquals("Stuff content", stuff.get("value").asText(), "/stuff has wrong content");

}


@Test
public void testData2Snippet() throws Exception {

	// http://localhost:8080/morfeu/dyn/snippets/target/test-classes/test-resources/snippets/data2.xml
	// &model=target/test-classes/test-resources/models/test-model.xsd%3Ffilter=/test/row/col/data2
	var model = pathPrefix + "models/test-model.xsd%3Ffilter=/test/row/col/data2";
	var uri = "snippets/" + pathPrefix + "snippets/data2.xml?model=" + model;
	InputStream content = fetchRemoteInputStreamFrom(uri);
	assertNotNull(content);
	// content = printInputStream(content);

	JsonNode data2 = parseJson(content);
	assertEquals(0, data2.get("schema").asInt(), "Wrong document schema");
	assertTrue(data2.isContainerNode(), "Data2 snippet should just have the content cell directly");
	assertEquals("data2", data2.get("name").asText(), "/children/data2(0) has a wrong name");

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
