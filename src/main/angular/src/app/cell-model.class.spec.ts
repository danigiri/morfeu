// CELL-MODEL . CLASS . SPEC . TS

import { Model } from './model.class';

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
		expect(model.child('test').getPresentationAllContent()).toBe('_name=test')
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

		const ac: [string, string[]] = cm.getAttributesByCategory();
		expect(ac).toBeDefined();
		expect(ac.length).toBe(2);
		expect(ac['X'].length).toBe(2);
		expect(ac['Y'].length).toBe(2);

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