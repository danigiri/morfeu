// LINKS . COMPONENT . TS

import { AfterViewInit, Component, ElementRef, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { filter, tap } from 'rxjs/operators';


import { Arrows } from './arrows.class';
import { Rect } from '../../utils/rect.class';

import { CellLinkEvent } from '../../events/cell-link.event';
import { EventListener } from '../../events/event-listener.class';
import { Arrow } from './arrow.class';
import { Point, Quadrant } from '../../utils/point.class';

@Component({
	selector: 'links',
	templateUrl: './links.component.svg',
	styles: [`
		.arrows {
			position: absolute;
			padding: 0;
			margin: 0;
  			left: 0;
  			top: 0;
			width: 100%;
			height: 100%;
			z-index: -1;
			transform: translateX(0);
		}
	`]
})

/** Component to hold references between cells */
export class LinksComponent extends EventListener implements AfterViewInit, OnChanges, OnInit {

private arrows: Arrows = new Arrows();	// we are assuming this will get initialised before input sets

@Input() id: string;
@Input() arrowsList: Arrow[] = this.arrows.list;	// default input, not supposed to be overriden


ngOnInit() {

	console.debug('LinksComponent::ngOnInit() – pre register');
	// we registger to events returing with the complete information so we can draw the arrows
	this.register(this.events.service.of<CellLinkEvent>(CellLinkEvent)
			.pipe(
//				tap(link => console.debug(link, link.destRect!==undefined)),
				filter(link => link.destRect!==undefined))
			//.pipe(filter(link => !this.links.has(link.destination)))
			.subscribe(link => this.addArrow(link.sourceRect, link.destRect))
	);
			
}
		
		
ngAfterViewInit() {}


ngOnChanges(changes: SimpleChanges) {
	//console.log('c:', this.x, this.y);
}


viewBox(): string {
	return '0 0 '+this.maxX()+' '+this.maxY();
}


style(): string {
	return 'width: '+this.maxX()+', height: '+this.maxY();
}


maxX(): number {
	
	const max = this.arrows.maxX;

	return max===0 ? 50 : max;

}


maxY(): number {

	const max = this.arrows.maxY;

	return max===0 ? 50 : max;
	
}


private addArrow(source: Rect, destination: Rect) {
	
	//console.log(new Error().stack);
	// from the source element and dest element we can create an arrow
	console.log('arrow:', source.center.x, source.center.y,'-->', destination.center.x, destination.center.y);
	let closest: Point;
	switch (source.center.quadrantOf(destination.center)) {
		case Quadrant.LU:
			console.debug('aaaaa')
			closest = destination.rightDown;
			break;
		case Quadrant.RU:
			console.debug('bbbbb')
			closest = destination.leftDown;
			break;
		case Quadrant.RD:
			console.debug('ccccc')
			closest = destination.origin;
			break;
		case Quadrant.LD:
			console.debug('ddddd')
			closest = destination.rightUp;
			break;
	
		}
	//const arrow = ;
	Promise.resolve(null).then(() => 
		this.arrows.push(new Arrow(source.center.x, source.center.y, closest.x, closest.y))
	);	// this will modify the SVG template state
	/*
	this.links.set(destination, arrow);
		console.log('>>'+arrow);
	*/
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
