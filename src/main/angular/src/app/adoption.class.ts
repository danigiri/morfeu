// ADOPTION . CLASS . TS

import { Cell } from './cell.class';
import { CellModel } from './cell-model.class';
import { FamilyMember } from './family-member.interface';


/** logic class to hold adoption logic */
export class Adoption {


public static hasCellModel(cell: Cell): boolean {
	console.debug("foo")
	return cell.cellModel!==undefined; // this will be true for the root node, though this can be refactored
}


public static canBeModified(cell: Cell): boolean {
	return cell.canBeModified();
}


public static isModelCompatible(cell: Cell, newMember: FamilyMember, position: number): boolean {
	return cell.cellModel.canAdopt(newMember, position);
}


/** we check that if we are a lone cell in a droppable parent, we cannot drop to end up in the same
	place, example:
	<col>
		[drop area 0]
		<thingie/>
		[drop area 1]
	</col>
	in this case, <thingie/> does not make sense to activate drop areas 0 and 1 as cell ends up the same
*/
public static isNotOurSingleChild(cell: Cell, newMember: FamilyMember): boolean {
	return cell.children===undefined || 
			!(cell.children && cell.children.length==1 && cell.equals(newMember.getParent()));
}

/** we check the allowed count (if we have no children we assume zero so we should be able to add) */
public static weHaveRoomForOneMore(cell: Cell, newMember: FamilyMember): boolean {

	let matchingChildren: Cell[] = cell.children ? cell.children.filter(c => c.matches(newMember)) : [];
	const childCount = matchingChildren.length;
	if (childCount>0) {
		// we are not considering the problem of the childcount being less than the minimum
		//TODO: add check: are we able to remove this cell as child?
		let matchingCellModel:CellModel = matchingChildren[0].cellModel;
		if (matchingCellModel.maxOccurs && childCount >= matchingCellModel.maxOccurs) { // notice we use '>=' as we are adding one more
			return false;
		}
	}

	return true;
}

/**	we check if we have more than one element but we are in the same droppable parent which means
	that we can actually reorder stuff around, as we will not be modifying counts, then we allow drops
	<col>
		[drop area 0]
		<thingie/>
		[drop area 1]  // TODO: if we want to move the first thingie, areas 0 and 1 are not needed :)
		<thingie/>
		[drop area 2]
	</col>
 */
public static itsTheRightPosition(cell: Cell, newMember: FamilyMember, position: number): boolean {
	return cell.children!==undefined && cell.parent!==undefined;
}


}

/*
 *	  Copyright 2020 Daniel Giribet
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
