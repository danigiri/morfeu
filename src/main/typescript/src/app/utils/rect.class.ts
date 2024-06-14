// RECT . CLASS . TS

import { Point, Quadrant } from './point.class';
import { Vector2D } from './vector-2d.class';

/**
 * 		origin(x,y)					centerup(center.x,y)				rightup(right,y)
 * 				+–––––––––––––––––––––––-––––––------––-–––----–––––––––––––¬
 * 				|															|
 * 				|															|
 * 				|															|
 * 				|															|
 * 				|															|
 *		leftcenter(x,center.y)				center							|	rightcenter(right, center.x)
 * 				|															|
 * 				|															|
 * 				|															|
 * 				|															|
 * 				|															|
 *				+––––––––––––––––––––––--–––––––––––––––––––––––––––––––––––+ 
 * 		leftdown(x,bottom)			centerdown(center.x,bottom)			rightdown(right, bottom)
 * 
 */
export abstract class Rect {

abstract get x(): number;
abstract get y(): number;
abstract get right(): number;
abstract get bottom(): number;


get origin(): Point {
	return new Point(this.x, this.y);
}


get width(): number {
	return this.right - this.x;
}


get height(): number {
	return this.bottom - this.y;
}


get center(): Point {
	return new Point((this.width/2)+this.x, (this.height/2)+this.y);
}


get centerUp(): Point {
	return new Point(this.center.x, this.y);
}


get rightUp(): Point {
	return new Point(this.right, this.y);
}


get rightCenter(): Point {
	return new Point(this.right, this.center.y);
}


get rightDown(): Point {
	return new Point(this.right, this.bottom);
}


get centerDown(): Point {
	return new Point(this.center.x, this.bottom);
}


get leftDown(): Point {
	return new Point(this.x, this.bottom);
}


get leftCenter(): Point {
	return new Point(this.x, this.center.y);
}

/**  
 * return which is the closest point of the current rect in respect to the input rect 
 * a) if rects are the same, return center
 * c) if param rect is outside, return closes point in quadrant of this center with param center
 *	 	+–––––––+   +–––––––+
 * 		| this  |	|r		|
 * 		|   ––––––––>		|
 * 		|       |	|		|
 *		+–––––––+	+–––––––+
 *		
 * */
public closestPoint(r: Rect): Point {
	// note we use the reverse
	switch(this.center.quadrantOf(r.center)) {
		case Quadrant.EQ:
			return this.center;
		case Quadrant.RU || Quadrant.RC:
				if (this.rightUp.y<=r.center.y) { //r center is right up but close to the this center
					return this.rightCenter;
				}
				return this.rightUp;
		case Quadrant.RD:
				if (this.rightDown.y>=r.center.y) {
					return this.rightCenter;
				} else if (this.rightDown.x>=r.center.x) {
					return this.centerDown;
				}
				return this.rightDown;
		case Quadrant.LU || Quadrant.LC:
			if (this.origin.y<=r.center.y) { //r center is left up but close to the this center
				return this.leftCenter;
			}
			return this.origin;
		case Quadrant.LD:
			return this.leftDown;
	}

	return undefined;
}


/** @returns a best effort fast implementation of a vector joining this rect to the parameter rect
*/
public fastVectorTo(r: Rect): Vector2D {
	return undefined;
}

public equals(r: Rect): boolean {
	return (r!=null && this.x===r.x && this.y===r.y && this.right===r.right && this.bottom===r.bottom);
}

public toString = (): string => { 
	return '{['+this.x+','+this.y+'],['+this.right+','+this.bottom+']}';
}



}

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
