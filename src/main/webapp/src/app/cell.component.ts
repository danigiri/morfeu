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


import { Component, Input, OnInit, AfterViewInit, QueryList, ViewChildren } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

import { FamilyMember } from './family-member.interface';
import { Cell } from './cell.class';
import { CellModel } from './cell-model.class';
import { Widget } from './widget.class';

import { CellActivatedEvent } from './events/cell-activated.event';
import { CellDeactivatedEvent } from './events/cell-deactivated.event';
import { CellModelDeactivatedEvent } from './events/cell-model-deactivated.event';
import { CellSelectionEvent } from './events/cell-selection.event';
import { CellSelectionClearEvent } from './events/cell-selection-clear.event';
import { CellModelActivatedEvent } from './events/cell-model-activated.event';
import { DropCellEvent } from './events/drop-cell.event';
import { EventService } from './events/event.service';


@Component({
	moduleId: module.id,
	selector: 'cell',
	template: `
			<ng-container *ngIf="cell.cellModel.presentation=='CELL'; else well">
	            <!-- TODO: check the model and the content as well (counts, etc.) -->
				<img id="{{cell.URI}}"
				     src="assets/images/cell.svg" 
					 class="cell img-fluid cell-img cell-level-{{level}}"
                     [class.cell-active]="active"
	                 [class.cell-selected]="selected"
                     (mouseenter)="focusOn(cell)" 
                     (mouseleave)="focusOff(cell)"
					 dnd-draggable 
					 [dragEnabled]="dragEnabled"
					 (onDragEnd)="dragEnd(cell)"
					 [dragData]="cell"
					 />
                <drop-area *ngIf="parent" [parent]="parent" [position]="position"></drop-area>
			</ng-container>
			<ng-template #well>
				<div id="{{cell.URI}}" 
				     class="cell-level-{{level}} {{cellClass()}}"
				     ><small>
				     {{cell.name}}({{position}}), subs: {{subscriptionCount()}},
				     </small>
				     
                     <small *ngIf="selected">[selected],</small>
                     <small *ngIf="cellSelectionSubscription">subscribed-selection,</small>
                     <small *ngIf="cellSelectionClearSubscription">subscribed-clear</small>
				     <drop-area  *ngIf="parent" [parent]="cell" position="0"></drop-area>
						<cell *ngFor="let c of cell.children; let i=index" 
    						[cell]="c" 
	                        [class.cell-selected]="selected"
    						[parent]="cell"
    						[level]="level+1"
    						[position]="i"
    						></cell>
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
				padding-top: 5px;
				padding-bottom: 5px;
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
            .cell-selected {
                border: 3px dashed #00f;
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

@Input() parent: FamilyMember;
@Input() cell: Cell;
@Input() level: number;
@Input() position: number;

active: boolean = false;
dragEnabled:boolean = false;
//isBeingDragged:boolean = false;

selected: boolean = false;          // are we selected?
@ViewChildren(CellComponent) children: QueryList<CellComponent>;

private cellSelectionSubscription: Subscription;
private cellSelectionClearSubscription: Subscription;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {
	
    console.log("[UI] CellComponent::ngOnInit()");
    
    this.subscribe(this.events.service.of( DropCellEvent )
            .filter(dc => dc.newParent==this.cell)
            .subscribe( dc => {
                console.log("-> cell comp gets dropcell event moving '"+dc.cell.name+"' to  "
                            +this.cell.URI+" at position ("
                            +dc.newPosition+")'");
                this.adoptCellAtPosition(dc.cell, dc.newPosition)
    }));
    
    this.subscribe(this.events.service.of( CellModelDeactivatedEvent )
            .filter(d => this.isCompatibleWith(d.cellModel))    // //
            .subscribe( d => {
                //console.log("-> cell comp gets cellmodel deactivated event for '"+d.cellModel.name+"'");
                this.becomeInactive(this.cell);
    }));
    
    this.subscribe(this.events.service.of( CellModelActivatedEvent )
            .filter(a => this.isCompatibleWith(a.cellModel))    // //
            .subscribe( a => {
                //console.log("-> cell comp gets cellmodel activated event for '"+a.cellModel.name+"'"); //
                this.becomeActive(this.cell);
    }));
    
}	 
	   

// we focus on this cell, we want to notify all listeners interested in this type of cell and highlight it
focusOn(cell:Cell) {
	
    //console.log("[UI] CellComponent::focusOn()");
	this.events.service.publish(new CellActivatedEvent(cell));
	this.becomeActive(cell);
	// TODO: OPTIMISATION we could precalculate the event receptor and do a O(k) if needed
	// to make that happen we can associate the cell-model.class with the component (view) and just do it
	// without events
	
}


// notify all interested in this type of cell that we do not have the focus any longer, remove highlight
focusOff(cell:Cell) {

    //console.log("[UI] CellComponent::focusOff()");
    this.becomeInactive(cell);
    this.events.service.publish(new CellDeactivatedEvent(cell));

}


// we drag outside any interesting area, we remove focus
dragEnd(cell:Cell) {
    console.log("[UI] CellComponent::dragEnd()");
   // this.isBeingDragged = false;
    this.focusOff(cell);
}


// the drop-area is sending us a cell to adopt
adoptCellAtPosition(newCell:Cell, position:number) {

    console.log("[UI] CellComponent::adoptCellAtPosition("+position+")");
    // deactivate based on old location
    this.events.service.publish(new CellDeactivatedEvent(newCell));
    this.cell.adopt(newCell, position);

}


// UI method to highlight the cell
becomeActive(cell:Cell) {

    //console.log("[UI] CellComponent::becomeActive("+cell.URI+")");
    this.active = true;
	this.dragEnabled = true;

}


// UI method to no longer be highlighted
becomeInactive(cell: Cell) {

    //console.log("[UI] CellComponent::becomeInactive("+cell.URI+")");
    this.active = false;
	this.dragEnabled = false;
    
}


// are we compatible with this element?
isCompatibleWith(element:FamilyMember): boolean {
    return this.cell.matches(element);
}


select(position:number) {

    if (this.selected) {        // we were selected, now our child will be selected, unselect and unregister
        this.selected = false;
        this.unsubscribeFromCellSelection();
    } else if (position==this.position) {
            // we were waiting for a selection and are selected, so we select ourselves
            // we make children eligible to be selected but do not unregister from
            // selection, at the next selection event we will clear ourselves while our child selects
            console.log("[UI] CellComponent::becomeSelected("+this.cell.name+")");
            this.selected = true; 
            this.children.forEach(c => c.subscribeToCellSelection());
     } else {
            this.clearSelection();  // out of bounds, sorry, clear all
    }
    
}

clearSelection() {

    console.log("[UI] SelectableCellWidget::clearSelection()");
    this.unsubscribeFromCellSelection();
    this.unsubscribeFromCellSelectionClear();
    this.selected = false;

}


subscribeToCellSelection() {  
        this.cellSelectionSubscription = this.subscribe(this.events.service.of( CellSelectionEvent )
                .subscribe( cs => this.select(cs.position) )
        );
        this.subscribeToCellSelectionClear();  // if we are selectable we are also clearable
}


protected subscribeToCellSelectionClear() {
        this.cellSelectionClearSubscription = this.subscribe(this.events.service.of( CellSelectionClearEvent )
                .subscribe( cs => this.clearSelection() )
        );
}


protected unsubscribeFromCellSelection() {

    if (this.cellSelectionSubscription){
        this.unsubscribe(this.cellSelectionSubscription);
        this.cellSelectionSubscription = undefined;
    }
    
}


protected unsubscribeFromCellSelectionClear() {
   
    if (this.cellSelectionClearSubscription){
        this.unsubscribe(this.cellSelectionClearSubscription);
        this.cellSelectionClearSubscription = undefined;
    }
    
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


}