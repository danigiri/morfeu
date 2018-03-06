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

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformYAMLToContentIntTest extends ModelTezt {

@Test
public void testTransformUsintTemplateDocument3() throws Exception {
	
	
	YAMLMapper mapper = new YAMLMapper();
	File inputFile = new File("target/test-classes/test-resources/transform/document3.yaml");
	String content = FileUtils.readFileToString(inputFile, Config.DEFAULT_CHARSET);
	JsonNode yaml = mapper.readTree(content);

	Document doc = produceDocumentFromPath("test-resources/documents/document3.json");
	assertNotNull(doc);
	Map<String, Object> values = new HashMap<String, Object>(2);
	values.put("yaml", yaml);
	values.put("cellmodels", doc.getModel().getRootCellModels());
	values.put("case","yaml-to-xml");
	values.put("i","");
	
	String transformed = DaggerViewComponent.builder()
			.withTemplate("templates/transform/content-yaml-to-xml.twig")
			.withValue(values)
			.build()
			.render();
	System.err.println(transformed);
	
} 

}
