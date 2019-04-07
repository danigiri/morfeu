package cat.calidos.morfeu.model.transform;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.transform.injection.DaggerYAMLConverterComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class YAMLConverterIntTest extends ModelTezt {


@Test
public void testYAMLConverter() throws Exception {
	
	String yamlPath = "target/test-classes/test-resources/transform/document1.yaml";
	String docPath = "test-resources/documents/document1.json";
	String xmlPath = "src/test/resources/test-resources/documents/document1.xml";
	JsonNode yaml = readYAMLFrom(yamlPath);

	Document doc = produceDocumentFromPath(docPath);
	assertNotNull(doc);

	String transformed = DaggerYAMLConverterComponent.builder().from(yaml).given(doc.getModel()).build().xml();
	assertNotNull(transformed);
	System.err.println(transformed);


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

