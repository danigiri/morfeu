// CELL - EDITOR - TEST . COMPONENT . TS

import { Component, Inject, AfterViewInit } from '@angular/core';

import { Content } from '../../content.class';
import { Model } from '../../model.class';

import { TestComponent } from '../../test/test-component.class';

import { CellEditEvent } from '../../events/cell-edit.event';

@Component({
	selector: 'cell-editor-test',
	template: '<cell-editor></cell-editor>'
})

export class CellEditorTestComponent extends TestComponent {

// http://localhost:3000/test/cell-editor/document5

private readonly model = 'target/test-classes/test-resources/models/test-model.xsd';
private cellPath: string;


protected test(case_: string) {
	switch (case_) {
		case 'document5' : this.document5(); break;
		case 'categories-all' : this.categoriesAll(); break;
		case 'code' : this.code(); break;
		default: this.document1();
	}
}


private document1() {

	this.cellPath = '/test(0)/row(0)/col(1)/row(0)/col(1)/data2(1)';
	this.load('target/test-classes/test-resources/documents/document1.xml', this.model);

}


private document5() {

	this.cellPath = '/test(0)/row(0)/col(0)/data3(0)';
	this.load('target/test-classes/test-resources/documents/document5.xml', this.model);

}


private categoriesAll() {

	this.cellPath = '/test(0)/row(0)/col(0)/row(0)/col(0)/categ(0)';
	this.load('target/test-classes/test-resources/documents/categories.xml', this.model);

}


private code() {

	this.cellPath = '/test(0)/row(0)/col(0)/code(0)';
	this.load('target/test-classes/test-resources/documents/code.xml', this.model);

}


protected override loaded(model: Model, content: Content): void {

	const cell = content.findCellWithURI(content.getURI()+this.cellPath);
	cell.associateWith(model, cell.cellModelURI);
	this.events.service.publish(new CellEditEvent(cell));

}


}

/*
 *	  Copyright 2024 Daniel Giribet
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
