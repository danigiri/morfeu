// YAML TEXTUAL TO XML PROCESSOR TEST . JAVA

package cat.calidos.morfeu.transform;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.injection.ModelTezt;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class YAMLTextualToXMLProcessorIntTest extends ModelTezt {

JsonNode			node;
private CellModel	stuff;

@BeforeEach
public void setup() throws Exception {

	URI modelURI = new URI("target/test-classes/test-resources/models/test-model.xsd");
	ComplexCellModel test = cellModelFrom(modelURI, "test").asComplex();
	stuff = test
			.children()
			.child("row")
			.asComplex()
			.children()
			.child("col")
			.asComplex()
			.children()
			.child("stuff");

	node = mock(JsonNode.class);
}


@Test
public void testOutputWithContent() throws Exception {

	var content = "stuff content";
	when(node.size()).thenReturn(content.length());
	when(node.asText()).thenReturn(content);

	var nodeCellModel = new JsonNodeCellModel(node, stuff);
	var processor = new YAMLTextualToXMLProcessor("\t", nodeCellModel);
	assertEquals("\t<stuff>" + content + "</stuff>\n", processor.output());

	content = "stuff content &";
	when(node.size()).thenReturn(content.length());
	when(node.asText()).thenReturn(content);
	nodeCellModel = new JsonNodeCellModel(node, stuff);
	processor = new YAMLTextualToXMLProcessor("\t", nodeCellModel);
	assertEquals("\t<stuff>" + content.replace("&", "&amp;") + "</stuff>\n", processor.output());

}


@Test
public void testOutputWithEmptyContent() {

	var content = "";
	when(node.size()).thenReturn(content.length());
	when(node.asText()).thenReturn(content);

	var jsonNodeCellModel = new JsonNodeCellModel(node, stuff);
	var processor = new YAMLTextualToXMLProcessor("\t", jsonNodeCellModel);
	assertEquals("\t<stuff/>\n", processor.output());

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
