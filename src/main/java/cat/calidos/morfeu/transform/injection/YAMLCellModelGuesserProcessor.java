// YAML CELL MODEL GUESSER PROCESSOR . JAVA

package cat.calidos.morfeu.transform.injection;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.transform.Context;
import cat.calidos.morfeu.transform.JsonNodeCellModel;
import cat.calidos.morfeu.transform.PrefixProcessor;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class YAMLCellModelGuesserProcessor extends PrefixProcessor<JsonNodeCellModel, String> {

private String				case_;
private JsonNodeCellModel	nodeCellmodel;
private Optional<String>	name;

YAMLCellModelGuesserProcessor(	String prefix, String case_, @Nullable String name,
								JsonNodeCellModel nodeCellmodel) {

	super(prefix);

	this.case_ = case_;
	this.nodeCellmodel = nodeCellmodel;
	this.name = Optional.ofNullable(name);
}


@Override
public Context<JsonNodeCellModel, String> generateNewContext(Context<JsonNodeCellModel, String> oldContext) {

	Context<JsonNodeCellModel, String> context = oldContext;

	var processors = new LinkedList<PrefixProcessor<JsonNodeCellModel, String>>();

	JsonNode node = nodeCellmodel.node();
	ComplexCellModel parent = nodeCellmodel.cellModel().asComplex();
	CellModel child = null;
	if (parent.children().size() == 1) {
		// trivial case, there is only one child so now we know which one to use

		child = parent.children().child(0);

	}
	// we have to do some creative matching next

	if (child == null && name.isPresent()) { // whecking if we come from an object parent that had a
												// field name to match
		child = parent
				.children()
				.stream()
				.filter(c -> matchName(c, case_).equals(name.get()))
				.findAny()
				.orElse(null);
	}
	if (child == null) { // otherwise we check the children
		child = parent.children().stream().filter(c -> matches(c, node)).findAny().orElse(null);
		// .orElseThrow(() -> new RuntimeException(new ParsingException("No child match for
		// "+parent)));
	}
	// TODO: find by attribute matching

	if (child != null) {
		DaggerYAMLCellToXMLProcessorComponent
				.builder()
				.withPrefix(prefix)
				.givenCase(case_)
				.cellModel(child)
				.fromNode(node)
				.build()
				.processors()
				.forEach(processors::add);
	}
	Collections.reverse(processors); // we are a stack we want reverse order
	processors.forEach(context::push);

	return context;

}


@Override
public JsonNodeCellModel input() {
	return nodeCellmodel;
}


public String output() {
	return prefix; // +"<!-- GUESS: parentCellModel='"+nodeCellmodel.cellModel().getName()+"' -->";
}


private boolean matches(CellModel candidate,
						JsonNode node) {

	boolean matches = false;
	boolean attributesOnly = candidate
			.getMetadata()
			.getAttributesFor(case_)
			.contains(Metadata.ATTRIBUTES_ONLY);

	if (candidate.isSimple()) {
		// first we see if we are in atributes only mode
		if (attributesOnly) {
			// if we have the attributes only it means we are complex
			throw new ClassCastException("Simple cell model " + candidate.getName() + " has "
					+ Metadata.ATTRIBUTES_ONLY);
		}
	} else {
		ComplexCellModel complexCandidate = candidate.asComplex();
		if (attributesOnly) {
			matches = complexCandidate
					.attributes()
					.stream()
					.anyMatch(a -> node.has(matchName(a, case_)));
		} else {
			matches = complexCandidate
					.children()
					.stream()
					.anyMatch(c -> node.has(matchName(c, case_)));
		}
	}

	return matches;

}


private String matchName(	CellModel model,
							String case_) {
	return model.getMetadata().getDirectivesFor(case_).contains(Metadata.LISTS_NO_PLURAL)
			? model.getName() : model.getName() + "s";
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
