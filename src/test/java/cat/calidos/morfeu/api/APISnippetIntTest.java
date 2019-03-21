// API SNIPPET INT TEST . JAVA

package cat.calidos.morfeu.api;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

/** Testing fetting snippet content trough the API
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class APISnippetIntTest extends APITezt {

@Test
public void testStuffSnippet() throws Exception {

	// http://localhost:8080/morfeu/dyn/snippets/target/test-classes/test-resources/snippets/stuff.xml
	//  &model=target/test-classes/test-resources/models/test-model.xsd%3Ffilter=/test/row/col/stuff
	String model = pathPrefix+"models/test-model.xsd%3Ffilter=/test/row/col/stuff";
	String uri = "snippets/"+pathPrefix+"snippets/stuff.xml?model="+model;
	InputStream content = fetchRemoteInputStreamFrom(uri);
	assertNotNull(content);

	JsonNode stuff = parseJson(content);
	assertEquals("Wrong document schema", 0, stuff.get("schema").asInt());
	assertTrue("Stuff snippet should just have the content cell directly", stuff.isContainerNode());
	assertEquals("/stuff has a wrong name", "stuff", stuff.get("name").asText());
	assertEquals("/stuff has wrong content", "Stuff content", stuff.get("value").asText());

}


@Test
public void testData2Snippet() throws Exception {

	// http://localhost:8080/morfeu/dyn/snippets/target/test-classes/test-resources/snippets/data2.xml
	//  &model=target/test-classes/test-resources/models/test-model.xsd%3Ffilter=/test/row/col/data2
	String model = pathPrefix+"models/test-model.xsd%3Ffilter=/test/row/col/data2";
	String uri = "snippets/"+pathPrefix+"snippets/data2.xml?model="+model;
	InputStream content = fetchRemoteInputStreamFrom(uri);
	assertNotNull(content);

	JsonNode data2 = parseJson(content);
	assertEquals("Wrong document schema", 0, data2.get("schema").asInt());
	assertTrue("Data2 snippet should just have the content cell directly", data2.isContainerNode());
	assertEquals("/children/data2(0) has a wrong name", "data2", data2.get("name").asText());

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
