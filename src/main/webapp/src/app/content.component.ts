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
import { CellDocumentLoadedEvent } from './events/cell-document-loaded.event';
import { CellDocumentSelectionEvent } from './events/cell-document-selection.event';
import { ContentRequestEvent } from './events/content-request.event';
import { StatusEvent } from './events/status.event';


@Component({
    moduleId: module.id,
    selector: 'content',
    template: `
    <div class="panel panel-default" *ngIf="content">
        <div id="#content" class="panel-body">
            <ng-container *ngFor="let cell of content.cells">
                <cell [cell]="cell"></cell>
            </ng-container>
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
    
content: Content;


constructor(eventService: EventService,
            @Inject("ContentService") private contentService: RemoteObjectService<Content, ContentJSON> ) {
    super(eventService);
}


ngOnInit() {

    console.log("ContentComponent::ngOnInit()");
    
    this.subscribe(this.events.service.of(CellDocumentSelectionEvent).filter(s => s.url==null).subscribe(
            selected => this.clearContent()
    ));

    // if we load a problematic document we don't display anything
    this.subscribe(this.events.service.of(CellDocumentLoadedEvent)
            .filter(loaded => loaded.document.problem!=null && loaded.document.problem.length>0 )
            .subscribe(loadedProblematicDocument => this.clearContent())
    );
    
    this.subscribe(this.events.service.of(ContentRequestEvent).subscribe(
            requested => this.fetchContent(requested.url, requested.model)
    ));
    
}


fetchContent(url:String, model:Model) {

    this.events.service.publish(new StatusEvent("Fetching content"));
    let contentURI = "/morfeu/content/"+url+"?model="+model.URI;
    this.contentService.get(contentURI, Content).subscribe( (content:Content) => {
        console.log("ContentComponent::fetchContent() Got content from Morfeu service ("+url+")");
        content.associateWith(model);
        this.displayContent(content);
        this.events.ok();
    },
    error => {
        this.events.problem(error);
    },
    () =>     this.events.service.publish(new StatusEvent("Fetching content", StatusEvent.DONE))
    );
    
}



displayContent(content: Content) {
    console.log("[UI] ContentComponent::displayContent()");
    this.content = content;
}


clearContent() {
    console.log("[UI] ContentComponent::clearContent()");
    this.content = null;
}


}

