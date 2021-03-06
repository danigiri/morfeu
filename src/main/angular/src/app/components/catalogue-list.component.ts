// CATALOGUE LIST . COMPONENT . TS

import { Component, Inject, OnInit } from '@angular/core';

import { Catalogue } from '../catalogue.class';

import { RemoteDataService } from '../services/remote-data.service';

import { CatalogueSelectionEvent } from '../events/catalogue-selection.event';
import { CataloguesRequestEvent } from '../events/catalogues-request.event';
import { CataloguesLoadedEvent } from '../events/catalogues-loaded.event';
import { StatusEvent } from '../events/status.event';
import { EventListener } from '../events/event-listener.class';
import { EventService } from '../services/event.service';

@Component({
	selector: "catalogue-list",
	template: `
	<collapsible header="Catalogues" class="mb-2"  [folded]="false">
				<div id="catalogue-list" class="list-group2">
					<a *ngFor="let c of catalogues"
						routerLink="."
						queryParamsHandling="merge"
						class="catalogue-list-entry list-group-item list-group-item-action"
						[class.active]="c.uri===selectedCatalogueURI"
						(click)="clickOnCatalogue(c)">
					<img src="assets/images/open-iconic/folder.svg" alt="catalogue"/> {{c.name}}</a>
				</div>
			<catalogue></catalogue>
		</collapsible>
	`,
	styles:[`
		#catalogue-list {}
		.catalogue-list-entry {}
	`],
	providers: [
				]
})

export class CatalogueListComponent extends EventListener implements OnInit {

catalogues: Catalogue[];
selectedCatalogueURI: string;


constructor(eventService: EventService, 
			@Inject("RemoteJSONDataService") private catalogueService: RemoteDataService ) {
	super(eventService);
}


ngOnInit() {

	console.log("StatusComponent::ngOnInit()");

	this.register(this.events.service.of<CataloguesRequestEvent>(CataloguesRequestEvent)
			.subscribe(s => {
				console.log("-> catalogue-list component gets request event for '%s'", s.url);
				if (s.url!==undefined) {
					this.fetchCatalogues(s.url);
				} else {
					console.error("Undefined catalogue list, doing nothing");
				}
			})
	);

	// on catalogue selection we highlight the selected catalogue and clear the document selection
	this.register(this.events.service.of<CatalogueSelectionEvent>(CatalogueSelectionEvent)
		.subscribe(s => {
			console.log("-> catalogue-list component gets selection event for '"+s.url+"'");
			this.markCatalogueAsSelected(s.url);
		})
	);

}


fetchCatalogues(url: string) {

	this.events.service.publish(new StatusEvent("Fetching catalogues"));
	// TODO: make this configurable and into an event
	const subs = this.register(
					this.catalogueService.getAll<Catalogue>(url).subscribe(
							c => {
								console.debug('Fetched catalogues from %s', url);
								this.catalogues = c;
								this.events.service.publish(new CataloguesLoadedEvent(c));
								this.events.ok();
							},
							error => this.events.problem(error.message), // error is of the type HttpErrorResponse
							() => {
								this.events.service.publish(new StatusEvent("Fetching catalogues", StatusEvent.DONE));
								this.unsubscribe(subs);	// avoid memory leak every time we fetch the list
								}
					)
	);
}


clickOnCatalogue(c: Catalogue) {

	console.log("[UI] Clicked on catalogue="+c.uri);
	this.events.service.publish(new CatalogueSelectionEvent(c.uri));	// catalogue component will pick this

}


markCatalogueAsSelected(uri: string) {
	this.selectedCatalogueURI = uri;
}


}

/*
 *	  Copyright 2016 Daniel Giribet
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
