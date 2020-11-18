// LOCATOR . CLASS . SPEC . TS


import { Cell } from '../cell.class';
import { Model } from '../model.class';

import { _model, _typesPrefix, _types, _readonly, _content1 } from '../test/test.data';
import { CellLocator } from './cell-locator.class';


describe('locator.class', () => {

	let typesContent: Cell;
	let document1Content: Cell;

	beforeEach(() => {

		const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
		const MODEL: Model = Object.create(Model.prototype); // to simulate static call

		const model = MODEL.fromJSON(_model);

		typesContent = CELL.fromJSON(_types);
		typesContent.associateWith(model, typesContent.cellModelURI);

		document1Content = CELL.fromJSON(_content1);
		document1Content.associateWith(model, document1Content.cellModelURI);

	});


	it('should find cell with URI', () => {

		const uri = _typesPrefix+'/test(0)/row(0)/col(0)/types(2)';
		const types2 = CellLocator.findCellWithURI(typesContent, uri);
		expect(types2).toBeDefined();
		expect(types2.getURI()).toBe(uri);

	});


	it('should find anything with unsupported locators', () => {

		const values = CellLocator.findValuesWithLocator(typesContent, '/WHATEVER');
		expect(values).toBeDefined();
		expect(values).toEqual([]);

		const values2 = CellLocator.findValuesWithLocator(typesContent, 'ERRONEOUS');
		expect(values2).toBeDefined();
		expect(values2).toEqual([]);

	});


	it('should find stuff values', () => {

		const values = CellLocator.findValuesWithLocator(typesContent, '/test/row/col/stuff');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(3);
		expect(values).toContain("V0");
		expect(values).toContain("V1");
		expect(values).toContain("V2");


	});


	it('should find stuff values using **', () => {

		const values = CellLocator.findValuesWithLocator(typesContent, '/**/stuff');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(3);
		expect(values).toContain("V0");
		expect(values).toContain("V1");
		expect(values).toContain("V2");

	});



	it('should find stuff values using ** and filters', () => {

		const values = CellLocator.findValuesWithLocator(typesContent, '/test/**/stuff');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(3);
		expect(values).toContain("V0");
		expect(values).toContain("V1");
		expect(values).toContain("V2");

	});


	it('should find stuff values using ** and filters', () => {

		const values = CellLocator.findValuesWithLocator(typesContent, '/**/col/stuff');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(3);
		expect(values).toContain("V0");
		expect(values).toContain("V1");
		expect(values).toContain("V2");

	});


	it('should find stuff values using ** and filters (2)', () => {

		const values = CellLocator.findValuesWithLocator(typesContent, '/test/row/**/stuff');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(3);
		expect(values).toContain("V0");
		expect(values).toContain("V1");
		expect(values).toContain("V2");

	});


	it('should find attributes', () => {

		const values = CellLocator.findValuesWithLocator(typesContent, '/test/row/col/types@list');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(4);
		expect(values).toContain("A0");
		expect(values).toContain("A1");
		expect(values).toContain("A2");
		expect(values.filter(v => v==="A0").length).toBe(2);

	});


	it('should find nested content', () => {

		const values = CellLocator.findValuesWithLocator(document1Content, '/test/row/col/data@text');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(1);
		expect(values[0]).toBe("blahblah");

	});


	it('should find nested content after ** with attributes', () => {

		const values = CellLocator.findValuesWithLocator(document1Content, '/**/col/data@text');
		expect(values).toBeDefined();
		expect(values.length).toBeDefined();
		expect(values.length).toBe(1);
		expect(values[0]).toBe("blahblah");

	});

	it('should find nested content after ** with attributes', () => {

		const values = CellLocator.findValuesWithLocator(document1Content, '/**/col/data@number');
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
