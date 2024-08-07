// LINKS - TEST . COMPONENT . TS
import { Component } from '@angular/core';

import { CellDocument } from '../../cell-document.class';
import { Model } from '../../model.class';

import { Cell } from '../../cell.class';
import { CellLocator } from '../../utils/cell-locator.class';

import { _model, _types, _typesDocument, _typesPrefix } from '../../test/test.data';
import { TestComponent } from '../../test/test-component.class';

import { ContentRequestEvent } from '../../events/content-request.event';

@Component({
	selector: 'arrow-test',
	template: `
		<div class="container">
			<links [id]="'foo'"></links>
			<div class="row">
				<div class="col">
					<cell *ngIf="cell" [cell]="cell" [level]="3" [position]="0"></cell>
				</div>
				<div class="col">
				</div>
				<div class="col">
					<div *ngFor="let e of [].constructor(1)">&nbsp;</div>
					<cell *ngIf="cell" [cell]="sourceCell" [level]="3" [position]="0"></cell>
				</div>
			</div>
		</div>
		<content *ngIf="document"></content>
		<debug [show]="true" [trackMouse]="true"></debug>
		`,
})

export class LinksTestComponent extends TestComponent {

cell: Cell;
sourceCell: Cell;
document: CellDocument;


protected test(case_: string): void {
	switch (case_) {
		case 'arrow' : this.showArrow(); break;
		case 'load' : this.showLinksOnLoad(); break;
		default: this.showLinksOnLoad();
	}
}


private showArrow() {

	console.debug('Show arrow test');
	const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
	const MODEL: Model = Object.create(Model.prototype); // to simulate static call

	const model = MODEL.fromJSON(_model);

	let types = CELL.fromJSON(_types);				// should contain links
	types.associateWith(model, types.cellModelURI);

	Promise.resolve(null).then(() => {
		const stuffURI = _typesPrefix+'/test(0)/row(1)/col(0)/stuff(0)';
		this.cell = CellLocator.findCellWithURI(types, stuffURI);
		const typesURI = _typesPrefix+'/test(0)/row(2)/col(0)/types(0)';
		this.sourceCell = CellLocator.findCellWithURI(types, typesURI);
	});

}


private showLinksOnLoad() {

	console.debug('Show arrows on load test');
	this.document = this.createDocument(_typesDocument);
	this.loadModel(this.document.modelURI);

}


protected override loadedModel(model: Model): void {
	this.events.service.publish(new ContentRequestEvent(this.document, model));
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