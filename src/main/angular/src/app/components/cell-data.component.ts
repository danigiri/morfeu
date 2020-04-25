// CELL-DATA . COMPONENT . TS

import { Component, Input, OnInit } from "@angular/core";
import { filter } from 'rxjs/operators';

import { Cell } from '../cell.class';
import { CellModel } from '../cell-model.class';

import { CellActivatedEvent } from '../events/cell-activated.event';
import { CellDeactivatedEvent } from '../events/cell-deactivated.event';
import { CellModelActivatedEvent } from '../events/cell-model-activated.event';
import { CellModelDeactivatedEvent } from '../events/cell-model-deactivated.event';
import { EventListener } from '../events/event-listener.class';
import { EventService } from '../services/event.service';

@Component({
	selector: "cell-data",
	template: `
		<div  *ngIf="cellModel" 
			class="card mt-2 cell-data cell-data-info"
			[class.cell-data-source-model]="cell===undefined"
			[class.cell-data-source-cell]="cell!==undefined"
		>
			<cell-header [uri]="uri" [cellModel]="cellModel"></cell-header>
			<img *ngIf="showCellPresentation() && cellPresentationIsIMG()"
				class="card-img-bottom"
				src="{{getCellPresentation()}}"
				alt="Image representation of the cell"
			/>
			<presentation *ngIf="showCellPresentation() && !cellPresentationIsIMG()" 
				[cell]="cell" [cellModel]="cellModel"
			></presentation>
			<!-- if we have a value field we should show it (readonly!) -->
			<div class="card-body">
				<form *ngIf="cell!=undefined && cell.value!=undefined && showValue()">
					<textarea readonly
						class="cell-data-value card-text"
						rows="3"
						name="{{cellModel.name}}.value"
						attr.aria-label="{{cellModel.name}}.value"
						attr.aria-describedby="{{cellModel.desc}} value"
						[(ngModel)]="cell.value"
					></textarea>
				</form>
			</div>
			<!-- even if we are showing a cell or a cell model, we use the model to iterate -->
			<ul class="list-group list-group-flush" *ngIf="cellModel.attributes">
				<attribute-data-info *ngFor="let a of cellModel.attributes; let i = index"
					[parentCell]="cell"
					[cellModel]="a"
					[isFromCell]="cell!=undefined" 
					[isFromModel]="cell==undefined"
					[index]="i"
				></attribute-data-info>
				<li *ngIf="cell!=undefined && remainingAttributes()==1" class="list-group-item"><small><em>[1 attribute not used]</em></small></li>
				<li *ngIf="cell!=undefined && remainingAttributes()>1" class="list-group-item"><small><em>[{{remainingAttributes()}} attributes not used]</em></small></li>
			</ul>
		</div>
				`,
	styles: [`
				.cell-data {}
				.cell-data-info {}
				.cell-data-value {}
				.cell-data-value-field {}
				.cell-data-uri {}
				.cell-data-source-model {}
				.cell-data-source-cell {}
	`]
})

export class CellDataComponent extends EventListener implements OnInit {

@Input() uri: string;
@Input() cell: Cell;
@Input() cellModel: CellModel;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	this.register(this.events.service.of<CellActivatedEvent>(CellActivatedEvent)
			.subscribe( activated => this.showCellInformation(activated.cell))
	);
	this.register(this.events.service.of<CellDeactivatedEvent>(CellDeactivatedEvent)
			.subscribe(() => this.hideCellInformation()));
	this.register(this.events.service.of<CellModelActivatedEvent>(CellModelActivatedEvent)
			.pipe(filter(activated => activated.cellModel!==undefined))
			.subscribe( activated => this.showCellModelInformation(activated.cellModel))
	);
	this.register(this.events.service.of<CellModelDeactivatedEvent>(CellModelDeactivatedEvent)
			.subscribe(() => this.hideCellInformation())
	);

}


private showCellInformation(cell: Cell) {

	this.uri = cell.URI;
	this.cell = cell;
	this.cellModel = cell.cellModel;

}


private showCellModelInformation(cellModel: CellModel) {

	this.uri = cellModel.URI;
	this.cell = undefined;
	this.cellModel = cellModel;

}


private hideCellInformation() {

	this.cellModel = undefined;
	this.cell = undefined; // not strictly needed, but for completeness and to avoid any future side-effects

}


private remainingAttributes() {
	return this.cellModel.attributes ? this.cellModel.attributes.length - this.cell.attributes.length : 0;
}


// TODO: this is duplicated code, should refactor
private cellPresentationIsIMG(): boolean {

	let cellModel = this.cell === undefined ? this.cellModel : this.cell.cellModel;

	return cellModel.getCellPresentationType()===CellModel.DEFAULT_PRESENTATION_TYPE;

}


private showCellPresentation() {
	return this.cellModel.presentation.startsWith("CELL");
}


private getCellPresentation(): string {
	return this.cell===undefined ? this.cellModel.getCellPresentation() : this.cell.getCellPresentation();
}


private showValue() {
	return this.cellModel.presentation.includes("TEXT"); // if we need to show the text area or not
}


}

/*
 *	  Copyright 2018 Daniel Giribet
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
