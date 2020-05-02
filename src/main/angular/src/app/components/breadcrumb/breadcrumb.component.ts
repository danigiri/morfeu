// BREADCRUMP . COMPONENT . TS

import { Component, Input, OnInit } from '@angular/core';

import { Cell } from '../../cell.class';
import { CellDocument } from '../../cell-document.class';

import { CellActivatedEvent } from '../../events/cell-activated.event';
import { CellDeactivatedEvent } from '../../events/cell-deactivated.event';
import { EventListener } from "../../events/event-listener.class";
import { EventService } from '../../services/event.service';

@Component({
	selector: 'breadcrumb',
	templateUrl: './breadcrumb.component.html',
	styles: [`
		#breadcrumb_ {},
		#breadcrumb-document: {}
		.breadcrumb-element: {}
		#breadcrumb-active-name {}
	`]
})

export class BreadcrumbComponent extends EventListener implements OnInit {

@Input() document_: CellDocument;
uriElements: string[];
name: string;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	this.register(this.events.service.of<CellActivatedEvent>(CellActivatedEvent)
			.subscribe(activated => this.displayBreadcrumb(activated.cell))
	);

	this.register(this.events.service.of<CellDeactivatedEvent>(CellDeactivatedEvent).subscribe(() => this.clear()));

}


private clear() {

	this.name = undefined;
	this.uriElements = undefined;

}


private displayBreadcrumb(cell: Cell): void {

	this.name = this.lastURIPart(cell.getURI());
	let ancestors = cell.getAncestors();
	ancestors.pop();	// we don't want to show the root node (the doc name is more usable)
	this.uriElements = ancestors.reverse().map(a => this.lastURIPart(a.getURI()));

}


private lastURIPart(u: string) {

	const uriElems = u.split("/");

	return uriElems[uriElems.length-1];

}


}