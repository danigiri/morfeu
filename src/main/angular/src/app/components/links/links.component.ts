// LINKS . COMPONENT . TS

import { AfterViewInit, Component, ElementRef, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { filter, tap } from 'rxjs/operators';


import { Arrows } from './arrows.class';
import { Cell } from 'app/cell.class';
import { Rectangle } from 'app/utils/rectangle.interface';

import { CellLinkEvent } from 'app/events/cell-link.event';
import { EventListener } from 'app/events/event-listener.class';
import { CellModel } from 'app/cell-model.class';
import { CellLocator } from 'app/utils/cell-locator.class';
import { Arrow } from './arrow.class';

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

@Input() id: string;
@Input() source: Cell;
@Input() x: number;
@Input() y: number;

links = new Map<Cell, Arrow>();	// destination cell, arrow
arrows: Arrows = new Arrows();


ngOnInit() {

	console.debug('LinksComponent::ngOnInit() – pre register');
	// we registger to events returing with the complete information so we can draw the arrows
	this.register(this.events.service.of<CellLinkEvent>(CellLinkEvent)
			.pipe(filter(link => link.source===this.source && link.destRect!==undefined))
			.pipe(filter(link => !this.links.has(link.destination)))
			.subscribe(link => this.addArrow(link.destRect, link.destination))
	);
			
}
		
		
ngAfterViewInit() {
			
	// now we can draw the arrows, starting from the starting element to the cells, we send a number of events
	// to get the linked components back
	//console.debug('LinksComponent::ngAfterViewInit()');
	let links: Cell[] = [];
	// first we look for potential values in the cell
	links = this.addLinks(this.source, links);
	this.source.attributes?.forEach(a => this.addLinks(a, links));
	// now we fire the appropriate events for all of them
	links.forEach(link => this.events.service.publish(new CellLinkEvent(this.source, link)));

}


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


private addLinks(c: Cell, links: Cell[]): Cell[] {
	
	if (c.cellModel?.presentation===CellModel.ATTR_LOCATOR_PRESENTATION) {
		const root = c.getRootAncestor().asCell();
		CellLocator.findCellsWithLocatorAndValue(root, c.cellModel.valueLocator, c.value).forEach(c => links.push(c));
	}
	
	return links;
	
}


private addArrow(destRect: Rectangle, destination: Cell) {
	
	//console.log(new Error().stack);
	// from the source element and dest element we can create an arrow
	//console.log('source:', this.x, this.y,'dest', destRect);
	const arrow = new Arrow(this.x, this.y, destRect.x, destRect.y);
	this.links.set(destination, arrow);
	Promise.resolve(null).then(() => {
		console.log('>>'+arrow);
		this.arrows.push(arrow);
	});	// this will modify the SVG template state
	
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
