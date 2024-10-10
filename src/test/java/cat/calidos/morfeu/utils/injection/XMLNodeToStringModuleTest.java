package cat.calidos.morfeu.utils.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.Tezt;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class XMLNodeToStringModuleTest extends Tezt {

private String content;

@BeforeEach
public void setup() throws Exception {

	String fullPath = testAwareFullPathFrom("test-resources/documents/document1.xml");
	URI fullURI = new URI(fullPath);
	content = IOUtils.toString(fullURI, Config.DEFAULT_CHARSET);

}


@Test @DisplayName("Generate xml test")
public void testGenerateXML() throws Exception {

	Document doc = DaggerXMLParserComponent.builder().withContent(content).build().document().get();

	String out = DaggerXMLNodeToStringComponent.builder().fromNode(doc).build().xml().get();
	assertNotNull(out);

	compareWithXML(out, content);

}


@Test @DisplayName("Generate xml node test")
public void testGenerateXMLNode() throws Exception {

	Document doc = DaggerXMLParserComponent.builder().withContent(content).build().document().get();

	Node node = doc.getChildNodes()
			.item(0)
			.getChildNodes()
			.item(1)
			.getChildNodes()
			.item(1)
			.getChildNodes()
			.item(1);

	String out = DaggerXMLNodeToStringComponent.builder().fromNode(node).build().xml().get();
	assertEquals("<data number=\"42\" text=\"blahblah\"/>", out);

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
