// ADOPTION . CLASS . SPEC . TS

import { Adoption } from './adoption.class';
import { Cell } from './cell.class';
import { Content } from './content.class';
import { Model } from './model.class';

import { _model, _readonly, _readonlyPrefix, _content1, _document1Prefix, _content3, 
		_document3Prefix  } from './test/test.data';


describe('adoption.class', () => {

	const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
	const MODEL: Model = Object.create(Model.prototype); // to simulate static call
	let model: Model;
	let content1: Content;
	let content3: Content;
	let readonly: Content;

	beforeEach(() => {

		model = MODEL.fromJSON(_model);
		model.normaliseReferences();

		content3 = Content.fromCell(CELL.fromJSON(_content3));
		content3.associateFromRoot(model);

		readonly = Content.fromCell(CELL.fromJSON(_readonly));
		readonly.associateFromRoot(model);

		content1 = Content.fromCell(CELL.fromJSON(_content1));
		content1.associateFromRoot(model);

	});

	it('should require a cell model', () => {

		const uri = _document3Prefix+'/test(0)/row(0)/col(0)/stuff(0)';
		const stuff = content3.findCellWithURI(uri);
		expect(stuff).toBeDefined();
		expect(Adoption.hasCellModel(stuff)).toBeTrue();
		stuff.cellModel = undefined;
		expect(Adoption.hasCellModel(stuff)).toBeFalse();

	});

	it('should not allow adoption on readonly', () => {

		const uri =_readonlyPrefix+'/test(0)/row(1)/col(0)/readonly(1)';
		const readonlyCell = readonly.findCellWithURI(uri);
		expect(Adoption.canBeModified(readonlyCell)).toBeFalse();

		const uri2 =_readonlyPrefix+'/test(0)/row(1)/col(0)/data(0)';
		const data = readonly.findCellWithURI(uri2);
		expect(Adoption.canBeModified(data)).toBeTrue();

	});

	it('should have a compatible model', () => {

		const uri = _document3Prefix+'/test(0)/row(0)/col(0)/stuff(0)';
		const stuff = content3.findCellWithURI(uri);
		const uri2 = _document3Prefix+'/test(0)/row(0)/col(0)';
		const col = content3.findCellWithURI(uri2);
		expect(Adoption.isModelCompatible(col, stuff)).toBeTrue();
		expect(Adoption.isModelCompatible(col, col)).toBeFalse();

	});

	it('should not allow children to move to adjacent positions', () => {

		const uri = _document1Prefix+'/test(0)/row(0)/col(0)/data(0)';
		const data = content1.findCellWithURI(uri);
		const uri2 = _document1Prefix+'/test(0)/row(0)/col(0)';
		const col = content1.findCellWithURI(uri2);
		expect(col).toBeDefined();
		expect(Adoption.isNotAdjacentPosition(col, data, 0)).toBeFalse();
		expect(Adoption.isNotAdjacentPosition(col, data, 1)).toBeFalse();

	});

	it('should not allow more children if there is no room left', () => {

		const uri = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(1)/data2(0)';
		const data2 = content1.findCellWithURI(uri);
		const uri2 = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(0)';
		const col = content1.findCellWithURI(uri2);
		expect(col).toBeDefined();
		expect(Adoption.weHaveRoomForOneMore(col, data2)).toBeTrue();

	});

	it('should allow moves of children within the same parent, as room limits do not apply', () => {

		const uri = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(1)/data2(0)';
		const data2 = content1.findCellWithURI(uri);
		const col = data2.getParent().asCell();
		expect(Adoption.weHaveRoomForOneMore(col, data2)).toBeTrue();

	});

	it('should only be allowed to move only maintaining the order', () => {

		const uri = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(0)/data(0)';
		const data = content1.findCellWithURI(uri);
		const uri2 = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(0)';
		const col = content1.findCellWithURI(uri2);
		expect(col).toBeDefined();
		expect(Adoption.itsTheRightOrder(col, data, 2)).toBeFalse();	// cannot move data after data2

		const uri3 = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(1)';
		const col3 = content1.findCellWithURI(uri3);
		expect(col3).toBeDefined();
		expect(Adoption.itsTheRightOrder(col3, data, 0)).toBeTrue();	// can move data at the beginning
		expect(Adoption.itsTheRightOrder(col3, data, 1)).toBeFalse();	// cannot move data between data2's'

	});

	it('should be able to move within the same cell model', () => {

		const uri = _document3Prefix+'/test(0)/row(0)/col(0)/stuff(0)';
		const stuff = content3.findCellWithURI(uri);
		const uri2 = _document3Prefix+'/test(0)/row(0)/col(0)';
		const col = content3.findCellWithURI(uri2);
		expect(col).toBeDefined();
		expect(Adoption.itsTheRightOrder(col, stuff, 0)).toBeTrue();	// can move stuff at the beginning
		expect(Adoption.itsTheRightOrder(col, stuff, 1)).toBeTrue();	// position 1 is ok
		expect(Adoption.itsTheRightOrder(col, stuff, 2)).toBeTrue();	// position 2 is ok
		expect(Adoption.itsTheRightOrder(col, stuff, 3)).toBeTrue();	// position 3 is ok
		expect(Adoption.itsTheRightOrder(col, stuff, 4)).toBeFalse();	// after data it's not possible

	});

	it('should be able to move within the same cell model (case 2)', () => {

		let content = Content.fromCell(CELL.fromJSON(_content1));
		content.associateFromRoot(model);

		const uri = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(1)/data2(0)';
		const data2 = content.findCellWithURI(uri);
		expect(data2).toBeDefined();

		const col = data2.getParent().asCell();
		expect(col).toBeDefined();

		expect(Adoption.itsTheRightOrder(col, data2, 0)).toBeTrue();	// all positions should be ok with only this
		expect(Adoption.itsTheRightOrder(col, data2, 1)).toBeTrue();	// method as all 2 elements are data2 model
		expect(Adoption.itsTheRightOrder(col, data2, 2)).toBeTrue();	//

	});


});

/*
 *	  Copyright 2021 Daniel Giribet
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
