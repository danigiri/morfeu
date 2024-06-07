// ADOPTION . CLASS . TS

import { Cell } from './cell.class';
import { CellModel } from './cell-model.class';
import { FamilyMember } from './family-member.interface';

/** logic class to hold adoption logic */
export class Adoption {


	public static hasCellModel(cell: Cell): boolean {
		return cell.cellModel !== undefined; // this will be true for the root node, though this can be refactored
	}


	public static canBeModified(cell: Cell): boolean {
		return cell.canBeModified();
	}


	public static isModelCompatible(cell: Cell, newMember: FamilyMember): boolean {
		return cell.cellModel.canAdopt(newMember);
	}


	/** we check the allowed count (if we have no children we assume zero so we should be able to add) */
	public static weHaveRoomForOneMore(cell: Cell, newMember: FamilyMember): boolean {

		// if the newMember is part of the children we always have room for one more
		if (cell === newMember.getParent()) {
			return true;
		}

		const matchingChildren: Cell[] = cell.children ? cell.children.filter(c => c.matches(newMember)) : [];
		const childCount = matchingChildren.length;
		if (childCount > 0) {
			// we are not considering the problem of the childcount being less than the minimum
			//TODO: add check: are we able to remove this cell as child?
			let matchingCellModel: CellModel = matchingChildren[0].cellModel;
			if (matchingCellModel.maxOccurs && childCount >= matchingCellModel.maxOccurs) { // notice we use '>=' as we are adding one more
				return false;
			}
		}

		return true;
	}



	/** we check that if we are trying to add into adjacent positions, this is a noop we want to avoid
		place, example:
		<col>
			[drop area 0]
			<thingie/>
			[drop area 1]
			<otherthing/>
			[drop area 2]
		</col>
		in this case, <thingie/> does not make sense to activate drop areas 0 and 1 as cell ends up the same
	*/
	public static isNotAdjacentPosition(cell: Cell, newMember: FamilyMember, position: number): boolean {

		// WARNING: in the case of a snippet, get parent is null, in this case the cell equivalence is automatically false
		// it's not clear if this is 100% the right behaviour, but it matches the semantics of this adjacency test
		if (cell.children === undefined || cell.children.length === 0 || !cell.equals(newMember.getParent())) {
			return true;
		}
		const child = cell.children.find(c => c.getURI() === newMember.getURI());

		return position != child.position && position != child.position + 1;
		//return true;

	}


	public static itsTheRightOrder(cell: Cell, newMember: FamilyMember, position: number): boolean {

		let ok = false;

		const childrenCount = cell.childrenCount();
		const model = cell.cellModel;
		if (childrenCount > 0 && model.children !== undefined && model.areChildrenOrdered) {

			const adoptionOrder = newMember.getAdoptionOrder();
			if (position === 0) {						// at the beginning, check first cell
				ok = (adoptionOrder <= cell.children[0].getAdoptionOrder());
			} else if (position === childrenCount) {	// at the end, check last cell
				ok = cell.children[cell.childrenCount() - 1].getAdoptionOrder() <= adoptionOrder;
			} else {								// in the middle, check previous and next cells
				ok = (cell.children[position - 1].getAdoptionOrder() <= adoptionOrder) &&
					(adoptionOrder <= cell.children[position].getAdoptionOrder());
			}

		} else {
			ok = true;
		}

		return ok;
	}


	public static adoptionMapToString(a: Map<string,boolean>): string {
		if (a==null) {
			return null;
		}
		let out: string[] = [];
		for (const k of a.keys()) {
			out.push(k+':'+(a.get(k) ? 'true' : 'false'));
		}
		return out.join(',');
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
