// PRESENTATION . COMPONENT . TS (NOT USED AT THE MOMENT)

import { Component, Input, ChangeDetectorRef, OnDestroy, ChangeDetectionStrategy } from "@angular/core";

import { Cell } from "../cell.class";
import { CellModel } from "../cell-model.class";

@Component({
	moduleId: module.id,
	selector: "presentation",
	template: `
		<!-- TODO: add inner html type? -->
		<iframe
			class="cell cell-html"
			[src]="getPresentation() | safe: 'resourceUrl' "
		></iframe>
		
	`,
})
// sets the ui to be too slow as the iframe blocks rendering
//	changeDetection: ChangeDetectionStrategy.OnPush

export class PresentationComponent { //} implements OnDestroy {

//private interval: NodeJS.Timer;

// if showing a cell with values or we are showing a cellmodel
@Input() cell?: Cell;
@Input() cellModel?: CellModel;

constructor(private ref: ChangeDetectorRef) {

// TODO: DETACH FROM CHANGE DETECTION,  AFTER EDITING A FIELD, DO DETECT CHANGES 
//	ref.detach();
//	this.interval = setInterval(() => { this.ref.detectChanges(); }, 1000);

}

/*
ngOnDestroy() {
	clearInterval(this.interval);
}
*/

/** do we present the cell with the default (IMG) presentation? */
private cellPresentationIsIMG(): boolean {

	let cellModel = this.cell === undefined ? this.cellModel : this.cell.cellModel;
	
	return cellModel.getPresentationType()===CellModel.DEFAULT_PRESENTATION_TYPE;

}


private getPresentation(): string {
	return this.cell===undefined ? this.cellModel.getPresentation() : this.cell.getPresentation();
}


}

/*
 *    Copyright 2019 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
