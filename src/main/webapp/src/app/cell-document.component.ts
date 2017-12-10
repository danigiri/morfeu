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

import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { Subscription }	  from 'rxjs/Subscription';

import { CellDocument } from './cell-document.class';
import { Widget } from './widget.class';
import { RemoteDataService } from './services/remote-data.service';

import { EventService } from './events/event.service';
import { CellDocumentSelectionEvent } from './events/cell-document-selection.event';
import { CellDocumentLoadedEvent } from './events/cell-document-loaded.event';
import { ContentRequestEvent } from './events/content-request.event';
import { ModelRequestEvent } from './events/model-request.event';
import { StatusEvent } from './events/status.event';
import { UXEvent } from './events/ux.event';

@Component({
	moduleId: module.id,
	selector: 'document',
	template: `
	<div id="document-info" class="card" *ngIf="document">
        <h5 id="document-name" class="card-header">{{document.name}} <span class="badge badge-primary float-right">{{document.kind}}</span></h5>
        <div class="card-body">
        		<div class="card-text">
        			<span id="document-desc">{{document.desc}}</span>
        			<span id="document-valid" *ngIf="document.valid" class="badge badge-pill badge-success float-right">VALID</span>
        			<span id="document-valid" *ngIf="!document.valid" class="badge badge-pill badge-danger float-right">NON VALID</span>
        		</div>
             <button type="button" *ngIf="document.valid"
                 class="btn btn-success btn-lg btn-block btn-sm mt-2"
                 [class.disabled]="saveDisabled" 
                 >SAVE</button>
             <button type="button" *ngIf="document.valid"
                 class="btn btn-warning btn-lg btn-block btn-sm mt-2">Restore</button>
	    </div>
    </div>
	`,
	styles:[`
			#document-info {}
			#document-name {}
			#document-desc {}
			#document-valid {}
	`] 
   
})
//`

export class CellDocumentComponent extends Widget implements OnInit {

document: CellDocument;
saveDisabled: boolean = true;

constructor(eventService: EventService,
			@Inject("RemoteJSONDataService") private documentService: RemoteDataService ) {
	super(eventService);
}


ngOnInit() {

	console.log("DocumentComponent::ngOnInit()");
	
	this.subscribe(this.events.service.of(CellDocumentSelectionEvent).subscribe(
			selected => {
				if (selected.url!=null) {
					this.loadDocument(selected.url);
				} else {
					this.clear();
				}

			}
	));
	
	this.subscribe(this.events.service.of(CellDocumentLoadedEvent).subscribe(
			loaded => { 
	
				this.display(loaded.document);
				if (loaded.document.problem==null || loaded.document.problem.length==0) {
					this.events.ok();
					this.events.service.publish(new ModelRequestEvent(loaded.document));
				} else {
					// even though there is a case where we could display the model of a problematic document,
					// for instance, when the model is ok but the content is not found, we're conservative
					// and not fetch the model, just display the problem
					this.events.problem(loaded.document.problem);
				}

			}
	));
	
	// when the document is dirty we can save, this will be notified by someone elsem (content area, etc)
	this.subscribe(this.events.service.of( UXEvent ) 
	        .filter( e => e.type==UXEvent.DOCUMENT_DIRTY)
	        .subscribe( e => this.enableSave() ));

}


loadDocument(url: string) {

	//this.events.service.publish(new DocumentSelectionEvent(null));  // we don't have a document now
	this.events.service.publish(new StatusEvent("Fetching document"));
	// notice we're using the enriched url here, as we want to display the JSON enriched data
	this.documentService.get<CellDocument>("/morfeu/documents/"+url).subscribe(d => {
				console.log("DocumentComponent::loadDocument() Got document from Morfeu ("+d.name+")");
				this.events.service.publish(new CellDocumentLoadedEvent(d)); // now we have it =)
			},
			error => {
				this.events.problem(error);
				this.events.service.publish(new CellDocumentSelectionEvent(null));
				this.document = null;
			},
			() =>	  this.events.service.publish(new StatusEvent("Fetching document", StatusEvent.DONE))
	);

}


display(d: CellDocument) {

	console.log("[UI] document component gets Document ("+d.name+")");
	this.document = d;
	this.disableSave();
	
}


clear() {

	console.log("[UI] document component gets null document (no document selected)");
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
    
    this.events.service.publish(new StatusEvent("Fetching content"));
    let contentURI = "/morfeu/content/"+this.document.uri+"?model="+this.document.modelURI;
    
}

}