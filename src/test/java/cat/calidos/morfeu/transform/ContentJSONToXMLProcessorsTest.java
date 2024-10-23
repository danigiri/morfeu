package cat.calidos.morfeu.transform;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;


public class ContentJSONToXMLProcessorsTest {

@Test @DisplayName("json to value xml")
public void testWithValue() throws Exception {

	var content = """
			{
			       "schema" : 0,
			       "URI" : "/file.xml/Title(0)",
			       "name" : "Title",
			       "desc" : "",
			       "value" : "Need some inspiration for today? Yes!",
			       "cellModelURI" : "/test-model.xsd/Title",
			       "isSimple" : true
			     }
			""";

	JsonNode json = DaggerJSONParserComponent.builder().from(content).build().json().get();
	assertNotNull(json);
	var processor = new ContentJSONToXMLProcessor("\t", json);
	var processorSlash = new ContentJSONToXMLProcessorSlash("\t", json);
	var output = processor.output() + processorSlash.output();
	// System.out.println(output);
	assertEquals("\n\t<Title>Need some inspiration for today? Yes!</Title>", output);

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
