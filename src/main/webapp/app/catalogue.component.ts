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

import { Component, Inject, OnInit } from '@angular/core';
import { Subscription }   from 'rxjs/Subscription';

import { Widget } from './widget.class';
import { Catalogue } from './catalogue.class';

import { CellDocument } from './cell-document.class';
import { RemoteDataService } from './services/remote-data.service';

import { CatalogueSelectionEvent } from './events/catalogue-selection.event';
import { CatalogueLoadedEvent } from './events/catalogue-loaded.event';
import { EventService } from './events/event.service';
import { CellDocumentSelectionEvent } from './events/cell-document-selection.event';
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
                    [class.active]="d.uri === selectedDocumentURI"
                    (click)="clickOnDocument(d)">
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
selectedDocumentURI: string;

constructor(eventService: EventService,
            @Inject("CatalogueService") private catalogueService: RemoteDataService<Catalogue> ) {
    super(eventService);
}

ngOnInit() {
    
    console.log("DocumentComponent::ngOnInit()");
    
    this.subscribe(this.events.service.of(CellDocumentSelectionEvent).subscribe(
            selected => this.markDocumentAsSelected(selected.url)
    ));

    this.subscribe(this.events.service.of(CatalogueSelectionEvent).subscribe(
            selected => this.loadCatalogueAt(selected.url)
    ));
    
            
}


loadCatalogueAt(selectedCatalogueUri: string) {

    this.selectedDocumentURI = null;
    this.events.service.publish(new CellDocumentSelectionEvent(null));  // reset document selection and related

    this.events.service.publish(new StatusEvent("Fetching catalogue"));
    this.catalogueService.get(selectedCatalogueUri)
            .subscribe(c => { 
                this.catalogue = c;
                this.events.service.publish(new CatalogueLoadedEvent(c));
                this.events.ok();
            },
            error => {
                this.events.problem(error);
                this.catalogue = null;
            },
            // FIXME: in case of error, the completed lambda is not ran, so the status bar is not updated ??
            () => {this.events.service.publish(new StatusEvent("Fetching catalogue", StatusEvent.DONE))}
        );
        
}
 

clickOnDocument(stub: CellDocument) {
    console.log("[UI] Clicked on document='"+stub.uri+"' from catalogue");
    this.events.service.publish(new CellDocumentSelectionEvent(stub.uri));
    
}

markDocumentAsSelected(uri: string) {
    console.log("[UI] Marking document='"+uri+"' as selected in catalogue");
    this.selectedDocumentURI = uri;
}

}