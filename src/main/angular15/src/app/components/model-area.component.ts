// MODEL - AREA . COMPONENT . TS

import { Component, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';

import { NgbNav, NgbNavChangeEvent } from '@ng-bootstrap/ng-bootstrap';

import { KeyListenerWidget } from '../key-listener-widget.class';
import { SnippetsListComponent } from './snippets-list/snippets-list.component';

import { CellDocument } from '../cell-document.class';
import { Model } from '../model.class';

import { CatalogueLoadedEvent } from '../events/catalogue-loaded.event';
import { CellDocumentClearEvent } from '../events/cell-document-clear.event';
import { CellDocumentLoadedEvent } from '../events/cell-document-loaded.event';
import { ModelDisplayEvent } from '../events/model-display.event';
import { ModelDisplayReadyEvent } from '../events/model-display-ready.event';
import { ModelRequestEvent } from '../events/model-request.event';
import { ModelLoadedEvent } from '../events/model-loaded.event';
import { EventService } from '../services/event.service';

@Component({
	selector: "model-area",
	template: `
		<div [hidden]="!isVisible()">
			<ul ngbNav id="model-navs" type="pills" activeId="model-tab" class="nav-tabs">
				<li ngbNavItem id="model-tab">
					<a ngbNavLink>Model</a>
					<ng-template ngbNavContent>
						<model></model>
					</ng-template>
				</li>
				<li ngbNavItem id="snippets-tab">
					<a ngbNavLink>Snippets</a>
					<ng-template ngbNavContent>
						<snippets *ngIf="snippets" [snippetStubs]="snippets" [model]="model"></snippets>
					</ng-template>
				</li>
			</ul>
		</div>
	`,
	styles: [`
				#model-tab {}
				#snippets-tab {}
	 `]
})

export class ModelAreaComponent extends KeyListenerWidget implements OnInit {

private static readonly MODEL_TAB = 'model-tab';
private static readonly SNIPPETS_TAB = 'snippets-tab';

model?: Model;
snippets?: CellDocument[];

protected override commandKeys: string[] = ['m', 's'];

@ViewChild('ngbNav') tabs: NgbNav;
@ViewChild(SnippetsListComponent) private snippetListComponent: SnippetsListComponent;

private modelDisplayReadySubscription: Subscription;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	console.log("ModelAreaComponent::ngOnInit()");

	this.register(this.events.service.of<CellDocumentClearEvent>(CellDocumentClearEvent).subscribe(() => this.clear()));

	this.register(this.events.service.of<CellDocumentLoadedEvent>(CellDocumentLoadedEvent)
			.subscribe(loaded => this.events.service.publish(new ModelRequestEvent(loaded.document)))
	);

	this.register(this.events.service.of<ModelLoadedEvent>(ModelLoadedEvent)
			.subscribe(loaded => this.store(loaded.model))
	);

	this.register(this.events.service.of<CatalogueLoadedEvent>(CatalogueLoadedEvent)
			.subscribe(loaded => this.snippets = loaded.catalogue.snippets)
	);

	this.registerKeyPressedEvents();

}


//// KeyListenerWidget ////

override commandPressedCallback(command: string) {


	// we select the appropriate tab, at the moment we need the user to re-issue the key again
	switch (command) {
	case "m":
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


beforeTabChange($event: NgbNavChangeEvent) {

	console.log("[UI] ModelAreaComponent:: beforeTabChange(%s)", $event.activeId);
	if ($event.activeId===ModelAreaComponent.MODEL_TAB) {
	} else if ($event.activeId===ModelAreaComponent.SNIPPETS_TAB) {
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
	this.events.service.publish(new ModelDisplayEvent(this.model));
	console.log("[UI] ModelAreaComponent:: redisplayModel(%s), display event sent", this.model.name);

}


private clear() {
	this.model = null;
}


}

/*
 *	  Copyright 2019 Daniel Giribet
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
