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
			[src]="getPresentation() | safe: 'resourceUrl'"
		></iframe>
		
	`,
})
// sets the ui to be too slow as the iframe blocks rendering
//	changeDetection: ChangeDetectionStrategy.OnPush

export class PresentationComponent implements OnDestroy {

//private interval: NodeJS.Timer;

// if showing a cell with values or we are showing a cellmodel
@Input() cell?: Cell;
@Input() cellModel?: CellModel;

constructor(private ref: ChangeDetectorRef) {

//	ref.detach();
//	this.interval = setInterval(() => { this.ref.detectChanges(); }, 1000);
	
}


ngOnDestroy() {
//	clearInterval(this.interval);
}

/** do we present the cell with the default (IMG) presentation? */
private cellPresentationIsIMG(): boolean {

	let cellModel = this.cell === undefined ? this.cellModel : this.cell.cellModel;
	
	return cellModel.getPresentationType()===CellModel.DEFAULT_PRESENTATION_TYPE;

}


private getPresentation(): string {
	return this.cell===undefined ? this.cellModel.getPresentation() : this.cell.getPresentation();
}


}