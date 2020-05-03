// CELL . CLASS . SPEC . TS

import { Cell } from './cell.class';
import { Model } from './model.class';

import { _model, _readonlyPrefix, _readonly, _typesPrefix, _types } from './test/test.data';


describe('cell.class', () => {

	let readonly: Cell;
	let typesContent: Cell;

	beforeEach(() => {

		const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
		const MODEL: Model = Object.create(Model.prototype); // to simulate static call

		const model = MODEL.fromJSON(_model);

		readonly = CELL.fromJSON(_readonly);
		readonly.associateWith(model, readonly.cellModelURI);

		typesContent = CELL.fromJSON(_types);
		typesContent.associateWith(model, typesContent.cellModelURI);

	});

	it('should read from json', () => {

		expect(readonly).toBeDefined();
		expect(readonly.desc).toBe('DESC');
		expect(readonly.isSimple).toBe(false);
		expect(readonly.value).toBeUndefined();

	});

	it('should find cell with correct URI', () => {

		const uri = _readonlyPrefix+'/test(0)/row(1)/col(0)/readonly(2)';
		const foundCell = readonly.findCellWithURI(uri);
		expect(foundCell).toBeDefined();
		expect(foundCell.URI).toBe(uri);

		const uri2 = _readonlyPrefix+'/test(111)/row(1)/col(0)/readonly(2)';
		const foundCell2 = readonly.findCellWithURI(uri2);
		expect(foundCell2).toBeUndefined();

	});

	it('should generate a correct all presentation', () => {

		const uri = _readonlyPrefix+'/test(0)/row(1)/col(0)/data(0)';
		let data0 = readonly.findCellWithURI(uri);
		expect(data0).toBeDefined();
		data0.value = 'VALUE';
		const presentationAllContent = data0.getCellPresentationAllContent();
		expect(presentationAllContent).toBe('_name=data&_value=VALUE&number=1');

	});

	it('should be able to delete and modify', () => {

		const uri = _readonlyPrefix+'/test(0)/row(1)/col(0)/data(0)';
		const data0 = readonly.findCellWithURI(uri);
		expect(data0).toBeDefined();
		expect(data0.canBeDeleted()).toBe(true);
		expect(data0.canBeModified()).toBe(true);

	});

	it('should not be able to delete nor modify', () => {

		const uri = _readonlyPrefix+'/test(0)/row(1)/col(0)/readonly(1)';
		const readonly1 = readonly.findCellWithURI(uri);
		expect(readonly1).toBeDefined();
		expect(readonly1.canBeDeleted()).toBe(false);
		expect(readonly1.canBeModified()).toBe(false);

	});

	it('should not be able to delete as it has readonly children but modify', () => {

		const uri = _readonlyPrefix+'/test(0)/row(1)';
		const row = readonly.findCellWithURI(uri);
		expect(row).toBeDefined();
		expect(row.canBeDeleted()).toBe(false);
		expect(row.canBeModified()).toBe(true);

	});

	it('should validate correct content with list', () => {

		const uri = _typesPrefix+'/test(0)/row(0)/col(0)/types(2)';
		const types = typesContent.findCellWithURI(uri);
		expect(types).toBeDefined();

		const list = types.getAttribute('list');
		expect(list).toBeDefined();
		expect(list.cellModel.validates(list.value));

	});

	it('should provide a suitable list of ancestors', () => {

		const uri = _typesPrefix+'/test(0)/row(0)/col(0)/types(2)';
		const types = typesContent.findCellWithURI(uri);
		expect(types).toBeDefined();

		const ancestors = types.getAncestors();
		expect(ancestors).toBeDefined();
		expect(ancestors.length).toBe(4);
		expect(ancestors[0].getAdoptionName()).toBe('col');
		expect(ancestors[1].getAdoptionName()).toBe('row');
		expect(ancestors[2].getAdoptionName()).toBe('test');
		expect(ancestors[3].getAdoptionName()).toBe('');

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