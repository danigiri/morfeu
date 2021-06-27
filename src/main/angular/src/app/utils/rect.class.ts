// RECT . CLASS . TS

import { Point } from 'app/utils/point.class';
import { Rectangle } from './rectangle.interface';

export class Rect implements Rectangle {


constructor(public x: number, public y: number, public right: number, public bottom: number) {}


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
	return '{['+this.x+','+this.y+'],['+this.right+','+this.bottom+']}';
}


static centerFrom(r: Rectangle): Point {
	return new Point((r.width/2)+r.x, (r.height/2)+r.y);
}


static rightUpFrom(r: Rectangle): Point {
	return new Point(r.right, r.y);
}


static rightDownFrom(r: Rectangle): Point {
	return new Point(r.right, r.bottom);
}


static leftDownFrom(r: Rectangle): Point {
	return new Point(r.x, r.bottom);
}


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
