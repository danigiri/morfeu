/*
 *    Copyright 2017 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import { CellModel } from './cell-model.class';
import { Model } from './model.class';


export class Cell {

attributes?: Cell[];
children?: Cell[];
cellModel?: CellModel;
    
constructor(public schema: number,
            public name: string,
            public desc: string,
            public value: string,
            public cellModelURI: string,
            public isSimple: boolean) {}
    

associateWith(model: Model):Cell {

    this.associateWith_(model.cellModels);
    
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

// we look for a field that has representation of COLUMN-FIELD or 1 as default
columnFieldValue():string {

    let value: string = "1";

    if (this.attributes) { 
        let attribute:Cell = this.attributes.find(a => a.cellModel.presentation=="COLUMN-FIELD");
        if (attribute) {
            value = attribute.value;
        }
    }
    return value;
}

private associateWith_(cellModels:CellModel[]):Cell {

    let cellModel:CellModel = undefined;
    if (cellModels) {
        
        cellModel = cellModels.find(cm => cm.URI===this.cellModelURI);  // current cell model level

        //TODO: handle inconsistent cell that cannot find cellmodule even though the content is valid        
//        if (!cellModel) {                                               // cell model children maybe?
//            cellModel = cellModels.map(cm => this.associateWith_(cm.children)).find(cm => cm!=undefined);
//        }

        if (cellModel) {                                                // now attributes and cell children
           if (this.attributes) {
               this.attributes = this.attributes.map(a => a.associateWith_(cellModel.attributes));
           }
           if (this.children) {
               this.children = this.children.map(c => c.associateWith_(cellModel.children));
           }
        }
       
    this.cellModel = cellModel;
    
    }

    return this;
    
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

        if (json.children) {
            cell = Object.assign(cell, {children: json.children.map(c => Cell.fromJSON(c))});
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
name: string,
desc: string,
value: string;
cellModelURI: string,
isSimple: boolean,
    
attributes?: CellJSON[];
children?: CellJSON[];
}