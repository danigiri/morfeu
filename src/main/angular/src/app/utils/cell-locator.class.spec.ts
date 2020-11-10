// LOCATOR . CLASS . SPEC . TS


import { Cell } from '../cell.class';
import { Model } from '../model.class';

import { _model, _readonlyPrefix, _readonly, _typesPrefix, _types } from '../test/test.data';


describe('locator.class', () => {

	let typesContent: Cell;


	beforeEach(() => {

		const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
		const MODEL: Model = Object.create(Model.prototype); // to simulate static call

		const model = MODEL.fromJSON(_model);
 
		typesContent = CELL.fromJSON(_types);
		typesContent.associateWith(model, typesContent.cellModelURI);

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
