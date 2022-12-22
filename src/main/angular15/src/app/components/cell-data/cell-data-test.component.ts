// CELL - DATA - TEST . COMPONENT . TS
import { Component } from '@angular/core';
import { TestComponent } from '../../test/test-component.class';

import { Cell } from '../../cell.class';
import { CellModel } from '../../cell-model.class';

import { _model, _content3} from '../../test/test.data';

@Component({
	selector: 'cell-data-test',
	template: '<cell-data [uri]="uri" [cellModel]="cellModel" [cell]="cell"></cell-data>'
})

export class CellDataTestComponent extends TestComponent {

readonly #cellPrefix = 'target/test-classes/test-resources/documents/document3.xml';

uri: string;
cell: Cell;
cellModel: CellModel;


protected test(case_: string): void {
	switch (case_) {
		case 'col' : this.col(); break;
		default: this.col();
	}
}


private col() {

	const model = this.createModel(_model);
	const content = this.createContent(_content3, model);

	Promise.resolve(null).then(() => {
										this.cell = content.findCellWithURI(this.#cellPrefix+'/test(0)/row(0)/col(0)');
										this.cellModel = this.cell.cellModel;
										this.uri = this.cell.URI;
	});
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