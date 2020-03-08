// MODEL . CLASS . TS

import {CellType} from './cell-type.class';
import {CellModel, CellModelJSON} from './cell-model.class';
import {SerialisableToJSON} from './serialisable-to-json.interface';

/** Root cell model, also called Model */
export class Model extends CellModel implements SerialisableToJSON<Model, ModelJSON> {


constructor(public schema: number, 
			public URI: string, 
			public name: string, 
			public desc: string, 
			public presentation: string,
			public cellPresentation: string,
			public cellPresentationType: string,
			public cellPresentationMethod: string,
			public thumb: string,
			public isSimple: boolean, 
			public type_: CellType,
			public minOccurs: number,
			public valid: boolean,
			public readonly?: boolean,
			public problem?: string,
			public isAttribute?: boolean,
			public maxOccurs?: number,
			public defaultValue?: string,
			public identifier?: CellModel
			) {
	super(schema,
			URI,
			name,
			desc,
			presentation,
			cellPresentation,
			cellPresentationType,
			cellPresentationMethod,
			thumb,
			isSimple, 
			type_,
			minOccurs,
			readonly,
			isAttribute,
			maxOccurs,
			defaultValue,
			identifier);
}

/** All cell models will point to references **/
normaliseReferences() {
	this.children.forEach(cm => cm.normaliseReferencesWith(this.children));
}


/** given a cell model URI, look for it in a cell model hierarchy, avoids following references */
findCellModel(uri: string): CellModel {

	let foundCellModels = this.children.map(cm => cm.findCellModel(uri)).filter(cm => cm!=undefined);
	if (foundCellModels.length==0) {
		console.error("Incorrect cell model reference %s", uri);
	}
	return foundCellModels[0];

}


//// SerialisableToJSON ////
// check out this excellent post http://choly.ca/post/typescript-json/ to find out how to deserialize objects
toJSON(): ModelJSON {
	return Object.assign({}, this, {cellModels: this.children.map(cm => cm.toJSON()) });
}


fromJSON(json: ModelJSON): Model {

	let model = Object.create(Model.prototype);

	return Object.assign(model, json, {children: json.children.map( cm => CellModel.fromJSON(cm))});

}


static reviver(key: string, value: any): any {
	return key === "" ? (Object.create(Model.prototype)).fromJSON(value) : value;
}

//// SerialisableToJSON [end] ////

}


export interface ModelJSON extends CellModelJSON {

valid: boolean;
problem?: string;

}

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
