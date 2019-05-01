package cat.calidos.morfeu.utils;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MorfeuUtilsTest {

@Test
public void paramMapTest() {

	Map<String, Object> map = MorfeuUtils.paramMap("foo", Collections.EMPTY_LIST, "bar", "more stuff");
	assertNotNull(map);
	
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

