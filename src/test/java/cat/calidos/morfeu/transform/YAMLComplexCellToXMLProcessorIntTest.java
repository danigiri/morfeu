package cat.calidos.morfeu.transform;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.transform.injection.DaggerYAMLCellToXMLProcessorComponent;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class YAMLComplexCellToXMLProcessorIntTest extends ModelTezt {

private ComplexCellModel test;

@BeforeEach
public void setup() throws Exception {

	var modelURI = new URI("target/test-classes/test-resources/models/test-model.xsd");
	test = cellModelFrom(modelURI, "test").asComplex();

}


@Test
public void testIdentifier() throws Exception {

	CellModel data3 = test
			.children()
			.child("row")
			.asComplex()
			.children()
			.child("col")
			.asComplex()
			.children()
			.child("data3");

	var mapper = new ObjectMapper(new YAMLFactory());
	var yaml = "text: identifier\n" + "color: 00ff00\n";
	JsonNode yamlNode = mapper.readTree(yaml);
	JsonNodeCellModel nodeCellModel = new JsonNodeCellModel(yamlNode, data3);

	YAMLComplexCellToXMLProcessor processor = new YAMLComplexCellToXMLProcessor("\t", "yaml-to-xml",
			nodeCellModel);
	assertEquals("	<data3 text=\"identifier\" color=\"00ff00\"/>\n", processor.output());

}


// FIXME: need to investigate why this test is disabled
// @Test
public void testData2() throws Exception {

	ComplexCellModel col = test
			.children()
			.child("row")
			.asComplex()
			.children()
			.child("col")
			.asComplex();
	ComplexCellModel data2 = test
			.children()
			.child("row")
			.asComplex()
			.children()
			.child("col")
			.asComplex()
			.children()
			.child("data2")
			.asComplex();

	var mapper = new ObjectMapper(new YAMLFactory());
	var yaml = """
			data2:
			  -\s
			    number: 42
			    text: blahblah
			  -\s
			    number: 42
			    text: blahblah""";
	JsonNode yamlNode = mapper.readTree(yaml);
	List<PrefixProcessor<JsonNodeCellModel, String>> processors = DaggerYAMLCellToXMLProcessorComponent
			.builder()
			.withPrefix("")
			.fromNode(yamlNode)
			.cellModel(col)
			.givenCase("yaml-to-xml")
			.build()
			.processors();

	assertEquals(2, processors.size());

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
