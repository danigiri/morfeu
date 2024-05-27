// CELL-MODEL . CLASS . SPEC . TS

import { Model } from './model.class';
import { CellModel } from './cell-model.class';

import { _model, _readonly } from './test/test.data';


describe('cell-model.class', () => {

	let model: Model;

	beforeEach(() => {

		const MODEL: Model = Object.create(Model.prototype); // to simulate static call
		model = MODEL.fromJSON(_model);

	});

	it('child should return correct children', () => {

		expect(model.child('xxx')).toBeUndefined();
		expect(model.child('test')).toBeDefined();
		expect(model.child('test').name).toBe('test');

	});

	it('getPresentationAllContent for test should be correct', () => {
		expect(model.child('test').getCellPresentationAllContent()).toBe('_name=test')
	});

	it('should have a correct category cell modell example', () => {

		const cm = model.findCellModel('target/test-classes/test-resources/models/test-model.xsd/test/row/col/categ');
		expect(cm).toBeDefined();
		expect(cm.name).toBe('categ');
		expect(cm.attributes).toBeDefined();
		expect(cm.attributes.length).toBe(4);
		expect(cm.getAttributesInCategory(undefined).length).toBe(0);	// no attributes without a category
		expect(cm.getAttributesInCategory('X').length).toBe(2);
		expect(cm.getAttributesInCategory('Y').length).toBe(2);
	
	});

	it('should have the right category attributes', () => {

		const cm = model.findCellModel('target/test-classes/test-resources/models/test-model.xsd/test/row/col/categ');
		expect(cm).toBeDefined();

		const categories = cm.getCategories();
		expect(categories.length).toBe(2);
		expect(categories.indexOf('X')).not.toBe(-1);
		expect(categories.indexOf('Y')).not.toBe(-1);

		const ac:  Map<string, CellModel[]> = cm.getAttributesByCategory();
		expect(ac).toBeDefined();
		expect(ac.size).toBe(2);
		expect(ac.get('X').length).toBe(2);
		expect(ac.get('Y').length).toBe(2);

	});

	it('should validate regexs', () => {

		const cm = model.findCellModel('target/test-classes/test-resources/models/test-model.xsd/test/row/col/data3');
		expect(cm).toBeDefined();

		const color = cm.getAttribute('color');
		expect(color).toBeDefined();
		expect(color.validates('FF00AA')).toBeTrue;
		expect(color.validates('12aacc')).toBeTrue;
		expect(color.validates('12aaccx')).toBeFalse;
		expect(color.validates('12aacx')).toBeFalse;

	});

	it('should validate regexs', () => {

		const cm = model.findCellModel('target/test-classes/test-resources/models/test-model.xsd/test/row/col/data3');
		expect(cm).toBeDefined();

		const color = cm.getAttribute('color');
		expect(color).toBeDefined();
		expect(color.validates('FF00AA')).toBeTrue;
		expect(color.validates('12aacc')).toBeTrue;
		expect(color.validates('12aaccx')).toBeFalse;
		expect(color.validates('12aacx')).toBeFalse;

	});

	it('should validate lists', () => {

		const cm = model.findCellModel('target/test-classes/test-resources/models/test-model.xsd/test/row/col/types');
		expect(cm).toBeDefined();

		const list = cm.getAttribute('list');
		expect(list).toBeDefined();
		expect(list.validates('A0')).toBeTrue;
		expect(list.validates('A1')).toBeTrue;
		expect(list.validates('A2')).toBeTrue;
		expect(list.validates('')).toBeFalse;
		expect(list.validates('Ax')).toBeFalse;

	});

	it('as a family member it should behave like a cell model', () => {

		const cm = model.findCellModel('target/test-classes/test-resources/models/test-model.xsd/test/row/col/types');
		expect(cm).toBeDefined();

		expect(cm.isCell()).toBeFalse();
		expect(cm.isCellModel()).toBeTrue();
		expect(cm.asCellModel()).toBe(cm);
		expect(cm.asCell()).toBeUndefined();

		const root = cm.getRootAncestor();
		expect(root).toBeDefined();
		expect(root.getAdoptionName()).toBe('');
		expect(root.getURI()).toBe('target/test-classes/test-resources/models/test-model.xsd');
		
	});


	it('can only have links when', () =>  {

		const stuff = model.findCellModel('target/test-classes/test-resources/models/test-model.xsd/test/row/col/stuff');
		expect(stuff).toBeDefined();
		expect(stuff.canLink).toBeFalse();

		const types = model.findCellModel('target/test-classes/test-resources/models/test-model.xsd/test/row/col/types');
		expect(types).toBeDefined();
		expect(types.canLink).toBeFalse();

		expect(types.getAttribute('locator').canLink).toBeTrue();
		
	});

	it('normalised model references should point to original cell model', () => {

		const pref = 'target/test-classes/test-resources/models/test-model.xsd';
		let data2 = model.findCellModel(pref+'/test/row/col/data2');
		// data2 is a reference to data
		expect(data2).toBeDefined();
		expect(data2.isReference).toBeTrue();
		expect(data2.referenceURI).toBe(pref+'/test/row/col/data');

		let rowRef = model.findCellModel(pref+'/test/row/col/holderWell/row');
		expect(rowRef).toBeDefined();
		expect(rowRef.isReference).toBeTrue();
		expect(rowRef.referenceURI).toBe(pref+'/test/row');
		// as we have not normalised the references, the row reference children are empty
		expect(rowRef.children).toEqual([]);

		model.normaliseReferences();

		data2 = model.findCellModel(pref+'/test/row/col/data2');
		const data = model.findCellModel(pref+'/test/row/col/data');
		expect(data2.children).toBe(data.children);		
		
		rowRef = model.findCellModel(pref+'/test/row/col/holderWell/row');
		const row = model.findCellModel(pref+'/test/row');
		expect(rowRef.children).toBe(row.children);		
		
	});

});

/*
 *	Copyright 2024 Daniel Giribet
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