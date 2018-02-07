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

import java.io.File;

import javax.xml.transform.Source;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.transform.injection.DaggerTransformComponent;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformJSONToXMLIntTest {


private String content;


@Before
public void setup() throws Exception {

	File inputFile = new File("target/test-classes/test-resources/transform/document1-as-view.json");
	content = FileUtils.readFileToString(inputFile, Config.DEFAULT_CHARSET);
	
}


@Test
public void testTransformUsingTemplate() throws Exception {

	JsonNode json = DaggerJSONParserComponent.builder().from(content).build().json().get();
	assertNotNull(json);

	String transformed = DaggerViewComponent.builder()
											.withTemplate("templates/transform/content-json-to-xml.twig")
											.withValue(json)
											.build()
											.render();
	compareStringWithFile(transformed, "target/test-classes/test-resources/documents/document1.xml");
	
}


@Test
public void testTransform() throws Exception {

	String transforms = "string-to-json,content-to-xml";
	
	Transform<String, String> transform = DaggerTransformComponent.builder()
																.transforms(transforms)
																.build()
																.transformation()
																.get();
	
	
	String transformed = transform.apply(content);
	//System.err.println(transformed);
	compareStringWithFile(transformed, "target/test-classes/test-resources/documents/document1.xml");
	
}


private void compareStringWithFile(String content, String path) {

	Source transformedSource = Input.fromString(content).build();
	
	File originalFile = new File(path);
	Source originalSource = Input.fromFile(originalFile).build();

	Diff diff = DiffBuilder.compare(originalSource)
							.withTest(transformedSource)
							.ignoreComments()
							.ignoreWhitespace()
							.build();
	
	assertFalse("Transformed JSON to XML should be the same as original", diff.hasDifferences());
}

}
