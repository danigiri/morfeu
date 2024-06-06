// RECT . CLASS . TS

import { Point } from './point.class';
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
	return new Point(this.x, this.center.y);71
}


/** @returns a best effort fast implementation of a vector joining this rect to the parameter rect
*/
public fastVectorTo(r: Rect): Vector2D {
	return undefined;
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
