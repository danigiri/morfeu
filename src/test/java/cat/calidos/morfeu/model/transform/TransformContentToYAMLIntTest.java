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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformContentToYAMLIntTest extends ModelTezt {

@Test
public void testTransformUsingTemplate() throws Exception {

	Document doc = produceDocumentFromPath("test-resources/documents/document1.json");
	assertNotNull(doc);

	Map<String, Object> values = new HashMap<String, Object>(2);
	values.put("cells", doc.getContent());
	values.put("model", doc.getModel());
	
	String transformed = DaggerViewComponent.builder()
			.withTemplate("templates/transform/content-to-yaml.twig")
			.withValue(values)
			.build()
			.render();
	
	
}


}
