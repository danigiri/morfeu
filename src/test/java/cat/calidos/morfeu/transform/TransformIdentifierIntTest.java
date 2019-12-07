// TRANSFORM IDENTIFIER INT TEST . JAVA

package cat.calidos.morfeu.transform;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformIdentifierIntTest extends TransformTezt {




@Test
public void testTransformJSONToYAML() throws Exception {

	Document doc = produceDocumentFromPath("test-resources/documents/document5.json");
	assertNotNull(doc);

	Map<String, Object> values = valueMapFrom(doc);
	String transformed = DaggerViewComponent.builder()
												.withTemplatePath("templates/transform/content-to-yaml.twig")
												.withValue(values)
												.build()
												.render();
	System.err.println(transformed);

	YAMLMapper mapper = new YAMLMapper();
	JsonNode yaml = mapper.readTree(transformed);
	assertNotNull(yaml);
	assertTrue(yaml.isObject());
	assertTrue(yaml.has("rows"));

	JsonNode col = yaml.get("rows").get(0).get("cols").get(0);	//rows/cols/col
	assertNotNull(col);
	assertTrue(col.isObject());
	assertTrue(col.has("data3"));

	JsonNode data3s = col.get("data3");
	assertNotNull(data3s);
	assertTrue(data3s.isArray());
	assertEquals(3, data3s.size());

	JsonNode data3s0 = data3s.get(0);

	assertEquals(2, data3s0.size());
	Iterator<String> fields = data3s0.fieldNames();	// let's check the order so the identifier is first
	assertTrue(fields.hasNext());
	assertEquals("'text' is not the first field name and it should be as it's the identifier", "text", fields.next());

	JsonNode data3s1 = data3s.get(1);
	assertTrue(data3s1.isObject());
	assertEquals(1, data3s1.size());

	JsonNode data3s2 = data3s.get(2);
	assertTrue(data3s2.isObject());
	assertEquals(2, data3s2.size());

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
