// CATALOGUE . COMPONENT . TS

import { Component, Inject, OnInit } from '@angular/core';

import { Catalogue } from '../catalogue.class';

import { CellDocument } from '../cell-document.class';
import { RemoteDataService } from '../services/remote-data.service';

import { CatalogueSelectionEvent } from '../events/catalogue-selection.event';
import { CatalogueLoadedEvent } from '../events/catalogue-loaded.event';
import { CellDocumentClearEvent } from '../events/cell-document-clear.event';
import { CellDocumentSelectionEvent } from '../events/cell-document-selection.event';
import { StatusEvent } from '../events/status.event';
import { EventListener } from '../events/event-listener.class';
import { EventService } from '../services/event.service';
import { RemoteEventService } from '../services/remote-event.service';
import { Configuration } from '../config/configuration.class';


@Component({
	selector: "catalogue",
	template: `
		<div id="catalogue" class="card bg-light mt-2" *ngIf="catalogue">
			<h5 id="catalogue-name" class="card-header">{{catalogue.name}}</h5>
			<div class="card-body">
				<div id="catalogue-desc" class="card-title">{{catalogue.desc}}</div>
				<div id="document-list" class="list-group">
					<a *ngFor="let d of catalogue.documents"
						routerLink="."
						queryParamsHandling="merge"
						class="document-list-entry list-group-item list-group-item-action"
						[class.active]="d.uri === selectedDocumentURI"
						(click)="clickOnDocument(d)">
						<img src="{{preview(d)}}" alt="document"/> {{d.name}}
					</a>
				</div>
			</div>
		</div>
	`,
	styles: [`
		#catalogue {}
		#catalogue-name {}
		#catalogue-desc {}
		#document-list {}
		#document-list-entry {}
	`],
	providers: [
	]
})

export class CatalogueComponent extends EventListener implements OnInit {

catalogue: Catalogue;
selectedDocumentURI: string;


constructor(eventService: EventService,
			remoteEventService: RemoteEventService,
			@Inject("RemoteJSONDataService") private catalogueService: RemoteDataService) {
	super(eventService, remoteEventService);
}


ngOnInit() {

	console.log("DocumentComponent::ngOnInit()");

	this.register(this.events.service.of<CellDocumentSelectionEvent>(CellDocumentSelectionEvent)
			.subscribe(selected => this.markDocumentAsSelected(selected.url))
	);

	this.register(this.events.service.of<CatalogueSelectionEvent>(CatalogueSelectionEvent)
			.subscribe(selected => this.loadCatalogueAt(selected.url))
	);

}


loadCatalogueAt(selectedCatalogueUri: string) {

	this.selectedDocumentURI = null;
	this.events.service.publish(new StatusEvent("Fetching catalogue"));
	const subs = this.register(
		this.catalogueService.get<Catalogue>(selectedCatalogueUri).subscribe(
				c => {
					this.catalogue = c;
					this.events.service.publish(new CatalogueLoadedEvent(c));
					this.events.ok();
				},
				error => {
					this.events.problem(error.message); // error is of the type HttpErrorResponse
					console.error("Could not get catalog '%s' (%s)", selectedCatalogueUri, error.message);
					this.catalogue = null;
				},
				// FIXME: in case of error, the completed lambda is not ran, so the status bar is not updated ??
				() => {
					this.events.service.publish(new CellDocumentClearEvent());	// also clear document
					this.events.service.publish(new StatusEvent("Fetching catalogue", StatusEvent.DONE));
					this.unsubscribe(subs); // avoid memory leak every time we get a catalogue
				}
		)
	);

}


clickOnDocument(stub: CellDocument) {

	console.log("[UI] Clicked on document='"+stub.uri+"' from catalogue");
	this.events.remote.publish(new CellDocumentSelectionEvent(stub.uri));

}


markDocumentAsSelected(uri: string) {

	console.log("[UI] Marking document='"+uri+"' as selected in catalogue");
	this.selectedDocumentURI = uri;

}


preview(d: CellDocument) {
	return d.presentation ? d.presentation : 'assets/images/open-iconic/file.svg';
}


public static catalogueURIFrom(config: Configuration, uri: string) {
	return Configuration.BACKEND_PREF+uri;
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