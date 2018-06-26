 /*
 *	  Copyright 2017 Daniel Giribet
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


import { Component, Input, OnInit } from "@angular/core";

import { Cell } from "../cell.class";
import { CellModel } from "../cell-model.class";

import { Widget } from "../widget.class";

import { CellActivatedEvent } from "../events/cell-activated.event";
import { CellDeactivatedEvent } from "../events/cell-deactivated.event";
import { CellModelActivatedEvent } from "../events/cell-model-activated.event";
import { CellModelDeactivatedEvent } from "../events/cell-model-deactivated.event";
import { EventService } from "../events/event.service";


@Component({
	moduleId: module.id,
	selector: 'cell-data',
	template: `
		<div  *ngIf="cellModel" 
			class="card mt-2 cell-data cell-data-info">
			<cell-header [uri]="uri" [cellModel]="cellModel"></cell-header>
			<img *ngIf="showPresentation()" class="card-img-bottom" src="{{getPresentation()}}" alt="Image representation of the cell">
			<!-- if we have a value field we should show it (readonly!) -->
			<div class="card-body">
				 <form *ngIf="cell!=undefined && cell.value!=undefined && showValue()">
						<textarea readonly
							class="cell-data-value card-text" 
							rows="3"
							name="{{cellModel.name}}.value"
							attr.aria-label="{{cellModel.name}}.value" 
							attr.aria-describedby="{{cellModel.desc}} value" 
							[(ngModel)]="cell.value"></textarea>
				 </form>
			</div>
			<!-- even if we are showing a cell or a cell model, we use the model to iterate -->
			<ul class="list-group list-group-flush" *ngIf="cellModel.attributes">
				<attribute-data-info *ngFor="let a of cellModel.attributes" 
					[isFromCell]="cell!=undefined" 
					[parentCell]="cell" 
					[cellModel]="a"
					[isFromModel]="cell==undefined"
					></attribute-data-info>
				<li *ngIf="cell!=undefined && remainingAttributes()==1" class="list-group-item"><small><em>[1 attribute not used]</em></small></li>
				<li *ngIf="cell!=undefined && remainingAttributes()>1" class="list-group-item"><small><em>[{{remainingAttributes()}} attributes not used]</em></small></li>
			</ul>
		</div>
				`,
	styles:[`
			.cell-data {}
			.cell-data-info {}
			.cell-data-value {}
			.cell-data-value-field {}
			.cell-data-model-desc {}
			.cell-data-uri {}
			.cell-data-source {}
	`]
})

export class CellDataComponent extends Widget implements OnInit {

@Input() uri: string;
@Input() cell: Cell;
@Input() cellModel: CellModel;

constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	this.subscribe(this.events.service.of( CellActivatedEvent )
				.subscribe( activated => this.showCellInformation(activated.cell)
	));
	this.subscribe(this.events.service.of( CellDeactivatedEvent )
			.subscribe( deactivated => this.hideCellInformation()
	));
	this.subscribe(this.events.service.of( CellModelActivatedEvent )
			.filter( activated => activated.cellModel!=undefined)
			.subscribe( activated => this.showCellModelInformation(activated.cellModel)
	));
	this.subscribe(this.events.service.of( CellModelDeactivatedEvent )
			.subscribe( activated => this.hideCellInformation()
	));

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


private showPresentation() {
	return this.cellModel.presentation.startsWith("CELL");
}


private getPresentation(): string {
	return this.cell==undefined ? this.cellModel.getPresentation() : this.cell.getPresentation();
}


private showValue() {
	return this.cellModel.presentation.includes("TEXT"); // if we need to show the text area or not
}


private createValue() {
	
	console.log("[UI] Create new (empty|default) value for '%s'", this.uri);
	Promise.resolve(null).then(() => this.cell.createValue());
	
}


private removeValue() {
	
	console.log("[UI] Removing value for '%s'", this.uri);
	Promise.resolve(null).then(() => this.cell.removeValue());
	
}

}