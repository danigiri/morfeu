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

import { Component, Inject, OnInit, AfterViewInit, OnDestroy, QueryList, ViewChildren } from "@angular/core";
import { Subscription } from "rxjs";


import { CellDocument } from "./cell-document.class";
import { Cell } from "./cell.class";
import { Content, ContentJSON } from "./content.class";
import { FamilyMember } from "./family-member.interface";
import { Model } from "./model.class";

import { RemoteDataService } from "./services/remote-data.service";
import { RemoteObjectService } from "./services/remote-object.service";
import { OperationResult } from "./services/operation-result.class";
import { SerialisableToJSON } from "./serialisable-to-json.interface";

import { CellComponent } from "./cell.component";
import { DropAreaComponent } from "./drop-area.component";
import { KeyListenerWidget } from "./key-listener-widget.class";

import { CellActivateEvent } from "./events/cell-activate.event";
import { CellDocumentClearEvent } from "./events/cell-document-clear.event";
import { CellDragEvent } from "./events/cell-drag.event";
import { CellEditEvent } from "./events/cell-edit.event";
import { CellSelectEvent } from "./events/cell-select.event";
import { CellSelectionClearEvent } from "./events/cell-selection-clear.event";
import { ContentRefreshedEvent } from "./events/content-refreshed.event";
import { ContentRequestEvent } from "./events/content-request.event";
import { ContentSaveEvent } from "./events/content-save.event";
import { DropAreaSelectEvent } from "./events/drop-area-select.event";
import { KeyPressedEvent } from "./events/keypressed.event";
import { StatusEvent } from "./events/status.event";
import { EventService } from "./events/event.service";


@Component({
	moduleId: module.id,
	selector: "content",
	template: `
	<div id="content" class="card" *ngIf="content">
		<div id="content" class="card-body">
			<drop-area [parent]="model" position="0"></drop-area> <!-- FIXME: this should activate and it doesn't -->
			<cell *ngFor="let cell of content.children; let i=index" 
				[parent]="content" 
				[cell]="cell" [level]="0" 
				[position]="i"
				></cell>
			<!-- TODO: static checks using the moel and not what's already present (cells) -->
		</div>
		<!--ng-container *ngIf="this.cellSelectingMode">cellSelectingMode</ng-container>
		<ng-container *ngIf="this.dropAreaSelectingMode">dropAreaSelectingMode</ng-container-->
	</div>
<!-- THIS DISPLAYS AS IT SHOULD -->
<!--div class="container-fluid" style="border: 2px solid rgba(86, 62, 128, .2)">
  <div class="row" style="border: 2px solid rgba(86, 62, 128, .2)">
	<div class="col-4" style="border: 2px solid rgba(86, 62, 128, .2)">
	  <img class="img-fluid" src="http://localhost:3000/assets/images/data-cell.svg" />
	</div>
	<div class="col-8" style="border: 2px solid rgba(86, 62, 128, .2)">
	  <div class="row">
	<div class="col-6" style="border: 2px solid rgba(86, 62, 128, .2)">
	  <img src="http://localhost:3000/assets/images/data-cell.svg" />
	  <img src="http://localhost:3000/assets/images/data-cell.svg" />
	</div>
	<div class="col-6" style="border: 2px solid rgba(86, 62, 128, .2)">
	  <img src="http://localhost:3000/assets/images/data-cell.svg" />
	  <img src="http://localhost:3000/assets/images/data-cell.svg" />
	</div>

	</div>
  </div>
</div-->
	`,
	styles: [`
		#content {}
	`],
	providers: [
	]
})


export class ContentComponent extends KeyListenerWidget implements OnInit, AfterViewInit {

content: Content;
model: Model;

protected commandKeys: string[] = ["c", "a", "d", "t", "e"];

@ViewChildren(CellComponent) childrenCellComponents: QueryList<CellComponent>;

private cellSelectionClearSubscription: Subscription;
private cellSelectingMode = false;
private dropAreaSelectingMode = false;


constructor(eventService: EventService,
			@Inject("ContentService") private contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("RemoteJSONDataService") private contentSaverService: RemoteDataService
			) {
	super(eventService);
}


ngOnInit() {

	console.log("ContentComponent::ngOnInit()");

	this.subscribe(this.events.service.of(CellDocumentClearEvent).subscribe(s => this.clear()));

	this.subscribe(this.events.service.of(ContentRequestEvent).subscribe(
			requested => this.fetchContentFor(requested.document, requested.model)
	));

	this.subscribe(this.events.service.of(ContentSaveEvent).subscribe(
			save => this.saveContent(save.document)
	));
}


// we make sure we subscribe to new elements if we are waiting for selections at root level
ngAfterViewInit() {

	console.log("ContentComponent::ngAfterViewInit()")
	this.childrenCellComponents.changes.subscribe(c => {
		if (this.cellSelectingMode) {
			this.subscribeChildrenToCellSelection();
			// if we send the vent immediately in the binding changing callback we'll probably be affecting the
			// component binding values after they have been read, we trigger it outside the callback then:
			Promise.resolve(null).then((d)=>this.events.service.publish(new ContentRefreshedEvent(this.content)));
		}
	});

}


fetchContentFor(document_: CellDocument, model: Model) {

	this.events.service.publish(new StatusEvent("Fetching content"));
	const uri = document_.contentURI;
	const contentURI = "/morfeu/dyn/content/"+uri+"?model="+model.URI;
	this.contentService.get(contentURI, Content).subscribe( (content:Content) => {
		console.log("ContentComponent::fetchContent() Got content from Morfeu service ('%s')", uri);
		// we associate the content with the document and the model so it al fits together
		document_.content = content;
		// as we want the model to have all references, we create a copy, as this may have
		// an infinite recursion structure
		let MODEL:Model = Object.create(Model.prototype); // to simulate a static call
		this.model = MODEL.fromJSON(model.toJSON());
		this.model.normaliseReferences();
		content.associateFromRoot(this.model);
		this.displayContent(content);
		this.events.ok();
	},
	error => this.events.problem(error.message),	// error is of the type HttpErrorResponse
	() =>	  this.events.service.publish(new StatusEvent("Fetching content", StatusEvent.DONE))
	);

}


displayContent(content: Content) {

	console.log("[UI] ContentComponent::displayContent()");
	this.content = content;
	this.cellSelectingMode = true;
	this.registerKeyPressedEvents();

}


clear() {

	console.log("[UI] ContentComponent::clearContent()");
	this.cellSelectingMode = false;
	this.unregisterKeyPressedEvents();
	this.content = null;

}


saveContent(document_:CellDocument) {

	this.events.service.publish(new StatusEvent("Saving content"));
	const postURI = "/morfeu/dyn/content/"+document_.contentURI+"?model="+document_.model.getURI();
	const content = document_.content.toJSON();
	console.log("ContentComponent::saveContent('%s')", postURI);

	this.contentSaverService.post<OperationResult>(postURI, content).subscribe(op => {	// YAY!
				console.log("ContentComponent::saveContent: saved in %s milliseconds ", op.operationTime);
				// reloading would go here
				//this.events.service.publish(new CellDocumentSelectionEvent(document_.uri));
			},
			error => this.events.problem(error.message),	 // error is of the type HttpErrorResponse
			() => this.events.service.publish(new StatusEvent("Saving content", StatusEvent.DONE))
	);

}


//// KeyListenerWidget ////

commandPressedCallback(command: string) {

	console.log("[UI] ContentComponent::keyPressed(%s)", command);
	if (this.dropAreaSelectingMode) {
		console.log("[UI] ContentComponent::selection mode deactivated");
		this.events.service.publish(new StatusEvent("Drop area selection mode", StatusEvent.DONE));
		this.dropAreaSelectingMode = false;
	}

	switch (command) {
		case "c":
			// we first send a clear so all children will clear, then back to registered in first level
			console.log("[UI] ContentComponent::cell selection clear");
			this.events.service.publish(new CellSelectionClearEvent());
			this.cellSelectingMode = true;
			this.subscribeChildrenToCellSelection();
			break;
		case "m":		// FIXME: this will now be called, check that it works

			break;
		case "a":
			if (this.cellSelectingMode || this.dropAreaSelectingMode) {
				console.log("[UI] ContentComponent::activating current selection");
				this.events.service.publish(new CellActivateEvent());
			}
			break;
		case "t":
			console.log("[UI] ContentComponent::selection mode active for next numeric key");
			this.events.service.publish(new StatusEvent("Drop area selection mode"));
			this.dropAreaSelectingMode = true;
			this.cellSelectingMode = false;
			break;
		case "d":
			this.events.service.publish(new CellDragEvent());
			break;
		case "e":
			console.log("[UI] ContentComponent::got key to edit current active cell");
			this.events.service.publish(new CellEditEvent());
			break;
	}

}


commandNotRegisteredCallback(command: string) {

	console.log("[UI] ContentComponent::not selecting anything in content");
	this.dropAreaSelectingMode = false;
	this.cellSelectingMode = false;

}


numberPressedCallback(num: number) {

	if (this.cellSelectingMode) {
		console.log("[UI] ContentComponent::numberPressed(%i) [cellSelectingMode]", num);
		this.events.service.publish(new CellSelectEvent(num));
	} else if (this.dropAreaSelectingMode) {
		console.log("[UI] ContentComponent::numberPressed(%i) [dropAreaSelectingMode]", num);
		this.events.service.publish(new DropAreaSelectEvent(num));
		this.events.service.publish(new StatusEvent("Drop area selection mode", StatusEvent.DONE));
	}

}

//// KeyListenerWidget [end] ////


private subscribeChildrenToCellSelection () {
	console.log("Content::subscribeChildrenToCellSelection()");
	// FIXME: detect changes: https://angular.io/api/core/ViewChildren
	// the list of children views is only available ngAfterViewInit but we assume that
	// fetching the content will have been much slower
	// we ensure there were no previous selections, avoiding double or triple selects
	this.unsubscribeChildrenFromCellSelection();
	this.childrenCellComponents.forEach(c => c.subscribeToSelection());
}


private unsubscribeChildrenFromCellSelection () {
	this.childrenCellComponents.forEach(c => c.unsubscribeFromSelection());
}

}

