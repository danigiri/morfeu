// ATTRIBUTE - DATA - INFO - TEST . COMPONENT . TS

import { Component, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Cell } from "../../cell.class";
import { CellModel } from "../../cell-model.class";
import { Content, ContentJSON } from '../../content.class';
import { Model, ModelJSON } from '../../model.class';

import { RemoteObjectService } from '../../services/remote-object.service';

import { TestComponent } from '../../test/test-component.class';

import { EventService } from '../../services/event.service';


@Component({
	selector: 'attribute-data-info-test',
	template: `<attribute-data-info *ngIf="cell"
				[parentCell]="cell"
				[cellModel]="attributeCellModel"
				[isFromCell]="true" 
				[isFromModel]="false"
				[index]="0"
				></attribute-data-info>`
})


export class AttributeDataInfoTestComponent extends TestComponent {

private readonly content = 'target/test-classes/test-resources/documents/types.xml';
private readonly model = 'target/test-classes/test-resources/models/test-model.xsd';

private cellPath: string;
cell: Cell;
attributeCellModel: CellModel;


constructor(eventService: EventService,
			route: ActivatedRoute,
			@Inject("ContentService") contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") modelService: RemoteObjectService<Model, ModelJSON>) {
	super(eventService, route, contentService, modelService);
}


protected test(case_: string) {
	switch (case_) {
		case 'boolean-true': this.booleanTrue(); break;
		case 'boolean-false': this.booleanFalse(); break;
		default: this.booleanTrue();
	}
}


private booleanTrue() {
	this.cellPath = '/test(0)/row(0)/col(0)/types(0)';
	this.load(this.content, this.model);
}


private booleanFalse() {
	this.cellPath = '/test(0)/row(0)/col(0)/types(1)';
	this.load(this.content, this.model);
}


protected loaded(model: Model, content: Content): void {

	const cell = content.findCellWithURI(content.getURI()+this.cellPath);
	cell.associateWith(model, cell.cellModelURI);

	this.attributeCellModel = cell.cellModel.getAttribute('bool');

	this.cell = cell; // now update test UI

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
