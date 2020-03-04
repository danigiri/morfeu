// CONTENT . CLASS . TS

import { Cell, CellJSON } from "./cell.class";
import { Model } from "./model.class";

import { SerialisableToJSON } from "./serialisable-to-json.interface";

export class Content extends Cell implements SerialisableToJSON<Content, ContentJSON> {


// associate the content cells with the corresponding cell models, starting from the root
associateFromRoot(model: Model) {
	this.children = this.children.map(c => c.associateWith(model));
}


//associate the content cells with the corresponding cell models, starting from the cell model uri of each
associate(model: Model) {
	this.associateWith(model, this.cellModelURI);
}


static fromCell(cell: Cell): Content {

	let content = Object.create(Content.prototype); 
	content = Object.assign(content, cell);

	return content;

}


//// SerialisableToJSON ////

toJSON(): ContentJSON {
	// not implementing the cell fragment option as it's not applicable yet
	return super.toJSON();
}


fromJSON(json: ContentJSON|string): Content {

	// not implementing the cell fragment option as it's not applicable yet

	if (typeof json === 'string') {
		return JSON.parse(json, Content.reviver);
	}

	const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
	const cell = CELL.fromJSON(json); 
	const content = Content.fromCell(cell);

	return content;

}


static reviver(key: string, value: any): any {
	return key === "" ? Object.create(Content.prototype).fromJSON(value) : value;
}


}

// serialisable interface
export interface ContentJSON extends CellJSON {}

/*
 *	  Copyright 2019 Daniel Giribet
 *
 *	 Licensed under the Apache License, Version 2.0 (the "License");
 *	 you may not use this file except in compliance with the License.
 *	 You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 *	 Unless required by applicable law or agreed to in writing, software
 *	 distributed under the License is distributed on an "AS IS" BASIS,
 *	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	 See the License for the specific language governing permissions and
 *	 limitations under the License.
 */
