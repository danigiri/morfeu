// MAIN . COMPONENT . TS

import { ActivatedRoute } from '@angular/router';
import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { isDevMode, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';	// new angular 5 http client

import { Configuration, ConfigJSON } from './config/configuration.class';
import { environment } from '../environments/environment';

import { RemoteDataService } from './services/remote-data.service';
import { RemoteObjectService } from './services/remote-object.service';

import { CellDocument, CellDocumentJSON } from './cell-document.class';
import { Content, ContentJSON } from './content.class';

import { ConfigurationLoadedEvent } from './events/configuration-loaded.event';
import { CataloguesRequestEvent } from './events/catalogues-request.event';
import { CataloguesLoadedEvent } from './events/catalogues-loaded.event';
import { CatalogueLoadedEvent } from './events/catalogue-loaded.event';
import { CatalogueSelectionEvent } from './events/catalogue-selection.event';
import { CellDocumentSelectionEvent } from './events/cell-document-selection.event';
import { EventListener } from './events/event-listener.class';
import { EventService } from './services/event.service';
import { RemoteEventService } from './services/remote-event.service';

@Component({
	selector: "app-main",
	template: `
	<div class="card">
			<div class="card-body">
				<h1 class="card-title">Morfeu application</h1>
				<debug></debug>
				<div class="container-fluid">
					<div class="row">
						<div class="col-2">
							<catalogue-list></catalogue-list>
							<document></document>
						</div>
						<div class="col-8">
							<content></content>
						</div>
						<div class="col-2">
							<model-area></model-area>
							<cell-data></cell-data>
					</div>
				</div>
			</div>
			<div class="container-fluid">
				<status></status>
				<problem></problem>
			</div>
		</div>
	<key-capture></key-capture>
		<cell-editor></cell-editor>
	`,
	providers: [
				// note that Http is injected by the HttpModule imported in the application module
				{provide: "RemoteJSONDataService",
					useFactory: (http: HttpClient) => (new RemoteDataService(http)),
					deps: [HttpClient]
				},
				{provide: "CellDocumentService",
					useFactory: (http: HttpClient) => (new RemoteObjectService<CellDocument, CellDocumentJSON>(http)),
					deps: [HttpClient]
				},
				{provide: "ConfigurationService",
					useFactory: (http: HttpClient) => (new RemoteObjectService<Configuration, ConfigJSON>(http)),
					deps: [HttpClient]
				},
				Configuration,
				{provide: "ContentService",
					useFactory: (http: HttpClient) => (new RemoteObjectService<Content, ContentJSON>(http)),
					deps: [HttpClient]
				},
				EventService,
				RemoteEventService,
				{provide: "SnippetContentService",
					useFactory: (http: HttpClient) => (new RemoteObjectService<Content, ContentJSON>(http)),
					deps: [HttpClient]
				}
	]
})

export class MainComponent extends EventListener implements AfterViewInit {

private cataloguesLoadedEventSubscription: Subscription;
private catalogueLoadedEventSubscription: Subscription;


constructor(eventService: EventService, private route: ActivatedRoute, private config: Configuration) {
	super(eventService);
}


// this hoock is called "after Angular initializes the component's views and child views." so everyone has
// been able to to register their listeners to appropriate events
ngAfterViewInit() {

	console.info("\t\t\t\t\t ****** APPLICATION STARTS ******");
	console.debug("AppComponent::ngAfterViewInit()");

	// THIS IS TO SPEED UP DEVELOPMENT, WE TRANSITION INTO THE DESIRED STATE
	const foo = !environment.production && true;
	if (isDevMode() && foo) {
		// we only want to do these once, hence the unsubscriptions
		this.cataloguesLoadedEventSubscription = this.register(this.events.service.of<CataloguesLoadedEvent>(CataloguesLoadedEvent)
				.subscribe(loaded => {
						this.unsubscribe(this.cataloguesLoadedEventSubscription);
						const catalogue = loaded.catalogues[0].uri;
						this.events.service.publish(new CatalogueSelectionEvent(catalogue));
				})
		);
		this.catalogueLoadedEventSubscription = this.register(this.events.service.of<CatalogueLoadedEvent>(CatalogueLoadedEvent)
				.subscribe(loaded => {
						this.unsubscribe(this.catalogueLoadedEventSubscription);
						const document = loaded.catalogue.documents[0].uri;
						Promise.resolve(null).then(() =>  // run this after that catalogue clears doc select
							this.events.service.publish(new CellDocumentSelectionEvent(document))
						);
				})
		);
//		this.subscribe(this.events.service.of(ContentRefreshedEvent).subscribe(
//				_ => {
					//this.events.service.publish(new CellSelectionClearEvent());
//					this.events.service.publish(new CellSelectEvent(0));
//					this.events.service.publish(new CellSelectEvent(0));
//					this.events.service.publish(new CellSelectEvent(0));
//					this.events.service.publish(new CellSelectEvent(0));
//					this.events.service.publish(new CellActivateEvent());
//				} 
//		));

	}
	
	// this event loads the default catalogue and starts everything in motion
	//
	// See https://hackernoon.com/everything-you-need-to-know-about-the-expressionchangedafterithasbeencheckederror-error-e3fd9ce7dbb4
	// the problem here is that we have created components with specific data bindings (for instance the
	// animation state of the status bar, which is setup to be hidden) and the load event will change some of
	// them. The changes will happen after the lifecycle stage of (re)setting data-bound input properties of
	// components) and will end up in an inconsistent state when verified. Therefore we're scheduling the request event 
	// to be fired asynchronously in a micro-event that runs after the data-binding verification step.
	// Also see https://angular.io/guide/lifecycle-hooks
	// 
	// This should be ok as we assume the subscriptions should be done at the ngOnInit event, to ensure that
	// events can use binding properties that have been setup properly

	this.register(this.events.service.of<ConfigurationLoadedEvent>(ConfigurationLoadedEvent)
			.subscribe(loaded => {
				if (loaded===undefined || loaded===null) {
					console.warn('Got an empty configuration loaded event');
					return;
				}
				console.log("Configuration loaded, we're about to request catalogue load: %s", loaded.toString());
				console.log("loaded.configuration.catalogues=%s", loaded.configuration.catalogues);
				this.config = loaded.configuration;
				Promise.resolve(null)
						.then(() => this.events.service
												.publish(new CataloguesRequestEvent(loaded.configuration.catalogues)));
			})
	);

	// we need to subscribe to the query params to override possible configuration
	this.register(this.route.queryParams.subscribe(
			params => {
				if (params.config && params.config!==undefined) {
					console.log("Configuration to be bootstrapped from config url '%s'", params.config);
					this.config.loadConfigFrom(params.config);
				} else {
					let merged = this.config.overwriteWithParams(params);
					console.log("Configuration bootstrapped from defaults, firing config loaded event ^^");
					this.events.service.publish(new ConfigurationLoadedEvent(merged));
					console.debug('Configuration loaded event fired');
				}
			}
	));

}


}

/*
 *	  Copyright 2018 Daniel Giribet
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

