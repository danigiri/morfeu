// MODEL . CLASS . SPEC . TS

import { Model } from './model.class';

import { _model } from './test/test.data';

describe('model.class', () => {

	const pref = 'target/test-classes/test-resources/models/test-model.xsd';
	const MODEL: Model = Object.create(Model.prototype); // to simulate static call
	let model: Model;


	beforeEach(() => {

		model = MODEL.fromJSON(_model);

	});

	it('serialise and deserialise should create a successful model', () => {
	
		expect(model).toBeDefined();
		const json1 = model.toJSON();
		expect(json1).toBeDefined();
		const model1 = MODEL.fromJSON(json1);
		expect(model1).toBeDefined();

	});

	
	it('after normalisation, serialise and deserialise should create a successful model', () => {
		// after normalisation it should also work, and not create an infinite loop
		model.normaliseReferences();
		const json2 = model.toJSON();
		expect(json2).toBeDefined();
		const model2 = MODEL.fromJSON(json2);
		expect(model2).toBeDefined();

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
