/*
 * Copyright 2017 Daniel Giribet
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

package cat.calidos.morfeu.utils.injection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.problems.FetchingException;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class URIModuleTest {

private String uri;

@BeforeEach
public void setup() {
	uri = "http://foo.com";
}


@Test
public void testURI() throws FetchingException {
	assertEquals(uri, URIModule.uri(uri).toString());
}


@Test
public void testURIException() throws FetchingException {
	assertThrows(FetchingException.class, () -> URIModule.uri(":/foo.com"));
}

}
