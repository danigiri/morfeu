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

package cat.calidos.morfeu.model.transform;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformContentToYAMLIntTest extends ModelTezt {

@Test
public void testTransformUsingTemplateDocument1() throws Exception {

	Document doc = produceDocumentFromPath("test-resources/documents/document1.json");
	assertNotNull(doc);

	Map<String, Object> values = new HashMap<String, Object>(2);
	values.put("cells", doc.getContent().asList());
	values.put("model", doc.getModel());
	
	String transformed = DaggerViewComponent.builder()
			.withTemplate("templates/transform/content-to-yaml.twig")
			.withValue(values)
			.build()
			.render();
	//System.err.println(transformed);
	
	YAMLMapper mapper = new YAMLMapper();
	JsonNode yaml = mapper.readTree(transformed);
	assertNotNull(yaml);
	assertTrue(yaml.isObject());
	assertTrue(yaml.has("rows"));

	JsonNode rows = yaml.get("rows");			//rows
	assertNotNull(rows);
	assertTrue(rows.isArray());
	assertEquals(1, rows.size());

	JsonNode cols = rows.get(0).get("cols");	//rows/cols
	assertNotNull(cols);
	assertTrue(cols.isArray());
	assertEquals(2, cols.size());

	JsonNode col0 = cols.get(0);				//rows/cols/col0
	assertTrue(col0.isObject());
	assertTrue(col0.has("size"));
	assertEquals(4, col0.get("size").asInt());
	assertTrue(col0.has("data"));
	
	JsonNode datas = col0.get("data");
	assertNotNull(datas);
	assertTrue(datas.isArray());
	assertEquals(1, datas.size());
	assertTrue(datas.has(0));

	JsonNode data0 = datas.get(0);			//rows/cols/col0/data0
	assertNotNull(data0);
	assertTrue(data0.isObject());
	assertEquals(42, data0.get("number").asInt());
	assertEquals("blahblah", data0.get("text").asText());

	// now we check the order of the attributes that should be the one we want (like the one in the xml)
	// this is not supported in the dom so we will skip this test
}


@Test
public void testTransformUsingTemplateDocument3() throws Exception {

	Document doc = produceDocumentFromPath("test-resources/documents/document3.json");
	assertNotNull(doc);

	Map<String, Object> values = new HashMap<String, Object>(2);
	values.put("cells", doc.getContent().asList());
	values.put("model", doc.getModel());

	String transformed = DaggerViewComponent.builder()
			.withTemplate("templates/transform/content-to-yaml.twig")
			.withValue(values)
			.build()
			.render();
	//System.err.println(transformed);

	YAMLMapper mapper = new YAMLMapper();
	JsonNode yaml = mapper.readTree(transformed);
	assertNotNull(yaml);

	JsonNode stuffs = yaml.get("rows").get(0).get("cols").get(0).get("stuff");
	assertNotNull(stuffs);
	assertTrue(stuffs.isArray());
	assertEquals(3, stuffs.size());
	assertEquals("Stuff content", stuffs.get(0).asText());
	assertEquals("Stuff content 2", stuffs.get(1).asText());
	assertEquals("Multiline stuff\ncontent\n", stuffs.get(2).asText());

}


@Test
public void testTransformUsingTemplateKeyValuesDocument() throws Exception {

	Document doc = produceDocumentFromPath("test-resources/documents/keyvalues-yaml.json");
	assertNotNull(doc);

	Map<String, Object> values = new HashMap<String, Object>(2);
	values.put("cells", doc.getContent().asList());
	values.put("model", doc.getModel());
	
	String transformed = DaggerViewComponent.builder()
			.withTemplate("templates/transform/content-to-yaml.twig")
			.withValue(values)
			.build()
			.render();
	//System.err.println(transformed);
	
	YAMLMapper mapper = new YAMLMapper();
	JsonNode yaml = mapper.readTree(transformed);
	assertNotNull(yaml);

	JsonNode keyvalues = yaml.get("rows").get(0).get("cols").get(0).get("keyvalues");
	assertNotNull(keyvalues);
	assertTrue(keyvalues.isObject());
	assertEquals(2, keyvalues.size());

	JsonNode foo = keyvalues.get("foo");			//rows/cols/col0/keyvalues/foo
	assertNotNull(foo);
	assertEquals("bar", foo.asText());

	JsonNode bar = keyvalues.get("bar");			//rows/cols/col0/keyvalues/bar
	assertNotNull(bar);
	assertEquals("foo", bar.asText());
	
}

}
