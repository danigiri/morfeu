package cat.calidos.morfeu.transform;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.transform.injection.DaggerYAMLCellModelGuesserProcessorComponent;
import cat.calidos.morfeu.transform.injection.DaggerYAMLCellToXMLProcessorComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class YAMLComplexCellToXMLProcessor extends PrefixProcessor<JsonNodeCellModel, String> {

private String case_;
private JsonNodeCellModel nodeCellModel;
private boolean hasChildren;


public YAMLComplexCellToXMLProcessor(String prefix, String case_, JsonNodeCellModel nodeCellModel) {

	super(prefix);

	this.case_ = case_;
	this.nodeCellModel = nodeCellModel;
	this.hasChildren = nodeCellModel.cellModel().asComplex().children().size()>0 && nodeCellModel.node().elements().hasNext();

}


@Override
public JsonNodeCellModel input() {
	return nodeCellModel;
}


@Override
public Context<JsonNodeCellModel, String> generateNewContext(Context<JsonNodeCellModel, String> oldContext) {

	Context<JsonNodeCellModel, String> context = oldContext;
	List<PrefixProcessor<JsonNodeCellModel, String>> processors = new LinkedList<PrefixProcessor<JsonNodeCellModel, String>>();
	
	CellModel cellModel = nodeCellModel.cellModel();
	JsonNode node = nodeCellModel.node();
	if (hasChildren) {
		context.push(DaggerYAMLCellToXMLProcessorComponent.builder()
															.withPrefix(prefix)
															.givenCase(case_)
															.fromNode(node)
															.cellModel(cellModel)
															.build()
															.processorSlash());


		// now we have to generate the guesses
		if (cellModel.isSimple()) {	//// SIMPLE CELL MODEL	////
			
		} else {					//// COMPLEX CELL MODEL	////
		
			ComplexCellModel complex = cellModel.asComplex();
			if (node.isArray()) {
				for (int i=0; i<node.size(); i++) {
					System.err.println(node.get(i).getNodeType());
					DaggerYAMLCellModelGuesserProcessorComponent.builder()
																	.withPrefix("\t"+prefix)
																	.givenCase(case_)
																	.fromNode(node.get(i))
																	.parentCellModel(complex)
																	.build()
																	.processors()
																	.forEach(processors::add);
				}
			} else if (node.isObject()) {
				node.fields().forEachRemaining(f ->
				DaggerYAMLCellModelGuesserProcessorComponent.builder()
																.withPrefix("\t"+prefix)
																.givenCase(case_)
																.name(f.getKey())
																.fromNode(f.getValue())
																.parentCellModel(complex)
																.build()
																.processors()
																.forEach(processors::add));
			}
		}
		Collections.reverse(processors);	// we are a stack we want reverse order
		processors.forEach(context::push);
	}

	return context;

}


@Override
public String output() {

	HashMap<String, Object> values = new HashMap<String, Object>(7);
	
	ComplexCellModel cellModel = nodeCellModel.cellModel().asComplex();
	Metadata metadata = cellModel.getMetadata();
	JsonNode node = nodeCellModel.node();

	values.put("cm", cellModel);
	values.put("yaml", node);
	values.put("hasChildren", hasChildren);

	List<String> caseAttributes = metadata.getAttributesFor(case_).stream().collect(Collectors.toList());
	values.put("caseAttr", caseAttributes);

	List<CellModel> attributeNames = cellModel.attributes().asList();
	List<String> attr = attributeNames.stream().map(a -> a.getName()).filter(node::has).collect(Collectors.toList());

	// check if there is an attribute that is an identifier, add it separately and skip it from the list
	boolean hasIdentifier = metadata.getIdentifier().isPresent();
	values.put("hasIdentifier", hasIdentifier);
	String identifier = hasIdentifier ? metadata.getIdentifier().get() : "";
	if (hasIdentifier) {
		values.put("identifier", identifier);
		String value = node.get(identifier).asText();
		values.put("identifierValue", value);
		attr = attr.stream().filter(a -> !a.equals(identifier)).collect(Collectors.toList());
	}
	values.put("attr", attr);

	String template =	"<{{v.cm.name}}"+
						"{% for a in v.caseAttr %} {{a}}{% endfor %}"+
						"{% if  v.hasIdentifier %} {{v.identifier}}={{quote(xmla(v.identifierValue))}}{% endif %}"+
						"{% for a in v.attr %} {{a}}={{quote(xmla(v.yaml.get(a)))}}{% endfor %}" +
						"{% if v.hasChildren %}" +
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

