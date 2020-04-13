// ATTRIBUTE - DATA - EDITOR - TEST . COMPONENT . TS


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
	selector: 'attribute-data-edutir-test',
	template: `
		<attribute-data-editor *ngIf="cell"
			[parentCell]="cell"
			[cellModel]="attributeCellModel"
			[index]="0"
		></attribute-data-editor>
	`
})

export class AttributeDataEditorTestComponent extends TestComponent {

private readonly model = 'target/test-classes/test-resources/models/test-model.xsd';

private cellPath: string;
private attributeName: string;
cell: Cell;
attributeCellModel: CellModel;


constructor(eventService: EventService,
			route: ActivatedRoute,
			@Inject("ContentService") contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") modelService: RemoteObjectService<Model, ModelJSON>) {
	super(eventService, route, contentService, modelService);
}


protected test(case_: string): void {
	switch (case_) {
		case 'boolean-true' : this.boolean(); break;
		case 'color-validation' : this.colorValidation(); break;
		default: this.boolean();
	}
}


private boolean() {

	this.cellPath = '/test(0)/row(0)/col(0)/types(0)';
	this.attributeName = 'bool';
	this.load('target/test-classes/test-resources/documents/types.xml', this.model);

}


colorValidation() {

	this.cellPath = '/test(0)/row(0)/col(0)/data3(0)';	// this has a color attribute
	this.attributeName = 'color';
	this.load('target/test-classes/test-resources/documents/document5.xml', this.model);

}


protected loaded(model: Model, content: Content): void {

	const cell = content.findCellWithURI(content.getURI()+this.cellPath);
	cell.associateWith(model, cell.cellModelURI);

	this.attributeCellModel = cell.cellModel.getAttribute(this.attributeName);
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