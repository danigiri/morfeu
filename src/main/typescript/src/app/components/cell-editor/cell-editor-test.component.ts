// CELL - EDITOR - TEST . COMPONENT . TS

import {Component, Inject, AfterViewInit, OnInit} from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

import {Content, ContentJSON } from '../../content.class';
import {Model, ModelJSON} from '../../model.class';

import { RemoteObjectService } from '../../services/remote-object.service';

import {CellEditEvent} from '../../events/cell-edit.event';
import {EventListener} from '../../events/event-listener.class';
import {EventService} from '../../services/event.service';


@Component({
	selector: 'cell-editor-test',
	template: `<cell-editor></cell-editor>`
})

export class CellEditorTestComponent extends EventListener implements AfterViewInit {


constructor(eventService: EventService,
			@Inject("ContentService") private contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") private modelService: RemoteObjectService<Model, ModelJSON>) {
	super(eventService);
}


ngAfterViewInit() {

	const content = 'target/test-classes/test-resources/documents/document1.xml';
	const model = 'target/test-classes/test-resources/models/test-model.xsd';
	this.loadContent(content, model);

}


private loadContent(contentURI: string, model: string) {

	const contentAndModelURI = '/morfeu/dyn/content/'+contentURI+'?model='+model;
	const modelURI = '/morfeu/dyn/models/'+model ;

		this.modelService.get(modelURI, Model).subscribe(m => {
		this.contentService.get(contentAndModelURI, Content).subscribe( (content: Content) => {
			let cell = content.findCellWithURI(contentURI+'/test(0)/row(0)/col(1)/row(0)/col(1)/data2(1)');
			cell.associateWith(m, cell.cellModelURI);
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
