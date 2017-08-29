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

import { Cell, CellJSON } from './cell.class';
import { SerialisableToJSON } from './serialisable-to-json.interface';


export class Content implements SerialisableToJSON<Content, ContentJSON> {
    
cells:Cell[];

constructor(public schema: number) {}


toJSON(): ContentJSON {    
   return Object.assign({}, this, {cells: this.cells.map(c => c.toJSON())});
}   


fromJSON(json: ContentJSON|string): Content {
    
    if (typeof json === 'string') {
        
        return JSON.parse(json, Content.reviver);
        
    } else {
        
        let content = Object.create(Content.prototype);
        content = Object.assign(content, json);

        return Object.assign(content, {cells: json.cells.map(c => Cell.fromJSON(c))});
        
        
    }
    
}


static reviver(key: string, value: any): any {
    return key === "" ? Object.create(Content.prototype).fromJSON(value) : value;
}
    
}


export interface ContentJSON {
    
schema:number;
cells:CellJSON[];
    
}
