// BASIC-RECT . CLASS . SPEC . TS

import { BasicRect } from './basic-rect.class';
import { Point } from './point.class';

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

	it('should return the closes point between two rects', () => {
		const r0 = new BasicRect(0, 0, 1, 1);
		
		expect(r0.closestPoint(r0)).toEqual(r0.center);
		
		// RU
		const ru = new BasicRect(1, -2, 2, -1);
		expect(r0.closestPoint(ru)).toEqual(r0.rightUp);
		
		// RC (near up)
		const rc1 = new BasicRect(1.9, -0.1, 2.9, 0.9);
		expect(r0.closestPoint(rc1)).toEqual(r0.rightCenter);

		// RC (near down)
		const rc2 = new BasicRect(1.1, 0.6, 1.1+0.5, 0.6+0.5);
		expect(r0.closestPoint(rc2)).toEqual(r0.rightCenter);

		// RD
		const rd = new BasicRect(2, 2, 3, 3);
		expect(r0.closestPoint(rd)).toEqual(r0.rightDown);

		// CD (near right)
		const cd1 = new BasicRect(0.6, 1.1, 0.6+0.5, 1.1+0.5);
		expect(r0.closestPoint(cd1)).toEqual(r0.centerDown);



		// LU
		const lu = new BasicRect(-2, -2, -1, -1);
		expect(r0.closestPoint(lu)).toEqual(r0.origin);

		// LE
		const r1le = new BasicRect(-0.9, -0.9, -0.1, 0.9);
		expect(r0.closestPoint(r1le)).toEqual(r0.leftCenter);

		
		}
	);


	it('should return a correct vector between two rects', () => {}
	);


});

/*
 *	  Copyright 2024 Daniel Giribet
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
