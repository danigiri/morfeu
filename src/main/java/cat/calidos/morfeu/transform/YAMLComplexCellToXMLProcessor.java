package cat.calidos.morfeu.transform;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.openqa.selenium.json.Json;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.Attributes;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class YAMLComplexCellToXMLProcessor extends StringProcessor<JsonNodeCellModel> {

private String case_;
private JsonNodeCellModel nodeCellModel;


public YAMLComplexCellToXMLProcessor(String prefix, String case_, JsonNodeCellModel nodeCellModel) {

	super(prefix);

	this.case_ = case_;
	this.nodeCellModel = nodeCellModel;

}


@Override
public JsonNodeCellModel input() {
	return nodeCellModel;
}


@Override
public String output() {

	HashMap<String, Object> values = new HashMap<String, Object>(3);
	
	ComplexCellModel cellModel = nodeCellModel.cellModel().asComplex();
	Metadata metadata = cellModel.getMetadata();
	JsonNode node = nodeCellModel.node();

	values.put("cm", cellModel);
	values.put("yaml", node);

	List<String> caseAttributes = metadata.getAttributesFor(case_).stream().collect(Collectors.toList());
	values.put("caseAttr", caseAttributes);

	// check if there is an attribute that is an identifier
	boolean hasIdentifier = metadata.getIdentifier().isPresent();
	values.put("hasIdentifier", hasIdentifier);
	String identifier = hasIdentifier ? metadata.getIdentifier().get() : "";
	if (hasIdentifier) {
		values.put("identifier", identifier);
		values.put("identifierValue", node.get(identifier).asText());	// double check
	}

	// now we check the rest of the attributes
	List<CellModel> attributeNames = cellModel.attributes().asList();
	List<String> attr = attributeNames.stream().map(a -> a.getName()).filter(node::has).collect(Collectors.toList());
	if (hasIdentifier) {	// we skip the identifier
		attr = attr.stream().filter(a -> !a.equals(identifier)).collect(Collectors.toList());
	}
	values.put("attr", attr);

	String template =	"<{{v.cm.name}}"+
						"{% for a in v.caseAttr %} {{a}}{% endfor %}"+
						"{% if  v.hasIdentifier %} {{v.identifier}}={{quote(xmla(v.identifierValue))}}{% endif %}"+
						"{% for a in v.attr %} {{a}}={{quote(xmla(v.yaml.get(a)))}}{% endfor %}" +
						"{% if cellmodel.asComplex.children.size()>0 %}" +
						">\n"+
						"{% else %}"+
						"/>\n"+
						"{% endif %}";
	
	String out = super.output()+DaggerViewComponent.builder()
													.withValue(values)
													.withTemplate(template)
													.andProblem("")
													.build()
													.render();

	return out;

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

