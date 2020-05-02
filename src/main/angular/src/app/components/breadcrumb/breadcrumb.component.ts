// BREADCRUMP . COMPONENT . TS

import { Component, Input, OnInit } from '@angular/core';

import { Cell } from '../../cell.class';
import { Content } from '../../content.class';

import { CellActivatedEvent } from '../../events/cell-activated.event';
import { CellDeactivatedEvent } from '../../events/cell-deactivated.event';
import { CellDocumentClearEvent } from '../../events/cell-document-clear.event';
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
	`]
})

export class BreadcrumbComponent extends EventListener implements OnInit {

visible = false;
isFragment = false;
root: Cell;
uriElements: string[] = [];
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


private clear() {

	this.name = undefined;
	this.uriElements = [];

}


private displayBreadcrumb(cell: Cell): void {

	this.name = this.lastURIPart(cell.getURI());
	let ancestors = cell.getAncestors();
	ancestors.pop();	// we don't want to show the root node
	// we filter until the root node, adding in reverse order
	this.uriElements = []
	ancestors.forEach(a => {
							const uri = a.getURI();
							if (uri!==this.root?.getURI()) {
								this.uriElements.unshift(this.lastURIPart(uri));	// we add in reverse order
							}
	});

}



private setDisplayMode(content: Content, what: ContentRefreshed) {

	this.root = content;
	this.isFragment = what===ContentRefreshed.FRAGMENT;
	this.visible = true;

}

private lastURIPart(u: string) {

	const uriElems = u.split("/");

	return uriElems[uriElems.length-1];

}


}