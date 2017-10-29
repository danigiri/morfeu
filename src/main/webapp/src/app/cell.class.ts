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
import { FamilyMember } from './family-member.interface';
import { CellModel } from './cell-model.class';
import { Model } from './model.class';


export class Cell implements Adopter {

attributes?: Cell[];
children?: Cell[];
cellModel?: CellModel;
parent?: Adopter;
position?: number;

constructor(public schema: number,
			public URI: string,
			public name: string,
			public desc: string,
			public value: string,
			public cellModelURI: string,
			public isSimple: boolean) {}
	

associateWith(model: Model):Cell {

	this.associateWith_(model.cellModels, model.cellModels);
	
	return this;
}


attribute(name:string):string {

	let value:string;
	if (this.attributes) {
		let attribute:Cell = this.attributes.find(a => a.name==name);
		if (attribute) {
			value = attribute.value;
		}
	}
	
	return value;

}


// we look for a field that has representation of COL-FIELD or 1 as default
columnFieldValue():string {

	let value: string = "1";
	if (this.attributes) { 
		let attribute:Cell = this.attributes.find(a => a.cellModel.presentation=="COL-FIELD");
		if (attribute) {
			value = attribute.value;
		}
	}
	
	return value;
	
}


getURI():string {
    return this.URI;
}


getAdoptionName():string {
    return this.name;
}


getAdoptionURI():string {
    return this.cellModelURI;
}


matches(e:FamilyMember):boolean {
   return this.getAdoptionName()==e.getAdoptionName() && this.getAdoptionURI()==e.getAdoptionURI();
}


// FIXME: need to check that we are not moving the same cell around in the same col (for instance change order)
canAdopt(newMember:FamilyMember):boolean {

    // we will do all checks one by one and return to optimise speed
    
    // we check the model compatibility first
    if (!this.cellModel.canAdopt(newMember)) {
        return false;
        
    }
    
    // next we check that if we are a lone cell in a droppable parent, we cannot drop to end up in the same
    // place, example:
    //  <col>
    //      [drop area 0]
    //      <thingie/>
    //      [drop area 1]
    //  </col>
    // in this case, <thingie/> does not make sense to activate drop areas 0 and 1 as cell ends up the same

    if (this.children && this.children.length==1 && this.parent && this.equals(newMember.getParent())) {
        return false;
    }
    
    // next, we check if we have more than one element but we are in the same droppable parent which means
    // that we can actually reorder stuff around, as we will not be modifying counts, then we allow drops
    //  <col>
    //      [drop area 0]
    //      <thingie/>
    //      [drop area 1]
    //      <thingie/>
    //      [drop area 2]
    //  </col>
    if (this.children && this.parent && this.equals(newMember.getParent())) {
        return true;
    }
    
    // next, we check the allowed count
    let matchingChildren:Cell[] = this.children.filter(c => c.matches(newMember));
    let childCount:number = matchingChildren.length;
    if (childCount>0) {
        // we are not considering the problem of the childcount being less than the minimum
        //TODO: add are we able to remove this cell as child?
        let matchingCellModel:CellModel = matchingChildren[0].cellModel;
        if (matchingCellModel.maxOccurs && childCount >= matchingCellModel.maxOccurs) { // notice we use '>=' as we are adding one more
            return false;
        }
    }
        
    console.log("true");
    return true;    // apologies for the long method

}


childrenCount():number {
    return this.children ? this.children.length : 0;   
}


getParent():FamilyMember {
    return this.parent;
}

equals(m:FamilyMember) {
    return m && this.getURI()==m.getURI();  // FIXME: at the beginning, if m is a model, it is undefined
}


adopt(newMember:Cell, position:number) {
    
    if (newMember.parent) {
        newMember.parent.removeChild(newMember);
    }
    
    newMember.parent = this;
    newMember.setPosition(position);
    
    if (!this.children) {
        this.children = [newMember];
    } else if (this.children.length<=position) { //> //> // works for empty list and also append at the end
        this.children.push(newMember);
    } else {
    
        let newChildren:Cell[] = [];
        let i:number = 0;
        this.children.forEach(c => {
            if (i<position) { //>
                newChildren.push(c);
            } else if (i==position) {
                newChildren.push(newMember);
                i++;
                newChildren.push(c.setPosition(i));    // set next to a a shifted position of +1
            } else {
                newChildren.push(c.setPosition(i));    // set the rest of children
            }
            i++;
        });
        this.children = newChildren;

    }

}


removeChild(child:Cell) {

    let position:number = child.position;
    let newChildren:Cell[] = [];
    let i:number = 0;
    this.children.forEach(c => {
        if (i<position) { //>
            newChildren.push(c);
        } else if (i>position) {
            newChildren.push(c.setPosition(i-1));    // set the following children to a shifted -1 position
        }
        i++;
    });       
    this.children = newChildren;
 
}
     

setPosition(position:number):Cell {

    this.position = position;
    this.URI = this.parent.getURI()+"/"+this.name+"("+position+")";
    if (this.attributes) {
        this.attributes = this.attributes.map(c => {
            c.URI = c.URI.substr(0,  c.URI.lastIndexOf("@"));
            c.URI = c.URI+"@"+c.name;
            return c;
        });
    }
    if (this.children) {
        this.children = this.children.map(c => c.setPosition(position));
    }
      
    return this;

}
                    

private associateWith_(rootCellmodels:CellModel[], cellModels:CellModel[]):Cell {

	let cellModel:CellModel = undefined;
	if (cellModels) {
		
		cellModel = cellModels.find(cm => cm.URI===this.cellModelURI);	// current cell model level
		if (cellModel.isReference) {
			// we take the philosophy of completing the cellmodel reference with the missing data
			// we keep rest of the cell model information (like the name, which can be different)
			let reference:CellModel = this.findCellModelWithURI(rootCellmodels, cellModel.referenceURI);
			cellModel.attributes = reference.attributes;
			cellModel.children = reference.children;
			
		}
		
		//TODO: handle inconsistent cell that cannot find cellmodule even though the content is valid		 
//		  if (!cellModel) {												  // cell model children maybe?
//			  cellModel = cellModels.map(cm => this.associateWith_(cm.children)).find(cm => cm!=undefined);
//		  }

		if (cellModel) {												// now attributes and cell children
		   if (this.attributes) {
			   this.attributes = this.attributes.map(a => a.associateWith_(rootCellmodels, cellModel.attributes));
		   }
		   if (this.children) {
			   this.children = this.children.map(c => c.associateWith_(rootCellmodels, cellModel.children));
		   }
		}
	   
	this.cellModel = cellModel;
	
	}

	return this;
	
}


private findCellModelWithURI(cellModels:CellModel[], uri: string): CellModel {

	let cellModel:CellModel;
	let pending:CellModel[] = [];
	cellModels.forEach(cm => pending.push(cm));
	
	while (!cellModel && pending.length>0) {
		
		let currentCellModel:CellModel = pending.pop();
		if (currentCellModel.URI==uri) {
			cellModel = currentCellModel;
		} else {
			if (currentCellModel.children) { 
				currentCellModel.children.forEach(cm => pending.push(cm));
			}
		}
	}

	return cellModel;

}


toJSON(): CellJSON {

	let serialisedCell:CellJSON = Object.assign({}, this);

	if (this.attributes) {
		serialisedCell.attributes = this.attributes.map(a => a.toJSON());
	}
	if (this.children) {
		serialisedCell.children = this.children.map(c => c.toJSON());
	}

	return serialisedCell;
	
}


static fromJSON(json: CellJSON|string):Cell {

	if (typeof json === 'string') {
		
		return JSON.parse(json, Cell.reviver);
		
	} else {
		
		let cell:Cell = Object.create(Cell.prototype);
		cell = Object.assign(cell, json);
		
		if (json.attributes) {
			cell = Object.assign(cell, {attributes: json.attributes.map(a => Cell.fromJSON(a))});
		}

		// we complete the children runtime information so we have the parent reference as well as position
		if (json.children) {
		    let i:number = 0;
		    cell = Object.assign(cell, {children: json.children.map(c => {
			    let fullCell:Cell = Cell.fromJSON(c);
			    fullCell.position = i++;
			    fullCell.parent = cell;
			    return fullCell;
			})});
		}
		
		return cell;
		
	}
	
}


static reviver(key: string, value: any): any {
	return key === "" ? Cell.fromJSON(value) : value;
}


}

export interface CellJSON {
	
schema: number,
URI: string,
name: string,
desc: string,
value: string;
cellModelURI: string,
isSimple: boolean,
	
attributes?: CellJSON[];
children?: CellJSON[];
}