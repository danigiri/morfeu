package cat.calidos.morfeu.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.transform.injection.DaggerContentJSONToXMLComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import cat.calidos.morfeu.view.injection.ViewComponent;

/** Base class that processes Content json nodes and outputs them to XML
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentJSONToXMLProcessor implements Processor<JsonNode, String> {

private static final String NAME_FIELD = "name";

private static final String CHILDREN_FIELD = "children";

private String prefix;
private JsonNode node;
private boolean hasChildren;


public ContentJSONToXMLProcessor(String prefix, JsonNode node) {

	this.prefix = prefix;
	this.node = node;
	if (node.has(CHILDREN_FIELD)) {
		this.hasChildren = node.get(CHILDREN_FIELD).isArray() && node.get(CHILDREN_FIELD).size()>0;
	} else {
		this.hasChildren = false;
	}

}


@Override
public Context<JsonNode, String> generateNewContext(Context<JsonNode, String> oldContext) {

	Context<JsonNode, String> newContext = oldContext;

	// now we push the children if there are any
	if (hasChildren) {

		if (node.has(NAME_FIELD)) {	// if it's an empty node we don't push a slash processor
			newContext.push(DaggerContentJSONToXMLComponent.builder()
															.fromNode(node)
															.withPrefix(prefix)
															.builder()
															.processorSlash()
			);
		}
		String newPrefix = "\t"+prefix;
		JsonNode children = node.get(CHILDREN_FIELD);	// adding directly to the context will mean reverse order
		LinkedList<Processor<JsonNode, String>> childrenList = new LinkedList<Processor<JsonNode, String>>();
		children.forEach(c -> childrenList.addFirst(DaggerContentJSONToXMLComponent.builder()	// add at beginning
																					.fromNode(c)
																					.withPrefix(newPrefix)
																					.builder()
																					.processor())
		);
		childrenList.forEach(newContext::push);	// now this is in the correct order
	}

	return newContext;

}


@Override
public JsonNode input() {
	return node;
}


@Override
public String output() {

	// if we don't have a name it means that it's a json node that has no XML representation (like the empty root)

	String render = "";
	if (node.has(NAME_FIELD)) {
		String name = node.get(NAME_FIELD).asText();
		boolean hasValue = node.has("value");
		String value = hasValue ? node.get("value").asText() : "";
		HashMap<String, Object> values = new HashMap<String, Object>(4);
		values.put(NAME_FIELD, name);
		values.put("value", value);
		values.put("pref", prefix);
		values.put("node", node);
	
		String template = null;
		if (!hasChildren && !hasValue) {
			template = "{{v.pref}}<{{v.name -}} "+
						"{%- for a in v.node.internalAttributes " +
						"	%} {{a.name.textValue}}=\"{{a.value.textValue}}\" {% " +
						"endfor -%}" + 
						"{%for a in v.node.attributes " +
						"	%} {{a.name.textValue}}=\"{{a.value.textValue}}\" {% " +
						"endfor -%} />\n";
		} else {
			template = "{{v.pref}}<{{v.name -}} "+
					"{%- for a in v.node.internalAttributes "
						+ "%} {{a.name.textValue}}=\"{{a.value.textValue}}\" {% "
					+ "endfor -%}" + 
					"{%for a in v.node.attributes "
						+ "%} {{a.name.textValue}}=\"{{a.value.textValue}}\" {% "
						+ "endfor -%} >" + (hasValue ? "{{value | xmlc}}" : "\n");
		}

		ViewComponent view = DaggerViewComponent.builder().withValue(values).withTemplate(template).andProblem("").build();
		render = view.render();
	}

	return render;

}


@Override
public String toString() {
	return output();
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

