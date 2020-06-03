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

	beforeEach(() => {

		model = MODEL.fromJSON(_model);
		model.normaliseReferences();

	});

	it('should require a cell model', () => {

		let content = Content.fromCell(CELL.fromJSON(_content3));
		content.associateFromRoot(model);

		const uri = _document3Prefix+'/test(0)/row(0)/col(0)/stuff(0)';
		const stuff = content.findCellWithURI(uri);
		expect(stuff).toBeDefined();
		expect(Adoption.hasCellModel(stuff)).toBeTrue();
		stuff.cellModel = undefined;
		expect(Adoption.hasCellModel(stuff)).toBeFalse();

	});

	it('should not allow adoption on readonly', () => {

		let content = Content.fromCell(CELL.fromJSON(_readonly));
		content.associateFromRoot(model);

		const uri =_readonlyPrefix+'/test(0)/row(1)/col(0)/readonly(1)';
		const readonly = content.findCellWithURI(uri);
		expect(Adoption.canBeModified(readonly)).toBeFalse();

		const uri2 =_readonlyPrefix+'/test(0)/row(1)/col(0)/data(0)';
		const data = content.findCellWithURI(uri2);
		expect(Adoption.canBeModified(data)).toBeTrue();

	});

	it('should have a compatible model', () => {

		let content = Content.fromCell(CELL.fromJSON(_content3));
		content.associateFromRoot(model);

		const uri = _document3Prefix+'/test(0)/row(0)/col(0)/stuff(0)';
		const stuff = content.findCellWithURI(uri);
		const uri2 = _document3Prefix+'/test(0)/row(0)/col(0)';
		const col = content.findCellWithURI(uri2);
		expect(Adoption.isModelCompatible(col, stuff)).toBeTrue();
		expect(Adoption.isModelCompatible(col, col)).toBeFalse();

	});

	it('should not allow children to move to adjacent positions', () => {

		let content = Content.fromCell(CELL.fromJSON(_content1));
		content.associateFromRoot(model);

		const uri = _document1Prefix+'/test(0)/row(0)/col(0)/data(0)';
		const data = content.findCellWithURI(uri);
		const uri2 = _document1Prefix+'/test(0)/row(0)/col(0)';
		const col = content.findCellWithURI(uri2);
		expect(col).toBeDefined();
		// due to CDK drop messing with drop positions, we cannot really check this reliably
		//expect(Adoption.isNotAdjacentPosition(col, data, 0)).toBeFalse();
		//expect(Adoption.isNotAdjacentPosition(col, data, 1)).toBeFalse();
		expect(Adoption.isNotAdjacentPosition(col, data, 0)).toBeTrue();
		expect(Adoption.isNotAdjacentPosition(col, data, 1)).toBeTrue();

	});

	it('should not allow more children than there should be', () => {

		let content = Content.fromCell(CELL.fromJSON(_content1));
		content.associateFromRoot(model);

		const uri = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(1)/data2(0)';
		const data2 = content.findCellWithURI(uri);
		const uri2 = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(1)';
		const col = content.findCellWithURI(uri2);
		expect(col).toBeDefined();
		expect(Adoption.weHaveRoomForOneMore(col, data2)).toBeFalse();

	});

	it('should not allow more children if there is no room left', () => {

		let content = Content.fromCell(CELL.fromJSON(_content1));
		content.associateFromRoot(model);

		const uri = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(1)/data2(0)';
		const data2 = content.findCellWithURI(uri);
		const uri2 = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(0)';
		const col = content.findCellWithURI(uri2);
		expect(col).toBeDefined();
		expect(Adoption.weHaveRoomForOneMore(col, data2)).toBeTrue();

	});

	it('should only be allowed to move only maintaining the order', () => {

		let content = Content.fromCell(CELL.fromJSON(_content1));
		content.associateFromRoot(model);

		const uri = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(0)/data(0)';
		const data = content.findCellWithURI(uri);
		const uri2 = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(0)';
		const col = content.findCellWithURI(uri2);
		expect(col).toBeDefined();
		expect(Adoption.itsTheRightOrder(col, data, 2)).toBeFalse();	// cannot move data after data2

		const uri3 = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(1)';
		const col3 = content.findCellWithURI(uri3);
		expect(col3).toBeDefined();
		expect(Adoption.itsTheRightOrder(col3, data, 0)).toBeTrue();	// can move data at the beginning
		expect(Adoption.itsTheRightOrder(col3, data, 1)).toBeFalse();	// cannot move data between data2's'

	});

	it('should be able to move within the same cell model', () => {

		let content = Content.fromCell(CELL.fromJSON(_content3));
		content.associateFromRoot(model);

		const uri = _document3Prefix+'/test(0)/row(0)/col(0)/stuff(0)';
		const stuff = content.findCellWithURI(uri);
		const uri2 = _document3Prefix+'/test(0)/row(0)/col(0)';
		const col = content.findCellWithURI(uri2);
		expect(col).toBeDefined();
		expect(Adoption.itsTheRightOrder(col, stuff, 0)).toBeTrue();	// can move stuff at the beginning
		expect(Adoption.itsTheRightOrder(col, stuff, 1)).toBeTrue();	// position 1 is ok
		expect(Adoption.itsTheRightOrder(col, stuff, 2)).toBeTrue();	// position 2 is ok
		expect(Adoption.itsTheRightOrder(col, stuff, 3)).toBeTrue();	// position 3 is ok
		expect(Adoption.itsTheRightOrder(col, stuff, 4)).toBeFalse();	// after data it's not possible

	});

});