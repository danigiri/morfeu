// CODE - EDITOR - TEST . COMPONENT . TS

import { Component, Inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Cell } from '../../cell.class';
import { Content, ContentJSON } from '../../content.class';
import { Model, ModelJSON } from '../../model.class';

import { RemoteObjectService } from '../../services/remote-object.service';

import { TestComponent } from '../../test/test-component.class';

import { CellChangeEvent, CellChange } from '../../events/cell-change.event';
import { EventService } from '../../services/event.service';

@Component({
	selector: 'code-editor-test',
	template: `
		<code-editor *ngIf="cell" [cell]="cell"></code-editor>
		<textarea [(ngModel)]="output"></textarea>
	`
})

export class CodeEditorTestComponent extends TestComponent implements OnInit  {

private readonly model = 'target/test-classes/test-resources/models/test-model.xsd';
private cellPath: string;
cell: Cell;
output: string = '';


constructor(eventService: EventService,
			route: ActivatedRoute,
			@Inject("ContentService") contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") modelService: RemoteObjectService<Model, ModelJSON>) {
	super(eventService, route, contentService, modelService);
}


ngOnInit() {

		this.register(this.events.service.of<CellChangeEvent>(CellChangeEvent)
			.subscribe(e => this.output = this.output.concat(e.toString()+'\n'))
	);

}


protected test(case_: string) {
	switch (case_) {
		case 'code1' : this.code1(); break;
		default: this.code1();
	}
}


private code1() {

	this.cellPath = '/test(0)/row(0)/col(0)/code(0)';
	this.load('target/test-classes/test-resources/documents/code.xml', this.model);

}


protected override loaded(model: Model, content: Content): void {

	const cell = content.findCellWithURI(content.getURI()+this.cellPath);
	cell.associateWith(model, cell.cellModelURI);

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
