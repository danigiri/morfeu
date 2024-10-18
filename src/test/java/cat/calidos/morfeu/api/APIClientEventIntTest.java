// API CLIENT EVENT INT TEST . JAVA

package cat.calidos.morfeu.api;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;


/**
 * Testing that we are able to receive client-side events in the backend
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class APIClientEventIntTest extends APITezt {

@Test
public void testClientEvent() throws Exception {

	InputStream content = fetchRemoteInputStreamFrom("events/foo?aa=b");
	assertNotNull(content);

	JsonNode eventResponse = parseJson(content);
	assertNotNull(eventResponse);
	assertEquals(
			"foo",
			eventResponse.get("result").asText(),
			"Result should include the event name");
	assertEquals("aa=b,", eventResponse.get("desc").asText(), "Desc should include the parameters");

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
