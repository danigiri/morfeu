

export class Arrow {

public static readonly PI180 = 180 / Math.PI;

public path: string;
public transform: string;

constructor(public sx: number, 	// The x position of the (padded) starting point
			public sy: number, 	// The y position of the (padded) starting point
			public cx: number,		// The x position of the (padded) center point
			public cy: number,		// The y position of the (padded) center point
			public ex: number, 	// The x position of the (padded) ending point
			public ey: number,		// The y position of the (padded) ending point
			public ae: number,		// The angle (in radians) for an ending arrowhead
			public as: number,		// The angle (in radians) for a starting arrowhead
			public ec: number) {	// The angle (in radians) for a center arrowhead

	this.path =  'M'+sx+','+sy+' Q'+cx+','+cy+' '+ex+','+ey;
	this.transform = 'translate('+ex+','+ey+') rotate('+ae*Arrow.PI180+')';

}


}