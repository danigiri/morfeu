package cat.calidos.morfeu.transform;

/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class YAMLComplexCellToXMLProcessorSlash extends PrefixProcessor<JsonNodeCellModel, String> {

private JsonNodeCellModel nodeCellModel;

public YAMLComplexCellToXMLProcessorSlash(	String prefix,
											JsonNodeCellModel nodeCellModel) {

	super(prefix);

	this.nodeCellModel = nodeCellModel;

}


@Override
public JsonNodeCellModel input() {
	return nodeCellModel;
}


@Override
public String output() {
	return super.output() + "</" + nodeCellModel.cellModel().getName() + ">\n";
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
