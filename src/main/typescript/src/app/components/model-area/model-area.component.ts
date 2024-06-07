// MODEL - AREA . COMPONENT . TS

import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';

import { NgbNav } from '@ng-bootstrap/ng-bootstrap';

import { KeyListenerWidget } from '../../key-listener-widget.class';
import { SnippetsListComponent } from '../snippets-list/snippets-list.component';

import { CellDocument } from '../../cell-document.class';
import { Model, ModelJSON } from '../../model.class';

import { CatalogueLoadedEvent } from '../../events/catalogue-loaded.event';
import { CellDocumentClearEvent } from '../../events/cell-document-clear.event';
import { CellDocumentLoadedEvent } from '../../events/cell-document-loaded.event';
import { ModelDisplayEvent } from '../../events/model-display.event';
import { ModelDisplayReadyEvent } from '../../events/model-display-ready.event';
import { ModelRequestEvent } from '../../events/model-request.event';
import { ModelLoadedEvent } from '../../events/model-loaded.event';
import { EventService } from '../../services/event.service';
import { Configuration } from '../../config/configuration.class';
import { ContentRequestEvent } from '../../events/content-request.event';
import { StatusEvent } from '../../events/status.event';
import { RemoteObjectService } from '../../services/remote-object.service';
import { RemoteEventService } from '../../services/remote-event.service';
import { Catalogue } from 'src/app/catalogue.class';
import { SnippetsDisplayEvent } from 'src/app/events/snippets-display.event';

@Component({
	selector: "model-area",
	template: `
		<div [hidden]="!isVisible()">
			<ul ngbNav #modnav="ngbNav" id="model-navs" type="pills"  class="nav-tabs">
				<li [ngbNavItem]="'model-tab'" id="model-tab">
					<a ngbNavLink (click)="select('model-tab')"> Model</a>
					<ng-template ngbNavContent>
						<model></model>
					</ng-template>
				</li>
				<li [ngbNavItem]="'snippets-tab'" id="snippets-tab">
					<a ngbNavLink (click)="select('snippets-tab')">Snippets</a>
					<ng-template ngbNavContent>
						<snippets [model]="model"></snippets>
					</ng-template>
				</li>
			</ul>
			<div [ngbNavOutlet]="modnav"></div>
		</div>
	`,
	styles: [`
				#model-tab {}
				#snippets-tab {}
	 `]
}) 

export class ModelAreaComponent extends KeyListenerWidget implements OnInit {

public static readonly MODEL_TAB = 'model-tab';
public static readonly SNIPPETS_TAB = 'snippets-tab';

model?: Model;
catalogue?: Catalogue;

protected override commandKeys: string[] = ['m', 's'];

@ViewChild('modnav') tabs: NgbNav;
@ViewChild(SnippetsListComponent) private snippetListComponent: SnippetsListComponent;

private modelDisplayReadySubscription: Subscription;


constructor(eventService: EventService,
	remoteEventService: RemoteEventService,
	@Inject("ModelService") private modelService: RemoteObjectService<Model, ModelJSON> ) {
	super(eventService, remoteEventService);
}


ngOnInit() {

	console.log("ModelAreaComponent::ngOnInit()");

	this.register(this.events.service.of<CellDocumentClearEvent>(CellDocumentClearEvent).subscribe(() => this.clear()));

	this.register(this.events.service.of<CellDocumentLoadedEvent>(CellDocumentLoadedEvent)
			.subscribe(loaded => this.events.service.publish(new ModelRequestEvent(loaded.document)))
	);

	this.register(this.events.service.of<ModelRequestEvent>(ModelRequestEvent)
			.subscribe(requested => this.loadModel(requested.document))
	);
	this.register(this.events.service.of<CatalogueLoadedEvent>(CatalogueLoadedEvent)
			.subscribe(loaded => this.catalogue = loaded.catalogue)
	);

	this.registerKeyPressedEvents();

}

loadModel(document: CellDocument) {

	this.events.service.publish(new StatusEvent("Fetching model"));
	const modelURI = Configuration.BACKEND_PREF+"/dyn/models/"+document.modelURI;
	this.register(
			this.modelService.get(modelURI, Model).subscribe( (model:Model) => {
				console.log("ModelComponent::loadModel() Got model from Morfeu service ("+model.name+")");
				document.model = model;	 // associating the document with the recently loaded model
				// now that we have loaded the model we can safely load the content (as both are related)
				this.events.service.publish(new ModelLoadedEvent(model));
				this.events.remote.publish(new ContentRequestEvent(document, model));
				// also display it (model component will not be loaded until store runs and 
				// the tree shows the <model> component to pick up the display event) so we run it as a promise
				// so the display event is fired in the next event loop
				this.store(model);
				Promise.resolve(null).then(() => this.events.service.publish(new ModelDisplayEvent(model)));
				this.events.ok();
			},
			// TODO: check for network errors (see https://angular.io/guide/http)
			error => this.events.problem(error.message),	// error is of the type HttpErrorResponse
			() =>	  this.events.service.publish(new StatusEvent("Fetching model", StatusEvent.DONE))
			)
	);

}

//// KeyListenerWidget ////

override commandPressedCallback(command: string) {


	// we select the appropriate tab, at the moment we need the user to re-issue the key again
	switch (command) {
	case "m":
		console.log("[UI] ModelAreaComponent:: commandPressedCallback(%s)", command);
		if (this.tabs.activeId===ModelAreaComponent.SNIPPETS_TAB) {
			this.tabs.select(ModelAreaComponent.MODEL_TAB);
		}
		break;
	case "s":
		console.log("[UI] ModelAreaComponent:: commandPressedCallback(%s)", command);
		if (this.tabs.activeId===ModelAreaComponent.MODEL_TAB) {
			this.tabs.select(ModelAreaComponent.SNIPPETS_TAB);
		} else {
			this.snippetListComponent.activateSnippetSelectingMode();
		}
		break;
	}

}

//// KeyListenerWidget [end] ////


select(selected: string) {
	if (selected===ModelAreaComponent.SNIPPETS_TAB) {
		this.events.service.publish(new SnippetsDisplayEvent(this.catalogue.snippets, this.catalogue));
	} else if (selected===ModelAreaComponent.MODEL_TAB) {
			// redisplay will unsuscribe
			this.modelDisplayReadySubscription = this.register(this.events.service.of<ModelDisplayReadyEvent>(ModelDisplayReadyEvent)
				.subscribe(() => this.redisplayModel())
		);
	}
}


isVisible(): boolean {
	return this.model!=undefined && this.model!=null;
}


private store(model: Model) {

	console.log("[UI] ModelAreaComponent:: store(%s)", model.URI);
	this.model = model;
}


private redisplayModel() {

	console.log("[UI] ModelAreaComponent:: redisplayModel(%s), sending display event", this.model.name);
	this.unsubscribe(this.modelDisplayReadySubscription);
	this.modelDisplayReadySubscription = null;
	this.events.service.publish(new ModelDisplayEvent(this.model));
	console.log("[UI] ModelAreaComponent:: redisplayModel(%s), display event sent", this.model.name);

}


private clear() {
	this.model = null;
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
