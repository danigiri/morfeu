// PRESENTATION . COMPONENT . TS (NOT USED AT THE MOMENT)

import { Component, Input } from "@angular/core";

import { Cell } from "../cell.class";
import { CellModel } from "../cell-model.class";

@Component({
	moduleId: module.id,
	selector: "presentation",
	template: `
		<!-- TODO: add inner html type? -->
		<iframe
			class="cell cell-html"
			[src]="getPresentation() | safe: 'resourceUrl'"
		></iframe>
	`,
	styles:[`

	`]
})

export class PresentationComponent {

// if showing a cell with values or we are showing a cellmodel
@Input() cell?: Cell;
@Input() cellModel?: CellModel;


/** do we present the cell with the default (IMG) presentation? */
private cellPresentationIsIMG(): boolean {

	let cellModel = this.cell === undefined ? this.cellModel : this.cell.cellModel;
	
	return cellModel.getPresentationType()===CellModel.DEFAULT_PRESENTATION_TYPE;

}


private getPresentation(): string {
	return this.cell===undefined ? this.cellModel.getPresentation() : this.cell.getPresentation();
}


}