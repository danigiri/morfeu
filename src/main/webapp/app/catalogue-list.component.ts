/*
 *    Copyright 2016 Daniel Giribet
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

import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription }   from 'rxjs/Subscription';
import { Observable } from 'rxjs/Observable';

import { Widget } from './widget.class';

import { CatalogueComponent } from './catalogue.component';
import { Catalogue } from './catalogue';
import { CatalogueService } from './catalogue.service';

import { CatalogueSelectionEvent } from './events/catalogue-selection.event';
import { CataloguesRequestEvent } from './events/catalogues-request.event';
import { CataloguesLoadedEvent } from './events/catalogues-loaded.event';
import { CellDocumentSelectionEvent } from './events/cell-document-selection.event';
import { EventService } from './events/event.service';
import { StatusEvent } from './events/status.event';


@Component({
    moduleId: module.id,
    selector: 'catalogue-list',
    template: `
     <div class="panel panel-default">
      <div class="panel-heading">
        <h4 class="panel-title">Catalogues</h4>
      </div>
      <div class="panel-body">
        <div id="catalogue-list" class="list-group">
            <a *ngFor="let c of catalogues"
                 href="#" 
                class="catalogue-list-entry list-group-item" 
                [class.active]="c.uri === selectedCatalogueURI"
                (click)="clickOnCatalogue(c)">
            {{c.name}}</a>
        </div>
      </div>
    </div>
    <catalogue></catalogue>
    `,
    styles:[`
    #catalogue-list {}
    .catalogue-list-entry {}
    `],
    providers: [
                ]
})
    
export class CatalogueListComponent extends Widget implements OnDestroy {
    
catalogues: Catalogue[];
selectedCatalogueURI: string;
eventSubscription: Subscription;

constructor(private catalogueService: CatalogueService, eventService: EventService) {
    super(eventService);
}


ngOnInit() {

    console.log("StatusComponent::ngOnInit()");
    
    this.eventSubscription = this.events.service.of( CataloguesRequestEvent ).subscribe( s => {
           console.log("-> catalogue-list component gets request event for '"+s.url+"'");
           this.fetchCatalogues(s.url);
    });
    
    // on catalogue selection we highlight the selected catalogue
    this.eventSubscription = this.events.service.of( CatalogueSelectionEvent ).subscribe( s => {
        console.log("-> catalogue-list component gets selection event for '"+s.url+"'");
        this.markCatalogueAsSelected(s.url);
    });
    
}


fetchCatalogues(url: string) {

    this.selectedCatalogueURI = null;
    this.events.service.publish(new StatusEvent("Fetching catalogues"));
    // TODO: make this configurable and into an event
    this.catalogueService.getAll(url)
    .subscribe(c => { 
                     
                     this.catalogues = c;
                     this.events.service.publish(new CataloguesLoadedEvent(c));
                     this.events.ok();
                    },
               error => this.events.problem(error),
               () => this.events.service.publish(new StatusEvent("Fetching catalogues", StatusEvent.DONE))
               );
}


clickOnCatalogue(c: Catalogue) {

    console.log("Clicked on catalogue="+c.uri);
    this.events.service.publish(new CatalogueSelectionEvent(c.uri));    // catalogue component will pick this 
    
}


markCatalogueAsSelected(uri: string) {
    this.selectedCatalogueURI = uri;
}


//TODO: we should pull this up to the widget superclass
ngOnDestroy() {
    this.eventSubscription.unsubscribe();
}


}