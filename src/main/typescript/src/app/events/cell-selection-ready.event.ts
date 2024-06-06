// CELL - SELECTION - READY . EVENT . TS

import { MorfeuEvent } from './morfeu-event.class';

import { FamilyMember } from '../family-member.interface';


export class CellSelectionReadyEvent extends MorfeuEvent {

constructor(public target: FamilyMember, public ready: boolean = true) {
	super('CellSelectionReadyEvent');
}


public override toString = (): string => {
	return "CellSelectionReadyEvent:{{'"+this.target.getURI()+"',"+this.ready+"})";
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
