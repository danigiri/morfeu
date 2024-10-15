package cat.calidos.morfeu.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;


/**
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
public class MorfeuUtilsTest {

@Test
public void paramMapTest() {

	Map<String, Object> map = MorfeuUtils
			.paramMap("foo", Collections.EMPTY_LIST, "bar", "more stuff");
	assertNotNull(map);
	assertEquals(2, map.size());
	assertTrue(map.containsKey("foo"));
	assertEquals(Collections.EMPTY_LIST, map.get("foo"));
	assertTrue(map.containsKey("bar"));
	assertEquals("more stuff", map.get("bar"));

}


@Test
public void emptyParamMapTest() {

	Map<String, Object> map = MorfeuUtils.paramMap();
	assertNotNull(map);
	assertEquals(0, map.size());

}


@Test
public void paramStringMapTest() {

	Map<String, String> map = MorfeuUtils.paramStringMap("foo", "x", "bar", "more stuff");
	assertNotNull(map);
	assertEquals(2, map.size());
	assertTrue(map.containsKey("foo"));
	assertEquals("x", map.get("foo"));
	assertTrue(map.containsKey("bar"));
	assertEquals("more stuff", map.get("bar"));

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
