package cat.calidos.morfeu.control.injection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;

/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class OperationResultModuleTest {


@Test
@DisplayName("OK result test")
public void okResultTest() throws Exception {
	String result = OperationResultModule.result("aaa", "bbb", "ccc", 111, null);
	assertNotNull(result);

	JsonNode json = DaggerJSONParserComponent.builder().from(result).build().json().get();
	assertAll("checking output", () -> assertNotNull(json), () -> assertEquals("aaa", json.get("result").asText()),
			() -> assertEquals("bbb", json.get("target").asText()),
			() -> assertEquals("ccc", json.get("operation").asText()),
			() -> assertEquals(111, json.get("operationTime").asLong()), () -> assertFalse(json.has("problem")));
}


@Test
@DisplayName("KO result test")
public void koResultTest() throws Exception {
	String result = OperationResultModule.result("aax", "bbx", "ccx", 111, "issue");
	assertNotNull(result);

	JsonNode json = DaggerJSONParserComponent.builder().from(result).build().json().get();
	assertAll("checking output", () -> assertNotNull(json), () -> assertEquals("aax", json.get("result").asText()),
			() -> assertEquals("bbx", json.get("target").asText()),
			() -> assertEquals("ccx", json.get("operation").asText()),
			() -> assertEquals(111, json.get("operationTime").asLong()),
			() -> assertEquals("issue", json.get("problem").asText()));

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

