// CELL - EDITOR - TEST . COMPONENT . TS

import {Component, Inject, AfterViewInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {Content, ContentJSON } from '../../content.class';
import {Model, ModelJSON} from '../../model.class';

import { ContentComponent } from '../content/content.component';
import { ModelComponent } from '../model.component';

import {RemoteObjectService} from '../../services/remote-object.service';

import { CellEditEvent } from '../../events/cell-edit.event';
import { EventListener } from '../../events/event-listener.class';
import { EventService } from '../../services/event.service';

@Component({
	selector: 'cell-editor-test',
	template: `<cell-editor></cell-editor>`
})

export class CellEditorTestComponent extends EventListener implements AfterViewInit {

private readonly model = 'target/test-classes/test-resources/models/test-model.xsd';


constructor(eventService: EventService,
			private route: ActivatedRoute, 
			@Inject("ContentService") private contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") private modelService: RemoteObjectService<Model, ModelJSON>) {
	super(eventService);
}


ngAfterViewInit() {
	this.route.paramMap.subscribe(params => this.load(params.get('case_')));
}


private load(case_: string) {
	switch (case_) {
		case 'document5' : this.document5(); break;
		case 'categories-all' : this.categoriesAll(); break;
		default: this.document1();
	}
}


private document1() {

	const content = 'target/test-classes/test-resources/documents/document1.xml';
	const cell = '/test(0)/row(0)/col(1)/row(0)/col(1)/data2(1)';
	this.loadContent(content, this.model, cell);

}


private document5() {

	const content = 'target/test-classes/test-resources/documents/document5.xml';
	const cell = '/test(0)/row(0)/col(0)/data3(0)';
	this.loadContent(content, this.model, cell);

}

private categoriesAll() {

	const content = 'target/test-classes/test-resources/documents/categories.xml';
	const cell = '/test(0)/row(0)/col(0)/row(0)/col(0)/categ(0)';
	this.loadContent(content, this.model, cell);

}


private loadContent(contentURI: string, model: string, cellPath: string) {

	const contentAndModelURI = ContentComponent.contenttURIFrom(contentURI, model);
	const modelURI = ModelComponent.modelURIFrom(model);

	this.modelService.get(modelURI, Model).subscribe(model => {
		this.contentService.get(contentAndModelURI, Content).subscribe( (content: Content) => {
			const cell = content.findCellWithURI(contentURI+cellPath);
			cell.associateWith(model, cell.cellModelURI);
			this.events.service.publish(new CellEditEvent(cell));
		});
	});

}


}

/*
 *	  Copyright 2019 Daniel Giribet
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
