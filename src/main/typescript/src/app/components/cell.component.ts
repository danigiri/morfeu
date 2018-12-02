// CELL . COMPONENT . TS

import {filter} from 'rxjs/operators';
import { Component, Input, OnInit, QueryList, ViewChild, ViewChildren } from "@angular/core";

import { FamilyMember } from "../family-member.interface";
import { Cell } from "../cell.class";
import { CellModel } from "../cell-model.class";

import { DropAreaComponent } from "../drop-area.component";
import { SelectableWidget } from "../selectable-widget.class";

import { CellActivateEvent } from "../events/cell-activate.event";
import { CellActivatedEvent } from "../events/cell-activated.event";
import { CellDeactivatedEvent } from "../events/cell-deactivated.event";
import { CellDragEvent } from "../events/cell-drag.event";
import { CellDropEvent } from "../events/cell-drop.event";
import { CellEditEvent } from "../events/cell-edit.event";
import { CellModelDeactivatedEvent } from "../events/cell-model-deactivated.event";
import { CellSelectEvent } from "../events/cell-select.event";
import { CellSelectionClearEvent } from "../events/cell-selection-clear.event";
import { CellModelActivatedEvent } from "../events/cell-model-activated.event";
import { EventService } from "../events/event.service";


@Component({
	moduleId: module.id,
	selector: "cell",
	templateUrl: "./cell.component.html",
	styleUrls: ["./cell.component.css", "./presentation.css"]
	//
// encapsulation: ViewEncapsulation.Emulated,
})

export class CellComponent extends SelectableWidget implements OnInit {

@Input() parent: FamilyMember;
@Input() cell: Cell;
@Input() snippet?: boolean;
@Input() level: number;
@Input() position: number;

active = false;
dragEnabled = false;

@ViewChildren(CellComponent) children: QueryList<CellComponent>;
@ViewChild(DropAreaComponent) dropArea: DropAreaComponent;		// we only have one of those!!!


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	// console.log("[UI] CellComponent::ngOnInit()");

	// Drop a cell to a position under this cell
	this.subscribe(this.events.service.of( CellDropEvent ).pipe(
			filter(dc => dc.newParent && dc.newParent===this.cell))
			.subscribe( dc => {
				console.log("-> cell comp gets dropcell event moving '"+dc.cell.name+"' to	"
							+this.cell.URI+" at position ("
							+dc.newPosition+")'");
				this.adoptCellAtPosition(dc.cell, dc.newPosition);
	}));

	// A cell model was deactivated that is compatible with this cell
	this.subscribe(this.events.service.of( CellModelDeactivatedEvent ).pipe(
			filter(d => d.cellModel && this.isCompatibleWith(d.cellModel))) // //
			.subscribe( d => {
				// console.log("-> cell comp gets cellmodel deactivated event for '"+d.cellModel.name+"'");
				this.becomeInactive(this.cell);
	}));

	// a cell model activated that is compatible with this cell
	this.subscribe(this.events.service.of( CellModelActivatedEvent ).pipe(
			filter( a => a.cellModel && this.isCompatibleWith(a.cellModel)))
			.subscribe( a => {
				console.log("-> cell comp gets cellmodel activated event for '"+a.cellModel.name+"'"); //
				this.becomeActive(this.cell);
	}));

	// an outsider component (like a keyboard shortcut) wants to activate this selected cell
	this.subscribe(this.events.service.of( CellActivateEvent ).pipe(
			filter(a => this.selected && this.canBeActivated()))
			.subscribe( a => {
				console.log("-> cell comp gets cell activate event and proceeds to focus :)");
				// FIXMWE: this allows for multiple activations when conflicting with rollover
				this.focusOn(this.cell);
	}));

	// A different cell was activated and we are active at this moment
	this.subscribe(this.events.service.of( CellActivatedEvent ).pipe(
			filter(a => this.active && a.cell!==this.cell))
			.subscribe( a => {
				console.log("-> cell comp gets cell activated event from other cell, we were active, clear");
				this.becomeInactive(this.cell);
	}));

	// External component (like a keyboard shortcut) wants to drag this cell somewhere
	this.subscribe(this.events.service.of( CellDragEvent ).pipe(
			filter(a => this.active))
			.subscribe( a => {
				console.log("-> cell comp gets cell drag event and will try to drop to a selection :)");
				this.events.service.publish(new CellDropEvent(this.cell));
	}));

	// Want to edit this cell
	this.subscribe(this.events.service.of( CellEditEvent ).pipe(
				filter(edit => !edit.cell && this.isEditable()))
				.subscribe( edit => {
					console.log("-> cell comp gets cell edit event and will try to edit :)");
					this.events.service.publish(new CellEditEvent(this.cell));
		}));

}


// we focus on this cell, we want to notify all listeners interested in this type of cell and highlight it
focusOn(cell: Cell) {

	// console.log("[UI] CellComponent::focusOn()");
	this.events.service.publish(new CellActivatedEvent(cell));
	this.becomeActive(cell);
	// TODO: OPTIMISATION we could precalculate the event receptor and do a O(k) if needed
	// to make that happen we can associate the cell-model.class with the component (view) and just do it
	// without events

}


// notify all interested in this type of cell that we do not have the focus any longer, remove highlight
focusOff(cell: Cell) {

	// console.log("[UI] CellComponent::focusOff()");
	this.becomeInactive(cell);
	this.events.service.publish(new CellDeactivatedEvent(cell));

}


// we drag outside any interesting area, we remove focus
dragEnd(cell: Cell) {

	console.log("[UI] CellComponent::dragEnd()");
	// this.isBeingDragged = false;
	this.focusOff(cell);
}


// the drop-area is sending us a cell to adopt
adoptCellAtPosition(newCell: Cell, position: number) {

	console.log("[UI] CellComponent::adoptCellAtPosition("+position+")");
	// deactivate based on old location
	this.events.service.publish(new CellDeactivatedEvent(newCell));
	// must be an orphan before adopting
	if (newCell.parent) {
		newCell.parent.remove(newCell);
	}
	
	// if we are adopting a cell that is actually a move and we are moving at the end, position is now position--
	// (or childrencount) as we have zero based arrays =)
	// Start:
	// cell0
	// cell1
	// Move:
	// cell0 ----------\
	// cell1           |
	// [position=2] <--/
	//
	// As we have removed cell0 temporarily the parent has only one cell, so the actual target position is 1 and not 2
	
	//if (newCell.parent.getAdoptionURI()===this.cell.getAdoptionURI()) {
	if (newCell.parent===this.cell && position>this.cell.childrenCount()) {
		position = this.cell.childrenCount();	// logically equivalent to 'position--;'
	}
	this.cell.adopt(newCell, position);

}


// UI method to highlight the cell
becomeActive(cell: Cell) {

	// console.log("[UI] CellComponent::becomeActive("+cell.URI+")");
	this.active = true;
	this.dragEnabled = true;
	// once we become active, selections are cleared, for instance to select the drag and drop destination
	this.events.service.publish(new CellSelectionClearEvent());

}


// UI method to no longer be highlighted
becomeInactive(cell: Cell) {

	// console.log("[UI] CellComponent::becomeInactive("+cell.URI+")");
	this.active = false;
	this.dragEnabled = false;

}


// are we compatible with this element?
isCompatibleWith(element: FamilyMember): boolean {
	return this.cell.matches(element);
}


// it can be activated (for drag and drop, etc) if it's not a well
// if it's a snippet, we can always activate it, so it can be cloned
canBeActivated(): boolean {
	return !this.cell.cellModel.presentation.includes("COL-WELL") || this.snippet;
}


select(position: number) {

	if (position===this.position) {

		// if we were activated we deactivate ourselves and become selectable again
		if (this.active) {
			this.becomeInactive(this.cell);
		}

		// we were waiting for a selection and we've matched the position, so we select ourselves
		// and unsubscribe from selection as we are not eligible anymore
		console.log("[UI] CellComponent::select("+this.cell.name+"("+this.position+"))");
		this.selected = true;
		this.unsubscribeFromSelection();

		// We temporarly unsubscribe from clear, send a clear event and re-subscribe
		// This means we are the only ones selected now (previous parent will be unselected, for instance)
		this.unsubscribeFromSelectionClear();
		this.events.service.publish(new CellSelectionClearEvent());
		this.subscribeToSelectionClear();

		// now our children are eligible to be selected
		this.children.forEach(c => c.subscribeToSelection());

		// if we have drop areas, they are also selectable now with the appropriate key shortcut
		// it's non trivial to get a list of dropAreas, in the case of wells, we have one drop area and the
		// rest are from our children cells, so we subscribe our explicit first and then the rest, if we have
		// children cells, that is.
		// Diagram:
		// if cell then
		//	  <img>
		//	  <drop-area>  (explicit in the template)
		// else (well)
		//	<cell>
		//	<drop-area> (explicit in the template)
		//		<foreach cell></cell>	(deep in each one there is the drop-area)
		//	</cell>
		// endif
		this.dropArea.subscribeToSelection();
		this.children.forEach(c => c.dropArea.subscribeToSelection());

	} else if (this.cell.parent && position>=this.cell.parent.childrenCount()) {
		console.log("[UI] CellComponent::select(out of bounds)");
	} else {
		this.clearSelection();	 // out of bounds, sorry, clear
	}

}


/** This cell now can be selected or can bubble down selections, and can also be cleared */
subscribeToSelection() {

	this.selectionSubscription = this.subscribe(this.events.service.of( CellSelectEvent )
				.subscribe( cs => this.select(cs.position) )
	);
	this.subscribeToSelectionClear();  // if we are selectable we are also clearable

}


private cellPresentationIsIMG(): boolean {
	return this.cell.cellModel.getPresentationType()===CellModel.DEFAULT_PRESENTATION_TYPE;
}


getCellPresentation() {
	return this.cell.getPresentation();
}


private isEditable(): boolean {
	return this.active && !this.cell.cellModel.presentation.includes("COL-WELL") && !this.snippet;
}


// data that is being dragged (and potentially dropped)
private cellDragData() {

	let cellDragData: Cell;
	if (this.snippet) {	// If we are cloning, we deep clone the cell and remove the parent ref
						// as cloning will also clone the reference to the parent.
						// Then it's effectively an orphan when the adoption takes place
		cellDragData = this.cell.deepClone();
		delete cellDragData["parent"];
	} else {
		cellDragData = this.cell;
	}

	return cellDragData;

}

private doubleClick() {
	this.events.service.publish(new CellEditEvent(this.cell));
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
