// MODEL - AREA . COMPONENT . TS

import { Component, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';

import { NgbTabset, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';

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
		<ngb-tabset type="pills" activeId="model-tab" (tabChange)="beforeTabChange($event)" #tabs="ngbTabset">
			<ngb-tab title="Model" id="model-tab">
				<ng-template ngbTabContent>
					<model></model>
				</ng-template>
			</ngb-tab>
			<ngb-tab title="Snippets" id="snippets-tab">
				<ng-template ngbTabContent>
					<snippets *ngIf="snippets" [snippetStubs]="snippets" [model]="model"></snippets>
				</ng-template>
			</ngb-tab>
		</ngb-tabset>
		</div>
	`,
	styles: [`
				#model-tab {}
				#snippets-tab {}
	 `]
})

export class ModelAreaComponent extends KeyListenerWidget implements OnInit {

private static readonly MODEL_TAB = 'model-tab';
private static SNIPPETS_TAB = 'snippets-tab';

model?: Model;
snippets?: CellDocument[];

protected commandKeys: string[] = ['m', 's'];

@ViewChild('tabs') tabs: NgbTabset;
@ViewChild(SnippetsListComponent) private snippetListComponent: SnippetsListComponent;

private modelDisplayReadySubscription: Subscription;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	console.log("ModelAreaComponent::ngOnInit()");

	this.register(this.events.service.of<CellDocumentClearEvent>(CellDocumentClearEvent).subscribe(() => this.clear()));

	console.log('aaaaaaaaa');
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

commandPressedCallback(command: string) {


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


beforeTabChange($event: NgbTabChangeEvent) {

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
