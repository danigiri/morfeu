package cat.calidos.morfeu.transform.injection;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.transform.Context;
import cat.calidos.morfeu.transform.JsonNodeCellModel;
import cat.calidos.morfeu.transform.PrefixProcessor;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class YAMLCellModelGuesserProcessor extends PrefixProcessor<JsonNodeCellModel, String> {

private String case_;
private JsonNodeCellModel nodeCellmodel;


YAMLCellModelGuesserProcessor(String prefix, String case_, JsonNodeCellModel nodeCellmodel) {

	super(prefix);

	this.case_ = case_;
	this.nodeCellmodel = nodeCellmodel;

}


@Override
public Context<JsonNodeCellModel, String> generateNewContext(Context<JsonNodeCellModel, String> oldContext) {

	Context<JsonNodeCellModel, String> context = oldContext;

	List<PrefixProcessor<JsonNodeCellModel, String>> processors = new LinkedList<PrefixProcessor<JsonNodeCellModel, String>>();

	JsonNode node = nodeCellmodel.node();
	ComplexCellModel parentCellModel = nodeCellmodel.cellModel().asComplex();
	if (parentCellModel.children().size()==1) {
		// trivial case, there is only one child so now we know which one to use

		CellModel child = parentCellModel.children().child(0);
		DaggerYAMLCellToXMLProcessorComponent.builder()
												.withPrefix(prefix)
												.givenCase(case_)
												.cellModel(child)
												.fromNode(node)
												.build()
												.processors()
												.forEach(processors::add);

	} else {

	}
	
	processors.forEach(context::push);
	
	return context;
}


@Override
public JsonNodeCellModel input() {
	return nodeCellmodel;
}


public String output() {
	return prefix; //+"<!-- GUESS: parentCellModel='"+nodeCellmodel.cellModel().getName()+"'  -->";
}


private String matchName(CellModel model, String case_) {
	return model.getMetadata().getDirectivesFor(case_).contains(Metadata.LISTS_NO_PLURAL)
			? model.getName() : model.getName()+"s";
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

