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
private static readonly ANY = '*';
//private static readonly ANY = '*';	// not supported (yet)
private static readonly ATTR_SEPARATOR = '@';
private static readonly LOCATOR_SEPARATOR = '/';

/** Use a locator to find a set of cells
*	@param startingCell is the starting point where to start looking
*	@param expr is the pattern to find
*	There are different types of locator
*		a) {@literal *}{@literal *}/name --&gt; all cell values of the name
*		b) {@literal *}{@literal *}/name{@literal @}attribute --&gt; all attributes of cells with name and attribute
*		c) {@literal *}/name --&gt; values of the name that have any name as parent
*	They can be combined, for instance /foo/{@literal *}{@literal *}/name
* 	@returns the list of cells that matches the locator pattern
*/
static findCellsWithLocator(startingCell: Cell, expr: string): Cell[] {

	if (startingCell===null || expr===null) {
		console.error('CellLocator::findCellsWithLocator - Null parameter(s)');
	}

	//// first, we parse the expression			////
	// do we start with '/' (mandatory), which also guarantees we have at least one item and we are not empty string 
	if (expr.indexOf(CellLocator.LOCATOR_SEPARATOR)!=0) {
		return [];
	}

	const prefix = startingCell.getURI();					// locators are calculated relative to the top URI
	const tokens = CellLocator._parseLocatorExpression(expr);
	const target = CellLocator._getTarget(tokens);
	const targetAttribute = CellLocator._getTargetAttribute(tokens);
	if (targetAttribute) {	// if the pattern ends with the attribute name (say, @foo),
		tokens.pop();		// we do not want the attribute '@foo' in the matching tokens, for convenience
	}

	return CellLocator._findCellsWithLocator([startingCell], prefix, tokens, target, targetAttribute);

}


/** Return one cell that matches the value or attribute in the locator expression, there are no guarantees which one
*	will be returned if there are more than one matching.
*	Note that if the locator expression is attribute based /xxx/(like @literal @}foo), we return the attribute cell
*	@param startingCell is the starting point where to start looking
*	@param expr is the pattern to find
* 	@param value return a cell that matches the value or attribute in the expression
*/
static findCellWithLocatorAndValue(startingCell: Cell, expr: string, value: string): Cell {
	return CellLocator.findCellsWithLocator(startingCell, expr)?.find(c => c.value===value);;
}

static hasAttribute(expr: string): boolean {
	return CellLocator._getTargetAttribute(CellLocator._parseLocatorExpression(expr))!==undefined;
}


/**  @returns a list of values from each cell, skipping undefineds */
static flattenValues(cells: Cell[]): string[] {
	return cells.map(c => c.value).filter(v => v!==undefined);
}


/** convenience method that returns the flattened values */
static findValuesWithLocator(startingCell: Cell, expr: string): string[] {
	return CellLocator.flattenValues(CellLocator.findCellsWithLocator(startingCell, expr));
}


private static _parseLocatorExpression(expr: string): string[] {

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

	// IMPORTANT: 
	// we keep the first empty string (first in the orginal list), as it will match with the empty name of the root
	// '/foo/bar' --> ["", "foo", "bar"]
	// which will match with
	// '' (root node with emtpy name)
	//		"foo(0)"
	//			"bar(0)"
	//			"bar(1)"


	return expressions;

}

private static _getTarget(tokens: string[]): string {

	// when we have .../foo@bar, return 'foo', when we have ../whatever, return 'whatever'
	const length = tokens.length;

	return (length>1 && tokens[length-1].startsWith(CellLocator.ATTR_SEPARATOR)) ? tokens[length-2] : tokens[length-1];

}


private static _getTargetAttribute(tokens: string[]): string {

	// when we have .../foo@bar, return 'bar', when we have ../whatever, return undefined
	const attr = tokens[tokens.length-1];

	return (attr.startsWith(CellLocator.ATTR_SEPARATOR)) ? attr.substr(CellLocator.ATTR_SEPARATOR.length) : undefined;

}


private static _findCellsWithLocator(pending: Cell[], 
										prefix: string, 
										tokens: string[], 
										target: string, 
										attribute: string): Cell[] {

	let values: Cell[] = [];

	// find targets first, namely all cells that have the target
	let candidates: Cell[] = [];

	while (pending.length>0) {
		const candidate = pending.pop();
		if (candidate.getAdoptionName()===target) {
			candidates.push(candidate);
		}
		// as we do not semantically examine the expression, we have no guarantee that children of the current
		// candidate will not have matching nodes, therefore we have to do a complete sweep of the tree
		if (candidate.childrenCount()>0) {
			candidate.children.forEach(c => pending.push(c));
		}
	}

	// second, for each cell, we see if the tokens can be matched to it, and if so, add the necessary value
	values = candidates.filter(c => CellLocator._locatorMatch(c.getURI(), prefix, tokens)).map(cell => {
		if (attribute) {
			return cell.getAttribute(attribute);
		} else {
			return cell;
		}
	}).filter(c => c!==undefined);	// this will filter out undefineds coming from not found attributes

	return values;

}


public static _locatorMatch(uri: string, prefix: string, tokens: string[]): boolean {

	// given the cell uri [prefix]/foo(0)/bar(1)/waz(0) and a set of tokens like ["","foo", "bar", "waz"] we return
	// true if the tokens can be matched to the cell uri, in reverse, for efficiency
	// Note that matching is done ignoring the prefix, as that is usually context specific and long, like
	// URIs are commonly [prefix=http://xxx.yyy.com]/whatever.yaml/foo(1)/bar(0)/waz(0)
	// so a locator would be /foo/bar/waz

	// this is fun, we have thre cases, 'any' with '/**', 'skip' with '/*' and 'filter' with '/foo'
	// depending on which one we are at, we perform one operation or the other
	// 'any' means we just gobble uri bits until we find a matching tokens or reach the root (in root return true)
	// 'skip' means we gobble the next uri bit and then we move on
	// 'filter' means we continue filtering matching tokens until we
	//		a) go back to 'any'
	//		b) reach the root, then we return true



	const uriTokens = uri.substr(prefix.length).split(CellLocator.LOCATOR_SEPARATOR);
	const _tokens  = Object.assign([], tokens);	// we do not want to mutate input parameters!!!

	let finished = false;
	while (!finished && _tokens.length>0 && uriTokens.length>0) {

		let token = _tokens.pop();
		let uriToken = CellLocator._getName(uriTokens.pop());
		if (token===CellLocator.ANYWHERE) {			//// any case 		////
			token = _tokens.pop();
			while (token===CellLocator.ANYWHERE || token===CellLocator.ANY) {
				token = _tokens.pop(); // while we have '**' or '*' one after the other, we skip them
			}
			while (uriTokens.length>0 && token!==uriToken) {		// we continue to gobble uri tokens until found
				uriToken = CellLocator._getName(uriTokens.pop());	// or reach the root
			}
		} else if (token===CellLocator.ANY) {		//// skip case		////
			// no op, we do not have to do a check agains the current uriToken
		} else {									//// precise case 	////
			if (token!==uriToken) {
				finished = true;
			}
		}

	}

	return _tokens.length===0 && uriTokens.length===0;

}



private static _getName(s: string): string {
	return s.length>0 ? s.substr(0, s.indexOf("(")) : s;
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
