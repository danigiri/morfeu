package cat.calidos.morfeu.transform;

import com.fasterxml.jackson.databind.JsonNode;


public class ContentJSONToXMLProcessorSlash implements Processor<JsonNode, String> {

private String		prefix;
private JsonNode	node;

public ContentJSONToXMLProcessorSlash(String prefix, JsonNode node) {

	this.prefix = prefix;
	this.node = node;

}


@Override
public JsonNode input() {
	return node;
}


@Override
public String output() {
	return prefix + "</" + node.get("name").asText() + ">\n";
}


@Override
public String toString() {
	return output();
}

}

/*
 * Copyright 2019 Daniel Giribet
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
