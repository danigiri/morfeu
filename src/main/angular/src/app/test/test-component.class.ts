// TESTABLE - COMPONENT . INTERFACE . TS

import { Inject, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Content, ContentJSON } from '../content.class';
import { Model, ModelJSON } from '../model.class';

import { ContentComponent } from '../components/content/content.component';
import { ModelComponent } from '../components/model.component';

import { RemoteObjectService } from '../services/remote-object.service';

import { EventListener } from '../events/event-listener.class';
import { EventService } from '../services/event.service';

export abstract class TestComponent extends EventListener implements AfterViewInit {


constructor(eventService: EventService,
			private route: ActivatedRoute,
			@Inject("ContentService") protected contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") protected modelService: RemoteObjectService<Model, ModelJSON>) {
	super(eventService);
}


ngAfterViewInit() {
	this.route.paramMap.subscribe(params => this.test(params.get('case_')));
}


protected abstract test(case_: string): void;


protected load(contentURI: string, model: string) {

	const contentAndModelURI = ContentComponent.contentURIFrom(contentURI, model);
	const modelURI = ModelComponent.modelURIFrom(model);

	this.modelService.get(modelURI, Model).subscribe((m: Model) => {
		this.contentService.get(contentAndModelURI, Content).subscribe( (c: Content) => this.loaded(m, c));
	});

}

protected loadModel(model: string): void {

	const modelURI = ModelComponent.modelURIFrom(model);
	this.modelService.get(modelURI, Model).subscribe((m: Model) => this.loadedModel(m));

}


protected loaded(model: Model, content: Content): void {}


protected loadedModel(model: Model): void {}




}

/*
 *	  Copyright 2020 Daniel Giribet
 *
 *	 Licensed under the Apache License, Version 2.0 (the "License");
 *	 you may not use this file except in compliance with the License.
 *	 You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 *	 Unless required by applicable law or agreed to in writing, software
 *	 distributed under the License is distributed on an "AS IS" BASIS,
 *	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	 See the License for the specific language governing permissions and
 *	 limitations under the License.
 */
