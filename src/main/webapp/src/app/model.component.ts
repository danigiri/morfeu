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

import { CellModelComponent } from './cell-model.component';

import { Model, ModelJSON } from './model.class';
import { Widget } from './widget.class';
import { RemoteObjectService } from './services/remote-object.service';

import { CellDocumentLoadedEvent } from './events/cell-document-loaded.event';
import { CellDocumentSelectionEvent } from './events/cell-document-selection.event';
import { EventService } from './events/event.service';
import { ModelRequestEvent } from './events/model-request.event';
import { StatusEvent } from './events/status.event';


@Component({
    moduleId: module.id,
    selector: 'model',
    template: `
    <div id="model-info" class="panel panel-info" *ngIf="model">
        <div class="panel-heading">
            <h4 id="model-name" class="panel-title">Model: {{model.name}}</h4>
        </div>
        <div class="panel-body">
            <div class="panel panel-info">
              <div id="model-desc" class="panel-body">
                {{model.desc}}
              </div>
            </div>
            <!-- non-intuitively, the nodes binding expects an array and not a root node-->
            <!-- we use direct binding as opposed to events for the moment -->
            <div id="model-cell-models" class="panel panel-info">
                <tree-root
                    [nodes]="model.cellModels">
                    <ng-template #treeNodeTemplate let-node let-index="index">
                       <cell-model [node]="node" [index]="index"></cell-model>
                    </ng-template>
                </tree-root>
            </div>
        </div>
    </div>
    `,

    styles:[`
                #model-info {}
                #model-name {}
                #model-desc {}
                #model-cell-model-list {}
                
    `]
})

export class ModelComponent extends Widget implements OnInit {
    
model: Model;
    

constructor(eventService: EventService, 
            @Inject("ModelService") private modelService: RemoteObjectService<Model, ModelJSON> ) {
    super(eventService);
}
    

ngOnInit() {
    
    console.log("ModelComponent::ngOnInit()"); 
    
    this.subscribe(this.events.service.of(CellDocumentSelectionEvent).filter(s => s.url==null).subscribe(
            selected => this.clearModel()
    ));

    // if we load a problematic document we don't display anything (enjoying event-based coding right now)
    this.subscribe(this.events.service.of(CellDocumentLoadedEvent)
            .filter(loaded => loaded.document.problem!=null && loaded.document.problem.length>0 )
            .subscribe(loadedProblematicDocument => this.clearModel())
    );
    
    this.subscribe(this.events.service.of(ModelRequestEvent).subscribe( requested =>
            this.loadModel(requested.url)           
    ));
    
}


loadModel(uri: string) {

    this.events.service.publish(new StatusEvent("Fetching model"));
    let modelURI = "/morfeu/models/"+uri;
    this.modelService.get(modelURI, Model).subscribe( (model:Model) => {
            console.log("ModelComponent::loadModel() Got model from Morfeu service ("+model.name+")");
            this.diplayModel(model);    // not firing a load event yet if not needed
            this.events.ok();
    },
    error => {
        this.events.problem(error);
    },
    () =>     this.events.service.publish(new StatusEvent("Fetching model", StatusEvent.DONE))
    );
    
}


clearModel() {

    console.log("[UI] ModelComponent::clearModel()");
    this.model = null;

}


diplayModel(m: Model) {
    
    console.log("[UI] ModelComponent::diplayModel("+m.name+")");
    this.model = m;
    
}

}