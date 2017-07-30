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


import { Model } from './model.class';
import { Widget } from './widget.class';
import { RemoteDataService } from './services/remote-data.service';

import { EventService } from './events/event.service';
import { ModelRequestEvent } from './events/model-request.event';
import { StatusEvent } from './events/status.event';


@Component({
    moduleId: module.id,
    selector: 'model',
    template: `
    <div id="model-info" class="panel panel-default" *ngIf="model">
        <div class="panel-heading">
            <h4 id="model-name" class="panel-title">Model: {{model.name}}</h4>
        </div>
        <div class="panel-body">
            <span id="model-desc">{{model.desc}}</span>
        </div>
    </div>
    `,

    styles:[`
                #model-info {}
                #model-name {}
                #model-desc {}
    `]
})

export class ModelComponent extends Widget implements OnInit {
    
model: Model;
    
    
constructor(eventService: EventService, 
            @Inject("ModelService") private modelService: RemoteDataService<Model> ) {
    super(eventService);
}
    

ngOnInit() {
    
    console.log("ModelComponent::ngOnInit()"); 
    
    this.subscribe(this.events.service.of(ModelRequestEvent).subscribe( requested =>
            this.loadModel(requested.url)           
    ));
    
}

loadModel(uri: string) {
    this.events.service.publish(new StatusEvent("Fetching model"));
    let modelURI = "/morfeu/models/"+uri;
    this.modelService.get(modelURI).subscribe( model => {
            console.log("ModelComponent::loadModel() Got model from Morfeu service ("+model.name+")");
            this.model = model;
            this.events.ok();
    },
    error => {
        this.events.problem(error);
    },
    () =>     this.events.service.publish(new StatusEvent("Fetching model", StatusEvent.DONE))
    );
}


}