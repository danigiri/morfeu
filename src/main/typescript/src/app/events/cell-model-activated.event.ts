// CELL - MODEL - ACITVATED . EVENT . TS

import { MorfeuEvent } from './morfeu-event.class';

import { Cell } from '../cell.class';
import { CellModel } from "../cell-model.class";


export class CellModelActivatedEvent extends MorfeuEvent {

newCell: Cell;

constructor(public cellModel?: CellModel) {
	super('CellModelActivatedEvent');
	this.newCell = cellModel?.generateCell(); // used for precise adoption logic, all receivers can test against it
}


public override toString = (): string => {
	return "CellModelActivatedEvent:{cellModel:'"+(this.cellModel ? this.cellModel.URI : "<selection>")+"}'}";
}


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