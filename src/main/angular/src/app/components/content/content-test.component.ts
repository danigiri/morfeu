// CONTENT - TEST . COMPONENT . TS

import { AfterViewInit, Component, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { CellDocument } from '../../cell-document.class';
import { Model, ModelJSON } from '../../model.class';

import { ModelComponent } from '../model.component';
import { TestComponent } from '../../test/test-component.interface';

import { RemoteObjectService } from '../../services/remote-object.service';

import { ContentRequestEvent } from '../../events/content-request.event';
import { EventListener } from '../../events/event-listener.class';
import { EventService } from '../../services/event.service';
import { RemoteEventService } from '../../services/remote-event.service';

@Component({
	selector: 'content-test',
	template: '<content></content>'
})

export class ContentTestComponent extends EventListener implements AfterViewInit, TestComponent {


constructor(eventService: EventService, 
			remoteEventService: RemoteEventService,
			private route: ActivatedRoute,
			@Inject("ModelService") private modelService: RemoteObjectService<Model, ModelJSON>) {
   // super(eventService);
super(eventService, remoteEventService);
}

ngAfterViewInit() {

	this.route.paramMap.subscribe(params => this.load(params.get('case_')));
}


load(case_: string) {

	switch (case_) {
		//case 'post' : this.loadPOST(); break;
		default: this.readonly();
	}

}


private readonly() {

	const docContent = `{
							"name": "Readonly test doc"
							,"desc": "Readonly content"
							,"kind": "xml"
							,"modelURI": "target/test-classes/test-resources/models/test-model.xsd"
							,"contentURI": "target/test-classes/test-resources/documents/document1.xml"
	}`;
	const DOC = Object.create(CellDocument.prototype); // to simulate a static call
	const doc = DOC.fromJSON(docContent);
	const modelURI = ModelComponent.modelURIFrom(doc.modelURI);
	this.modelService.get(modelURI, Model).subscribe(model => 
		this.events.service.publish(new ContentRequestEvent(doc, model))
	);

}


}