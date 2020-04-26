// BREADCRUMP . COMPONENT . TS

import { Component,OnInit } from '@angular/core';
import { filter } from 'rxjs/operators';

import { Cell } from '../../cell.class';

import { CellActivatedEvent } from '../../events/cell-activated.event';
import { CellActivateEvent } from '../../events/cell-activate.event';
import { CellDeactivatedEvent } from '../../events/cell-deactivated.event';
import { CellDocumentClearEvent } from '../../events/cell-document-clear.event';
import { CellDocumentLoadedEvent } from '../../events/cell-document-loaded.event';
import { EventListener } from "../../events/event-listener.class";
import { EventService } from '../../services/event.service';

@Component({
	selector: 'breadcrumb',
	templateUrl: './breadcrumb.component.html',
	styles: [`
		#breadcrumb {}
	`]
})

export class BreadcrumbComponent extends EventListener implements OnInit {

documentName: string;
uriElements: string[];
name: string;

constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {


	this.register(this.events.service.of<CellDocumentClearEvent>(CellDocumentClearEvent)
			.subscribe(() => this.documentName = undefined)
	);

	this.register(this.events.service.of<CellDocumentLoadedEvent>(CellDocumentLoadedEvent)
			.subscribe(loaded => this.documentName = loaded.document.name )
	);

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