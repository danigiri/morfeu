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


private static readonly ANYWHERE = '**';
//private static readonly ANY = '*';	// not supported (yet)
private static readonly ATTR_SEPARATOR = '@';
private static readonly LOCATOR_SEPARATOR = '/';

/** Use a locator to find a set of values
*	@param startingCell is the starting point where to start looking
*	@param locator is the pattern to find
*	There are different types of locator
*		a)  {@literal *}{@literal *}/name --&gt; all cell values of the name
*		b) {@literal *}{@literal *}/name{@literal @}attribute --&gt; all attributes of cells with name and attribute
*/
static findValuesWithLocator(startingCell: Cell, expr: string): string[] {

	if (startingCell===null || expr===null) {
		console.error('CellLocator::findCellsWithLocator - Null parameter(s)');
	}
	
	//// first, we parse the expression			////
	// do we start with '/' (mandatory), which also guarantees we have at least one item
	if (expr.indexOf(CellLocator.LOCATOR_SEPARATOR)!=0) {
		return [];
	}

	let tokens = CellLocator.parseLocatorExpression(expr);
	const firstToken = tokens.pop();
	const start = startingCell.childrenCount()>0 ? startingCell.children : [startingCell];

	return CellLocator._findValuesWithLocator(start, tokens, firstToken);

}


/*
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
*/



private static parseLocatorExpression(expr: string): string[] {

	// we parse the expression so we turn '/aa/bb/**/cc/*/a@b' to ['aa','bb','**','cc','*','a','@b']
	let expressions = expr.split(CellLocator.LOCATOR_SEPARATOR);
	const last = expressions.pop();
	const attributeSeparatorIndex = last.lastIndexOf(CellLocator.ATTR_SEPARATOR);
	if (attributeSeparatorIndex===-1) {	// no attribute, we add the last token back
		expressions.push(last);
	} else {							// attribute marker on the last token, we split it into two, put it back
		const name = last.substr(0, attributeSeparatorIndex);
		const attribute = last.substr(attributeSeparatorIndex);
		expressions.push(name);
		expressions.push(attribute);
	}

	expressions = expressions.reverse();// top is now at the end, so it's easier to pop 
	expressions.pop();					// remove what is now the last empty string, coming from the split

	return expressions;

}


private static _findValuesWithLocator(pending: Cell[], tokens: string[], token: string): string[] {


	// this is fun, we ahve to major modes of operation, 'any mode' with '/**' and 'precise mode' with '/foo'
	// depending on which one we are at, we perform one operation or the other

	let values: string[] = [];

	while (pending.length>0) {

		if (token===CellLocator.ANYWHERE) {				//// ANY MODE		////

			if (tokens.length<=1) {
				console.error('Using "%s" at the end of a locator is not supported yet', CellLocator.ANYWHERE);
				return [];
			}
			// now we consume until we find instance of the next token, when we find it, it will be added
			// to the pending cells list
			const nextToken = tokens[tokens.length-1];
			pending = CellLocator._anywhereMode(pending, nextToken);

		} else {										//// PRECISE MODE	////
			return [];	// TO BE IMPLEMENTED
		}
	}

	return values;

}


private static _anywhereMode(pending: Cell[], nextToken: string): Cell[] {

	let newPending = [];
	
	while (pending.length>0) {
		const currentCell = pending.pop();
		if (currentCell.getAdoptionName()===nextToken) {
			newPending.push(currentCell);
		} else if (currentCell.childrenCount()>0) {
			currentCell.children.forEach(c => pending.push(c));
		}
	}

	return newPending;

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
