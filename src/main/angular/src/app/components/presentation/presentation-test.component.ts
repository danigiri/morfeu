// PRESENTATION - TEST . COMPONENT . TS

import {AfterViewInit, Component, Inject} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import { Configuration } from '../../config/configuration.class';

import {Cell} from '../../cell.class';
import {CellModel} from '../../cell-model.class';
import {Model, ModelJSON} from '../../model.class';

import {RemoteObjectService} from '../../services/remote-object.service';

@Component({
	selector: 'presentation-test',
	template: `<presentation *ngIf="model" [cellModel]="model"></presentation>
				<presentation *ngIf="cell" [cell]="cell"></presentation>`
})

export class PresentationTestComponent implements AfterViewInit {

private readonly modelURI = Configuration.BACKEND_PREF+'/dyn/models/target/test-classes/test-resources/models/test-model.xsd';
model: CellModel;
cell: Cell;


constructor(@Inject("ModelService") private modelService: RemoteObjectService<Model, ModelJSON>,
			private route: ActivatedRoute) {}


ngAfterViewInit() {
	this.route.paramMap.subscribe(params => this.load(params.get('case_')));
}


private load(case_: string) {
	switch (case_) {
		case 'post' : this.loadPOST(); break;
		default: this.loadModel();
	}
}


private loadPOST() {
	this.modelService.get(this.modelURI, Model).subscribe(m => {

		console.debug('Creating test cell...');
		const cellModelURI = 'target/test-classes/test-resources/models/test-model.xsd/test/row/col/data5';
		const cellJSON: string = `
			{
				"schema": 0,
				"URI": "/testPostCell(0)",
				"name": "testPostCell",
				"desc": "test post cell",
				"cellModelURI": "`+cellModelURI+`",
				"isSimple": true,
				"attributes": [
									{
										"schema": 0,
										"URI": "/testPostCell(0)@text",
										"name": "text",
										"desc": "test post cell text",
										"value": "testvalue",
										"cellModelURI": "`+cellModelURI+`@text",
										"isSimple": true
									},
																		{
										"schema": 0,
										"URI": "/testPostCell(0)@color",
										"name": "color",
										"desc": "test post cell color",
										"value": "01A1FB",
										"cellModelURI": "`+cellModelURI+`@color",
										"isSimple": true
									}
								]
			}
		`;

		const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
		const cell = CELL.fromJSON(cellJSON);
		cell.associateWith(m, cell.cellModelURI);
		this.cell = cell;

	});
}


private loadModel() {

	const data3CellmodelURI = 'target/test-classes/test-resources/models/test-model.xsd/test/row/col/data3';
	this.modelService.get(this.modelURI, Model).subscribe(m => this.model = m.findCellModel(data3CellmodelURI));

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
