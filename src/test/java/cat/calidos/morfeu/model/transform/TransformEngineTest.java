package cat.calidos.morfeu.model.transform;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.transform.injection.TransformEngine;
import cat.calidos.morfeu.problems.ConfigurationException;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformEngineTest {


@Test @DisplayName("Parse transform list test")
public void parseTransformsBasic() {

	String transforms = "a;b;c";
	List<String> t = TransformEngine.parseTransforms(transforms);
	assertAll("basic parse transforms",
			() -> assertNotNull(t),
			() -> assertEquals(3, t.size(), "Should be three transforms")
	);
	
	transforms = "";
	List<String> t2 = TransformEngine.parseTransforms(transforms);
	assertAll("basic parse transforms",
			() -> assertNotNull(t2),
			() -> assertEquals(0, t2.size(), "Should be zero transforms")
	);

}


@Test @DisplayName("Parse parameters test")
public void parseTransformsParameters() throws Exception {

	String transforms = "a{ \"p0\":\"foo\", \"p1\": 1}";
	List<String> t = TransformEngine.parseTransforms(transforms);
	assertAll("parse params transform",
			() -> assertNotNull(t),
			() -> assertEquals(1, t.size(), "Should be one transform")
	);

	JsonNode params = TransformEngine.parseParametersFrom(t.get(0));
	assertAll("parse params",
			() -> assertNotNull(params),
			() -> assertFalse(params.isEmpty(), "Should not get an empty param"),
			() -> assertEquals("foo", params.get("p0").asText(), "Should have a correctly parsed json"),
			() -> assertEquals(1, params.get("p1").asInt(), "Should have a correctly parsed json")
	);

}


@Test @DisplayName("Parse parameters edge cases test")
public void parseTransformsParametersEdgeCases() throws Exception {

	String transforms = "a{ \"p0\":\"fo\\;o\", \"p1\": 0};b{}";
	List<String> t = TransformEngine.parseTransforms(transforms);
	assertAll("parse params transform",
			() -> assertNotNull(t),
			() -> assertEquals(2, t.size(), "Should be two transforms")
	);

	JsonNode params = TransformEngine.parseParametersFrom(t.get(0));
	assertAll("parse params 0",
			() -> assertNotNull(params),
			() -> assertFalse(params.isEmpty(), "Should not get an empty param"),
			() -> assertEquals("fo;o", params.get("p0").asText(), "Should have a correctly parsed json"),
			() -> assertEquals(0, params.get("p1").asInt(), "Should have a correctly parsed json")
	);

	JsonNode params1 = TransformEngine.parseParametersFrom(t.get(1));
	assertAll("parse params 1",
			() -> assertNotNull(params1),
			() -> assertTrue(params1.isEmpty(), "Should get an empty param")
	);

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

