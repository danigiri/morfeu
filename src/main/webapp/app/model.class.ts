/*
 *    Copyright 2016 Daniel Giribet
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

export class Model {

public cellModels: CellModel[];

constructor(schema: number, name: string, desc: string, valid: boolean) {
    this.cellModels = [];
}

normalise() {
    this.cellModels = this.cellModels.map(cm => cm.normalised());
}

toJSON(): ModelJSON {
    return Object.assign({}, this, {cellModels: this.cellModels.toString()});
}

}


interface ModelJSON {

schema: number;
name: string;
desc: string;
valid: boolean;
cellModels: CellModelJSON[];
 
    
}