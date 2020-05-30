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

	it('should not allow single children to move in the parent', () => {

		let content = Content.fromCell(CELL.fromJSON(_content1));
		content.associateFromRoot(model);

		const uri = _document1Prefix+'/test(0)/row(0)/col(0)/data(0)';
		const data = content.findCellWithURI(uri);
		const uri2 = _document1Prefix+'/test(0)/row(0)/col(0)';
		const col = content.findCellWithURI(uri2);
		expect(Adoption.isNotOurSingleChild(col, data)).toBeFalse();


	});

	it('should not allow more children than there should be', () => {

		let content = Content.fromCell(CELL.fromJSON(_content1));
		content.associateFromRoot(model);

		const uri = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(1)/data2(0)';
		const data2 = content.findCellWithURI(uri);
		const uri2 = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(1)';
		const col = content.findCellWithURI(uri2);
		expect(Adoption.weHaveRoomForOneMore(col, data2)).toBeFalse();

	});

	it('should not allow more children if there is room', () => {

		let content = Content.fromCell(CELL.fromJSON(_content1));
		content.associateFromRoot(model);

		const uri = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(1)/data2(0)';
		const data2 = content.findCellWithURI(uri);
		const uri2 = _document1Prefix+'/test(0)/row(0)/col(1)/row(0)/col(0)';
		const col = content.findCellWithURI(uri2);
		expect(Adoption.weHaveRoomForOneMore(col, data2)).toBeTrue();

	});



});