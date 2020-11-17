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
	// do we start with '/' (mandatory), which also guarantees we have at least one item and we are not empty string 
	if (expr.indexOf(CellLocator.LOCATOR_SEPARATOR)!=0) {
		return [];
	}

	const tokens = CellLocator.parseLocatorExpression(expr);
	return CellLocator._findValuesWithLocator([startingCell], tokens);

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

	// we parse the expression so we turn '/aa/bb/**/cc/*/a@b' to ['', 'aa','bb','**','cc','*','a','@b']
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

	expressions = expressions.reverse();	// top is now at the end, so it's easier to pop 
	// IMPORTANT: 
	// we keep the last empty string (first in the token list), as it will match with the empty name of the root
	//	expressions.pop();					// remove what is now the last empty string, coming from the split
	// '/foo/bar' --> ["", "foo", "bar"]
	// which will match with
	// '' (root node with emtpy name)
	//		"foo(0)"
	//			"bar(0)"
	//			"bar(1)"


	return expressions;

}


private static _findValuesWithLocator(pending: Cell[], tokens: string[]): string[] {

	// this is fun, we ahve to major modes of operation, 'any mode' with '/**' and 'filter mode' with '/foo'
	// depending on which one we are at, we perform one operation or the other
	// 'any' means we just gobble cells until either finished or find the next token
	// 'filter' means we continue filtering matching tokens until we
	//		a) go back to 'any'
	//		b) reach the end of the tokens list, then we return the list of values or attribute values

	let values: string[] = [];

	while (pending.length>0 && tokens.length>0) {

		const token = tokens.pop();
		if (token===CellLocator.ANYWHERE) {				//// ANY MODE		////

			// we skip any chained **/** as they are equivalent to **
			let nextToken = tokens[tokens.length-1];
			while (nextToken===CellLocator.ANYWHERE && tokens.length>0) {
				nextToken = tokens[tokens.length-1];
			}
			if (tokens.length===0) {
				console.error('Using "%s" at the end of a locator is not supported yet', CellLocator.ANYWHERE);
				return [];
			}
			// now we consume until we find instance of the next token, when we find it, it will be added
			// to the pending cells list
			pending = CellLocator._anywhereMode(pending, nextToken);

		} else {										//// PRECISE MODE	////

			// filter out cells that do not match the current token
			pending = pending.filter(cell => token===cell.getAdoptionName());
			// then look for the end (length=0 for values and length=1 for attributes )
			if (tokens.length===0) {	// we are at the end, we get the values of all the pending items
				values = pending.filter(cell => cell.value!==undefined).map(cell => cell.value);
			} else if (tokens.length===1 && tokens[0].startsWith(CellLocator.ATTR_SEPARATOR)) {
				values = CellLocator._getAttributeValues(pending, tokens[0]);
				tokens = [];	// we have consumed the last attribute token by this operation, we are done
			} else {
				pending = CellLocator._nextChildren(pending);
			}
		}

	} 

	return values;

}


private static _anywhereMode(pending: Cell[], nextToken: string): Cell[] {

	// we do a breadth-first approach to find the nextToken, we stop when found, but as we are in '**' it means we do an
	// exhaustive search

	let newPending = [];
	while (pending.length>0) {
		const currentCell = pending.pop();
		if (currentCell.getAdoptionName()===nextToken) {
			newPending.push(currentCell);
		} else if (currentCell.childrenCount()>0) {
			currentCell.children.forEach(cell => pending.push(cell));
		}
	}

	return newPending;

}


private static _getAttributeValues(cells: Cell[], attributeName: string): string [] {

	// get all attributes, filter out undefineds when the attribute is not present, get the value, filter nonpresent
	const name = attributeName.substr(CellLocator.ATTR_SEPARATOR.length); 

	return cells.map(c => c.getAttribute(name)).filter(a => a!==undefined).map(a => a.value).filter(a => a!==undefined);

}

private static _nextChildren(cells: Cell[]) {

	const newPending: Cell[] = []; 
	let next = cells.filter(cell => cell.childrenCount()>0);
	next.forEach(c => c.children.forEach(cell => newPending.push(cell)));

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
