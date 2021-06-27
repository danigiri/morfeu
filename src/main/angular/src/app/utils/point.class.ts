// POINT . CLASS . TS

export class Point {


constructor(public x: number, public y: number) {}


quadrantOf(p: Point) {

	if (p.x===this.x && p.y===this.y) {
		return Quadrant.EQ;
	}
	if (p.x<this.x && p.y>this.y) {
		return Quadrant.LU;
	}
	if (p.x>this.x && p.y>this.y) {
		return Quadrant.RU;
	}
	if (p.x>this.x && p.y<this.y) {
		return Quadrant.RD;
	}

	return Quadrant.LD;

}

}

/**
	This is an enumerator to represent quadrants in respect to a point
	LU	|	RU
	----+-----
	LD	|	RD
	EQ: means the points are in the same place, basically equal (not necessarily the same object)
*/
export enum Quadrant {
	EQ,
	LU,
	RU,
	RD,
	LD
}

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
