// TRANSFORM JSON TO XML INT TEST . JAVA

package cat.calidos.morfeu.model.transform;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.transform.injection.DaggerTransformComponent;
import cat.calidos.morfeu.transform.injection.DaggerContentConverterComponent;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformJSONToXMLIntTest extends TransformTezt {

private String content;

@Before
public void setup() throws Exception {

	File inputFile = new File("target/test-classes/test-resources/transform/document1-as-view.json");
	content = FileUtils.readFileToString(inputFile, Config.DEFAULT_CHARSET);

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
	compareWithXMLFile(transformed, "target/test-classes/test-resources/documents/document1.xml");

}



@Test
public void testConverter() throws Exception {

	JsonNode json = DaggerJSONParserComponent.builder().from(content).build().json().get();
	assertNotNull(json);

	String transformed = DaggerContentConverterComponent.builder().from(json).build().xml();

	//System.err.println(transformed);
	compareWithXMLFile(transformed, "target/test-classes/test-resources/documents/document1.xml");

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