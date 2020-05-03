// CONTENT - TEST . COMPONENT . TS

import { Component, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { CellDocument } from '../../cell-document.class';
import { Content, ContentJSON } from '../../content.class';
import { Model, ModelJSON } from '../../model.class';

import { _readonlyDocument } from '../../test/test.data';
import { TestComponent } from '../../test/test-component.class';

import { RemoteObjectService } from '../../services/remote-object.service';

import { ContentRequestEvent } from '../../events/content-request.event';
import { EventService } from '../../services/event.service';

@Component({
	selector: 'content-test',
	template: `
		<div class="container-fluid">
			<content></content>
			<key-capture></key-capture>
			<cell-editor></cell-editor>
		</div>
	`
})

export class ContentTestComponent extends TestComponent {

document: CellDocument;


constructor(eventService: EventService,
			route: ActivatedRoute,
			@Inject("ContentService") contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") modelService: RemoteObjectService<Model, ModelJSON>) {
	super(eventService, route, contentService, modelService);
}


protected test(case_: string) {

	switch (case_) {
		//case 'post' : this.loadPOST(); break;
		default: this.readonly();
	}

}


private readonly() {

	const DOCUMENT = Object.create(CellDocument.prototype); // to simulate a static call
	this.document = DOCUMENT.fromJSON(_readonlyDocument);
	this.loadModel(this.document.modelURI);

}


protected loadedModel(model: Model): void {
	this.events.service.publish(new ContentRequestEvent(this.document, model))
}


}