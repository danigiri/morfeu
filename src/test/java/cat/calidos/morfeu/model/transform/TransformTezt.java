// TRANSFORM TEZT . JAVA

package cat.calidos.morfeu.model.transform;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;

import org.apache.commons.io.FileUtils;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.transform.injection.DaggerYAMLConverterComponent;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformTezt extends ModelTezt {


protected void compareWithXMLFile(String content, String path) {

	Source transformedSource = Input.fromString(content).build();
	
	File originalFile = new File(path);
	Source originalSource = Input.fromFile(originalFile).build();

	Diff diff = DiffBuilder.compare(originalSource)
							.withTest(transformedSource)
							.ignoreComments()
							.ignoreWhitespace()
							.build();

	assertFalse("Transformed JSON to XML should be the same as original"+diff.toString(), diff.hasDifferences());

}


protected void compareWithXML(String content, String expected) {

	Source transformedSource = Input.fromString(content).build();
	
	Source originalSource = Input.fromString(expected).build();

	Diff diff = DiffBuilder.compare(originalSource)
							.withTest(transformedSource)
							.ignoreComments()
							.ignoreWhitespace()
							.build();

	assertFalse("Transformed JSON to XML should be the same as original"+diff.toString(), diff.hasDifferences());

}



protected String transformYAMLToXML(String yamlPath, String documentPath) throws Exception {

	Document doc = produceDocumentFromPath(documentPath);
	JsonNode yaml = readYAMLFrom(yamlPath);
	String transformed = DaggerYAMLConverterComponent.builder().from(yaml).given(doc.getModel()).build().xml();

	return transformed;

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
