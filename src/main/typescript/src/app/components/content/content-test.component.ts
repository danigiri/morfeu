// CONTENT - TEST . COMPONENT . TS

import { Component, Inject } from '@angular/core';

import { CellDocument } from '../../cell-document.class';
import { Model } from '../../model.class';

import { _readonlyDocument, _document1Document, _document3Document, _document5Document } from '../../test/test.data';
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
		case 'readonly' : this.readonly(); break;
		case 'dnd-1' : this.dragAndDrop1(); break;
		case 'dnd-3' : this.dragAndDrop3(); break;
		case 'dnd-5' : this.dragAndDrop5(); break;
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


 private dragAndDrop3() {

 	console.debug('Drag and Drop 3 test')
	this.document = this.createDocument(_document3Document);
	this.loadModel(this.document.modelURI);

 }


 private dragAndDrop5() {

	console.debug('Drag and Drop 35test')
	this.document = this.createDocument(_document5Document);
	this.loadModel(this.document.modelURI);
	//this.loadModel(this.document.modelURI);

}


protected override loadedModel(model: Model): void {
	this.events.service.publish(new ContentRequestEvent(this.document, model))
}


}