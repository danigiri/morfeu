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

export class Cell {

attributes?: Cell[];
children?: Cell[];
    
constructor(public schema: number,
            public name: string,
            public desc: string,
            public cellModelURI: string,
            public isSimple: boolean) {}
       

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
cellModelURI: string,
isSimple: boolean,
    
attributes?: CellJSON[];
children?: CellJSON[];
}