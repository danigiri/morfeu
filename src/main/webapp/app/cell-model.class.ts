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


import { TreeModel } from 'ng2-tree';

import { Type_ } from './type_.class';

export class CellModel implements TreeModel {
    
value: string;
id: string;


attributes?: CellModel[];
children?: CellModel[];

constructor(schema: number, name: string, desc: string, isSimple: boolean, type_: Type_) {  
    this.value = name;
    this.id =;
}    
    
normalised():CellModel {
    
    // WORK CONTINUES HERE
    return this;
}

toJSON: CellModelJSON {
    return Object.assign({}, this, {cellModels: this.cellModels.toString()});
}

}

interface CellModelJSON {}
