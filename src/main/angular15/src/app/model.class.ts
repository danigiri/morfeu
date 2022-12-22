// MODEL . CLASS . TS

import { CellType } from './cell-type.class';
import { CellModel, CellModelJSON } from './cell-model.class';
import { SerialisableToJSON } from './serialisable-to-json.interface';

/** Root cell model, also called Model */
export class Model extends CellModel implements SerialisableToJSON<Model, ModelJSON> {


constructor(public override schema: number, 
			public override URI: string, 
			public override name: string, 
			public override desc: string, 
			public override presentation: string,
			public override cellPresentation: string,
			public override cellPresentationType: string,
			public override cellPresentationMethod: string,
			public override thumb: string,
			public override isSimple: boolean, 
			public override type_: CellType,
			public override minOccurs: number,
			public valid: boolean,
			public override areChildrenOrdered?: boolean,
			public override readonly?: boolean,
			public override valueLocator?: string,
			public problem?: string,
			public override isAttribute?: boolean,
			public override maxOccurs?: number,
			public override defaultValue?: string,
			public override category?: string,
			public override identifier?: CellModel
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
			areChildrenOrdered,
			readonly,
			valueLocator,
			isAttribute,
			maxOccurs,
			defaultValue,
			category,
			identifier);
}

/** All cell models will point to references **/
normaliseReferences() {
	this.children.forEach(cm => cm.normaliseReferencesWith(this.children));
}


/** given a cell model URI, look for it in a cell model hierarchy, avoids following references */
	override findCellModel(uri: string): CellModel {

	if (this.URI===uri) {
		return this;
	}

	let foundCellModels = this.children.map(cm => cm.findCellModel(uri)).filter(cm => cm!=undefined);
	if (foundCellModels.length==0) {
		console.error("Incorrect cell model reference %s", uri);
	}
	return foundCellModels[0];

}


//// SerialisableToJSON ////
// check out this excellent post http://choly.ca/post/typescript-json/ to find out how to deserialize objects
	override toJSON(): ModelJSON {
	return Object.assign({}, this, {cellModels: this.children.map(cm => cm.toJSON())});
}


fromJSON(json: ModelJSON|string): Model {

	if (typeof json === 'string') {

		return JSON.parse(json, Model.reviver);

	}

	let model = Object.create(Model.prototype);

	return Object.assign(model, json, {children: json.children.map( c => {
		let cm = CellModel.fromJSON(c);
		cm.parent = model;
		return cm;
	})});

}


static override reviver(key: string, value: any): any {
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
