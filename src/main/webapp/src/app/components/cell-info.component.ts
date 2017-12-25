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


import { Component, OnInit } from "@angular/core";

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
	selector: 'cell-info',
	template: `
		<div id="cell-info" class="card mt-2" *ngIf="cellModel">
	            <h4 class="card-title card-header">
	                {{cellModel.name}}
	                <ng-container *ngIf="cellModel.minOccurs!=0 || (cellModel.maxOccurs && cellModel.maxOccurs!=-1)">
	                [
	                    <ng-container *ngIf="cellModel.minOccurs!=01">{{cellModel.minOccurs}}</ng-container>..<ng-container *ngIf="cellModel.maxOccurs && cellModel.maxOccurs!=-1">{{cellModel.maxOccurs}}
	                    </ng-container>
	                ]
	                </ng-container>
	            </h4>
	        <div class="card-body">
	            <p class="card-subtitle text-muted">{{cellModel.desc}}<p>
	            
		    </div>
		    <img *ngIf="showPresentation()" class="card-img-bottom" src="{{this.cellModel.getPresentation()}}" alt="Card image cap">
	        <ul class="list-group list-group-flush" *ngIf="cellModel.attributes">
	            <attribute-info *ngFor="let a of cellModel.attributes" [cellModel]="a">
	            </attribute-info>
	        </ul>
	    </div>
		
			   `,
	styles:[`
			#cell-info {}
	`]
})

export class CellInfoComponent extends Widget implements OnInit {

cell: Cell;
cellModel: CellModel;
	
constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	this.subscribe(this.events.service.of( CellActivatedEvent )
	    .subscribe( activated => this.showCellModelInformation(activated.cell.cellModel)
	));
	this.subscribe(this.events.service.of( CellDeactivatedEvent )
	    .subscribe( deactivated => this.hideCellModelInformation()
	));
	this.subscribe(this.events.service.of( CellModelActivatedEvent )
		.subscribe( activated => this.showCellModelInformation(activated.cellModel)
	));
	this.subscribe(this.events.service.of( CellModelDeactivatedEvent )
		.subscribe( activated => this.hideCellModelInformation()
	));

}

showCellModelInformation(cellModel: CellModel) {

    this.cellModel = cellModel;

}

hideCellModelInformation() {
	this.cellModel = undefined;
}

showPresentation() {
    return this.cellModel.presentation=="CELL";
}

}