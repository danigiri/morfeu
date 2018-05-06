/*
 *	  Copyright 2018 Daniel Giribet
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


import { Cell } from "./cell.class";
import { FamilyMember } from "./family-member.interface";
import { NameValue } from "./name-value.interface";
import { CellType } from "./cell-type.class";

// //// COMPONENT STUFF										////
// //// PRESENT HERE DUE TO LIMITATIONS IN TREE COMPONENT	////
import { CellModelComponent } from "./cell-model.component";
// ////														////

export class CellModel implements NameValue, FamilyMember {

static readonly DEFAULT_EMPTY_VALUE = "";
	
id: string;
isExpanded: boolean;

attributes?: CellModel[];
children: CellModel[];
isReference: boolean;
referenceURI?: string;

// //// COMPONENT STUFF										////
// to circumvent limitations of the angular tree, we establish a relationship with the cell model component //
component: CellModelComponent;
// ////														////

constructor(public schema: number, 
			public URI: string, 
			public name: string, 
			public desc: string, 
			public presentation: string,
			public cellPresentation: string,
			public thumb: string,
			public isSimple: boolean, 
			public type_: CellType,
			public minOccurs: number,
			public isAttribute?: boolean,
			public maxOccurs?: number,
			public defaultValue?: string,
			public identifier?: CellModel
			) {
	this.init();
}	 


setComponent(c: CellModelComponent) {
	this.component = c;
}

// there are values specific to comply wit the treemodel model, we set them explicitly here 
init() {

	this.id = this.URI;	 // this is guaranteed to be unique 
	this.isExpanded = true;

}


getURI():string {
	return this.URI;
}


getAdoptionName():string {
	return this.name;
}


getAdoptionURI():string {
	return this.URI;
}


matches(e:FamilyMember):boolean {
	return this.getAdoptionName()==e.getAdoptionName() && this.getAdoptionURI()==e.getAdoptionURI();
}


canAdopt(element:FamilyMember):boolean {
	return this.children.some(c => c.matches(element));
}


childrenCount():number {
	return this.children ? this.children.length : 0;
}


equals(m:FamilyMember) {
	return this.getURI()==m.getURI();
}


getParent():FamilyMember {
	return undefined;	//TODO: we do not need to setup the parent yet
}


getRawPresentation() {
	return (this.cellPresentation=='DEFAULT') ? "assets/images/cell.svg" : this.cellPresentation;
}


getPresentation() {
	
	let effectivePresentation = this.getRawPresentation();
	
	if (effectivePresentation.includes("$")) {
		if (effectivePresentation.includes("$ATTRIBUTES")) {	// basic preview: attributes as parameters
			let attribs = this.attributes ? this.attributes.map(a => a.name+"="+a.type_.name).join("&") : "";
			attribs = "_name="+ this.name+"&"+attribs;	 // adding the name at the beginning
			effectivePresentation = effectivePresentation.replace("$ATTRIBUTES", attribs);
		}
	}
	
	return effectivePresentation;

}


canGenerateNewCell(): boolean {
	// not dragging WELL-type cell models for the moment, as it is quite a complex use case, so we only allow
	// cell models that do not have any children
	return this.childrenCount()==0;
}


/** Generate a new cell from this model, using defaults if available */
generateCell():Cell {
	
	let cellURI = "/"+this.getAdoptionName()+"(0)";	   // this is will be changed on adoption
	let desc = "";									// empty description for the moment
	let newCell:Cell = new Cell(this.schema, 
								cellURI, 
								this.getAdoptionName(), 
								desc,
								this.getAdoptionURI(), 
								this.isSimple);
	if (this.defaultValue) {
		newCell.value = this.defaultValue;
	}
	
	newCell.cellModel = this;						// we associate the cell model straightaway, easy peasy =)
 
	if (this.attributes) {							// now we set the attributes when we have defaults
		newCell.attributes = this.attributes.filter(a => a.defaultValue)
												.map(a => this.generateAttributeFrom(a));
	}
	
	return newCell;
}


//// SerialisableToJSON ////

toJSON(): CellModelJSON {

	let serialisedCellModel:CellModelJSON = Object.assign({}, this);

	if (serialisedCellModel.identifier) {
		serialisedCellModel.identifier = this.identifier.name;	// we serialise to the (attribute) name
	}

	if (this.attributes) {
		serialisedCellModel.attributes = this.attributes.map(a => a.toJSON());
	}
	if (this.children) {
		serialisedCellModel.children = this.children.map(c => c.toJSON());
	}
	
	return serialisedCellModel;
	
}


static fromJSON(json: CellModelJSON|string): CellModel {

	if (typeof json === 'string') {

		return JSON.parse(json, CellModel.reviver);

	} else {

		let cellModel = Object.create(CellModel.prototype);
		cellModel = Object.assign(cellModel, json); // add parsed attributes like schema, URI, name...
		cellModel.init();							// make sure we have all attributes ok

		if (json.attributes) {
			cellModel = Object.assign(cellModel, 
									  {attributes: json.attributes.map(a => CellModel.fromJSON(a))});
		}

		// handle the identifier if we have one defined, so we turn it into a reference to the attribute
		if (cellModel.identifier) {
			cellModel.identifier = cellModel.attributes.find(a => a.name==cellModel.identifier);
			if (cellModel.identifier==undefined) {
				console.error("Wrong identifier reference in %s", cellModel.name);
			}
		}
		
		if (json.children) {
			cellModel = Object.assign(cellModel, 
									  {children: json.children.map(c => CellModel.fromJSON(c))});
		} else {
			cellModel = Object.assign(cellModel, {children: []});  // empty as the Tree class requires it
		}
	
		return cellModel;

	}
}


static reviver(key: string, value: any): any {
	return key === "" ? CellModel.fromJSON(value) : value;
}

//// SerialisableToJSON [end] ////


private generateAttributeFrom(attribute: CellModel): Cell {
	
	let attrURI = "/"+this.getAdoptionName()+"(0)@"+attribute.getAdoptionName(); //	 be changed on adoption
	let desc = "";									// empty description for the moment
	let value = (attribute.defaultValue) ? attribute.defaultValue : ""; 
	let newCell:Cell = new Cell(attribute.schema, 
								attrURI, 
								attribute.getAdoptionName(), 
								desc,
								attribute.getAdoptionURI(), 
								attribute.isSimple);	// should always be true

	if (attribute.defaultValue) {				   // sanity check, as we only generate		   
		newCell.value = attribute.defaultValue;	   // attributes for defaults for now
	}
	
	newCell.cellModel = attribute; // associate the cell model straightaway, yo! =)
 
	return newCell;

}

}


export interface CellModelJSON {
	
schema: number; 
URI: string;
name: string; 
desc: string;
presentation: string;
cellPresentation: string,
thumb: string;
isSimple: boolean; 
isReference: boolean;
type_: CellType;
minOccurs: number;
maxOccurs?: number;
isAttribute?: boolean;
defaultValue?: string;
identifier?: string | CellModel;	// coming from the JSON it will be a string, coming from an object it will
									// be an reference to the attribute that is the identifier
attributes?: CellModelJSON[];
children?: CellModelJSON[];
referenceURI?: string;

}


