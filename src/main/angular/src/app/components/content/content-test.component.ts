// CONTENT - TEST . COMPONENT . TS

import { Component, Inject } from '@angular/core';

import { CellDocument } from '../../cell-document.class';
import { Model } from '../../model.class';

import { _readonlyDocument } from '../../test/test.data';
import { TestComponent } from '../../test/test-component.class';

import { ContentRequestEvent } from '../../events/content-request.event';

@Component({
	selector: 'content-test',
	template: `
		<div>
			<content></content>
			<key-capture></key-capture>
			<cell-editor></cell-editor>
		</div>
	`
})

export class ContentTestComponent extends TestComponent {

document: CellDocument;


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