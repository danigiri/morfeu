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

import { Component, OnInit, ViewChild } from "@angular/core";
import { Subscription } from "rxjs";

import { NgbTabChangeEvent } from "@ng-bootstrap/ng-bootstrap";

import { ModelComponent } from "./model.component";
import { Widget } from "../widget.class";

import { CellDocument } from "../cell-document.class";
import { Model } from "../model.class";

import { CellDocumentClearEvent } from "../events/cell-document-clear.event";
import { CellDocumentLoadedEvent } from "../events/cell-document-loaded.event";
import { ModelDisplayEvent } from "../events/model-display.event";
import { ModelDisplayReadyEvent } from "../events/model-display-ready.event";
import { ModelRequestEvent } from "../events/model-request.event";
import { ModelLoadedEvent } from "../events/model-loaded.event";
import { EventService } from "../events/event.service";

@Component({
	moduleId: module.id,
	selector: "model-area",
	template: `
		<div [hidden]="!isVisible()">
		<ngb-tabset type="pills" activeId="model-tab" (tabChange)="beforeTabChange($event)">
			<ngb-tab title="Model" id="model-tab">
				<ng-template ngbTabContent>
					<model></model>
				</ng-template>
			</ngb-tab>
			<ngb-tab title="Snippets" id="snippets-tab">
				<ng-template ngbTabContent>
					
				</ng-template>
			</ngb-tab>
		</ngb-tabset>
		</div>
	`,
	styles:[`
	 `]
})

export class ModelAreaComponent extends Widget implements OnInit {

model?: Model;
//snippet?: ;
	
@ViewChild(ModelComponent) private modelComponent: ModelComponent;

private modelDisplayReadySubscription: Subscription;

constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {
	
	console.log("ModelComponent::ngOnInit()");

	this.subscribe(this.events.service.of(CellDocumentClearEvent).subscribe(s => this.clear()));

	this.subscribe(this.events.service.of(CellDocumentLoadedEvent).subscribe(
			loaded => this.events.service.publish(new ModelRequestEvent(loaded.document))
	));
	
	this.subscribe(this.events.service.of(ModelLoadedEvent).subscribe(loaded => this.store(loaded.model)));



}


private beforeTabChange($event: NgbTabChangeEvent) {
	console.log("[UI] ModelAreaComponent:: beforeTabChange(%s)", $event.activeId);
	if ($event.activeId=="model-tab") {
	} else if ($event.activeId=="snippets-tab") {
		this.modelDisplayReadySubscription = this.subscribe(this.events.service.of(ModelDisplayReadyEvent)
				.subscribe(loaded => this.redisplayModel()));
	}

}


private isVisible(): boolean {
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
	//this.snippet = false;

}

}