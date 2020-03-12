// CELL . CLASS . TS

import { Adopter } from './adopter.interface';
import { Lifecycle } from './lifecycle.interface';
import { FamilyMember } from './family-member.interface';
import { CellModel } from './cell-model.class';
import { Model } from './model.class';
import { NameValue } from './name-value.interface';

import { VariableParser } from './variable-parser.class';
import { SerialisableToJSON } from './serialisable-to-json.interface';


export class Cell implements NameValue, Adopter, Lifecycle, SerialisableToJSON<Cell, CellJSON> {

private static readonly VALUE_FIELD = 'value';
private static readonly _NAME = "$_NAME";
private static readonly _VALUE = "$_VALUE";
private static readonly _ATTRIBUTES = "$_ATTRIBUTES";

value?: string;
attributes?: Cell[];
internalAttributes?: Cell[];
children?: Cell[];
cellModel?: CellModel;
parent?: Adopter;
position?: number;


constructor(public schema: number,
			public URI: string,
			public name: string,
			public desc: string,
			public cellModelURI: string,
			public isSimple: boolean) {}


/** We associate this cell with the given model, optionally specifying a deep uri within the model */
associateWith(model: Model, uri?: string): Cell {

	if (uri) {
		this.associateWith_(model.children, [model.findCellModel(uri)]);	// deep uri within the model
	} else {
		this.associateWith_(model.children, model.children);	// we start at the root of the model
	}

	return this;

}


// remove prefix (context) from this cell, its attributes and children
stripPrefixFromURIs(prefix: string) {

	if (this.getURI().startsWith(prefix)) {
		this.URI = this.URI.substr(prefix.length);
		if (this.attributes) {
			this.attributes = this.attributes.map(a => a.stripPrefixFromURIs(prefix));
		}
		if (this.children) {
			this.children = this.children.map(c => c.stripPrefixFromURIs(prefix));
		}
	}

	return this;

}


/** get the attribute named this way, or return undefined if no such attribute is present */
attribute(name: string): string {

	let value: string;
	if (this.attributes) {
		const attribute:Cell = this.attributes.find(a => a.name===name);
		if (attribute) {
			value = attribute.value;
		}
	}

	return value;

}


/** we look for an attribute that has representation of COL-FIELD and return its value (1 as default) */
columnFieldValue(): string {

	let value = "1";
	if (this.attributes) {
		let attribute: Cell = this.attributes.find(a => a.cellModel.presentation=="COL-FIELD");
		if (attribute) {
			value = attribute.value;
		}
	}

	return value;

}


/** find a cell that has this URI or return undefined */
findCellWithURI(uri: string): Cell {

	let cell: Cell;
	let pending: Cell[] = [this];

	while (!cell && pending.length>0) {
		const currentCell = pending.pop();
		if (currentCell.getURI()===uri) {
			cell = currentCell;
		} else if (currentCell.childrenCount()>0) {
			currentCell.children.forEach(c => pending.push(c));
		}
	}

	return cell;

}


/** set ourselves at this position, uses information from the parent but does not mutate the parent */
setPosition(position: number): Cell {

	// This is tricky, imagine this cases
	// /foo(0)/bar(0), bar(0) position:1 --> /foo(0)/bar(1), easy peasy
	// But what about:
	// /foo(0)/bar(0), now we get bar(0) to position:1
	// /foo(0)/bar(0)/geez(0)
	// /foo(0)/bar(0)/geez(1)
	// This means we end up with
	// /foo(0)/bar(1)
	// /foo(0)/bar(1)/geez(0)
	// /foo(0)/bar(1)/geez(1)
	// Neat, uh?

	const oldPrefix = this.parent.getURI()+"/"+this.name+"("+this.position;
	const newPrefix = this.parent.getURI()+"/"+this.name+"("+position;

	this.URI = newPrefix+")";
	this.position = position;
	if (this.attributes) {
		this.attributes = this.attributes.map(c => {
			// c.URI = c.URI.substr(0,	 c.URI.lastIndexOf("@"));
			c.URI = this.URI+"@"+c.name;
			return c;
		});
	}
	// now what we need to do, is replace the old prefix of the URI with the new one
	if (this.children) {
		this.children = this.children.map(c => c.replaceURIPrefix_(oldPrefix, newPrefix));
	}

	return this;

}


replaceWith(replacement: Cell) {} // PLEASE IMPLEMENT


/** return a deep clone of this cell, it includes all children plus runtime information (parent ref, ...) */
deepClone(): Cell {

	const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
	let clone = CELL.fromJSON(this.toJSON()); // easy peasy cloning :)

	// let's not forget the runtime information
	clone = this.cloneRuntimeInformationInto(clone, this.parent);

	return clone;

}


// no value for this cell
removeValue() {
	delete this[Cell.VALUE_FIELD];
}


// create a new value for this cell, using the cellmodel default value or empty
createValue() {
	this.value = this.cellModel.defaultValue ? this.cellModel.defaultValue : CellModel.DEFAULT_EMPTY_VALUE;
}


//
equalValues(c: Cell): boolean {

	if (this.value!==c.value) {
		return false;
	}

	if (this.cellModel.URI!==c.cellModel.URI) {
		return false;
	}
	
	if ((this.attributes && !c.attributes) || (!this.attributes && c.attributes)) {
		return false;
	}
	
	// we are assuming the same order for the attributes and internal attributes
	if (this.attributes && c.attributes && !this.cellsEqualValues(this.attributes, c.attributes)) {
			return false;
	}


	if ((this.internalAttributes && !c.internalAttributes) || (!this.internalAttributes && c.internalAttributes)) {
		return false;
	}
	if (this.internalAttributes && c.internalAttributes && 
		!this.cellsEqualValues(this.internalAttributes, c.internalAttributes)) {
			return false;
	}

	return true;

}


private cellsEqualValues(a: Cell[], b: Cell[]) {

	if (a.length!=b.length) {
			return false;
	}
	for (let i=0; i<a.length; i++) {
		if (!a[i].equalValues(b[i])) {
			return false;
		}
	}

	return true;

}


// assign runtime information to the 'clone', including parenthood, position, cellmodel, and then recursively
private cloneRuntimeInformationInto(clone: Cell, parent: Adopter): Cell {

	if (parent) {
		clone.parent = parent;
	}

	if (this.position) {
		clone.position = this. position;
	}
	if (this.cellModel) {
		clone.cellModel = this. cellModel;
	}

	// for the attributes, we go through them and we assign the runtime information to the clone's attributes
	if (this.attributes) {
		this.attributes.forEach((a, i) => a.cloneRuntimeInformationInto(clone.attributes[i], clone));
	}
	// for the children we do the same, also keeping the parent
	if (this.children) {
		this.children.forEach((c, i) => c.cloneRuntimeInformationInto(clone.children[i], clone));
	}

	return clone;

}


// replaces the prefix of the URI with a new one, recursively
private replaceURIPrefix_(old: string, newPrefix: string): Cell {

	this.URI = this.URI.replace(old, newPrefix);
	if (this.children) {
		this.children = this.children.map(c => c.replaceURIPrefix_(old, newPrefix));
	}

	return this;

}


private associateWith_(rootCellmodels: CellModel[], cellModels: CellModel[]): Cell {

	let cellModel:CellModel = undefined;
	if (cellModels) {

		cellModel = cellModels.find(cm => cm.URI===this.cellModelURI);	// current cell model level

		//TODO: handle inconsistent cell that cannot find cellmodule even though the content is valid
// 		  if (!cellModel) {												  // cell model children maybe?
// 			  cellModel = cellModels.map(cm => this.associateWith_(cm.children)).find(cm => cm!=undefined);
// 		  }

		if (cellModel) {												// now attributes and cell children
			if (this.attributes) {
				this.attributes = this.attributes.map(a => a.associateWith_(rootCellmodels, cellModel.attributes));
			}
			// notice we do not associate the internal attributes as there is no model for them
			if (this.children) {
				this.children = this.children.map(c => c.associateWith_(rootCellmodels, cellModel.children));
			}
		} else {
			console.error();
		}

	this.cellModel = cellModel;

	}

	return this;

}


/** return presentation with the variable substitutions in the presentation URL for dynamic preview */
getPresentation(): string {

	let finalPres = this.cellModel.getRawPresentation();
	if (finalPres.includes("$")) {
		finalPres = this.replacePresentationVariables(finalPres);
	}

	return finalPres;

}

/** return presentation with all variables included for dynamic preview */
getPresentationAllContent(): string {

	const template = '_name='+Cell._NAME+'&_value='+Cell._VALUE+'&'+Cell._ATTRIBUTES;

	return this.replacePresentationVariables(template);

}


private replacePresentationVariables(input: string): string {
	let output = input;
	// expand special variables, like name, value and the attributes as GET params
	output = VariableParser.expand(output, Cell._NAME , this.name);
	output = VariableParser.expand(output, Cell._VALUE, this.value);
	output = VariableParser.expand(output, Cell._ATTRIBUTES, this.attributes);

	// the rest will be cell attributes as ${var}
	output = VariableParser.expandVariables(output, this.attributes);

	return output;

}

//// Adopter ////

adopt(orphan: Cell, position?: number) {


	if (orphan.cellModel.isAttribute) {
		this.attributes.push(orphan);
	} else {

		// notice that we are adopting only orphan cells as we do not want this method to have side effects on
		// the old parent (otherwise it's a non-intuitive method call that alters state of the orphan, this cell
		// and the old parent, this last change would be non-intuitive), therefore we only accept orphans
		if (orphan.parent) {
			console.error("Adopting cell that was not an orphan!!");
		}
		orphan.parent = this;
		if (position===undefined) {
			console.error("Adopting child without a position!!!");
		}

		orphan.setPosition(position);	// this actually changes the URI fo the new member to the correct one
										//       DOUBLE CHECK DOUBLE CHECK DOUBLE CHECK DOUBLE CHECK DOUBLE CHECK
										// TODO: DOUBLE CHECK FOR SNIPPET ADOPTION AS THERE IS NO CONTEXT
										//       DOUBLE CHECK DOUBLE CHECK DOUBLE CHECK DOUBLE CHECK DOUBLE CHECK

		if (!this.children) {
			this.children = [ orphan ];
		} else if (this.children.length <= position) { //> //> // works for empty list and also append at the end
			this.children.push(orphan);
		} else {

			let newChildren: Cell[] = [];
			let i = 0;
			this.children.forEach(c => {
				if (i<position) {
					newChildren.push(c);
				} else if (i===position) {
					newChildren.push(orphan);
					i++;
					newChildren.push(c.setPosition(i));	   // set next to a a shifted position of +1
				} else {
					newChildren.push(c.setPosition(i));	   // set the rest of children
				}
				i++;
			});
			this.children = newChildren;

		}
	}

}


remove(child: Cell) {

	if (child.cellModel.isAttribute) {

		this.attributes =  this.attributes.filter( a => a.getURI()!==child.getURI());

	} else {	// assuming child
		const position = child.position;
		let newChildren: Cell[] = [];
		let i = 0;
		this.children.forEach(c => {
			if (i<position) { //>
					newChildren.push(c);
			} else if (i>position) {
				newChildren.push(c.setPosition(i-1));	 // set the following elems to a shifted -1 position
			}
			i++;
		});
		this.children = newChildren;

	}

}


// FamilyMember ////

getURI(): string {
	return this.URI;
}


getAdoptionName(): string {
	return this.name;
}


getAdoptionURI(): string {
	return this.cellModel.getAdoptionURI();	// it could be that our model is a reference so we need the adoption uri
}


matches(e: FamilyMember): boolean {
	return this.getAdoptionName()===e.getAdoptionName() && this.getAdoptionURI()===e.getAdoptionURI();
}


// FIXME: need to check that we are not moving the same cell around in the same col (for instance change order)
canAdopt(newMember: FamilyMember): boolean {

	// we will do all checks one by one and return to optimise speed
	
	// we check the model compatibility first
	if (!this.cellModel.canAdopt(newMember)) {
		return false;
	}

	// next we check that if we are a lone cell in a droppable parent, we cannot drop to end up in the same
	// place, example:
	//	<col>
	//		[drop area 0]
	//		<thingie/>
	//		[drop area 1]
	//	</col>
	// in this case, <thingie/> does not make sense to activate drop areas 0 and 1 as cell ends up the same

	if (this.children && this.children.length==1 && this.parent && this.equals(newMember.getParent())) {
		return false;
	}

	// next, we check if we have more than one element but we are in the same droppable parent which means
	// that we can actually reorder stuff around, as we will not be modifying counts, then we allow drops
	//	<col>
	//		[drop area 0]
	//		<thingie/>
	//		[drop area 1]  // TODO: if we want to move the first thingie, areas 0 and 1 are not needed :)
	//		<thingie/>
	//		[drop area 2]
	//	</col>
	if (this.children && this.parent && this.equals(newMember.getParent())) {
		return true;
	}

	// next, we check the allowed count (if we have no children we assume zero so we should be able to add)
	let matchingChildren: Cell[] = this.children ? this.children.filter(c => c.matches(newMember)) : [];
	const childCount = matchingChildren.length;
	if (childCount>0) {
		// we are not considering the problem of the childcount being less than the minimum
		//TODO: add check: are we able to remove this cell as child?
		let matchingCellModel:CellModel = matchingChildren[0].cellModel;
		if (matchingCellModel.maxOccurs && childCount >= matchingCellModel.maxOccurs) { // notice we use '>=' as we are adding one more
			return false;
		}
	}

	return true;	// apologies for the long method (mostly comments ^^')

}


childrenCount(): number {
	return this.children ? this.children.length : 0;
}


getParent(): FamilyMember {
	return this.parent;
}


equals(m: FamilyMember) {
	return m && this.getURI()===m.getURI();	// FIXME: at the beginning, if m is a model, it is undefined
}


//// Lifecycle ////

delete() {

	if (this.parent) {	// sanity check
		this.parent.remove(this);
	}

}


//// SerialisableToJSON ////

toJSON(): CellJSON {

	let serialisedCell: CellJSON = Object.assign({}, this);

	// we ensure that we do not serialised unwanted properties (like pointers to other structurea) that do not 
	// belong to the serialised object
	delete serialisedCell["cellModel"];
	delete serialisedCell["parent"];

	// TODO: add sanity checks for reference to avoid future infinite loops 
	if (this.attributes) {
		serialisedCell.attributes = this.attributes.map(a => a.toJSON());
	}
	if (this.children) {
		serialisedCell.children = this.children.map(c => c.toJSON());
	}


	return serialisedCell;

}


fromJSON(json: CellJSON | string): Cell {

	if (typeof json === 'string') {

		return JSON.parse(json, Cell.reviver);

	}

	const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
	let cell: Cell = Object.create(Cell.prototype);
	cell = Object.assign(cell, json);

	if (json.attributes) {
		cell = Object.assign(cell, {attributes: json.attributes.map(a => CELL.fromJSON(a))});
	}
	if (json.internalAttributes) {
		cell = Object.assign(cell, {internalAttributes: json.internalAttributes.map(a => CELL.fromJSON(a))});
	}

	// we complete the children runtime information so we have the parent reference as well as position
	if (json.children) {
		let i = 0;
		cell = Object.assign(cell, {children: json.children.map(c => {
			let fullCell: Cell = CELL.fromJSON(c);
			fullCell.position = i++;
			fullCell.parent = cell;
			return fullCell;
		})});
	}

	return cell;

}


static reviver(key: string, value: any): any {

	let CELL:Cell = Object.create(Cell.prototype); // to simulate static call

	return key === "" ? CELL.fromJSON(value) : value;

}

//// SerialisableToJSON [end] ////

}

export interface CellJSON {

schema: number;
URI: string;
name: string;
desc: string;
cellModelURI: string;
isSimple: boolean;

value?: string;
attributes?: CellJSON[];
internalAttributes?: CellJSON[];
children?: CellJSON[];

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