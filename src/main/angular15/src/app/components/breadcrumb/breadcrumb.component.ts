// BREADCRUMP . COMPONENT . TS

import { Component, Input, OnInit } from '@angular/core';

import { Cell } from '../../cell.class';
import { Content } from '../../content.class';

import { CellActivatedEvent } from '../../events/cell-activated.event';
import { CellDeactivatedEvent } from '../../events/cell-deactivated.event';
import { CellDocumentClearEvent } from '../../events/cell-document-clear.event';
import { ContentFragmentBackEvent } from '../../events/content-fragment-back.event';
import { ContentRefreshedEvent, ContentRefreshed } from '../../events/content-refreshed.event';
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
		#breadcrumb-fragment-back {}
	`]
})

export class BreadcrumbComponent extends EventListener implements OnInit {

visible = false;
isFragment = false;
root: Cell;
uriElements: string[] = [];
cell: Cell;
name: string;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	this.register(this.events.service.of<CellActivatedEvent>(CellActivatedEvent)
			.subscribe(activated => this.displayBreadcrumb(activated.cell))
	);

	this.register(this.events.service.of<ContentRefreshedEvent>(ContentRefreshedEvent)
			.subscribe(refreshed => this.setDisplayMode(refreshed.content, refreshed.what))
	);

	this.register(this.events.service.of<CellDeactivatedEvent>(CellDeactivatedEvent).subscribe(() => this.clear()));

	this.register(this.events.service.of<CellDocumentClearEvent>(CellDocumentClearEvent)
			.subscribe(() => this.visible = false));


}


fragmentBack() {
	this.events.service.publish(new ContentFragmentBackEvent(this.cell));	// we save the changes for now
}

private clear() {

	this.cell = undefined;
	this.name = undefined;
	this.uriElements = [];

}


private displayBreadcrumb(cell: Cell): void {

	this.cell = cell;
	this.name = this.lastURIPart(this.cell.getURI());

	const ancestors = cell.getAncestors();
	// we filter until the root node, and add in reverse order
	this.uriElements = []
	let found = false;
	let i = 0;
	while (!found && i<ancestors.length) {
		const a = ancestors[i++];
		const uri = a.getURI();
		found = uri===this.root?.getURI();
		if (!found) {
			this.uriElements.unshift(this.lastURIPart(uri));
		}
	}

}



private setDisplayMode(content: Content, what: ContentRefreshed) {

	this.root = content;
	this.isFragment = what===ContentRefreshed.FRAGMENT;
	this.clear();
	this.visible = true;

}


private lastURIPart(u: string) {

	const uriElems = u.split("/");

	return uriElems[uriElems.length-1];

}


}