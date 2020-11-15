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
static findVacluesWithLocator(startingCell: Cell, expression: string): string[] {

	if (startingCell===null || expression===null) {
		console.error('CellLocator::findCellsWithLocator - Null parameter(s)');
	}

	let values: string[] = [];
	let pending: Cell[] = [];

	// now we find out what type of locator we have
	const locator = new Locator(expression);
	if (locator.type()===LocatorType.UNKNOWN) {
		console.error('CellLocator::findCellsWithLocator with unsupported locator, returning empty list');
	} else {
		pending.push(startingCell);
	}
	while (pending.length>0) {
		const currentCell = pending.pop();
		const nextBatch = CellLocator.matchLocator(currentCell, values, locator);
		nextBatch.forEach(c => pending.push(c));
	}

	return values;

}


private static matchLocator(cell: Cell, values: string[], locator: Locator):  Cell[] {

/*
	let nextBatch: Cell[] = [];
	if (cell.childrenCount()>0) {
		cell.children.forEach(c => pending.push(c));
	}
*/


return [];

}


}

class Locator {

private static readonly ANYWHERE = '**';
//private static readonly ANY = '*';	// not supported (yet)
private static readonly ATTR_SEPARATOR = '@';
private static readonly LEVEL_SEPARATOR = '/';

private readonly _type: LocatorType;
private readonly _name: string;
private readonly _attribute: string;


constructor(private readonly expr: string)  {

	if (expr.startsWith(Locator.ANYWHERE)) {
		this._type = (expr.indexOf(Locator.ATTR_SEPARATOR)==-1) ? LocatorType.BASIC : LocatorType.BASIC_ATTR;
	} else {
		this._type = LocatorType.UNKNOWN;
	}
	const lastSlashIndex = expr.lastIndexOf(Locator.LEVEL_SEPARATOR);
	if (lastSlashIndex==-1) {
		console.error('Locator "%s" does not contain any "%s" separator', expr, Locator.LEVEL_SEPARATOR);
	}

	const criteria = expr.substr(lastSlashIndex);
	//this._name = this._type===LocatorType.BASIC ? criteria : 

}


type(): LocatorType {
	return this._type;
}


}

enum LocatorType {
	BASIC,			// **/name
	BASIC_ATTR,		// **/name@attribute
	UNKNOWN			// unsupported or unknown
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
