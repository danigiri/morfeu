// TEST - COMPONENT . CLASS . TS

import { Inject, AfterViewInit, Directive } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { CellDocument } from '../cell-document.class';
import { Content, ContentJSON } from '../content.class';
import { Model, ModelJSON } from '../model.class';

import { ContentComponent } from '../components/content/content.component';
import { ModelComponent } from '../components/model.component';

import { RemoteObjectService } from '../services/remote-object.service';

import { EventListener } from '../events/event-listener.class';
import { EventService } from '../services/event.service';

@Directive()
export abstract class TestComponent extends EventListener implements AfterViewInit {


protected case: string;


constructor(eventService: EventService,
			private route: ActivatedRoute,
			@Inject("ContentService") protected contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") protected modelService: RemoteObjectService<Model, ModelJSON>) {
	super(eventService);
}


ngAfterViewInit() {
	this.route.paramMap.subscribe(params => {
												this.case = params.get('case_');
												this.test(this.case);
	});
}


protected abstract test(case_: string): void;


protected load(contentURI: string, model: string) {

	const contentAndModelURI = ContentComponent.contentURIFrom(contentURI, model);
	const modelURI = ModelComponent.modelURIFrom(model);

	this.modelService.get(modelURI, Model).subscribe((m: Model) => {
		this.contentService.get(contentAndModelURI, Content).subscribe( (c: Content) => this.loaded(m, c));
	});

}


protected loadModel(url: string): void {

	const modelURI = ModelComponent.modelURIFrom(url);
	this.modelService.get(modelURI, Model).subscribe((m: Model) => this.loadedModel(m));

}


protected createDocument(document: string): CellDocument {

	const DOCUMENT = Object.create(CellDocument.prototype); // to simulate a static call

	return DOCUMENT.fromJSON(document);

}


protected createContent(content: ContentJSON | string, model?: Model): Content {

	const CONTENT = Object.create(Content.prototype); // to simulate static call 

	let c = CONTENT.fromJSON(content);
	c.associateFromRoot(model);

	return c;
}


protected createModel(model: string): Model {

	const MODEL = Object.create(Model.prototype); // to simulate static call 

	let m = MODEL.fromJSON(model);
	m.normaliseReferences();

	return m;

}


protected createSnippets(snippets: string[]): CellDocument[] {

	const CELLDOCUMENT = Object.create(CellDocument.prototype); // to simulate static call 

	return snippets.map(s => CELLDOCUMENT.fromJSON(s));

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
