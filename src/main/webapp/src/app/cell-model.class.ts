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

import { Type_ } from './type_.class';


export class CellModel {

id: string;
isExpanded: boolean;

attributes?: CellModel[];
children: CellModel[];
isReference: boolean;
referenceURI?: string;

constructor(public schema: number, 
            public URI: string, 
            public name: string, 
            public desc: string, 
            public presentation: string, 
            public isSimple: boolean, 
            public type_: Type_) {  

    this.init();

}    


// there are values specific to comply wit the treemodel model, we set them explicitly here 
init() {
   
    this.id = this.URI;  // this is guaranteed to be unique 
    this.isExpanded = true;
}


toJSON(): CellModelJSON {

    let serialisedCellModel:CellModelJSON = Object.assign({}, this);
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
        cellModel.init();                           // make sure we have all attributes ok
        
        if (json.attributes) {
            cellModel = Object.assign(cellModel, 
                                      {attributes: json.attributes.map(a => CellModel.fromJSON(a))});
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

}

export interface CellModelJSON {
    
schema: number; 
URI: string;
name: string; 
desc: string;
presentation: string;
isSimple: boolean; 
isReference: boolean;
type_: Type_;
    
attributes?: CellModelJSON[];
children?: CellModelJSON[];
referenceURI?: string;

}
