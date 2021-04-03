// RECT . CLASS . SPEC . TS

import { Rect } from './rect.class';

describe('rect.class', () => {

	it('should get correct width and height', () => {

		const rect = new Rect(1, 1, 6, 7);
		expect(rect).toBeDefined();
		expect(rect.width).toBe(5);
		expect(rect.height).toBe(6);

	});

});

/*
 *	  Copyright 2019 Daniel Giribet
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
