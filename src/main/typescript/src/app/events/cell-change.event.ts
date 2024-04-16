// CELL - CHANGED . EVENT . TS

import { MorfeuEvent } from './morfeu-event.class';

import { Cell } from '../cell.class';

export class CellChangeEvent extends MorfeuEvent {


constructor(public cell: Cell, public what: CellChange, public valid?: boolean, public attribute?: Cell) {
	super('CellChangedEvent');
}


public override toString = (): string => {
	return 'CellChangedEvent:{'+this.cell?.name+','+this.changeToString()+'}';
}


protected changeToString():string {
	switch(this.what) {
		case CellChange.INIT_ATTRIBUTE:
			return 'INIT_ATTRIBUTE';
		break;
		case CellChange.ADD_ATTRIBUTE:
			return 'ADD_ATTRIBUTE';
		break;
		case CellChange.MODIFIED_ATTRIBUTE:
			return 'MODIFIED_ATTRIBUTE';
		break;
		case CellChange.REMOVE_ATTRIBUTE:
			return 'REMOVE_ATTRIBUTE';
		break;
		case CellChange.CREATED_VALUE:
			return 'CREATED_VALUE';
		break;
		case CellChange.MODIFIED_VALUE:
			return 'MODIFIED_VALUE';
		break;
		case CellChange.REMOVED_VALUE:
			return 'REMOVED_VALUE';
		break;
		case CellChange.COMPLETED:
			return 'COMPLETED';
		break;
	}
}

}

export enum CellChange {

INIT_ATTRIBUTE = 0,
ADD_ATTRIBUTE = 1,
MODIFIED_ATTRIBUTE = 2,
REMOVE_ATTRIBUTE = 3,
CREATED_VALUE = 4,
MODIFIED_VALUE = 5,
REMOVED_VALUE = 6,
COMPLETED = 7

}

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
