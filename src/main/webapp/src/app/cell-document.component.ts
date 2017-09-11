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


@Component({
	moduleId: module.id,
	selector: 'document',
	template: `
	<div id="document-info" class="panel panel-default" *ngIf="document">
		<div class="panel-heading">
		  <h4 id="document-name" class="panel-title">{{document.name}} <span class="badge">{{document.kind}}</span></h4>
		</div>
		<div class="panel-body">
			<span id="document-desc">{{document.desc}}</span>
			<span id="document-valid" *ngIf="document.valid" class="label label-success">VALID</span>
			<span id="document-valid" *ngIf="!document.valid" class="label label-danger">NON VALID</span>
		
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

constructor(eventService: EventService,
			@Inject("CatalogueService") private documentService: RemoteDataService<CellDocument> ) {
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
	
}


loadDocument(url: string) {

	//this.events.service.publish(new DocumentSelectionEvent(null));  // we don't have a document now
	this.events.service.publish(new StatusEvent("Fetching document"));
	// notice we're using the enriched url here, as we want to display the JSON enriched data
	this.documentService.get("/morfeu/documents/"+url).subscribe(d => {
				console.log("DocumentComponent::loadDocument() Got document from Morfeu service ("+d.name+")");
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

}


clear() {

	console.log("[UI] document component gets null document (no document selected)");
	this.document = null;

}

}