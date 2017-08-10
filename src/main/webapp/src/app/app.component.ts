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

import { Component, AfterViewInit } from '@angular/core';
import { Subscription }   from 'rxjs/Subscription';
import { isDevMode } from '@angular/core';
import { HttpModule } from '@angular/http';
import { Http } from '@angular/http';

import { CatalogueListComponent } from './catalogue-list.component';
import { ContentComponent } from './content.component';
import { CellDocumentComponent } from './cell-document.component';
import { ProblemComponent } from './problem.component';
import { StatusComponent } from './status.component';
import { RemoteDataService } from './services/remote-data.service';
import { RemoteObjectService } from './services/remote-object.service';

import { Model, ModelJSON } from './model.class';
import { Catalogue } from './catalogue.class';
import { CellDocument } from './cell-document.class';

import { Widget } from './widget.class';

import { CataloguesRequestEvent } from './events/catalogues-request.event';
import { CataloguesLoadedEvent } from './events/catalogues-loaded.event';
import { CatalogueLoadedEvent } from './events/catalogue-loaded.event';
import { CatalogueSelectionEvent } from './events/catalogue-selection.event';
import { CellDocumentSelectionEvent } from './events/cell-document-selection.event';
import { EventService } from './events/event.service';


@Component({
    selector: 'app-root',
    template: `
      <div class="page-header">
          <h1>Morfeu application</h1>
      </div>
      
      <div class="container-fluid">
          <div class="row">
            <div class="col-md-2">
              <catalogue-list></catalogue-list>
              <document></document>
            </div>
            <div class="col-md-8">
                <content></content>
            </div>
            <div class="col-md-2">
                <model></model>
            </div>
          </div>
          <div class="row">
              <div class="col-md-12">
                  <status></status>
                  <problem></problem>
              </div>
          </div>
      </div>


      `,
    providers:    [
                   // note that Http is injected by the HttpModule imported in the application module
                   {provide: 'CatalogueService', useFactory: (http:Http) => (new RemoteDataService<Catalogue>(http)), deps: [Http]}                   
                   ,EventService
                   ,{provide: 'CellDocumentService', useFactory: (http:Http) => (new RemoteDataService<CellDocument>(http)), deps: [Http]}
                   ,{provide: 'ModelService', useFactory: (http:Http) => (new RemoteObjectService<Model, ModelJSON>(http)), deps: [Http]}
                   ]
})


export class AppComponent extends Widget implements AfterViewInit {
   
private cataloguesLoadedEventSubscription: Subscription;
private catalogueLoadedEventSubscription: Subscription

constructor(eventService: EventService) {
    super(eventService);
}

//export function dataService = (http:Http) => {return new RemoteDataService<Model>(http)}


// this hoock is called "after Angular initializes the component's views and child views." so everyone has
// been able to to register their listeners to appropriate events
ngAfterViewInit() {
    
    console.log("AppComponent::ngAfterViewInit()");
    
    // this event loads the default catalogue and starts everything in motion
    //
    // See https://hackernoon.com/everything-you-need-to-know-about-the-expressionchangedafterithasbeencheckederror-error-e3fd9ce7dbb4
    // the problem here is that we have created components with specific data bindings (for instance the
    // animation state of the status bar, which is setup to be hidden) and the load event will change some of
    // them. The changes will happen after the lifecycle stage of (re)setting data-bound input properties of
    // components) and will end up in an inconsistent state when verified. Therefore we're scheduling this to 
    // be fired asynchronously in a micro-event that runs after the data-binding verification step
    // Also see https://angular.io/guide/lifecycle-hooks
    // 
    // This should be ok as we assume the subscriptions should be done at the ngOnInit event, to ensure that
    // events can use binding properties that have been setup properly
   
    // THIS IS TO SPEED UP DEVELOPMENT, WE SPEED THE STATE INTO THE DESIRED ONE

    if (isDevMode()) {
        // we only want to do these once, hence the unsubscriptions
//        this.cataloguesLoadedEventSubscription = this.subscribe(this.events.service.of( CataloguesLoadedEvent ).subscribe( loaded => {
//            this.unsubscribe(this.cataloguesLoadedEventSubscription);
//            let catalogue = loaded.catalogues[0].uri;
//            this.events.service.publish(new CatalogueSelectionEvent(catalogue));
//        }));
//        this.catalogueLoadedEventSubscription = this.subscribe(this.events.service.of( CatalogueLoadedEvent ).subscribe( loaded => {
//            this.unsubscribe(this.catalogueLoadedEventSubscription);
//            let document = loaded.catalogue.documents[0].uri;
//            this.events.service.publish(new CellDocumentSelectionEvent(document));
//        })); 
    }
    
    console.log("\t\t\t\t\t **** APPLICATION STARTS ****");
    let allCatalogues = "/morfeu/test-resources/catalogues.json";
    Promise.resolve(null).then(() => this.events.service.publish(new CataloguesRequestEvent(allCatalogues)));
    
}

}

