package cat.calidos.morfeu.transform;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class YAMLKeyValueToXMLProcessor extends PrefixProcessor<JsonNodeCellModel, String> {

private String				case_;			// <identifier>="<key>"
private String				identifierValue;
private JsonNodeCellModel	nodeCellModel;

public YAMLKeyValueToXMLProcessor(	String prefix, String case_, String identifierValue,
									JsonNodeCellModel nodeCellModel) {

	super(prefix);

	this.case_ = case_;
	this.identifierValue = identifierValue;
	this.nodeCellModel = nodeCellModel;

}


@Override
public JsonNodeCellModel input() {
	return nodeCellModel;
}


@Override
public String output() {

	var values = new HashMap<String, Object>(5);

	CellModel cellModel = nodeCellModel.cellModel();
	Metadata metadata = cellModel.getMetadata();

	values.put("name", cellModel.getName());

	List<String> caseAttributes = metadata
			.getAttributesFor(case_)
			.stream()
			.collect(Collectors.toList());
	values.put("caseAttr", caseAttributes);

	String identifier = metadata.getIdentifier().get();
	values.put("identifier", identifier);
	values.put("identifierValue", identifierValue);

	List<CellModel> attributes = cellModel.asComplex().attributes().asList();
	String valueKey = attributes.get(0).getName().equals(identifier) ? attributes.get(1).getName()
			: attributes.get(0).getName();
	values.put("valueKey", valueKey);
	values.put("value", nodeCellModel.node().asText());

	var template = "<${v.name}" + "<#list v.caseAttr as a>${a}<#sep> </#list> "
			+ "${v.identifier}=${f.quote(f.xmla(v.identifierValue))} "
			+ "${v.valueKey}=${f.quote(f.xmla(v.value))} " + "/>\n";

	String out = super.output() + DaggerViewComponent
			.builder()
			.withValue(values)
			.withTemplate(template)
			.andProblem("")
			.build()
			.render();

	return out;

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
