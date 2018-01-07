 /*
 *	  Copyright 2017 Daniel Giribet
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

import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { Subscription }	  from 'rxjs/Subscription';

import { HotkeysService, Hotkey } from 'angular2-hotkeys';
import { TreeComponent } from 'angular-tree-component';

import { CellModelComponent } from './cell-model.component';

import { Model, ModelJSON } from './model.class';
import { RemoteObjectService } from './services/remote-object.service';
import { CellDocument } from './cell-document.class';
import { KeyListenerWidget } from "./key-listener-widget.class";

import { CellDocumentLoadedEvent } from './events/cell-document-loaded.event';
import { CellDocumentSelectionEvent } from './events/cell-document-selection.event';
import { CellSelectEvent } from './events/cell-select.event';
import { CellSelectionClearEvent } from './events/cell-selection-clear.event';
import { CellModelActivatedEvent } from './events/cell-model-activated.event';
import { ContentRequestEvent } from './events/content-request.event';
import { EventService } from './events/event.service';
import { KeyPressedEvent } from "./events/keypressed.event";
import { ModelRequestEvent } from './events/model-request.event';
import { NewCellFromModelEvent } from "./events/new-cell-from-model.event";
import { StatusEvent } from "./events/status.event";


@Component({
	moduleId: module.id,
	selector: 'model',
	template: `
	<ng-container *ngIf="model">
			<div id="model-info" class="card">
				<h5 id="model-name" class="card-header">Model: {{model.name}}</h5>
				<div class="card-body">
					  <div id="model-desc" class="card-title">{{model.desc}}</div>
					<!-- non-intuitively, the nodes binding expects an array and not a root node-->
					<!-- we use direct binding as opposed to events for the moment -->
					<div id="model-cell-models" class="">
						<tree-root
							[nodes]="model.cellModels">
							<ng-template #treeNodeTemplate let-node let-index="index">
							   <cell-model [node]="node" [index]="index"></cell-model>
							</ng-template>
						</tree-root>
					</div>
					<!--ng-container *ngIf="this.cellModelSelectingMode">cellModelSelectingMode</ng-container-->
				</div>
			</div>
	</ng-container>
	`,

	styles:[`
				#model-info {}
				#model-name {}
				#model-desc {}
				#model-cell-model-list {}
	`]
})

export class ModelComponent extends KeyListenerWidget implements OnInit {
	
	
model: Model;
	
protected commandKeys: string[] = ["m", "a", "n"];
private cellModelSelectingMode: boolean = false;
protected selectionClearSubscription: Subscription;

@ViewChild(TreeComponent) private cellModelComponentsRoot: TreeComponent;

constructor(eventService: EventService,
			@Inject("ModelService") private modelService: RemoteObjectService<Model, ModelJSON> ) {
	super(eventService);
   // super(eventService, hotkeysService);
}


ngOnInit() {
	
	console.log("ModelComponent::ngOnInit()"); 
	
	this.subscribe(this.events.service.of(CellDocumentSelectionEvent).filter(s => s.url==null).subscribe(
			selected => this.clearModel()
	));

	// if we load a problematic document we don't display anything (enjoying event-based coding right now)
	this.subscribe(this.events.service.of(CellDocumentLoadedEvent)
			.filter(loaded => loaded.document.hasProblem() )
			.subscribe(loadedProblematicDocument => this.clearModel())
	);
	
	this.subscribe(this.events.service.of(ModelRequestEvent).subscribe( requested =>
			this.loadModel(requested.document) 
	));
	
	//this.subscribeToCellSelectionClear();
	
}


loadModel(document:CellDocument) {

	this.events.service.publish(new StatusEvent("Fetching model"));
	let modelURI = "/morfeu/models/"+document.modelURI;
	this.modelService.get(modelURI, Model).subscribe( (model:Model) => {
			console.log("ModelComponent::loadModel() Got model from Morfeu service ("+model.name+")");
			this.diplayModel(model);	// not firing a load event yet if not needed
			document.model = model;	 // associating the document with the recently loaded model
			// now that we have loaded the model we can safely load the content (as both are related
			this.events.service.publish(new ContentRequestEvent(document, model));
			this.events.ok();
	},
	//TODO: check for network errors (see https://angular.io/guide/http)
	error => this.events.problem(error.message),	// error is of the type HttpErrorResponse
	() =>	  this.events.service.publish(new StatusEvent("Fetching model", StatusEvent.DONE))
	);
	
}


diplayModel(m: Model) {

	console.log("[UI] ModelComponent::diplayModel("+m.name+")");
	let i = 0;
//	m.cellModels.forEach(cm => cm.activateEventService(this.events.service, i++));
	this.model = m;
	this.registerKeyPressedEvents();

}


clearModel() {

	console.log("[UI] ModelComponent::clearModel()");
	this.unregisterKeyPressedEvents();
//	if (this.model) {
//		this.model.cellModels.forEach(cm => cm.deactivateEventService());
//	}
	this.model = null;

}


commandPressedCallback(command: string) {

	switch (command) {
	case "m":
		this.cellModelSelectingMode = true;
		this.unsubscribeFromCellSelectionClear();
		this.events.service.publish(new CellSelectionClearEvent()); // clear any other subscriptions
		this.cellModelSelectingMode = true;
		this.subscribeChildrenToCellSelection();
		break;	 
	case "a":
		if (this.cellModelSelectingMode) {
			this.events.service.publish(new CellModelActivatedEvent());	   // will activate the 
			this.cellModelSelectingMode = false;							// current selection if any
		}
		break;
	case "n":
		// Instantiate a new instance if any cell model is activated and a drop area is active as well 
	    console.log("[UI] CellModelComponent::commandPressedCallback() sending new cell event");
		this.events.service.publish(new NewCellFromModelEvent());
		break;
	}

}


commandNotRegisteredCallback(command: string) {
    
    console.log("[UI] ModelComponent::keyPressed(%s) not interested", command, this.cellModelSelectingMode);
    this.cellModelSelectingMode = false;
    
}


numberPressedCallback(num: number) {

	if (this.cellModelSelectingMode) {

		console.log("[UI] ModelComponent::numberPressed(%i)", num);
		this.events.service.publish(new CellSelectEvent(num));

	} 

}




private subscribeChildrenToCellSelection () {

	console.log("ModelComponent::subscribeChildrenToCellSelection()");
	this.unsubscribeChildrenFromCellSelection();
	this.cellModelComponentsRoot.treeModel.roots.forEach(r => {
		r.expand();
		r.data.widget.subscribeToSelection();	// breaks class-component abstraction, but there does not seem 
												//to be an easy way to do this with the tree component
		}
	);

}


unsubscribeChildrenFromCellSelection() {
	// breaks class-component abstraction, but there does not seem to
	// be an easy way to do this with the tree component we're using
	this.cellModelComponentsRoot.treeModel.getVisibleRoots().forEach(n => 
		n.data.widget.unsubscribeFromSelection()
	); 
}


//private subscribeToCellSelectionClear() {
// // selection clear, we clear and are no longer eligible to receive selection events
//	this.selectionClearSubscription = this.subscribe(this.events.service.of(CellSelectionClearEvent).subscribe(
//			  clear => {
//				  console.log("[UI] ModelComponent::received CellSelectionClearEvent");
//				  this.cellModelSelectingMode = false;
//				  this.unsubscribeChildrenFromCellSelection();
//			  }
//   ));
//}


private unsubscribeFromCellSelectionClear() {
	if (this.selectionClearSubscription) {
		this.unsubscribe(this.selectionClearSubscription);
	}
}
}