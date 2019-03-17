// TRANSFORM NO VALUE INT TEST . JAVA

package cat.calidos.morfeu.model.transform;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.transform.injection.DaggerContentConverterComponent;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformNoValueIntTest extends TransformTezt {

private String content;

@Before
public void setup() throws Exception {

	File inputFile = new File("target/test-classes/test-resources/transform/document4-as-view.json");
	content = FileUtils.readFileToString(inputFile, Config.DEFAULT_CHARSET);

}


@Test
public void testTransformJSONToXML() throws Exception {

	JsonNode json = DaggerJSONParserComponent.builder().from(content).build().json().get();
	assertNotNull(json);

	String transformed = DaggerContentConverterComponent.builder().from(json).builder().xml();
	System.err.println(transformed);
	compareWithXML(transformed, "target/test-classes/test-resources/documents/document4.xml");

}


@Test
public void testTransformJSONToYAML() throws Exception {

	Document doc = produceDocumentFromPath("test-resources/documents/document4.json");
	assertNotNull(doc);

	Map<String, Object> values = new HashMap<String, Object>(2);
	values.put("cells", doc.asComplex().children().asList());
	values.put("model", doc.getModel());

	String transformed = DaggerViewComponent.builder()
												.withTemplatePath("templates/transform/content-to-yaml.twig")
												.withValue(values)
												.build()
												.render();
	//System.err.println(transformed);

	YAMLMapper mapper = new YAMLMapper();
	JsonNode yaml = mapper.readTree(transformed);
	assertNotNull(yaml);
	assertTrue(yaml.isObject());
	assertTrue(yaml.has("rows"));

	JsonNode col = yaml.get("rows").get(0).get("cols").get(0);	//rows/cols/col
	assertNotNull(col);
	assertTrue(col.isObject());
	assertTrue(col.has("stuff"));
	assertTrue(col.has("data4"));

	JsonNode stuff = col.get("stuff");
	assertTrue(stuff.isArray());
	assertEquals("stuff : [] in transformed yaml should have no elements", 0, stuff.size());

	JsonNode data4 = col.get("data4");
	assertTrue(data4.isObject());
	assertEquals("data4: {} in transformed yaml should be empty", 0, data4.size());

}


@Test
public void testTransformYAMLToXML() throws Exception {

	String yamlPath = "target/test-classes/test-resources/transform/document4.yaml";
	String documentPath = "test-resources/documents/document4.json";
	String xmlPath = "src/test/resources/test-resources/documents/document4.xml";

	String transformed = transformYAMLToXML(yamlPath, documentPath);
	//System.err.println(transformed);
	compareWithXML(transformed,  xmlPath);

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
