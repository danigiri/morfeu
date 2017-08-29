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

import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { Subscription }   from 'rxjs/Subscription';

import { CellDocument } from './cell-document.class';
import { Content, ContentJSON } from './content.class';
import { Model } from './model.class';
import { RemoteObjectService } from './services/remote-object.service';
import { Widget } from './widget.class';
import { SerialisableToJSON } from './serialisable-to-json.interface';

import { EventService } from './events/event.service';
import { CellDocumentSelectionEvent } from './events/cell-document-selection.event';
import { ContentRequestEvent } from './events/content-request.event';


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


export class ContentComponent extends Widget implements OnInit {
    
document: CellDocument;


constructor(eventService: EventService,
            @Inject("ContentService") private contentService: RemoteObjectService<Content, ContentJSON> ) {
    super(eventService);
}


ngOnInit() {

    console.log("ContentComponent::ngOnInit()");
    
    this.subscribe(this.events.service.of(CellDocumentSelectionEvent).filter(s => s.url==null).subscribe(
            selected => this.clearContent()
    ));

    
    this.subscribe(this.events.service.of(ContentRequestEvent).subscribe(
            requested => this.fetchContent(requested.url, requested.model)
    ));
    
}

fetchContent(url:String, model:Model) {

}

clearContent() {
    
}

displayContent() {
    
}

}

