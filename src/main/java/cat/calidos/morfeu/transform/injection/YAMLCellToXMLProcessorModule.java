package cat.calidos.morfeu.transform.injection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import dagger.Module;
import dagger.Provides;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.transform.JsonNodeCellModel;
import cat.calidos.morfeu.transform.PrefixProcessor;
import cat.calidos.morfeu.transform.YAMLComplexCellToXMLProcessor;
import cat.calidos.morfeu.transform.YAMLComplexCellToXMLProcessorSlash;
import cat.calidos.morfeu.transform.YAMLTextualToXMLProcessor;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class YAMLCellToXMLProcessorModule {


@Provides
List<PrefixProcessor<JsonNodeCellModel, String>> processors(String pref,
															@Named("Case") String case_,
															JsonNode node,
															CellModel cellModel) {

	List<PrefixProcessor<JsonNodeCellModel, String>> processors = new LinkedList<PrefixProcessor<JsonNodeCellModel, String>>();

	if (cellModel.isSimple()) {		//// SIMPLE CELL MODEL	////
		
	} else {						//// COMPLEX CELL MODEL ////
		
		if (node.isObject()) {
			processors.add(generateComplexProcessor(pref, case_, node, cellModel));
		} else if (node.isArray()) {
			node.elements().forEachRemaining(e -> processors.add(generateComplexProcessor(pref, case_, e, cellModel)));
		} else if (node.isTextual()) {
			// error
		} else {
			// error
		}
	}

	return processors;

}


@Provides
PrefixProcessor<JsonNodeCellModel, String> processorSlash(String pref, JsonNode node, CellModel cellModel) {

	if (cellModel==null) {
		throw new NullPointerException("Cannot create a slash processor without the node");
	}

	JsonNodeCellModel nodeCellModel = new JsonNodeCellModel(node, cellModel);
	return new YAMLComplexCellToXMLProcessorSlash(pref, nodeCellModel);

}


private YAMLTextualToXMLProcessor generateTextualProcessor(String pref, JsonNode node, CellModel cellModel) {
	return new YAMLTextualToXMLProcessor(pref, new JsonNodeCellModel(node, cellModel));
}


private YAMLComplexCellToXMLProcessor generateComplexProcessor(String pref,
																String case_,
																JsonNode node,
																CellModel cellModel) {
	return new YAMLComplexCellToXMLProcessor(pref, case_, new JsonNodeCellModel(node, cellModel));
}


private List<PrefixProcessor<JsonNodeCellModel, String>> generateChildrenProcessors(String pref, 
																			String case_, JsonNode node,
																			CellModel parentCellModel) {
	return parentCellModel.asComplex()
							.children()
							.stream()
							.filter(cm -> node.has(matchName(cm, case_)))
							.map(cm -> generateComplexProcessor(pref, case_, node, cm))
							.collect(Collectors.toList());
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

