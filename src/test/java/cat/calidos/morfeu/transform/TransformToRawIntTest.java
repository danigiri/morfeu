// TRANSFORM TO RAW INT TEST . JAVA
package cat.calidos.morfeu.transform;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformToRawIntTest extends TransformTezt {

@Test @DisplayName("Stream chain test")
public void streamChainTest() throws Exception {

	Document doc = produceDocumentFromPath("test-resources/documents/document1.json");
	assertNotNull(doc);

	Map<String, Object> values = valueMapFrom(doc);
	String output = DaggerViewComponent
			.builder()
			.withTemplatePath("transform/content-to-raw.ftl")
			.withValue(values)
			.build()
			.render();
	// System.err.println(output);
	// hard to test, let's do some basic assertions
	assertAll(
			"raw output test",
			() -> assertNotNull(output),
			() -> assertTrue(output.contains("blahblah")),
			() -> assertFalse(output.contains("data")));

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
