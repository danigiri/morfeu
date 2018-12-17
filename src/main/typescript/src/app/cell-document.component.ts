// CELL - DOCUMENT . COMPONENT . TS

import {filter} from 'rxjs/operators';
import { Component, Inject, OnInit } from "@angular/core";

import { CellDocument, CellDocumentJSON } from "./cell-document.class";
import { RemoteObjectService } from "./services/remote-object.service";

import { CellDocumentSelectionEvent } from "./events/cell-document-selection.event";
import { CellDocumentClearEvent } from "./events/cell-document-clear.event";
import { CellDocumentLoadedEvent } from "./events/cell-document-loaded.event";
import { ContentSaveEvent } from "./events/content-save.event";
import { StatusEvent } from "./events/status.event";
import { UXEvent } from "./events/ux.event";
import { EventListener } from "./events/event-listener.class";
import { EventService } from "./services/event.service";
import { RemoteEventService } from "./services/remote-event.service";

@Component({
	moduleId: module.id,
	selector: "document",
	template: `
	<div id="document-info" class="card mt-2" *ngIf="document">
		<h5 id="document-name"
			class="card-header"
		>{{document.name}} <span class="badge badge-primary float-right">{{document.kind}}</span></h5>
		<div class="card-body">
				<div class="card-text">
					<span id="document-desc">{{document.desc}}</span>
					<span id="document-valid" *ngIf="document.valid" class="badge badge-pill badge-success float-right"
					>VALID</span>
					<span id="document-valid" *ngIf="!document.valid" class="badge badge-pill badge-danger float-right"
					>NON VALID</span>
				</div>
				<!-- we have the buttons here as it makes sense from a UI perspective, but the event itself
					 will be handled by the content component -->
			 <button *ngIf="document.valid"
				id="document-save"
				type="button"
				class="btn btn-success btn-lg btn-block btn-sm mt-2"
				[class.disabled]="saveDisabled"
				(click)="saveDocument()"
			>SAVE</button>
			 <button type="button" *ngIf="document.valid"
				 class="btn btn-warning btn-lg btn-block btn-sm mt-2">Restore</button>
		</div>
	</div>
	`,
	styles: [`
			#document-info {}
			#document-name {}
			#document-desc {}
			#document-valid {}
			#document-save {}
	`]
})
// `

export class CellDocumentComponent extends EventListener implements OnInit {

document: CellDocument;
saveDisabled = true;

constructor(eventService: EventService,
			remoteEventService: RemoteEventService,
			@Inject("CellDocumentService") private documentService: RemoteObjectService<CellDocument, CellDocumentJSON>
			) {
	super(eventService, remoteEventService);
}


ngOnInit() {

	console.log("DocumentComponent::ngOnInit()");

	this.subscribe(this.events.service.of(CellDocumentClearEvent).subscribe(s => {
			this.clear();
			this.events.ok();
		}
	));

	this.subscribe(this.events.service.of(CellDocumentSelectionEvent).subscribe(
			selected => this.loadDocument(selected.url)
	));

	// when the document is dirty we can save, this will be notified by someone elsem (content area, etc)
	this.subscribe(this.events.service.of( UXEvent ).pipe(
			filter(e => e.type===UXEvent.DOCUMENT_DIRTY))
			.subscribe(e => this.enableSave())
	);

}


loadDocument(url: string) {

	// this.events.service.publish(new DocumentSelectionEvent(null));  // we don't have a document now
	this.events.service.publish(new StatusEvent("Fetching document"));
	// notice we're using the enriched url here, as we want to display the JSON enriched data
	this.documentService.get("/morfeu/dyn/documents/"+url, CellDocument).subscribe(d => {

				console.log("DocumentComponent::loadDocument() Got document from Morfeu ("+d.name+")");
				this.events.remote.publish(new CellDocumentClearEvent());	// clear everything (subscriptions, etc.)
				if (!d.hasProblem()) {	// we only publish the load if we have no issues with the doc
					this.events.service.publish(new CellDocumentLoadedEvent(d));
					this.display(d);
					this.events.ok();
				} else {
					// after clearing we show the problem message and the problematic document stub
					this.problem(d.problem);   // document loaded but was problematic
					this.document = d;		   // we still show whatever was answered back
				}
			},
			error => {
				console.log("DocumentComponent::loadDocument() itself got an error");
				this.problem(error.message);	 // error is of the type HttpErrorResponse
				this.events.remote.publish(new CellDocumentClearEvent());	// also clear document
				this.document = null;			 // we have no document loaded at all, so no show here
			},
			() => this.events.service.publish(new StatusEvent("Fetching document", StatusEvent.DONE))
	);

}


display(d: CellDocument) {

	console.log("[UI] document component gets Document ("+d.name+")");
	this.document = d;
	this.disableSave();

}


clear() {

	console.log("[UI] CellDocumentComponent::clear()");
	this.document = null;
	this.disableSave();

}


disableSave() {
	this.saveDisabled = true;
}


enableSave() {
	this.saveDisabled = false;
}


saveDocument() {

	console.log("[UI] User clicked on save document, let's go!!!");
	this.events.service.publish(new ContentSaveEvent(this.document));

}


private problem(message: String) {

	console.log("[UI] CellDocumentComponent::problem()");
	this.events.problem(message);

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
