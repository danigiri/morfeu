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

import { Adopter } from './adopter.interface';

import { Cell, CellJSON } from './cell.class';
import { FamilyMember } from './family-member.interface';
import { Model } from './model.class';

import { SerialisableToJSON } from './serialisable-to-json.interface';


export class Content implements Adopter, SerialisableToJSON<Content, ContentJSON> {
	
children?:Cell[];

constructor(public schema: number) {}


// associate the content cells with the corresponding cell models
associateWith(model: Model) {
   this.children = this.children.map(c => c.associateWith(model));
}


//// FamilyMember ////

getURI(): string {
	return this.getAdoptionURI();
}


// part of the drag and drop scaffolding, we return true if this cell can be one of the root cells
getAdoptionName(): string {
	return "";
}


getAdoptionURI(): string {
	return "/";
}


matches(element: FamilyMember):boolean {
	return false;	// content does not match with anything
}


canAdopt(newMember: FamilyMember):boolean {
	return this.children.some(c => c.canAdopt(newMember));
}

childrenCount():number {
	return this.children ? this.children.length : 0;
}


equals(m:FamilyMember) {
	return this.getURI()==m.getURI();
}


getParent():FamilyMember {
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

	if (typeof json === 'string') {

		return JSON.parse(json, Content.reviver);

	} else {
	    
		let i:number = 0;
		let content = Object.create(Content.prototype);
		content = Object.assign(content, json);
		content = Object.assign(content, {children: json.children.map(c => {
			let fullCell:Cell = Object.create(Cell.prototype).fromJSON(c);
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

schema:number;
children:CellJSON[];
	
}
