/// ARROW . CLASS . TS
import { getArrow } from 'perfect-arrows';
import { Vector2D } from 'src/app/utils/vector-2d.class';

/** Arrow class that takes start and end coordinates */
export class Arrow {

private static readonly options = {bow: 0.1};
public static readonly PI180 = 180 / Math.PI;
public readonly pi180 = Arrow.PI180;	// so we can access it from the template

public cx: number;
public cy: number;
public ae: number;
public transform: string;
public path: string;


/**
 * @param sx The x position of the starting point
 * @param sy The y position of the (padded) starting point
 * @param ex The x position of the (padded) ending point
 * @param ey The y position of the (padded) ending point
 */
constructor(public sx: number, public sy: number, public ex: number, public ey: number) {

	const [sx_, sy_, cx_, cy_, ex_, ey_, ae_, as_, ec_] = getArrow(sx, sy, ex, ey, Arrow.options);
	this.cx = cx_;
	this.cy = cy_;
	this.ex = ex_;
	this.ey = ey_;
	this.ae = ae_;
	this.transform = `translate(${ex_},${ey_}) rotate(${ae_ * Arrow.PI180})`;
	this.path = `M${this.sx},${this.sy} Q${this.cx},${this.cy} ${this.ex},${this.ey}`;
}



equals(a: Arrow): boolean {
	return this.sx===a.sx && this.sy===a.sy && this.ex===a.ex && this.ey===a.ey;
}


static from(v: Vector2D): Arrow {
	return new Arrow(v.v0.x, v.v0.y, v.v1.x, v.v1.y);
}


public toString = (): string => {
	return `Arrow:{${this.sx},${this.sy},${this.ex},${this.ey}}`;
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
