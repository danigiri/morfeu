// CELL-MODEL . CLASS . TS

import { BasicFamilyMember } from './basic-family-member.class';
import { Cell } from './cell.class';
import { CellType } from './cell-type.class';
import { FamilyMember } from './family-member.interface';
import { NameValue } from './utils/name-value.interface';

import { VariableParser } from './utils/variable-parser.class';

// //// COMPONENT STUFF										////
import { TreeNode } from './components/tree-node/tree-node.component';
// ////														////

export class CellModel extends BasicFamilyMember implements NameValue, TreeNode {

static readonly DEFAULT_EMPTY_VALUE = '';
// cell presentation constants
public static readonly DEFAULT_PRESENTATION_TYPE = 'IMG';
public static readonly ATTR_TEXT_PRESENTATION = 'CELL';
public static readonly ATTR_BOOLEAN_PRESENTATION = 'BOOLEAN';
public static readonly ATTR_LIST_PRESENTATION = 'LIST';
// presentation constants
public static readonly ATTR_LINK_PRESENTATION = 'VALUELOCATOR';

attributes?: CellModel[];
children: CellModel[];
index: number;
isReference: boolean;
referenceURI?: string;
parent?: FamilyMember;
canLink: boolean;
attributesCanLink: boolean;


constructor(public schema: number,
			public override URI: string,
			public override name: string,
			public desc: string,
			public presentation: string,
			public cellPresentation: string,
			public cellPresentationType: string,
			public cellPresentationMethod: string,
			public thumb: string,
			public isSimple: boolean, 
			public type_: CellType,
			public minOccurs: number,
			public areChildrenOrdered?: boolean,
			public readonly?: boolean,
			public valueLocator?: string,
			public isAttribute?: boolean,
			public maxOccurs?: number,
			public defaultValue?: string,
			public category?: string,
			public identifier?: CellModel | string
			) {

	super(URI, name);
	this.init();

}


// there are values specific to comply wit the treemodel model, we set them explicitly here 
// also compile the regexp for validity
init() {

	if (this.type_?.regex) {
		this.type_.regexCompiled = new RegExp(this.type_.regex);
	}

	this.canLink = this.presentation===CellModel.ATTR_LINK_PRESENTATION;
	this.attributesCanLink = this.attributes?.find(a => a.presentation==CellModel.ATTR_LINK_PRESENTATION)!==undefined;
	this.index = 0; // unless set by the parent
}


child(name: string): CellModel {
	return this.children?.find(c => c.name===name);
}


getAttribute(name: string): CellModel {
	return this.attributes?.find((a: CellModel) => a.name===name);
}


getAttributesByCategory(): Map<string, CellModel[]> {

	let categoryAttributes: Map<string, CellModel[]> = new Map();
	if (!this.attributes) {
		return categoryAttributes;
	}

	// create the category keys and then fill out the arrays
	this.attributes.filter(a => a.category!==undefined).forEach(a => categoryAttributes.set(a.category, []));
	this.attributes.filter(a => a.category!==undefined).forEach(a => categoryAttributes.get(a.category).push(a));

	return categoryAttributes;

}


getAttributesInCategory(c: string): CellModel[] {
	return this.attributes?.filter(a => a.category===c);
}


getCategories(): string[] {
	return Array.from(new Set(this.attributes?.filter(a => a.category!==undefined).map(c => c.category)));
}


getRawCellPresentation() {

	// TODO: handle HTML presentation
	// TODO: for proper separation of concerns, these values should be in the component and also configurable

	switch (this.cellPresentation) {
		case "DEFAULT":
			return "assets/images/cell.svg";
		case "ROW-WELL":
			return "assets/images/drag.svg";
		default:
			return this.cellPresentation;
	}

}

getCellPresentation() {

	let finalPres = this.getRawCellPresentation();

	if (finalPres.includes("$")) {
			finalPres = VariableParser.expand(finalPres, "$_NAME", this.name);
	}

	return finalPres;

}


/** we return all the possible content for presentation (not much, given models have no data) */
getCellPresentationAllContent() {
	return '_name='+this.name;
}


getCellPresentationType() {
	return this.cellPresentationType;
}


/** @returns the list of possible values this model can take, mostly useful for list-like stuff, undefined otherwise */
getPossibleValues(): string[] {
	return this.type_.possibleValues;
}


/** Mutates the cellModel so any references point to  the original cell model**/
normaliseReferencesWith(rootCellModels: CellModel[]) {

	if (this.isReference) {
		const reference: CellModel = this.findCellModelWithURI(rootCellModels, this.referenceURI);
		if (!reference) {
			console.error("Could not find cellModel of reference cellModel:%s", this.name);
		}

		// we take the philosophy of completing the cellmodel reference with the missing data (children)
		// we keep rest of the cell model information (like the name, which can be different)
		this.children = reference.children;

	} else {
		if (this.children) {
			this.children.forEach(c => c.normaliseReferencesWith(rootCellModels));
		}
		if (this.attributes) {
			this.attributes.forEach(a => a.normaliseReferencesWith(rootCellModels));
		}
	}

}


canGenerateNewCell(): boolean {
	return true;
}


/** Generate a new cell from this model, using defaults if available */
generateCell(): Cell {

	const cellURI = "/"+this.getAdoptionName()+"(0)";  // this is will be changed on adoption
	const desc = "";								   // empty description for the moment
	let newCell: Cell = new Cell(this.schema,
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


/** return a deep clone of this cell model, it includes all children and so forth */
deepClone(): CellModel {
	return CellModel.fromJSON(this.toJSON());
}


// either return this or one of the descendants, avoids following references, can return undefined 
findCellModel(uri: string): CellModel {
	return this.findCellModelWithURI(this, uri);
}


/** return true if the current value (or supplied parameter) is a valid value given our model */
validates(v: string): boolean {

	const regexp = this.type_?.regexCompiled;
	const possibleValues = this.type_?.possibleValues;
	if (!regexp && !possibleValues) {
		return true;
	}

	let isValid = true;
	if (regexp) {
		const result = regexp.exec(v);	// a single match and for the whole thing, the backend checker complains with ^$
		isValid = result?.length===1 && result[0].length===v.length;
	}
	if (isValid && possibleValues) {
		isValid = possibleValues.find(pv => pv===v)!==undefined;
	}

	return isValid;

}

//// FamilyMember ////


getAdoptionURI(): string {	// we try to work out using a reference (works for model but not for snippets)
	return !this.isReference ? this.URI : this.referenceURI;
}


getAdoptionOrder(): number {
	return this.index;
}


canAdopt(element: FamilyMember): boolean {
	return this.canAdoptMap(element).size>0;
}


canAdoptMap(element:FamilyMember): Map<string, boolean> {
	let c = new Map<string, boolean>();
	const i = this.children.findIndex(e => e.matches(element))
	if (i!==-1) {
		c.set(i.toString(), true);
	}
	return c;
}


childrenCount(): number {
	return this.children ? this.children.length : 0;
}


getParent(): FamilyMember {
	return this.parent;
}


override isCellModel(): boolean {
	return true;
}


	override asCellModel(): CellModel {
	return this;
}


//// SerialisableToJSON ////

toJSON(): CellModelJSON {	
	// we ensure that we do not serialised unwanted properties (like pointers to other structures) that do not 
	// belong to the serialised object, so we store it temporarily, delete it, restore it. The CellModelJSON type
	// does not have parent as it is only a runtime non-serialisable piece of data. We also do not serialise the
	// children of references
	const previousParent = this.parent;
	delete this["parent"];
	let serialisedCellModel: CellModelJSON = Object.assign({}, this);
	// these are set below, let's unset them to be more explicit on their values
	delete serialisedCellModel['children'];
	delete serialisedCellModel['attributes'];
	if (previousParent !== undefined && previousParent !==null) {
		this.parent = previousParent;
	}

	if (serialisedCellModel.identifier) {
		serialisedCellModel.identifier = (<CellModel>this.identifier).name;	// we serialise to the (attribute) name
	}

	if (this.attributes) {
		serialisedCellModel.attributes = this.attributes.map(a => a.toJSON());
	}
	if (this.children) {
		if (!this.isReference) {
			serialisedCellModel.children = this.children.map(c => c.toJSON());
		} else {
			serialisedCellModel.children = [];
		}
	}
	return serialisedCellModel;
}


static fromJSON(json: CellModelJSON|string): CellModel {

	if (typeof json === 'string') {

		return JSON.parse(json, CellModel.reviver);

	} else {
		let cellModel: CellModel = Object.create(CellModel.prototype);
		cellModel = Object.assign(cellModel, json); // add parsed attributes like schema, URI, name...
		cellModel.init();							// make sure we have all attributes ok

		if (json.attributes) {
			cellModel = Object.assign(cellModel,
										{attributes: json.attributes.map(a => CellModel.fromJSON(a))});
		}

		// handle the identifier if we have one defined, so we turn it into a reference to the attribute
		// we only do it when the identifier refers to a string, so if it's already been referenced we do not
		// overwrite the reference
		if (cellModel.identifier && typeof cellModel.identifier === 'string' ) {
			// this was modified in the angular15 migration
			cellModel.identifier = cellModel.attributes.find(a => a.name===<string>cellModel.identifier);
			if (cellModel.identifier==undefined) {
				console.error("Wrong identifier reference in %s", cellModel.name);
			}
		}

		if (json.children) {
			cellModel = Object.assign(cellModel, {children: json.children.map(c => {
				let cm = CellModel.fromJSON(c);
				cm.parent = cellModel;
				return cm;
			})});
			cellModel.children.forEach((c: CellModel, i: number) => c.index = i);
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

/*
// utility to debug json serialisation issues
static findCellModelJSONByName(json: CellModelJSON, name: string): CellModelJSON[] {
	let pending: CellModelJSON[] = [json];
	let found: CellModelJSON[] = [];
	while (pending.length>0) {
		const current = pending.shift();
		if (current.name === name) {
			found.push(current);
		}
		current.children.forEach(c => pending.push(c));
	}
	return found;
}
*/

private generateAttributeFrom(attribute: CellModel): Cell {

	const attrURI = "/"+this.getAdoptionName()+"(0)@"+attribute.getAdoptionName(); // be changed on adoption
	const desc = "";															  // empty description
	const value = (attribute.defaultValue) ? attribute.defaultValue : ""; 
	let newCell: Cell = new Cell(attribute.schema,
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


// given a cell model URI, look for it in a cell model hierarchy, avoids following references
private findCellModelWithURI(cellModels: CellModel[] | CellModel, uri: string): CellModel {

	let cellModel: CellModel;
	let pending: CellModel[] = [];
	if (cellModels instanceof Array) {
		cellModels.forEach(cm => pending.push(cm));
	} else {
		pending.push(cellModels);
	}

	while (!cellModel && pending.length>0) {

		const currentCellModel: CellModel = pending.pop();
		if (currentCellModel.URI==uri) {
			cellModel = currentCellModel;
		} else {
			// Only do a recursive call if current cellModel is not what we look for *and* not a reference.
			// This is to avoid infinite loops in nested structures, a nested reference to a parent
			// will necessarily be a reference cellModel, therefore do not add its children to be processed
			if (!currentCellModel.isReference && currentCellModel.children) {
				currentCellModel.children.forEach(cm => pending.push(cm));
			}
		}
	}

	return cellModel;

}


}


export interface CellModelJSON {

schema: number;
URI: string;
name: string;
desc: string;
presentation: string;
cellPresentation: string;
cellPresentationType: string;
cellPresentationMethod: string;
thumb: string;
isSimple: boolean;
isReference: boolean;
type_: CellType;
minOccurs: number;
areChildrenOrdered?: boolean;
readonly?: boolean;
valueLocator?: string,
maxOccurs?: number;
isAttribute?: boolean;
defaultValue?: string;
category?: string;
identifier?: string|CellModel;	// coming from the JSON it will be a string, coming from an object it will
								// be an reference to the attribute that is the identifier
attributes?: CellModelJSON[];
children?: CellModelJSON[];
referenceURI?: string;

}

/*
 *	  Copyright 2024 Daniel Giribet
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
