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

import { Component, Input, OnDestroy } from '@angular/core';
import { Subscription }   from 'rxjs/Subscription';

import { Document } from './document.class';
import { Widget } from './widget.class';

import { EventService } from './events/event.service';
import { DocumentSelectionEvent } from './events/document-selection.event';


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

export class DocumentComponent extends Widget implements OnDestroy {

document: Document;
documentSubscription: Subscription;


constructor(eventService: EventService) {
    super(eventService);
    
    console.log("DocumentComponent::constructor()");
    this.documentSubscription = this.events.service.of(DocumentSelectionEvent).subscribe(
            selected => {
                if (selected.document!=null) {
                    this.loadDocument(selected.document);
                } else {
                    this.clearDocument();
                }
                
            }
    );
    
}


loadDocument(d: Document) {
    console.log("-> document component gets Document ("+d.name+")");
    this.document = d;
    if (d.problem==null || d.problem!="") {
        this.events.problem(d.problem);
    }
}


clearDocument() {
    console.log("-> document component gets null document (no document selected)");
    this.document = null;
}


ngOnDestroy() {
    this.documentSubscription.unsubscribe();
}

}