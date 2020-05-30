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


public static isModelCompatible(cell: Cell, newMember: FamilyMember): boolean {
	return cell.cellModel.canAdopt(newMember);
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


public static itsTheRightOrder(cell: Cell, newMember: FamilyMember, position: number): boolean {

	let ok = false;

	const childrenCount = cell.childrenCount();
	const model = cell.cellModel;
	if (childrenCount>0 && model.children!==undefined && model.areChildrenOrdered) {

		if (childrenCount===position) {	//  we are at the end and the last children model matches with the new member
			ok = model.children[model.childrenCount()-1].matches(newMember);
		} else if (position===0) { 		// if we are moving to the beginning check if first children model matches
			ok = model.children[0].matches(newMember);
		} else if (cell.children[position].matches(newMember)) {	// add to list of same model cells
			ok = cell.children[position].URI!==newMember.getURI();	// no-op move
		} else {
			const index = model.children.findIndex(cm => cm.matches(newMember));
			const nextCellModel = index+1<model.childrenCount() ? model.children[index+1] : undefined;
			if (nextCellModel===undefined) {
				console.error('next cell model should never be undefined');
			}
			ok = nextCellModel.matches(cell.children[position]);
		}


	} else {
		ok = true;
	}

	return ok;	
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
