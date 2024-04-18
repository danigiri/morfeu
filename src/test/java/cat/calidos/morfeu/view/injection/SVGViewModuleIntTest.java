// SVG VIEW MODULE INT TEST . JAVA

package cat.calidos.morfeu.view.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SVGViewModuleIntTest {


// FIXME: skip when there is no network or either avoid fetching remote resources
@Test
public void testRenderSVGTruncate() throws Exception {

	Optional<String> header = Optional.empty();
	String svg = DaggerSVGViewComponent.builder().from("Short text").withHeader(header).truncate(true).build().render();
	assertNotNull(svg);
	assertTrue( svg.length() > 0, "Should not get an empty SVG");
	// System.err.println(svg);

	NodeList nodeList = readFromSVG(svg, "//text");
	assertNotNull(nodeList);
	assertEquals(1, nodeList.getLength(), "Should only have one text element in the svg");

	Node text = nodeList.item(0).getFirstChild(); // text is the top XML node and the first child
													// has the content
	assertEquals("Should have the short text as SVG content", "Short text", text.getNodeValue());

}


@Test
public void testRenderSVGHeader() throws Exception {

	Optional<String> header = Optional.of("Header");
	String svg = DaggerSVGViewComponent.builder().from("Short text").withHeader(header).truncate(true).build().render();
	assertNotNull(svg);

	NodeList nodeList = readFromSVG(svg, "//text");

	Node text = nodeList.item(0).getFirstChild(); // text is the top XML node and the first child
													// has the content
	assertTrue(text.getNodeValue().contains("Header"), "Should have the header in the SVG content");

}


private NodeList readFromSVG(String svg, String xpath) throws Exception {

	DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	builderFactory.setValidating(false); // no need to validate, just parse
	DocumentBuilder builder = builderFactory.newDocumentBuilder();
	InputSource svgSource = new InputSource(new StringReader(svg));
	Document xmlDocument = builder.parse(svgSource);
	XPath xPath = XPathFactory.newInstance().newXPath();
	NodeList nodeList = (NodeList) xPath.compile(xpath).evaluate(xmlDocument, XPathConstants.NODESET);

	return nodeList;

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
