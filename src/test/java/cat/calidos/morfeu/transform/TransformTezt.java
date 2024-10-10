// TRANSFORM TEZT . JAVA

package cat.calidos.morfeu.transform;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.transform.injection.DaggerYAMLConverterComponent;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformTezt extends ModelTezt {

protected String transformYAMLToXML(String yamlPath,
									String documentPath)
		throws Exception {

	Document doc = produceDocumentFromPath(documentPath);
	JsonNode yaml = readYAMLFrom(yamlPath);
	String transformed = DaggerYAMLConverterComponent.builder()
			.from(yaml)
			.given(doc.getModel())
			.build()
			.xml();

	return transformed;

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
