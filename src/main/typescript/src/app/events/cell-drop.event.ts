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

import { Cell } from "../cell.class";
// import { CellModel } from '../cell-model.class';
import { FamilyMember } from "../family-member.interface";

export class CellDropEvent {


constructor(public cell:Cell,
			public newParent: FamilyMember = undefined, 
			public newPosition: number = undefined) {}


public toString = () : string => {

	return "DropCellEvent:{cell:'"+this.cell.URI
			+ (this.newParent ? "', newParent:"+this.newParent.getAdoptionURI() : "")
			+ (this.newPosition ? "', newPosition:"+this.newPosition : "")
			+"}";

}

}
