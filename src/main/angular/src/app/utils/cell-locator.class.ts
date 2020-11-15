// LOCATOR . CLASS . TS

import { Cell } from '../cell.class';

export class CellLocator {


/** find a cell that has this URI or return undefined */
static findCellWithURI(startingCell: Cell, uri: string): Cell {

	let cell: Cell;
	let pending: Cell[] = [startingCell];

	while (!cell && pending.length>0) {
		const currentCell = pending.pop();
		if (currentCell.getURI()===uri) {
			cell = currentCell;
		} else if (currentCell.childrenCount()>0) {
			currentCell.children.forEach(c => pending.push(c));
		}
	}

	return cell;

}


/** Use a locator to find a set of values
*	@param startingCell is the starting point where to start looking
*	@param locator is the pattern to find
*	There are different types of locator
*		a)  {@literal *}{@literal *}/name --&gt; all cell values of the name
*		b) {@literal *}{@literal *}/name{@literal @}attribute --&gt; all attributes of cells with name and attribute
*/
static findCellsWithLocator(startingCell: Cell, locator: string): string[] {

	let values: string[] = [];
	let pending: Cell[] = [startingCell];

	// now we find out what type of locator we have


	while (pending.length>0) {
		const currentCell = pending.pop();
		if (false) {
		} else if (currentCell.childrenCount()>0) {
			currentCell.children.forEach(c => pending.push(c));
		}
	}

	return values;

}


}

/*
 *	Copyright 2020 Daniel Giribet
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
