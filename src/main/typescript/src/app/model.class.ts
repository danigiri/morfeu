// MODEL . CLASS . TS

import { FamilyMember } from './family-member.interface';
import { Cell } from './cell.class';
import { CellModel, CellModelJSON } from './cell-model.class';
import { SerialisableToJSON } from './serialisable-to-json.interface';

export class Model implements FamilyMember, SerialisableToJSON<Model, ModelJSON> {

public cellModels: CellModel[];


constructor(public schema: number, 
			public URI:string,
			public name: string, 
			public desc: string, 
			public valid: boolean) {
	this.cellModels = [];
}


/** All cell models will point to references **/
normaliseReferences() {
    this.cellModels.forEach(cm => cm.normaliseReferencesWith(this.cellModels));
}


//// FamilyMember ////

getURI():string {
	return this.URI;
}


getAdoptionName():string {
	return this.name;
}


getAdoptionURI():string {
	return this.URI;
}


matches(element:FamilyMember):boolean {
	return false;
}


canAdopt(element: FamilyMember): boolean {
	return this.cellModels.some(c => c.matches(element));	
}


childrenCount(): number {
	return this.cellModels.length;
}


getParent(): FamilyMember {
	return undefined;
}


equals(m: FamilyMember) {
	return this.getURI()==m.getURI();
}


// given a cell model URI, look for it in a cell model hierarchy, avoids following references
findCellModel(uri: string): CellModel {
    let foundCellModels = this.cellModels.map(cm => cm.findCellModel(uri)).filter(cm => cm!=undefined);
    if (foundCellModels.length==0) {
        console.error("Incorrect cell model reference %s", uri);
    }
    return foundCellModels[0];
}


adopt(newMember:Cell, position:number) {}


//// SerialisableToJSON ////
// check out this excellent post http://choly.ca/post/typescript-json/ to find out how to deserialize objects
toJSON(): ModelJSON {
	return Object.assign({}, this, {cellModels: this.cellModels.map(cm =>cm.toJSON()) });
}


fromJSON(json: ModelJSON|string): Model {

	if (typeof json === 'string') {

		return JSON.parse(json, Model.reviver);

	} else {

		let model = Object.create(Model.prototype);

		return Object.assign(model, json, {cellModels: json.cellModels.map( cm => CellModel.fromJSON(cm))});

	}

}


static reviver(key: string, value: any): any {
	return key === "" ? (Object.create(Model.prototype)).fromJSON(value) : value;
}

}


export interface ModelJSON {

schema: number;
URI: string;
name: string;
desc: string;
valid: boolean;
cellModels: CellModelJSON[];

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
