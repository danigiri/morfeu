// CELL - CHANGED . EVENT . TS

import { MorfeuEvent } from './morfeu-event.class';

import { Cell } from '../cell.class';

export class CellChangeEvent extends MorfeuEvent {


constructor(public cell: Cell, public what: CellChange, public attribute?: Cell, public valid?: boolean) {
	super('CellChangedEvent');
}


public toString = (): string => {
	return 'CellChangedEvent:{'+this.cell?.URI+','+this.what+'}';
}


}

export enum CellChange {

ADD_ATTRIBUTE = 0,
MODIFIED_ATTRIBUTE = 1,
REMOVE_ATTRIBUTE = 2,
CREATED_VALUE = 3,
MODIFIED_VALUE = 4,
REMOVED_VALUE = 5,
COMPLETED = 6

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
