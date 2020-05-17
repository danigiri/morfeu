// CONTENT - TEST . COMPONENT . TS

import { Component, Inject } from '@angular/core';

import { CellDocument } from '../../cell-document.class';
import { Model } from '../../model.class';

import { _readonlyDocument, _document3Document } from '../../test/test.data';
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
		case 'readonly' : this.readonly(); break;
		case 'dnd-1' : this.dragAndDrop1(); break;
		default: this.readonly();
	}

}


private readonly() {

	console.debug('Readonly test')
	this.document = this.createDocument(_readonlyDocument);
	this.loadModel(this.document.modelURI);

}


private dragAndDrop1() {

	console.debug('Drag and Drop 1 test')
	this.document = this.createDocument(_document3Document);
	this.loadModel(this.document.modelURI);

}


protected loadedModel(model: Model): void {
	this.events.service.publish(new ContentRequestEvent(this.document, model))
}


}