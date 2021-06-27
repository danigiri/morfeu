// ELEMENT - RECT . CLASS . TS

import { ElementRef } from '@angular/core';

import { Point } from 'app/utils/point.class';
import { Rect } from 'app/utils/rect.class';
import { Rectangle } from 'app/utils/rectangle.interface';

/** This class encapsulates a native element into a rectangle class, holds no additional state  */
export class ElementRect  implements Rectangle {


public constructor(public e: ElementRef) {}


get origin(): Point {
	return new Point(this.x, this.y);
}


get x(): number {
	return this.e.nativeElement.getBoundingClientRect().x ; //+ document.documentElement.scrollTop;
}


get y(): number {

	return  this.e.nativeElement.getBoundingClientRect().y + document.documentElement.scrollTop;

}


get right(): number {
	return this.x+this.e.nativeElement.clientWidth;		// for some reason the bounding rect does not work
	//return this.e.nativeElement.getBoundingClientRect().right;
}


get bottom(): number {
	return this.y+this.e.nativeElement.clientHeight;	// for some reason the bounding rect does not work
	//return this.e.nativeElement.getBoundingClientRect().y + this.e.nativeElement.getBoundingClientRect().height;
}


get width(): number {
	return this.right - this.x;
}


get height(): number {
	return this.bottom - this.y;
}


get center(): Point {
	return Rect.centerFrom(this);
}


get rightUp(): Point {
	return Rect.rightUpFrom(this);
}


get rightDown(): Point {
	return Rect.rightDownFrom(this);
}


get leftDown(): Point {
	return Rect.leftDownFrom(this);
}


public toString = (): string => {
	return `ElementRect:{${this.x},${this.y},${this.right},${this.bottom}}`;
}


}

/*

static fromElement(element: Element): Rect {

	const elemRect = element.getBoundingClientRect();
	const scrollTop = document.documentElement.scrollTop;
	const x = elemRect.x;
	const y = elemRect.y + scrollTop;
	const right = elemRect.right;
	const bottom = elemRect.top + scrollTop;

	return new Rect(x, y, right, bottom);

}

*/

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
