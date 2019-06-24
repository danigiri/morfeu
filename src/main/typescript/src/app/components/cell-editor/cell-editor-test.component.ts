// CELL - EDITOR - TEST . COMPONENT . TS

import {Component, Inject, AfterViewInit, OnInit} from '@angular/core';

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

export class CellEditorTestComponent extends EventListener implements AfterViewInit { //}implements OnInit {


constructor(eventService: EventService,
			@Inject("ContentService") private contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") private modelService: RemoteObjectService<Model, ModelJSON>) {
	super(eventService);
}


ngAfterViewInit() {
//ngOnInit() {

	const model = 'target/test-classes/test-resources/models/test-model.xsd';
	const contentURI = 'target/test-classes/test-resources/documents/document1.xml';
	const contentAndModelURI = '/morfeu/dyn/content/'+contentURI+'?model='+model;

	const modelURI = '/morfeu/dyn/models/target/test-classes/test-resources/models/test-model.xsd';
	const data3CellmodelURI = 'target/test-classes/test-resources/models/test-model.xsd/test/row/col/data3';
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
