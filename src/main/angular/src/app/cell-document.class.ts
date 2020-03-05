// CELL - DOCUMENT . CLASS . TS
import { Content } from "./content.class";
import { Model } from "./model.class";

import { SerialisableToJSON } from './serialisable-to-json.interface';

export class CellDocument implements SerialisableToJSON<CellDocument, CellDocumentJSON> {

model?: Model;
content?: Content;


constructor(public name: string,
			public uri: string,
			public desc: string,
			public kind: string,
			public modelURI: string,
			public contentURI: string,
			public valid:boolean,
			public problem: string,
			public presentation?: string) {}


hasProblem(): boolean {
	return this.problem!=null && this.problem.length>0;
}

//// SerialisableToJSON ////

toJSON(): CellDocumentJSON {
	return Object.assign({}, this);
}


fromJSON(json: CellDocumentJSON|string): CellDocument {

	if (typeof json === 'string') {

		return JSON.parse(json, CellDocument.reviver);

	} else {

		let document_ = Object.create(CellDocument.prototype);

		return Object.assign(document_, json);

	}

}


static reviver(key: string, value: any): any {
	return key === "" ? Object.create(CellDocument.prototype).fromJSON(value) : value;
}

//// SerialisableToJSON [end] ////

}

// TODO: this is implementation leaky, turn into an object so we can:
// TODO: replace all the problem!=null && problem.length>0 with proper method as we're leaking implementation


//serialisable interface
export interface CellDocumentJSON {
	
name: string;
uri: string;
desc: string;
kind: string;
modelURI: string;
contentURI: string;
valid:boolean;
problem: string;
presentation?: string;

}

/*
 *	  Copyright 2017 Daniel Giribet
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
