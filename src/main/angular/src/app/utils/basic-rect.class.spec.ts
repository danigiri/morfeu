// BASIC-RECT . CLASS . SPEC . TS

import { BasicRect } from './basic-rect.class';

describe('rect.class', () => {

	it('should have correct points', () => {
		
		const rect = new BasicRect(1, 1, 6, 7);
		expect(rect).toBeDefined();
		expect(rect.origin.x).toBe(1);
		expect(rect.origin.y).toBe(1);

	});


	it('should get correct width and height', () => {

		const rect = new BasicRect(1, 1, 6, 7);
		expect(rect).toBeDefined();
		expect(rect.width).toBe(5);
		expect(rect.height).toBe(6);

	});

	it('should have the correct points', () => {

		const rect = new BasicRect(1, 1, 6, 8);
		expect(rect).toBeDefined();
		expect(rect.origin.x).toBe(1);
		expect(rect.origin.y).toBe(1);
		expect(rect.center.x).toBe(3.5);
		expect(rect.center.y).toBe(4.5);
		expect(rect.rightUp.x).toBe(6);
		expect(rect.rightUp.y).toBe(1);
		expect(rect.rightDown.x).toBe(6);
		expect(rect.rightDown.y).toBe(8);
		expect(rect.leftDown.x).toBe(1);
		expect(rect.leftDown.y).toBe(8);

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
