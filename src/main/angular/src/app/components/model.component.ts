// MODEL . COMPONENT . TS

import {AfterViewInit, Component, Inject, OnDestroy, OnInit, ViewChild} from "@angular/core";

//import { TreeComponent } from '@circlon/angular-tree-component';

import { Configuration } from '../config/configuration.class';

import { CellModel } from "../cell-model.class";
import { Model, ModelJSON } from "../model.class";
import { RemoteObjectService } from "../services/remote-object.service";
import { CellDocument } from "../cell-document.class";
import { KeyListenerWidget } from "../key-listener-widget.class";

import { CellDocumentClearEvent } from "../events/cell-document-clear.event";
import { CellSelectEvent } from "../events/cell-select.event";
import { CellSelectionClearEvent } from "../events/cell-selection-clear.event";
import { CellModelActivatedEvent } from "../events/cell-model-activated.event";
import { ContentRequestEvent } from "../events/content-request.event";
import { ModelLoadedEvent } from "../events/model-loaded.event";
import { ModelDisplayEvent } from "../events/model-display.event";
import { ModelDisplayReadyEvent } from "../events/model-display-ready.event";
import { ModelRequestEvent } from "../events/model-request.event";
import { NewCellFromModelEvent } from "../events/new-cell-from-model.event";
import { StatusEvent } from "../events/status.event";
import { EventService } from "../services/event.service";
import { RemoteEventService } from "../services/remote-event.service";

@Component({
	selector: "model",
	template: `
	<ng-container *ngIf="model">
			<div id="model-info" class="card">
				<h5 id="model-name" class="card-header">Model at: ...{{displayName}}</h5>
				<div class="card-body">
					  <div id="model-desc" class="card-title">{{model.desc}}</div>
					<!-- non-intuitively, the nodes binding expects an array and not a root node-->
					<!-- we use direct binding as opposed to events for the moment -->
					<div id="model-cell-models" class="">
						<!--tree-root
							[nodes]="model.children">
							<ng-template #treeNodeTemplate let-node let-index="index">
								<cell-model [node]="node" [index]="index"></cell-model>
							</ng-template>
						/tree-root-->
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

export class ModelComponent extends KeyListenerWidget implements OnInit, AfterViewInit, OnDestroy {

model: Model;
displayName: string;

protected override commandKeys: string[] = ["m", "a", "n"];
private cellModelSelectingMode = false;

//@ViewChild(TreeComponent) private cellModelComponentsRoot: TreeComponent;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	console.log("ModelComponent::ngOnInit()"); 
	// if we are in a tab area this is redundant, as the parent will remove us from the component tree
	this.register(this.events.service.of<CellDocumentClearEvent>(CellDocumentClearEvent)
			.subscribe(() => this.clearModel())
	);

	this.register(this.events.service.of<ModelDisplayEvent>(ModelDisplayEvent)
			.subscribe(display => this.displayModel(display.model))
	);

	//this.subscribeToCellSelectionClear();

}


ngAfterViewInit() {

	// we are ready to reload the model
	console.log("ModelComponent::ngAfterViewInit()"); 
	Promise.resolve(null).then(() => this.events.service.publish(new ModelDisplayReadyEvent()));

}


displayModel(m: Model) {

	console.log("[UI] ModelComponent::displayModel("+m.name+")");
//	let i = 0;
//	m.cellModels.forEach(cm => cm.activateEventService(this.events.service, i++));

	const uri = m.getURI();
	this.displayName = uri.substring(uri.lastIndexOf("/"), uri.length);
	this.model = m;
	this.registerKeyPressedEvents();
	console.log("[UI] ModelComponent::displayModel [end]");

}


clearModel() {

	console.log("[UI] ModelComponent::clearModel()");
	this.unregisterKeyPressedEvents();
	//	if (this.model) {
	//		this.model.cellModels.forEach(cm => cm.deactivateEventService());
	//	}
	this.model = null;

}


//// KeyListenerWidget ////

override commandPressedCallback(command: string) {

	switch (command) {
	case "m":
		this.activateCellModelSelectingMode();
		break;
	case "a":
		if (this.cellModelSelectingMode) {
			this.events.service.publish(new CellModelActivatedEvent());	 // will activate the 
			this.cellModelSelectingMode = false;						   // current selection (if any)
		}
		break;
	case "n":
		// Instantiate a new instance if any cell model is activated and a drop area is active as well 
		console.log("[UI] CellModelComponent::commandPressedCallback() sending new cell event");
		this.events.service.publish(new NewCellFromModelEvent());
		break;
	}

}


override numberPressedCallback(num: number) {

	if (this.cellModelSelectingMode) {

		console.log("[UI] ModelComponent::numberPressed(%i)", num);
		this.events.service.publish(new CellSelectEvent(num));

	}

}


override commandNotRegisteredCallback(command: string) {

	console.log("[UI] ModelComponent::keyPressed(%s) not interested", command, this.cellModelSelectingMode);
	this.cellModelSelectingMode = false;

}

//// KeyListenerWidget [end] ////


activateCellModelSelectingMode() {

	console.log("CellModelComponent::activateCellModelSelectingMode()");

	//this.cellModelSelectingMode = true;
	this.events.service.publish(new CellSelectionClearEvent()); // clear any other subscriptions
	this.cellModelSelectingMode = true;
	this.subscribeChildrenToCellSelection();

}


static modelURIFrom(model: string): string {
	return Configuration.BACKEND_PREF+'/dyn/models/'+model;
}


private subscribeChildrenToCellSelection () {

	console.log("ModelComponent::subscribeChildrenToCellSelection()");
	this.unsubscribeChildrenFromCellSelection();
	// this.cellModelComponentsRoot.treeModel.roots.forEach(r => {
	// 	r.expand();
	// 	(r.data as CellModel).component.subscribeToSelection(); // breaks class-component abstraction,
	// 															  // but there does not seem to be an easy 
	// 															  // way to do this with the tree component
	// 	}
	// );

}


private unsubscribeChildrenFromCellSelection() {
	// breaks class-component abstraction, but there does not seem to
	// be an easy way to do this with the tree component we're using
	// this.cellModelComponentsRoot.treeModel.getVisibleRoots().forEach(n => 
	// 	(n.data as CellModel).component.unsubscribeFromSelection()
	// ); 
}


	override ngOnDestroy() {
	console.log("ModelComponent::ngOnDestroy()");
	super.ngOnDestroy();
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
