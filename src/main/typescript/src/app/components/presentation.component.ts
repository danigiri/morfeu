// PRESENTATION . COMPONENT . TS (NOT USED AT THE MOMENT)

import { Component, Input } from "@angular/core";

import { Cell } from "../cell.class";
import { CellModel } from "../cell-model.class";

@Component({
	moduleId: module.id,
	selector: "presentation",
	template: `
		<ng-container *ngIf="cell && draggable">
			<img *ngIf="cellPresentationIsIMG()"
				id="{{cell.URI}}"
				class="cell cell-img cell-level-{{level}}"
				src="{{getCellPresentation()}}"
				[class.cell-active]="active"
				[class.cell-selected]="selected"
				(mouseenter)="focusOn(cell)"
				(mouseleave)="focusOff(cell)"
				(dblclick)="doubleClick()"
				dnd-draggable
				[dragEnabled]="dragEnabled"
				(onDragEnd)="dragEnd(cell)"
				[dragData]="cellDragData()"
			/>
			<!-- TODO: add innerhtml type? -->
			<iframe *ngIf="!cellPresentationIsIMG()" 
				id="{{cell.URI}}"
				[src]="getCellPresentation() | safe: 'resourceUrl'"
				[class.cell-active]="active"
				[class.cell-selected]="selected"
				(mouseenter)="focusOn(cell)"
				(mouseleave)="focusOff(cell)"
				(dblclick)="doubleClick()"
				dnd-draggable
				[dragEnabled]="dragEnabled"
				(onDragEnd)="dragEnd(cell)"
				[dragData]="cellDragData()"
			></iframe>

		</ng-container>
		<ng-container *ngIf="cellModel && !draggable">
		</ng-container>
	`,
	styles:[`
		.cell-level-0 {}
		.cell-level-1 {}
		.cell-level-2 {}
		.cell-level-3 {}
		.cell-level-4 {}
		.cell-level-5 {}
		.cell-level-6 {}
		.cell-level-7 {}
		.cell-level-8 {}
		.cell-level-9 {}
		.cell-level-10 {}
		.cell-level-11 {}
		.cell-level-12 {}
		.cell-level-13 {}
		.cell-level-14 {}
		.cell-level-15 {}
		.cell-level-16 {}
		.cell-level-17 {}
		.cell-level-18 {}
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