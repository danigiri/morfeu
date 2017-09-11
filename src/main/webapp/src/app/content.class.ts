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

import { CellHolder } from './cell-holder.interface';
import { Cell, CellJSON } from './cell.class';
import { Model } from './model.class';
import { SerialisableToJSON } from './serialisable-to-json.interface';


export class Content implements CellHolder, SerialisableToJSON<Content, ContentJSON> {
	
cells:Cell[];

constructor(public schema: number) {}


// associate the content cells with the corresponding cell models
associateWith(model: Model) {
   this.cells = this.cells.map(c => c.associateWith(model));
}

// part of the drag and drop scaffolding, we return true if this cell can be one of the root cells
canHaveAsChild(cell:Cell):boolean {
    return this.cells.find(c => c.name==cell.name && c.cellModelURI==cell.cellModelURI)!=undefined;
}


toJSON(): ContentJSON {
   return Object.assign({}, this, {cells: this.cells.map(c => c.toJSON())});
}	


fromJSON(json: ContentJSON|string): Content {
	
	if (typeof json === 'string') {
		
		return JSON.parse(json, Content.reviver);
		
	} else {
		
		let content = Object.create(Content.prototype);
		content = Object.assign(content, json);
		content = Object.assign(content, {cells: json.cells.map(c => Cell.fromJSON(c))});
		
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
cells:CellJSON[];
	
}
