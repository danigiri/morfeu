/// ARROW . CLASS . TS

export class Arrow {

public static readonly PI180 = 180 / Math.PI;

public path: string;
public transform: string;


constructor(public sx: number, 		// The x position of the (padded) starting point
			public sy: number, 		// The y position of the (padded) starting point
			public cx: number,		// The x position of the (padded) center point
			public cy: number,		// The y position of the (padded) center point
			public ex: number, 		// The x position of the (padded) ending point
			public ey: number,		// The y position of the (padded) ending point
			public ae: number,		// The angle (in radians) for an ending arrowhead
			public as: number,		// The angle (in radians) for a starting arrowhead
			public ec: number,		// The angle (in radians) for a center arrowhead
			) {

	this.path =  'M'+sx+','+sy+' Q'+cx+','+cy+' '+ex+','+ey;
	this.transform = 'translate('+ex+','+ey+') rotate('+ae*Arrow.PI180+')';

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
