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

package cat.calidos.morfeu.view;

import static org.junit.Assert.*;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import cat.calidos.morfeu.view.injection.DaggerSVGViewComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SVGViewModuleIntTest {

@Test
public void testRenderSVG() throws Exception {
	
	String svg = DaggerSVGViewComponent.builder().from("Short text").truncate(true).build().render();
	assertNotNull(svg);
	assertTrue("Should not get an empty SVG", svg.length()>0);
	System.err.println(svg);

	DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	builderFactory.setValidating(false);	// no need to validate, just parse
	DocumentBuilder builder = builderFactory.newDocumentBuilder();
	InputSource svgSource = new InputSource(new StringReader(svg));
	Document xmlDocument = builder.parse(svgSource);
	XPath xPath = XPathFactory.newInstance().newXPath();
	String expression = "//text";

	NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
	assertNotNull(nodeList);
	assertEquals("Should only have one text element in the svg", 1, nodeList.getLength());

	Node text = nodeList.item(0).getFirstChild();	// text is the top XML node and the first child has the content
	assertEquals("Should have the short text as SVG content", "Short text", text.getNodeValue());

}

}
