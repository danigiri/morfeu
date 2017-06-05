/*
 *    Copyright 2017 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import { Component, Input } from '@angular/core';

import { Widget } from './widget.class';
import { Catalogue } from './catalogue';
import { CatalogueService } from './catalogue.service';
import { DocumentService } from './document.service';
import { Document } from './document.class';

import { EventService } from './events/event.service';
import { DocumentSelectionEvent } from './events/document-selection.event';
import { StatusEvent } from './events/status.event';


@Component({
	moduleId: module.id,
	selector: 'catalogue',
	template: `
    <div id="catalogue" class="panel panel-default" *ngIf="catalogue">
        <div class="panel-heading">
          <h4 id="catalogue-name" class="panel-title">{{catalogue.name}}</h4>
        </div>
        <div id="catalogue-desc" class="panel-body">
            {{catalogue.desc}}
        </div>
        <div class="panel-body">
	        <div id="document-list" class="list-group">
                <a *ngFor="let d of catalogue.documents"
                    href="#" 
                    class="document-list-entry list-group-item" 
                    [class.active]="d === currentDocument"
                    (click)="selectdocument(d)">
                    {{d.name}}
                </a>
        </div>
        </div>
      </div>
	`,
    styles:[`
        #catalogue {}
        #catalogue-name {}
        #catalogue-desc {}
        #document-list {}
        #document-list-entry {}
    `],
    providers:[
    ]
})
	
	//`
export class CatalogueComponent extends Widget {
	
catalogue: Catalogue;
currentDocument: Document;

constructor(private catalogueService : CatalogueService, 
            eventService: EventService,
            private documentService: DocumentService) {
    super(eventService);
}


@Input() 
set selectedCatalogueUri(selectedCatalogueUri: string) {
    
    this.events.service.publish(new StatusEvent("Fetching catalogue"));
    this.catalogueService.getCatalogue(selectedCatalogueUri)
    .subscribe(c => { 
        this.catalogue = c;
        this.events.ok();
    },
    error => {
        this.events.problem(error);
        this.catalogue = null;
    },
    () => {this.events.service.publish(new StatusEvent("Fetching catalogue", StatusEvent.DONE))}
    );
    
}
 

selectdocument(d: Document) {
        
    console.log("Selected document="+d.uri);
    this.currentDocument = d;                   // notice 'd' is a stub from the catalogue, not a full doc
    this.events.service.publish(new DocumentSelectionEvent(null));  // we don't have a document now
    this.events.service.publish(new StatusEvent("Fetching document"));
    this.documentService.getDocument("/morfeu/documents/"+d.uri)
    .subscribe(d => {
        console.log("Got document from Morfeu service ("+d.name+")");
       // this.documentService.setDocument(d);
        this.events.service.publish(new DocumentSelectionEvent(d)); // now we have it =)
        this.events.ok();
    },
    error => {
        this.events.problem(error);
        this.events.service.publish(new DocumentSelectionEvent(null));
        this.currentDocument = null;
    },
    () =>     this.events.service.publish(new StatusEvent("Fetching document", StatusEvent.DONE))
    );
    
}

}