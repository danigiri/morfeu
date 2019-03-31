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
import cat.calidos.morfeu.transform.StringProcessor;
import cat.calidos.morfeu.transform.YAMLComplexCellToXMLProcessor;
import cat.calidos.morfeu.transform.YAMLComplexCellToXMLProcessorSlash;
import cat.calidos.morfeu.transform.YAMLTextualToXMLProcessor;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class YAMLComplexCellToXMLProcessorModule {


@Provides
List<StringProcessor<JsonNodeCellModel>> processors(String pref,
												@Named("Case") String case_,
												JsonNode node,
												@Nullable CellModel cellModel,
												@Named("Parent") @Nullable ComplexCellModel parentCellModel) {

	List<StringProcessor<JsonNodeCellModel>> processors = new LinkedList<StringProcessor<JsonNodeCellModel>>();
	
	if (cellModel==null && parentCellModel==null) {
			throw new NullPointerException("cannot get cell model of the processor without either parent or cell model");
	}

	if (node.isObject()) {														//// OBJECT ////

		if (cellModel==null) {	// we have go through the children and generate processors
	
			// are we in attributes only?
			Optional<CellModel> guess = parentCellModel.children()
														.stream()
														.filter(cm -> cm.getMetadata()
																			.getDirectivesFor(case_)
																			.contains(Metadata.ATTRIBUTES_ONLY))
														.filter(cm -> node.has(cm.getName()))
														.findAny();
			if (guess.isPresent()) {
				// if we are in attributes only mode we generate only one processor with the correct guess
				processors.add(generateComplexProcessor(pref, case_, node, guess.get()));
			} else {
				// we have a list of children, we match and generate a list
				processors = generateChildrenProcessors(pref, case_, node, parentCellModel);
			}
		} else {				// we know the cell model
			if (cellModel.isSimple()) {						// .. and it's a simple one
				processors.add(generateTextualProcessor(pref, node, cellModel));
			} else {										// it's a complex one
				if (cellModel.getMetadata().getDirectivesFor(case_).contains(Metadata.KEY_VALUE)) {
					Iterator<Entry<String, JsonNode>> fields = node.fields();
					while (fields.hasNext()) {
						processors.add(generateComplexProcessor(pref, case_, fields.next().getValue(), cellModel));
					}
				} else {	// just a complex known cell
					processors.add(generateComplexProcessor(pref, case_, node, cellModel));
				}
			}
		}


	} else if (node.isTextual()) {	// we know the cell model here				//// TEXTUAL ////

		processors.add(generateTextualProcessor(pref, node, cellModel));

	} else if (node.isArray()) {												//// ARRAY ////
		for (int i=0;i<node.size();i++) {
			processors.add(DaggerYAMLCellToXMLProcessorComponent.builder()
																	.withPrefix("\t"+pref)
																	.fromNode(node.get(i))
																	.parentCellModel(parentCellModel)
																	.givenCase(case_)
																	.build()
																	.processors()
																	.get(0));
		}
	}

	return processors;

}


@Provides
StringProcessor<JsonNodeCellModel> processorSlash(String pref, JsonNode node, @Nullable CellModel cellModel) {

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


private List<StringProcessor<JsonNodeCellModel>> generateChildrenProcessors(String pref, 
																			String case_, JsonNode node,
																			ComplexCellModel parentCellModel) {
	return parentCellModel.children()
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

