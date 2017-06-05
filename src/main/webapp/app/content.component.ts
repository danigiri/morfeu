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
import { Subscription }   from 'rxjs/Subscription';

import { Document } from './document.class';
import { Widget } from './widget.class';

import { EventService } from './events/event.service';
import { DocumentSelectionEvent } from './events/document-selection.event';


@Component({
    moduleId: module.id,
    selector: 'content',
    template: `
    <div class="panel panel-default" *ngIf="document">
        <div id="#content" class="panel-body">

        </div>
    </div>
    `,
    styles:[`
        #content {}
    `],
    providers:[
    ]
})

export class ContentComponent extends Widget {
    
document: Document;
documentSubscription: Subscription;

constructor(eventService: EventService) {
    super(eventService);
    
    console.log("DocumentComponent::constructor()");
    this.documentSubscription = this.events.service.of(DocumentSelectionEvent).subscribe(
            selected => {
                this.document = selected.document;
                
            }
    );
    
}

ngOnDestroy() {
    this.documentSubscription.unsubscribe();
}


}

