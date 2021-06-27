// RECTANGLE . INTERFACE . TS

import { Point } from 'app/utils/point.class';

/**
 * 			origin(x,y)				righup(right,y)
 * 				+–––––––––––––––––––––––¬
 * 				|						|
 * 				|						|
 * 				|						|
 *				|		center			|
 * 				|						|
 * 				|						|
 * 				|						|
 *				+–––––––––––––––––––––––+ 
 * 			leftdown(x,bottom)		rightdown(right, bottom)
 * 
 */
export interface Rectangle {

	x: number;
	y: number;
	right: number;
	bottom: number;
	width: number;
	height: number;
	origin: Point;
	center: Point;
	rightUp: Point;
	rightDown: Point;
	leftDown: Point;

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
