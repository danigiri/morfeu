import { Cell } from './cell.class';
import { Model } from './model.class';

import { _model, _readonly } from './test/test.data';


describe('Cell', () => {

	const prefix = 'target/test-classes/test-resources/documents/readonly.xml';
	let cell: Cell;

	beforeEach(() => {
		const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
		const MODEL: Model = Object.create(Model.prototype); // to simulate static call
		//wget -O - http://localhost:3000/dyn/content/target/test-classes/test-resources/documents/readonly.xml\?model\=target/test-classes/test-resources/models/test-model.xsd | jq > foo.json

		cell = CELL.fromJSON(_readonly);
		const model = MODEL.fromJSON(_model);
		cell.associateWith(model, cell.cellModelURI);

	});

	it('should read from json', () => {

		expect(cell).toBeDefined();
		expect(cell.desc).toBe('DESC');
		expect(cell.isSimple).toBe(false);
		expect(cell.value).toBeUndefined();

	});

	it('should find cell with correct URI', () => {

		const uri = prefix+'/test(0)/row(1)/col(0)/readonly(2)';
		const foundCell = cell.findCellWithURI(uri);
		expect(foundCell).toBeDefined();
		expect(foundCell.URI).toBe(uri);

		const uri2 = prefix+'/test(111)/row(1)/col(0)/readonly(2)';
		const foundCell2 = cell.findCellWithURI(uri2);
		expect(foundCell2).toBeUndefined();

	});

	it('should generate a correct all presentation', () => {

		const uri = prefix+'/test(0)/row(1)/col(0)/data(0)';
		let data0 = cell.findCellWithURI(uri);
		expect(data0).toBeDefined();
		data0.value = 'VALUE'; 
		const presentationAllContent = data0.getPresentationAllContent();
		expect(presentationAllContent).toBe('_name=data&_value=VALUE&number=1');

	});

	it('should be able to delete and modify', () => {

		const uri = prefix+'/test(0)/row(1)/col(0)/data(0)';
		const data0 = cell.findCellWithURI(uri);
		expect(data0).toBeDefined();
		expect(data0.canBeDeleted()).toBe(true);
		expect(data0.canBeModified()).toBe(true);

	});

	it('should not be able to delete nor modify', () => {

		const uri = prefix+'/test(0)/row(1)/col(0)/readonly(1)';
		const readonly1 = cell.findCellWithURI(uri);
		expect(readonly1).toBeDefined();
		expect(readonly1.canBeDeleted()).toBe(false);
		expect(readonly1.canBeModified()).toBe(false);

	});

	it('should not be able to delete as it has readonly children but modify', () => {

		const uri = prefix+'/test(0)/row(1)';
		const row = cell.findCellWithURI(uri);
		expect(row).toBeDefined();
		expect(row.canBeDeleted()).toBe(false);
		expect(row.canBeModified()).toBe(true);

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