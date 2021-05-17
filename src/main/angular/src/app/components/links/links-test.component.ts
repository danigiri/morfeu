// LINKS - TEST . COMPONENT . TS
import { Component } from '@angular/core';

import { CellDocument } from '../../cell-document.class';
import { Model } from '../../model.class';

import { Cell } from 'app/cell.class';
import { CellLocator } from 'app/utils/cell-locator.class';

import { _model, _types, _typesDocument, _typesPrefix } from 'app/test/test.data';
import { TestComponent } from 'app/test/test-component.class';

import { ContentRequestEvent } from 'app/events/content-request.event';

@Component({
	selector: 'arrow-test',
	template: `
		<div class="container">
			<div class="row">
				<div class="col">
				<cell *ngIf="cell" [cell]="cell" [level]="3" [position]="0"></cell>
				</div>
				<div class="col">
				</div>
				<div class="col">
					<div>&nbsp;</div>
					<div>&nbsp;</div>
					<cell *ngIf="cell" [cell]="sourceCell" [level]="3" [position]="0"></cell>
				</div>
			</div>
		</div>
		<!--div style="position:absolute; left:0; top:0; z-index:-2; width:100%; height:100%">
		<svg:svg xmlns:svg="http://www.w3.org/2000/svg"
			viewBox="0 0 200 200"
			
			stroke="#aaa"
			fill="#aaa"
			strokeWidth="3"
			id="aaa"
			>
			<svg:circle id="circle" cx="10" cy="10" r="100" ></svg:circle>
		</svg:svg>
		</div-->
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


protected loadedModel(model: Model): void {
	this.events.service.publish(new ContentRequestEvent(this.document, model));
}


}

/*
 *	  Copyright 2021 Daniel Giribet
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