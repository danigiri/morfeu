package cat.calidos.morfeu.filter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.filter.injection.FilterEngine;
import cat.calidos.morfeu.problems.ConfigurationException;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class FilterEngineTest {


@Test @DisplayName("Parse transform list test")
public void parseFiltersBasic() {

	String transforms = "a;b;c";
	List<String> filters = FilterEngine.parseFilters(transforms);
	assertAll("basic parse transforms",
			() -> assertNotNull(filters),
			() -> assertEquals(3, filters.size(), "Should be three filters")
	);

	transforms = "";
	List<String> filters2 = FilterEngine.parseFilters(transforms);
	assertAll("basic parse filters",
			() -> assertNotNull(filters2),
			() -> assertEquals(0, filters2.size(), "Should be zero filters")
	);

}


@Test @DisplayName("Parse parameters test")
public void parseFiltersParameters() throws Exception {

	String filters = "a{ \"p0\":\"foo\", \"p1\": 1}";
	List<String> f = FilterEngine.parseFilters(filters);
	assertAll("parse params transform",
			() -> assertNotNull(f),
			() -> assertEquals(1, f.size(), "Should be one filter")
	);

	JsonNode params = FilterEngine.parseParametersFrom(f.get(0));
	assertAll("parse params",
			() -> assertNotNull(params),
			() -> assertFalse(params.isEmpty(), "Should not get an empty param"),
			() -> assertEquals("foo", params.get("p0").asText(), "Should have a correctly parsed json"),
			() -> assertEquals(1, params.get("p1").asInt(), "Should have a correctly parsed json")
	);

}


@Test @DisplayName("Parse parameters edge cases test")
public void parseFilterssParametersEdgeCases() throws Exception {

	var filters = "a{ \"p0\":\"fo\\;o\", \"p1\": 0};b{}";
	List<String> f = FilterEngine.parseFilters(filters);
	assertAll("parse params filter",
			() -> assertNotNull(f),
			() -> assertEquals(2, f.size(), "Should be two transforms")
	);

	JsonNode params = FilterEngine.parseParametersFrom(f.get(0));
	assertAll("parse params 0",
			() -> assertNotNull(params),
			() -> assertFalse(params.isEmpty(), "Should not get an empty param"),
			() -> assertEquals("fo;o", params.get("p0").asText(), "Should have a correctly parsed json"),
			() -> assertEquals(0, params.get("p1").asInt(), "Should have a correctly parsed json")
	);

	JsonNode params1 = FilterEngine.parseParametersFrom(f.get(1));
	assertAll("parse params 1",
			() -> assertNotNull(params1),
			() -> assertTrue(params1.isEmpty(), "Should get an empty param")
	);

	var filters2 = "a{\"p0\":\"0\"";
	List<String> f2 = FilterEngine.parseFilters(filters2);
	assertThrows(ConfigurationException.class, () ->FilterEngine.parseParametersFrom(f2.get(0)));

}


}

/*
 *    Copyright 2024 Daniel Giribet
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

