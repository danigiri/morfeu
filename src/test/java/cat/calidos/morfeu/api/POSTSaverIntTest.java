package cat.calidos.morfeu.api;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Optional;

import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.utils.POSTSaver;
import cat.calidos.morfeu.utils.injection.HttpClientModule;

/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class POSTSaverIntTest extends APITezt {


@Test
public void testPostToPing() throws Exception {

	CloseableHttpClient client = HttpClientModule.produceHttpClient();
	final URI uri = new URI(webappPrefix + "ping");
	var content = new HashMap<String, String>(2);
	content.put("foo", "bar");
	POSTSaver postSaver = new POSTSaver(client, uri, content);

	postSaver.save();

	Optional<String> response = postSaver.getResponse();
	assertTrue(response.isPresent());
	assertEquals("OK {foo=bar}", response.get());

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

