// CONTENT JSON TO XML PROCESSOR . JAVA

package cat.calidos.morfeu.transform;

import java.util.LinkedList;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.transform.injection.DaggerContentJSONToXMLComponent;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import cat.calidos.morfeu.view.injection.ViewComponent;

/**
 * Base class that processes Content json nodes and outputs them to XML
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentJSONToXMLProcessor implements Processor<JsonNode, String> {

private static final String	NAME_FIELD		= "name";

private static final String	CHILDREN_FIELD	= "children";

private String				prefix;
private JsonNode			node;
private boolean				hasChildren;
private boolean				needToRender;

private boolean				hasValue;
private String				value;


public ContentJSONToXMLProcessor(String prefix, JsonNode node) {

	this.prefix = prefix;
	this.node = node;
	if (node.has(CHILDREN_FIELD)) {
		this.hasChildren = node.get(CHILDREN_FIELD).isArray() && node.get(CHILDREN_FIELD).size() > 0;
	} else {
		this.hasChildren = false;
	}

	hasValue = node.has("value");
	value = hasValue ? node.get("value").asText() : "";

	// if no name field or it's empty means that it's a node that has no XML representation (like
	// the empty root)
	this.needToRender = node.has(NAME_FIELD) && node.get(NAME_FIELD).asText().length() > 0;

}


@Override
public Context<JsonNode, String> generateNewContext(Context<JsonNode, String> oldContext) {

	Context<JsonNode, String> newContext = oldContext;

	// if we have children or value and we're not the root (empty) node, we need to add a slash
	if ((hasChildren || hasValue) && needToRender) {
		// if we have a value we do not add a prefix to the slash so we don't implicitly add the
		// prefix to the value
		var appliedPrefix = hasValue ? "" : prefix;
		newContext.push(DaggerContentJSONToXMLComponent.builder().fromNode(node).withPrefix(appliedPrefix).build()
				.processorSlash());

	}

	// now we push the children if there are any
	if (hasChildren) {
		var newPrefix = "\t" + prefix;
		JsonNode children = node.get(CHILDREN_FIELD); // adding directly to the context will mean
														// reverse order
		var childrenList = new LinkedList<Processor<JsonNode, String>>();
		children.forEach(c -> childrenList.addFirst(DaggerContentJSONToXMLComponent.builder() // add
																								// at
																								// beginning
				.fromNode(c).withPrefix(newPrefix).build().processor()));
		childrenList.forEach(newContext::push); // now this is in the correct order
	}

	return newContext;

}


@Override
public JsonNode input() {
	return node;
}


@Override
public String output() {

	var render = "";
	if (needToRender) {
		var name = node.get(NAME_FIELD).asText();
		Map<String, Object> v = MorfeuUtils.paramMap(NAME_FIELD, name, "value", value, "pref", prefix, "node", node);
		String template = null;
		if (!hasChildren && !hasValue) {
			template = "[(${v.pref})]<[(${v.name })] " // - postfix
					+ "[# th:each=\"a : ${v.node.get('internalAttributes')}\"]" // - prefix
					+ " [(${a.get('name').textValue})]=\"[(${a.get('value').textValue})]\" [/]" // - postfix
					+ "[# th:each=\"a : ${v.node.get('attributes')}\"] [(${a.get('name').textValue})]=\"[(${a.get('value').textValue})]\" [/] />\n";
		} else {
			template = "[(${v.pref})]<[(${v.name })] " // - postfix
					+ "[# th:each=\"a : ${v.node.get('internalAttributes')}\"]" // - prefix
					+ " [(${a.get('name').textValue})]=\"[(${a.get('value').textValue})]\" [/]" // - postfix
					+ "[# th:each=\"a : ${v.node.get('attributes')}\"] [(${a.get('name').textValue})]=\"[(${a.get('value').textValue})]\" [/] >"
					+ (hasValue ? "[(${#str.xmlc(v.value)})]" : "\n");
		}

		ViewComponent view = DaggerViewComponent.builder().withValue(v).withTemplate(template).andProblem("").build();
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

