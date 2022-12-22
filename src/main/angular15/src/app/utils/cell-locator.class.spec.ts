// LOCATOR . CLASS . SPEC . TS

import { Cell } from '../cell.class';
import { Model } from '../model.class';

import { _model, _typesPrefix, _types, _readonly, _content1 } from '../test/test.data';
import { CellLocator } from './cell-locator.class';


describe('locator.class', () => {

	let types: Cell;
	let document1: Cell;

	beforeEach(() => {

		const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
		const MODEL: Model = Object.create(Model.prototype); // to simulate static call

		const model = MODEL.fromJSON(_model);

		types = CELL.fromJSON(_types);
		types.associateWith(model, types.cellModelURI);

		document1 = CELL.fromJSON(_content1);
		document1.associateWith(model, document1.cellModelURI);

	});

	it('should find cell with URI', () => {

		const uri = _typesPrefix+'/test(0)/row(0)/col(0)/types(2)';
		const types2 = CellLocator.findCellWithURI(types, uri);
		expect(types2).toBeDefined();
		expect(types2.getURI()).toBe(uri);

	});

	it('should not fast find cell with nonexistant URI', () => {
		
		const uri2 = _typesPrefix+'/test(0)/row(0)/col(0)/types(11)';
		console.log('foof')
		const notFound = CellLocator.findCellWithURI(types, uri2);
		expect(notFound).toBeUndefined();

	});	

	it('should find anything with unsupported locators', () => {

		const values = CellLocator.findValuesWithLocator(types, '/WHATEVER');
		expect(values).toBeDefined();
		expect(values).toEqual([]);

		const values2 = CellLocator.findValuesWithLocator(types, 'ERRONEOUS');
		expect(values2).toBeDefined();
		expect(values2).toEqual([]);

	});

	it('should find stuff cells and values', () => {

		const cells = CellLocator.findCellsWithLocator(types, '/test/row/col/stuff');
		expect(cells).toBeDefined();
		expect(cells.length).toBeDefined();
		expect(cells.length).toBe(3);

		const stuff0 = types.findCellWithURI(_typesPrefix+'/test(0)/row(1)/col(0)/stuff(0)');
		const stuff1 = types.findCellWithURI(_typesPrefix+'/test(0)/row(1)/col(0)/stuff(1)');
		const stuff2 = types.findCellWithURI(_typesPrefix+'/test(0)/row(1)/col(0)/stuff(2)');
		expect(cells).toContain(stuff0);
		expect(cells).toContain(stuff1);
		expect(cells).toContain(stuff2);

		const values = CellLocator.flattenValues(cells);
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(3);
		expect(values).toContain("V0");
		expect(values).toContain("V1");
		expect(values).toContain("V2");

	});


	it('should find stuff values using **', () => {

		const values = CellLocator.findValuesWithLocator(types, '/**/stuff');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(3);
		expect(values).toContain("V0");
		expect(values).toContain("V1");
		expect(values).toContain("V2");

	});


	it('should find stuff values using ** and filters', () => {

		const values = CellLocator.findValuesWithLocator(types, '/test/**/stuff');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(3);
		expect(values).toContain("V0");
		expect(values).toContain("V1");
		expect(values).toContain("V2");

	});


	it('should find the cell with a locator and a value', () => {

		const v1 = CellLocator.findCellWithLocatorAndValue(types, '/test/**/stuff','V1');
		expect(v1).toBeDefined();
		const uri = _typesPrefix+'/test(0)/row(1)/col(0)/stuff(1)';
		const expectedV1 = CellLocator.findCellWithURI(types, uri);
		expect(v1).toBe(expectedV1);
	
		const notFound = CellLocator.findCellWithLocatorAndValue(types, '/test/**/stuff','VX');
		expect(notFound).toBeUndefined();

	});


	it('should find the cells with a locator and a value', () => {

		const v1 = CellLocator.findCellsWithLocatorAndValue(types, '/test/**/stuff','V1');
		expect(v1).toBeDefined();
		const uri = _typesPrefix+'/test(0)/row(1)/col(0)/stuff(1)';
		const expectedV1 = [CellLocator.findCellWithURI(types, uri)];
		expect(v1).toEqual(expectedV1);
	
		const notFound = CellLocator.findCellWithLocatorAndValue(types, '/test/**/stuff','VX');
		expect(notFound).toBeUndefined();

	});

	

	it('should find stuff values using ** and filters (2)', () => {

		const values = CellLocator.findValuesWithLocator(types, '/**/col/stuff');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(3);
		expect(values).toContain("V0");
		expect(values).toContain("V1");
		expect(values).toContain("V2");

	});


	it('should find stuff values using ** and filters (3)', () => {

		const values = CellLocator.findValuesWithLocator(types, '/test/row/**/stuff');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(3);
		expect(values).toContain("V0");
		expect(values).toContain("V1");
		expect(values).toContain("V2");

	});


	it('should find attributes', () => {

		const values = CellLocator.findValuesWithLocator(types, '/test/row/col/types@list');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(4);
		expect(values).toContain("A0");
		expect(values).toContain("A1");
		expect(values).toContain("A2");
		expect(values.filter(v => v==="A0").length).toBe(2);

	});


	it('should find the cell with a locator attribute and a value', () => {

		const a1 = CellLocator.findCellWithLocatorAndValue(types, '/test/row/col/types@list', 'A1');
		expect(a1).toBeDefined();
		expect(a1.getParent().getAdoptionName()).toBe('types');

	});


	it('should know if locator is attribute or not', () => {
		
		const hasAttribute = CellLocator.hasAttribute('/foo/bar@attr');
		expect(hasAttribute).toBeTrue();

		const hasNoAttribute = CellLocator.hasAttribute('/foo/bar');
		expect(hasNoAttribute).toBeFalse();

	});


	it('should find nested content', () => {

		const cells = CellLocator.findCellsWithLocator(document1, '/test/row/col/data@text');
		expect(cells).toBeDefined();
		const values = CellLocator.flattenValues(cells);
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(1);
		expect(values[0]).toBe("blahblah");

	});


	it('should find nested content after ** with attributes', () => {

		const values = CellLocator.findValuesWithLocator(document1, '/**/col/data@text');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(1);
		expect(values[0]).toBe("blahblah");

	});


	it('should find nested content after ** with attributes (2)', () => {

		const values = CellLocator.findValuesWithLocator(document1, '/**/col/data@number');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(2);
		expect(values.filter(v => v==="42").length).toBe(2);

	});


	it('should find content with *', () => {

		const values = CellLocator.findValuesWithLocator(document1, '/test/row/*/data@text');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(1);
		expect(values[0]).toBe('blahblah');

	});


	it('should handle */** edge case', () => {

		const values = CellLocator.findValuesWithLocator(document1, '/test/*/**/data@number');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(2);
		expect(values.filter(v => v==="42").length).toBe(2);

	});

});

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
