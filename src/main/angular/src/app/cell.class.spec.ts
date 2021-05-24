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

	it('as a family member it should behave like a cell', () => {

		const uri = _typesPrefix+'/test(0)/row(0)/col(0)/types(1)';
		const types = typesContent.findCellWithURI(uri);
		expect(types).toBeDefined();

		expect(types.isCell()).toBeTrue();
		expect(types.isCellModel()).toBeFalse();
		expect(types.asCell()).toBe(types);
		expect(types.asCellModel()).toBeUndefined();

		const root = types.getRootAncestor();
		expect(root).toBeDefined();
		expect(root.getAdoptionName()).toBe('');
		expect(root.getURI()).toBe('target/test-classes/test-resources/documents/types.xml');

	});

	it('attributes should also have parent information', () => {

		const uri = _typesPrefix+'/test(0)/row(0)/col(0)/types(1)';
		const locator = typesContent.findCellWithURI(uri).attributes[0];
		expect(locator).toBeDefined();

		const parent = locator.getParent();
		expect(parent).toBeDefined();

		const root = locator.getRootAncestor();
		expect(root).toBeDefined();
		expect(root.getAdoptionName()).toBe('');
		expect(root.getURI()).toBe('target/test-classes/test-resources/documents/types.xml');

	});

	it('should give good values for a locator', () => {

		const uri = _typesPrefix+'/test(0)/row(2)/col(0)/types(0)';
		const types = typesContent.findCellWithURI(uri);
		expect(types).toBeDefined();

		const locatorAttribute = types.getAttribute('locator');
		expect(locatorAttribute).toBeDefined();

		const values = locatorAttribute.getPossibleValues();
		expect(values).toBeDefined();
		expect(values.length).toBe(3);
		expect(values.includes('V0')).toBeTrue();
		expect(values.includes('V1')).toBeTrue();
		expect(values.includes('V2')).toBeTrue();
		
	});	

	it('should deep clone cells correctly', () => {

		const uri = _typesPrefix+'/test(0)/row(2)/col(0)/types(0)';
		const types = typesContent.findCellWithURI(uri);
		const typesClone = types.deepClone();
		expect(typesClone).toEqual(types);

	});

	it('should remove the position information from a name', () => {
		
		const name = Cell.removePositionFromName('foo(0)')
		expect(name).toBe('foo');

		const name2 =  Cell.removePositionFromName('foo(1234)');
		expect(name2).toBe('foo');
		
	});

	it('should return links for cells with linked content', () => {

		const uri = _typesPrefix+'/test(0)/row(1)/col(0)/stuff(0)';
		const stuff = typesContent.findCellWithURI(uri);
		expect(stuff).toBeDefined();
		expect(stuff.getLinks()).toEqual([]);

		console.log('FOOOOOOOOOOO')
		const uri2 = _typesPrefix+'/test(0)/row(2)/col(0)/types(0)';
		const types = typesContent.findCellWithURI(uri2);
		expect(types).toBeDefined();
		expect(types.getLinks()).toEqual([]);
		
		expect(types.getAttribute('locator').getLinks()).toEqual([stuff]);
		
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