package cat.calidos.morfeu.transform.injection;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import dagger.Module;
import dagger.Provides;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.transform.JsonNodeCellModel;
import cat.calidos.morfeu.transform.PrefixProcessor;
import cat.calidos.morfeu.transform.YAMLComplexCellToXMLProcessor;
import cat.calidos.morfeu.transform.YAMLComplexCellToXMLProcessorSlash;
import cat.calidos.morfeu.transform.YAMLKeyValueToXMLProcessor;
import cat.calidos.morfeu.transform.YAMLTextualToXMLProcessor;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class YAMLCellToXMLProcessorModule {

@Provides
List<PrefixProcessor<JsonNodeCellModel, String>> processors(String pref,
															@Named("Case") String case_,
															JsonNode node,
															CellModel cellModel) {

	var processors = new LinkedList<PrefixProcessor<JsonNodeCellModel, String>>();

	if (cellModel.isSimple()) { //// SIMPLE CELL MODEL ////

		if (node.isTextual() || node.isObject()) { // isObject: we probably have an [empty] object
			processors.add(generateTextualProcessor(pref, node, cellModel));
		} else if (node.isArray()) { // we have a list of textuals
			node.elements()
					.forEachRemaining(
							e -> processors.add(generateTextualProcessor(pref, e, cellModel)));
		} else {
			throw new IllegalStateException("Unhandled guess state of simple cell model");
		}

	} else { //// COMPLEX CELL MODEL ////

		if (node.isObject()) {
			// we check if we are a key value node {"key1": "v1", "key2": "v2"} or a normal object
			if (cellModel.getMetadata().getDirectivesFor(case_).contains(Metadata.KEY_VALUE)) {
				node.fields()
						.forEachRemaining(f -> processors
								.add(generateKeyValueProcessor(pref, case_, f, cellModel)));
			} else {
				processors.add(generateComplexProcessor(pref, case_, node, cellModel));
			}
		} else if (node.isArray()) {
			node.elements()
					.forEachRemaining(e -> processors
							.add(generateComplexProcessor(pref, case_, e, cellModel)));
		} else if (node.isTextual()) {
			throw new IllegalStateException(
					"Unhandled guess state of complex cell model [textual]");
		} else {
			throw new IllegalStateException("Unhandled guess state of simple cell model [else]");
		}

	}

	return processors;

}


@Provides
PrefixProcessor<JsonNodeCellModel, String> processorSlash(	String pref,
															JsonNode node,
															CellModel cellModel) {

	if (cellModel == null) {
		throw new NullPointerException("Cannot create a slash processor without the node");
	}

	var nodeCellModel = new JsonNodeCellModel(node, cellModel);
	return new YAMLComplexCellToXMLProcessorSlash(pref, nodeCellModel);

}


private YAMLComplexCellToXMLProcessor generateComplexProcessor(	String pref,
																String case_,
																JsonNode node,
																CellModel cellModel) {
	return new YAMLComplexCellToXMLProcessor(pref, case_, new JsonNodeCellModel(node, cellModel));
}


private YAMLTextualToXMLProcessor generateTextualProcessor(	String pref,
															JsonNode node,
															CellModel cellModel) {
	return new YAMLTextualToXMLProcessor(pref, new JsonNodeCellModel(node, cellModel));
}


private YAMLKeyValueToXMLProcessor generateKeyValueProcessor(	String pref,
																String case_,
																Entry<String, JsonNode> kv,
																CellModel cellModel) {

	String id = cellModel.getMetadata().getIdentifier().get();

	return new YAMLKeyValueToXMLProcessor(pref, id, kv.getKey(),
			new JsonNodeCellModel(kv.getValue(), cellModel));

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
