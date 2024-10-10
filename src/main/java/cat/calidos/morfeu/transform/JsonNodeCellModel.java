package cat.calidos.morfeu.transform;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.CellModel;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class JsonNodeCellModel {

private JsonNode	node;
private CellModel	cellModel;

public JsonNodeCellModel(	JsonNode node,
							CellModel cellModel) {

	this.node = node;
	this.cellModel = cellModel;

}


public JsonNode node() {
	return node;
}


public CellModel cellModel() {
	return cellModel;
}


@Override
public String toString() {
	return "<" + node.asText() + "," + cellModel.getName() + ">";
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
