// POINT . CLASS . SPECT . TS

import { Point, Quadrant } from './point.class';
import { assert } from 'node:console';


describe('point.class', () => {

	it('should have correct attributes', () => {
		
		const p = new Point(1, 2);
		expect(p).toBeDefined();
		expect(p.x).toBe(1);
		expect(p.y).toBe(2);

	});

	it('should give the right quadrant', () => {
		
		const p = new Point(0, 0);
		expect(p.quadrantOf(p)).toBe(Quadrant.EQ);

		const p0 = new Point(-1, -1);
		expect(p.quadrantOf(p0)).toBe(Quadrant.LU);
		
		const p0u = new Point(0, -1);
		expect(p.quadrantOf(p0u)).toBe(Quadrant.CU);
		
		const p1 = new Point(1, -1);
		expect(p.quadrantOf(p1)).toBe(Quadrant.RU);
		
		const p1r = new Point(1, 0);
		expect(p.quadrantOf(p1r)).toBe(Quadrant.RC);
		
		const p2 = new Point(1, 1);
		expect(p.quadrantOf(p2)).toBe(Quadrant.RD);
		
		const p2d = new Point(0, 1);
		expect(p.quadrantOf(p2d)).toBe(Quadrant.CD);
		
		const p3 = new Point(-1, 1);
		expect(p.quadrantOf(p3)).toBe(Quadrant.LD);
		
		const p3l = new Point(-1, 0);
		expect(p.quadrantOf(p3l)).toBe(Quadrant.LC);
		
		/*
		*/
	});

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
