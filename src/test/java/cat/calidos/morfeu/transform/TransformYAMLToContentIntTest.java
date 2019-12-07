// TRANSFORM YAML TO CONTENT INT TEST . JAVA

package cat.calidos.morfeu.transform;

import static org.junit.Assert.*;

import org.junit.Test;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformYAMLToContentIntTest extends TransformTezt {

@Test
public void testTransformUsingTemplateDocument1() throws Exception {

	String yamlPath = "target/test-classes/test-resources/transform/document1.yaml";
	String documentPath = "test-resources/documents/document1.json";
	String xmlPath = "src/test/resources/test-resources/documents/document1.xml";

	String transformed = transformYAMLToXML(yamlPath, documentPath);
	assertNotNull(transformed);
	//System.err.println(transformed);
	compareWithXMLFile(transformed, xmlPath);

}


@Test
public void testTransformUsingTemplateDocument3() throws Exception {

	String yamlPath = "target/test-classes/test-resources/transform/document3.yaml";
	String documentPath = "test-resources/documents/document3.json";
	String xmlPath = "src/test/resources/test-resources/documents/document3.xml";

	String transformed = transformYAMLToXML(yamlPath, documentPath);
	assertNotNull(transformed);
//	System.err.println(transformed);
	compareWithXMLFile(transformed, xmlPath);

}


@Test
public void testTransformUsingTemplateKeyValuesDocument() throws Exception {

	String yamlPath = "target/test-classes/test-resources/transform/keyvalues.yaml";
	String documentPath = "test-resources/documents/keyvalues-yaml.json";
	String xmlPath = "src/test/resources/test-resources/transform/keyvalues.xml";

	String transformed = transformYAMLToXML(yamlPath, documentPath);
	assertNotNull(transformed);
	//System.err.println(transformed);
	compareWithXMLFile(transformed, xmlPath);

}


@Test
public void testTransformUsingTemplateEscapeDocument() throws Exception {

	String yamlPath = "target/test-classes/test-resources/transform/escape.yaml";
	String documentPath = "test-resources/documents/escape-yaml.json";
	String xmlPath = "src/test/resources/test-resources/transform/escape.xml";

	String transformed = transformYAMLToXML(yamlPath, documentPath);
	assertNotNull(transformed);
	//System.err.println(transformed);
	compareWithXMLFile(transformed, xmlPath);

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