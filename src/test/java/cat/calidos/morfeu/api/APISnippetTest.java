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

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class APISnippetTest extends APITezt {

@Test
public void testStuffSnippet() throws Exception {

	// http://localhost:8080/morfeu/dyn/snippets/target/test-classes/test-resources/snippets/stuff.xml?cell-model=&model=
	String model = pathPrefix+"models/test-model.xsd";
	String cellModel = model+"/test/row/col/stuff";
	String uri = "snippets/"+pathPrefix+"documents/document1.xml?cell-model="+cellModel+"&model="+model;
	InputStream content = fetchRemoteInputStreamFrom(uri);
	assertNotNull(content);

}

}
