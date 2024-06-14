// ARROWS . CLASS . SPEC . TS

import { Arrow } from './arrow.class';
import { Arrows } from './arrows.class';

describe('arrows.class', () => {

it('should add selectively', () => {

	let arrows = new Arrows();
	const a0 = new Arrow (0.0, 0.0, 1.0, 2.0);
	expect(arrows.push(a0)).toBeTrue();
	expect(arrows.list.length).toBe(1);
	expect(arrows.push(a0)).toBeFalse();
	expect(arrows.list.length).toBe(1);

	const a1 = new Arrow (0.0, 0.0, 1.0, 2.0);
	expect(arrows.push(a1)).toBeFalse();
	expect(arrows.list.length).toBe(1);

	const a2 = new Arrow (0.0, 0.0, 0.1, 0.1);
	expect(arrows.push(a2)).toBeTrue();
	expect(arrows.list.length).toBe(2);

});

it('should handle min and max with no values', () => {

	const arrows = new Arrows();
	expect (arrows.minX).toBe(0.0);
	expect (arrows.minY).toBe(0.0);
	expect(arrows.maxX).toBe(0.0);
	expect(arrows.maxY).toBe(0.0);

});

it('should handle min and max', () => {

	let arrows = new Arrows();
	const a0 = new Arrow (0.0, 0.0, 1.0, 2.0);
	expect(arrows.push(a0)).toBeTrue();
	expect(arrows.minX).toBe(0.0);
	expect(arrows.minY).toBe(0.0);
	expect(arrows.maxX).toBe(1.0);
	expect(arrows.maxY).toBe(2.0);

	const a1 = new Arrow (0.0, 0.0, 0.1, 0.1);
	expect(arrows.push(a1)).toBeTrue();
	expect(arrows.minX).toBe(0.0);
	expect(arrows.minY).toBe(0.0);
	expect(arrows.maxX).toBe(1.0);
	expect(arrows.maxY).toBe(2.0);

	const a2 = new Arrow (-1.0, 0.0, 1.1, 0.1);
	expect(arrows.push(a2)).toBeTrue();
	expect(arrows.minX).toBe(-1.0);
	expect(arrows.minY).toBe(0.0);
	expect(arrows.maxX).toBe(1.1);
	expect(arrows.maxY).toBe(2.0);

	expect(arrows.remove(a2)).toBeTrue();
	expect(arrows.minX).toBe(0.0);
	expect(arrows.minY).toBe(0.0);
	expect(arrows.maxX).toBe(1.0);
	expect(arrows.maxY).toBe(2.0);

});

it('should handle min and max more cases', () => {

	let arrows = new Arrows();
	const a0 = new Arrow (0.0, 0.0, 1.0, 2.0);
	expect(arrows.push(a0)).toBeTrue();
	expect(arrows.minX).toBe(0.0);
	expect(arrows.minY).toBe(0.0);
	expect(arrows.maxX).toBe(1.0);
	expect(arrows.maxY).toBe(2.0);

	const a1 = new Arrow (0.0, 0.0, 0.1, 3.0);
	expect(arrows.push(a1)).toBeTrue();
	expect(arrows.minX).toBe(0.0);
	expect(arrows.minY).toBe(0.0);
	expect(arrows.maxX).toBe(1.0);
	expect(arrows.maxY).toBe(3.0);

});

it('should handle min and max pointing in the other direction', () => {

	let arrows = new Arrows();
	const a0 = new Arrow (1.0, 2.0, 0.0, 0.0);
	expect(arrows.push(a0)).toBeTrue();
	expect(arrows.minX).toBe(0.0);
	expect(arrows.minY).toBe(0.0);
	expect(arrows.maxX).toBe(1.0);
	expect(arrows.maxY).toBe(2.0);

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