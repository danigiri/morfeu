package cat.calidos.morfeu.transform;

import java.util.HashMap;

import cat.calidos.morfeu.view.injection.DaggerViewComponent;


/**
 * This creates a simple xml text node witjout any attributes &lt;foo&gt;bar&lt;/foo&gt;
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class YAMLTextualToXMLProcessor extends PrefixProcessor<JsonNodeCellModel, String> {

private JsonNodeCellModel nodeCellModel;

public YAMLTextualToXMLProcessor(	String prefix,
									JsonNodeCellModel nodeCellModel) {

	super(prefix);
	this.nodeCellModel = nodeCellModel;

}


@Override
public JsonNodeCellModel input() {
	return nodeCellModel;
}


@Override
public String output() {

	var values = new HashMap<String, Object>(2);
	values.put("cm", nodeCellModel.cellModel());
	values.put("yaml", nodeCellModel.node().asText());
	var template = "";
	if (nodeCellModel.node().asText().isEmpty()) {
		template = "<${v.cm.name}/>\n"; // null or empty?
	} else {
		template = "<${v.cm.name}>${f.xmlc(v.yaml)}</${v.cm.name}>\n";
	}
	String out = super.output() + DaggerViewComponent.builder()
			.withValue(values)
			.withTemplate(template)
			.andProblem("")
			.build()
			.render();

	return out;

}


@Override
public String toString() {
	return output();
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
