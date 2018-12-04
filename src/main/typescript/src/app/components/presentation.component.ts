// PRESENTATION . COMPONENT . TS (NOT USED AT THE MOMENT)

import { Component, Input } from "@angular/core";

import { Cell } from "../cell.class";
import { CellModel } from "../cell-model.class";

@Component({
	moduleId: module.id,
	selector: "presentation",
	template: `
		<!-- TODO: add inner html type? -->
		<iframe *ngIf="!cellPresentationIsIMG()" 
					class="cell cell-html"
					[src]="getCellPresentation() | safe: 'resourceUrl'"
					[class.cell-active]="active"
					[class.cell-selected]="selected"
		></iframe>
	`,
	styles:[`

	`]
})

export class PresentationComponent {

// are we draggable or are we just showing the presentation? MOVE THE DRAGGABLE LOGIC TO THE UPPER COMPONENT
@Input() draggable: boolean = false;

// if showing a cell with values within a cell structure (then we have the cell and 
@Input() cell?: Cell;
@Input() level?: number;

@Input() cellModel?: CellModel;

@Input() active = false;
@Input() selected = false;


/** do we present the cell with the default (IMG) presentation? */
private cellPresentationIsIMG(): boolean {
	return this.cell.cellModel.getPresentationType()===CellModel.DEFAULT_PRESENTATION_TYPE;
}


private getCellPresentation() {
	return this.cell.getPresentation();
}


}