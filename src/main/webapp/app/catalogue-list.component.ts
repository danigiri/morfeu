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

import { Component } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Widget } from './widget.class';

import { CatalogueComponent } from './catalogue.component';
import { Catalogue } from './catalogue';
import { CatalogueService } from './catalogue.service';
import { DocumentService } from './document.service';

import { EventService } from './events/event.service';
import { StatusEvent } from './events/status.event';
import { DocumentSelectionEvent } from './events/document-selection.event';


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
                [class.active]="c === currentCatalogue"
                (click)="selectCatalogue(c)">
            {{c.name}}</a>
        </div>
      </div>
    </div>
    <catalogue *ngIf="currentCatalogue" [selectedCatalogueUri]="currentCatalogue.uri"></catalogue>
    `,
    styles:[`
    #catalogue-list {}
    .catalogue-list-entry {}
    `],
    providers: [
                ]
})
    
export class CatalogueListComponent extends Widget {
    
catalogues: Catalogue[];
currentCatalogue: Catalogue;

constructor(private catalogueService: CatalogueService, eventService: EventService) {
    super(eventService);
}

ngOnInit() {

    this.events.service.publish(new StatusEvent("Fetching catalogue list"));
    // TODO: make this configurable and into an event
    this.catalogueService.getAll("/morfeu/test-resources/catalogues.json")
    .subscribe(c => { 
                     
                     this.catalogues = c;
                     this.events.ok();
                    },
               error => this.events.problem(error),
               () => this.events.service.publish(new StatusEvent("Fetching catalogue list", StatusEvent.DONE))
               );

}

selectCatalogue(c:Catalogue) {

    console.log("Selected catalogue="+c.uri);
    this.events.service.publish(new DocumentSelectionEvent(null));  // reset document selection
    this.currentCatalogue = c;

}


}