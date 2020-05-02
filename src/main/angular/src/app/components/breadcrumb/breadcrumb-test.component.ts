

import { Component, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Cell } from '../../cell.class';
import { Content, ContentJSON } from '../../content.class';
import { Model, ModelJSON } from '../../model.class';

import { RemoteObjectService } from '../../services/remote-object.service';

import { _readonlyDocument } from '../../test/test.data';
import { TestComponent } from '../../test/test-component.class';

import { CellActivatedEvent } from '../../events/cell-activated.event';
import { CellDeactivatedEvent } from '../../events/cell-deactivated.event';
import { ContentRefreshedEvent, ContentRefreshed } from '../../events/content-refreshed.event';
import { EventService } from '../../services/event.service';

@Component({
	selector: 'breadcrumb-test',
	template: '<breadcrumb></breadcrumb>'
})

export class BreadcrumbTestComponent extends TestComponent {

private cell: Cell;


constructor(eventService: EventService,
			route: ActivatedRoute,
			@Inject("ContentService") contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") modelService: RemoteObjectService<Model, ModelJSON>) {
	super(eventService, route, contentService, modelService);
}


protected test(case_: string): void {
	switch (case_) {
		case 'display':
			this.display();
		break;
		case 'display-and-hide':
			this.displayAndDisappear();
		break;
		default:
			this.display();
	}
}


protected loaded(model: Model, content: Content): void {

	const cellPath  = '/test(0)/row(0)/col(1)/row(0)/col(1)/data2(1)';

	this.cell = content.findCellWithURI(content.getURI()+cellPath);
	this.cell.associateWith(model, this.cell.cellModelURI);
	this.events.service.publish(new ContentRefreshedEvent(content));
	this.events.service.publish(new CellActivatedEvent(this.cell));

}


private display() {

	const docLocation = 'target/test-classes/test-resources/documents/document1.xml';
	const modelLocation =  'target/test-classes/test-resources/models/test-model.xsd';
	this.load(docLocation, modelLocation);

}


private displayAndDisappear() {

	this.register(this.events.service.of<CellActivatedEvent>(CellActivatedEvent)
			.subscribe(() => setTimeout(() => this.events.service.publish(new CellDeactivatedEvent(this.cell)), 600))
	);
	this.display();

}


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
