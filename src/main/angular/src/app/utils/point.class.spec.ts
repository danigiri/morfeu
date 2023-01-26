// POINT . CLASS . SPECT . TS

import { Point, Quadrant } from 'app/utils/point.class';
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

		const p1 = new Point(1, -1);
		expect(p.quadrantOf(p1)).toBe(Quadrant.RU);

		const p2 = new Point(1, 1);
		expect(p.quadrantOf(p2)).toBe(Quadrant.RD);

		const p3 = new Point(-1, 1);
		expect(p.quadrantOf(p3)).toBe(Quadrant.LD);

	});

});

/*
 *	  Copyright 2021 Daniel Giribet
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
