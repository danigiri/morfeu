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
		      class="card mt-2 cell-data" 
              [class.cell-data-info]="!editor" 
              [class.cell-data-editor]="editor">
				<h4 class="cell-data-header card-title card-header">
					{{cellModel.name}}
					[{{cellModel.minOccurs}}..<ng-container *ngIf="cellModel.maxOccurs && cellModel.maxOccurs!=-1">{{cellModel.maxOccurs}}</ng-container><ng-container *ngIf="!cellModel.maxOccurs || cellModel.maxOccurs==-1">∞</ng-container>]
						<span *ngIf="cell!=undefined" class="cell-data-source badge badge-pill badge-secondary float-secondary float-right">CELL</span>
                         <span *ngIf="cell==undefined" class="cell-data-source badge badge-pill badge-dark float-dark float-right">MODEL</span>
				</h4>
			<div class="card-body">
				<p class="cell-data-model-desc card-subtitle">{{cellModel.desc}}<p>
				<p class="cell-data-model-uri card-text">URI: <span class="cell-data-uri text-muted">{{uri}}</span></p>
			</div>
	        <ng-container *ngIf="!editor">
   			    <img *ngIf="showPresentation()" class="card-img-bottom" src="{{this.cellModel.getPresentation()}}" alt="Card image cap">        
        	        <!-- even if we are showing a cell or a cell model, we use the model to iterate -->
        			<ul class="list-group list-group-flush" *ngIf="cellModel.attributes">
        				<attribute-data-info *ngFor="let a of cellModel.attributes" 
                            [isFromCell]="cell!=undefined"				    
        				    [parentCell]="cell" 
        				    [cellModel]="a"
        				    [isFromModel]="cell==undefined"
        				    ></attribute-data-info>
        			</ul>
            </ng-container>
	        <ng-container *ngIf="editor">
                    <form *ngIf="cellModel.attributes">
                          <attribute-data-editor *ngFor="let a of cellModel.attributes" 
                            [parentCell]="cell" 
                            [cellModel]="a"
                            ></attribute-data-editor>
                    </form>
                    <img *ngIf="showPresentation()" class="card-img-bottom" src="{{this.cellModel.getPresentation()}}" alt="Card image cap">        
            </ng-container>
		</div>
		
			   `,
	styles:[`
            .cell-data {}
            .cell-data-info {}
            .cell-data-editor {}
	        .cell-data-header {}
	        .cell-data-model-desc {}
	        .cell-data-uri {}
	        .cell-data-source {}
	`]
})

export class CellDataComponent extends Widget implements OnInit {

@Input() uri: string;
@Input() cell: Cell;
@Input() cellModel: CellModel;
@Input() editor: boolean = false;

constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

    if (!this.editor) {
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

}


showCellInformation(cell: Cell) {

	this.uri = cell.URI;
	this.cell = cell;
	this.cellModel = cell.cellModel;

}


showCellModelInformation(cellModel: CellModel) {

	this.uri = cellModel.URI;
	this.cell = undefined;
	this.cellModel = cellModel;

}


hideCellInformation() {

    this.cellModel = undefined;
	this.cell = undefined; // not strictly needed, but for completeness and to avoid any future side-effects
}


showPresentation() {
	return this.cellModel.presentation=="CELL";
}

}