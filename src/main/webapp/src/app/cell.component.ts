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

import { FamilyMember } from './family-member.interface';
import { Cell } from './cell.class';
import { CellModel } from './cell-model.class';

import { DropAreaComponent } from './drop-area.component';
import { SelectableWidget } from './selectable-widget.class';

import { CellActivateEvent } from './events/cell-activate.event';
import { CellActivatedEvent } from './events/cell-activated.event';
import { CellDeactivatedEvent } from './events/cell-deactivated.event';
import { CellDragEvent } from './events/cell-drag.event';
import { CellDropEvent } from './events/cell-drop.event';
import { CellModelDeactivatedEvent } from './events/cell-model-deactivated.event';
import { CellSelectEvent } from './events/cell-select.event';
import { CellSelectionClearEvent } from './events/cell-selection-clear.event';
import { CellModelActivatedEvent } from './events/cell-model-activated.event';
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
				<!-- the position of the drop area is always where droped cells will go -->
                <drop-area *ngIf="parent" [parent]="parent" [position]="position+1"></drop-area>
			</ng-container>
			<ng-template #well>
				<div id="{{cell.URI}}" 
				     class="cell-level-{{level}} {{cellClass()}}"
	                 [class.cell-selected]="selected"
				     ><!--small>
				     {{cell.name}}({{position}}), subs: {{subscriptionCount()}},
				     </small>
				     
                     <small *ngIf="selected">[selected],</small>
                     <small *ngIf="cellSelectionSubscription">subscribed-selection,</small>
                     <small *ngIf="cellSelectionClearSubscription">subscribed-clear</small-->
				     <drop-area  *ngIf="parent" [parent]="cell" position="0"></drop-area>
						<cell *ngFor="let c of cell.children; let i=index" 
    						[cell]="c" 
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

export class CellComponent extends SelectableWidget implements OnInit {

@Input() parent: FamilyMember;
@Input() cell: Cell;
@Input() level: number;
@Input() position: number;

active: boolean = false;
dragEnabled:boolean = false;
//isBeingDragged:boolean = false;

@ViewChildren(CellComponent) children: QueryList<CellComponent>;
@ViewChildren(DropAreaComponent) dropAreas: QueryList<DropAreaComponent>;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {
	
    console.log("[UI] CellComponent::ngOnInit()");
    
    // Drop a cell to a position under this cell
    this.subscribe(this.events.service.of( CellDropEvent )
            .filter(dc => dc.newParent==this.cell)
            .subscribe( dc => {
                console.log("-> cell comp gets dropcell event moving '"+dc.cell.name+"' to  "
                            +this.cell.URI+" at position ("
                            +dc.newPosition+")'");
                this.adoptCellAtPosition(dc.cell, dc.newPosition)
    }));
    
    // A cell model was deactivated that is compatible with this cell
    this.subscribe(this.events.service.of( CellModelDeactivatedEvent )
            .filter(d => this.isCompatibleWith(d.cellModel))    // //
            .subscribe( d => {
                //console.log("-> cell comp gets cellmodel deactivated event for '"+d.cellModel.name+"'");
                this.becomeInactive(this.cell);
    }));
    
    // a cell model activated that is compatible with this cell
    this.subscribe(this.events.service.of( CellModelActivatedEvent )
            .filter(a => this.isCompatibleWith(a.cellModel))    // //
            .subscribe( a => {
                //console.log("-> cell comp gets cellmodel activated event for '"+a.cellModel.name+"'"); //
                this.becomeActive(this.cell);
    }));
    
    // an outsider component (like a keyboard shortcut) wants to activate this selected cell
    this.subscribe(this.events.service.of( CellActivateEvent )
            .filter(a => this.selected && this.canBeActivated())
            .subscribe( a => {
                console.log("-> cell comp gets cell activate event and proceeds to focus :)");
                // FIXMWE: this allows for multiple activations when conflicting with rollover
                this.focusOn(this.cell);
    }));

    // A cell different cell was activated and we are active at this moment
    this.subscribe(this.events.service.of( CellActivatedEvent )
            .filter(a => this.active && a.cell!=this.cell)
            .subscribe( a => {
                console.log("-> cell comp gets cell activated event from other cell, we were active, clear");
                this.becomeInactive(this.cell);
    }));
   
    // External component (like a keyboard shortcut) wants to drag this cell somewhere
    this.subscribe(this.events.service.of( CellDragEvent )
            .filter(a => this.active)
            .subscribe( a => {
                console.log("-> cell comp gets cell drag event and will try to drop to a selection :)");
                //this.events.service.publish(new CellDropEvent(this.cell));
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


dragStart(cell:Cell) {
    console.log("[UI] CellComponent::dragStart()");
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
	// once we become active, selections are cleared, for instance to select the drag and drop destination
    this.events.service.publish(new CellSelectionClearEvent());

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


canBeActivated():boolean {
    return !this.cell.cellModel.presentation.startsWith("WELL");
}


select(position:number) {

    if (position==this.position) {
        
        // we were waiting for a selection we match the position, so we select ourselves
        // and unsubscribe from selection as we are not eligible anymore
        console.log("[UI] CellComponent::select("+this.cell.name+"("+this.position+"))");
        this.selected = true; 
        this.unsubscribeFromSelection();
        
        // We unsubscribe from clear, send a clear event and re-subscribe
        // This means we are the only ones selected now (previous parent will be unselected, for instance)
        this.unsubscribeFromSelectionClear();
        this.events.service.publish(new CellSelectionClearEvent());
        this.subscribeToSelectionClear();
        
        // we make children eligible to be selected 
        this.children.forEach(c => c.subscribeToSelection());

        // if we have drop areas, they are also selectable now with the appropriate key shortcut
        this.dropAreas.forEach(da => da.subscribeToSelection());
        
     } else {
         this.clearSelection();  // out of bounds, sorry, clear
    }
    
}


/** This cell now can be selected or can bubble down selections, and can also be cleared */
subscribeToSelection() {
    
    this.selectionSubscription = this.subscribe(this.events.service.of( CellSelectEvent )
                .subscribe( cs => this.select(cs.position) )
    );
    this.subscribeToSelectionClear();  // if we are selectable we are also clearable
    
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