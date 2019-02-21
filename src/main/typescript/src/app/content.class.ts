// CONTENT . CLASS . TS

import { Adopter } from "./adopter.interface";

import { Cell, CellJSON } from "./cell.class";
import { FamilyMember } from "./family-member.interface";
import { Model } from "./model.class";

import { SerialisableToJSON } from "./serialisable-to-json.interface";


export class Content implements Adopter, SerialisableToJSON<Content, ContentJSON> {
	
children?: Cell[];
adoptionName: string = "";
adoptionURI: string = "/";


constructor(public schema: number) {}


// associate the content cells with the corresponding cell models, starting from the root
associateFromRoot(model: Model) {
	this.children = this.children.map(c => c.associateWith(model));
}


//associate the content cells with the corresponding cell models, starting from the cell model uri of each
associate(model: Model) {
	this.children = this.children.map(c => c.associateWith(model, c.cellModelURI));
}


// mutate the content so this prefix is stripped from all content URIs, useful for snippet documents
stripPrefixFromURIs(prefix: string) {

	//if (this.getURI().startsWith(prefix)) {   // not used until we add uri support to content
	this.children = this.children.map(c => c.stripPrefixFromURIs(prefix));
	//}

	return this;

}


/** Create a content instance  from a cell fragment, useful to edit bits of the content */
static fromCell(cell: Cell): Content {

	let contentFragment = new Content(0);
	contentFragment.children = [cell];
	contentFragment.adoptionName = cell.getAdoptionName();
	contentFragment.adoptionURI = cell.getAdoptionURI();

	return contentFragment;

}


//// FamilyMember ////

getURI(): string {
	return this.getAdoptionURI();
}


// part of the drag and drop scaffolding, we return true if this cell can be one of the root cells
getAdoptionName(): string {
	return this.adoptionName;
}


getAdoptionURI(): string {
	return this.adoptionURI;
}


matches(element: FamilyMember): boolean {
	return false;	// content does not match with anything
}


canAdopt(newMember: FamilyMember): boolean {
	return this.children.some(c => c.canAdopt(newMember));
}


childrenCount():number {
	return this.children ? this.children.length : 0;
}


equals(m: FamilyMember) {
	return this.getURI()===m.getURI();
}


getParent(): FamilyMember {
	return undefined;
}


//// Adopter ////

adopt(orphan:Cell, position:number) {
	//TODO: to be implemented
}


remove(child:Cell) {
	//TODO: to be implemented
}


//// SerialisableToJSON ////

toJSON(): ContentJSON {
	return Object.assign({}, this, {children: this.children.map(c => c.toJSON())});
}


fromJSON(json: ContentJSON|string): Content {

	if (typeof json === "string") {

		return JSON.parse(json, Content.reviver);

	} else {

		let i = 0;
		let content = Object.create(Content.prototype);
		content = Object.assign(content, json);
		content = Object.assign(content, {children: json.children.map(c => {
			let fullCell: Cell = Object.create(Cell.prototype).fromJSON(c);
				fullCell.position = i++;
				fullCell.parent = content;
				return fullCell;
			})});

		return content;

	}

}


static reviver(key: string, value: any): any {
	return key === "" ? Object.create(Content.prototype).fromJSON(value) : value;
}

}


// serialisable interface
export interface ContentJSON {

schema: number;
children: CellJSON[];

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
