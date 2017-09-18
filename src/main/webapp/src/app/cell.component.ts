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


import { Component, Input, OnInit } from '@angular/core';

import { Cell } from './cell.class';
import { Widget } from './widget.class';

import { CellActivatedEvent } from './events/cell-activated.event';
import { CellDeactivatedEvent } from './events/cell-deactivated.event';
import { EventService } from './events/event.service';


@Component({
	moduleId: module.id,
	selector: 'cell',
	template: `
			<ng-container *ngIf="cell.cellModel.presentation=='CELL'; else well">
	            <!-- TODO: check the model and the content as well (counts, etc.) -->
                <drop-area *ngIf="parent" [parent]="parent"></drop-area>
				<img src="assets/images/cell.svg" 
					 class="cell img-fluid cell-img"
					 [class.cell-active]="active" 
					 alt="{{cell.name}}" 
					 (mousedown)="clickDown(cell)" 
					 (mouseup)="clickUp(cell)"
                     (mouseenter)="clickDown(cell)" 
                     (mouseleave)="clickUp(cell)"
					 
					 dnd-draggable [dragEnabled]="dragEnabled"
					 />
			</ng-container>
			<ng-template #well>
				<div class="cell-level-{{level}} {{cellClass()}}">
	                    <drop-area *ngIf="cell.children.length==0" [parent]="cell"></drop-area>
						<cell *ngFor="let c of cell.children" 
						[cell]="c" 
						[parent]="cell" 
						[level]="level+1"></cell>
			</div>
			</ng-template>
	`,
	styles:[`
			.cell {}
			.well {}
			.row-well {}
			.col-well {}
			.cell-img {
				width: 100%;
				height: auto;
			}
			.show-grid	{
				padding-top: 10px;
				padding-bottom: 10px;
				background-color: #ddd;
				background-color: rgba(86, 62, 128, .15);
				border: 2px solid #ccc;
			    border-radius: 5px;
			    border: 2px solid rgba(86, 62, 128, .2)
			}
			.cell-active {
			    border: 3px solid #f00;
			    border-radius: 5px;
			 }
			 .cell-dragged {
				 opacity: .2;
			 }
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
`],
	providers:[
	]
})
// `

export class CellComponent extends Widget implements OnInit {

@Input() parent?: Cell;
@Input() cell: Cell;
@Input() level: number;

active: boolean = false;
dragEnabled:boolean = false;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {
	console.log("[UI] CellComponent::ngOnInit()");
}	 
	   
	
clickDown(cell:Cell) {
	
	console.log("[UI] CellComponent::clickDown("+cell.URI+")");
	this.active = true;
	// TODO: OPTIMISATION we could precalculate the event receptor and do a O(k) if needed
	this.events.service.publish(new CellActivatedEvent(cell));
	this.dragEnabled = true;

}

clickUp(cell:Cell) {
	
	console.log("[UI] CellComponent::clickUp("+cell.URI+")");
	this.active = false;   
	this.events.service.publish(new CellDeactivatedEvent(cell));
	this.dragEnabled = false;

}
	

//TODO: depending on the level go from -md- to -xs- col styling
//TODO: this function gets called and we should have an attribute or input to optimise the client stuff
cellClass() {
	if (this.cell.cellModel.presentation=="WELL") {
		return "well container-fluid";
	} else if (this.cell.cellModel.presentation=="ROW-WELL") {
		return "row-well row show-grid";
   } else if (this.cell.cellModel.presentation=="COL-WELL") {
	   return "col-well show-grid col-sm-"+this.cell.columnFieldValue();
   } else {
		return "";
	}
}


cellIsDroppable() {
	return this.cell.cellModel.presentation=="COL-WELL";
}


}