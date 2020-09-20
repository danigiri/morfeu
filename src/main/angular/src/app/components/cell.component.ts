// CELL . COMPONENT . TS

import { AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { filter } from 'rxjs/operators';

import { FamilyMember } from '../family-member.interface';
import { Cell } from '../cell.class';
import { CellModel } from '../cell-model.class';

import { DropAreaComponent } from './drop-area.component';
import { SelectableWidget } from '../selectable-widget.class';

import { CellActivateEvent } from '../events/cell-activate.event';
import { CellActivatedEvent } from '../events/cell-activated.event';
import { CellDeactivatedEvent } from '../events/cell-deactivated.event';
import { CellDragEvent } from '../events/cell-drag.event';
import { CellDropEvent } from '../events/cell-drop.event';
import { CellEditEvent } from '../events/cell-edit.event';
import { CellModelDeactivatedEvent } from '../events/cell-model-deactivated.event';
import { CellRemoveEvent } from '../events/cell-remove.event';
import { CellSelectEvent } from '../events/cell-select.event';
import { CellSelectionClearEvent } from '../events/cell-selection-clear.event';
import { CellModelActivatedEvent } from '../events/cell-model-activated.event';
import { InfoModeEvent } from '../events/info-mode.event';
import { EventService } from '../services/event.service';

@Component({
	selector: 'cell',
	//changeDetection: ChangeDetectionStrategy.OnPush,
	templateUrl: './cell.component.html',
	styleUrls: ['./cell.component.css', './presentation/presentation.css']
	// encapsulation: ViewEncapsulation.Emulated,
})

export class CellComponent extends SelectableWidget implements OnInit {

private static readonly _MAX_PRESENTATION_SIZE = 1024;	// used to detect issues with too long presentation

@Input() parent?: FamilyMember;	// may not have a parent if we are a snippet
@Input() cell: Cell;
@Input() snippet?: boolean;
@Input() isFragment?: boolean;
@Input() level: number;
@Input() position: number;

active = false;
activeReadonly = false;
dragEnabled = false;
canBeDeleted = true;
canBeModified = true;
info = false;

@ViewChildren(CellComponent) children: QueryList<CellComponent>;
@ViewChild(DropAreaComponent) dropArea: DropAreaComponent;	// we only have one of those!!!


constructor(eventService: EventService, private element: ElementRef, private cdr: ChangeDetectorRef) {
	super(eventService);
}


ngOnInit() {

	// console.log('[UI] CellComponent::ngOnInit()');
	this.canBeDeleted = this.cell.canBeDeleted();
	this.canBeModified = this.cell.canBeModified();

	// drop a cell to a position under this cell
	this.register(this.events.service.of<CellDropEvent>(CellDropEvent)
			.pipe(filter(dc => dc.newParent && dc.newParent===this.cell && this.canBeModified))
			.subscribe( dc => {
				console.log('-> cell comp gets dropcell event moving '+dc.cell.name+' to '
							+this.cell.URI+' at position ('+dc.newPosition+')');
				this.adoptCellAtPosition(dc.cell, dc.newPosition);
			})
	);

	// a cell model was deactivated that is compatible with this cell
	this.register(this.events.service.of<CellModelDeactivatedEvent>(CellModelDeactivatedEvent)
			.pipe(filter(d => d.cellModel && this.isCompatibleWith(d.cellModel)))
			.subscribe(() => this.becomeInactive(this.cell))
				// console.log('-> cell comp gets cellmodel deactivated event for ''+d.cellModel.name+''');
	);

	// a cell model activated that is compatible with this cell
	this.register(this.events.service.of<CellModelActivatedEvent>(CellModelActivatedEvent)
			.pipe(filter( a => a.cellModel && this.isCompatibleWith(a.cellModel)))
			.subscribe(() => this.becomeActive(this.cell))
				//console.log('-> cell comp gets cellmodel activated event for ''+a.cellModel.name+'''); 
	);

	// an outsider component (like a keyboard shortcut) wants to activate this selected cell
	this.register(this.events.service.of<CellActivateEvent>(CellActivateEvent)
			.pipe(filter(() => this.selected && this.canBeActivated()))
			.subscribe(() => {
				console.log('-> cell comp gets cell activate event and proceeds to focus :)');
				// FIXMWE: this allows for multiple activations when conflicting with rollover
				this.focusOn(this.cell);
			})
	);

	// a different cell was activated and we are active at this moment
	this.register(this.events.service.of<CellActivatedEvent>(CellActivatedEvent)
			.pipe(filter(a => this.active && a.cell!==this.cell))
			.subscribe(() => {
				console.log('-> cell comp gets cell activated event from other cell, we were active, clear');
				this.becomeInactive(this.cell);
			})
	);

	// external component (like a keyboard shortcut) wants to drag this cell somewhere
	this.register(this.events.service.of<CellDragEvent>(CellDragEvent)
			.pipe(filter(() => this.active && this.canBeModified))
			.subscribe(() => {
				console.log('-> cell comp gets cell drag event and will try to drop to a selection :)');
				this.events.service.publish(new CellDropEvent(this.cell));
			})
	);

	// want to edit this cell
	this.register(this.events.service.of<CellEditEvent>(CellEditEvent)
			.pipe(filter(edit => !edit.cell && this.isEditable()))
			.subscribe(() => {
					console.log('-> cell comp gets cell edit event and will try to edit :)');
					this.events.service.publish(new CellEditEvent(this.cell));
			})
	);

	// want to remove this cell (skip if readonly) [can be optimised to not even subscribe if needed]
	this.register(this.events.service.of<CellRemoveEvent>(CellRemoveEvent)
			.pipe(filter(remove => !remove.cell && (this.active || this.selected) && this.canBeModified))
			.subscribe(() => {
					console.log('-> cell comp gets cell remove event and will get removed');
					// we could re-issue an event with the specific cell to be removed if needed
					//this.events.service.publish(new CellRemoveEvent(this.cell));
					this.remove();
			})
	);

	this.register(this.events.service.of<InfoModeEvent>(InfoModeEvent).subscribe(mode => this.info = mode.active));

	//this.cdr.markForCheck();

}


// we focus on this cell, we want to notify all listeners interested in this type of cell and highlight it
focusOn(cell: Cell) {

	// console.log('[UI] CellComponent::focusOn()');
	this.events.service.publish(new CellActivatedEvent(cell));
	this.becomeActive(cell);
	// TODO: OPTIMISATION we could precalculate the event receptor and do a O(k) if needed
	// to make that happen we can associate the cell-model.class with the component (view) and just do it
	// without events

	//this.cdr.markForCheck();

}


// notify all interested in this type of cell that we do not have the focus any longer, remove highlight
focusOff(cell: Cell) {

	// console.log('[UI] CellComponent::focusOff()');
	this.becomeInactive(cell);
	this.events.service.publish(new CellDeactivatedEvent(cell));

	//this.cdr.markForCheck();

}


// we drag outside any interesting area, we remove focus
dragEnd(cell: Cell) {

	console.log('[UI] CellComponent::dragEnd()');
	// this.isBeingDragged = false;
	this.focusOff(cell);

}


// the drop-area is sending us a cell to adopt
adoptCellAtPosition(newCell: Cell, position: number) {

	console.log('[UI] CellComponent::adoptCellAtPosition('+position+')');
	// deactivate based on old location
	this.events.service.publish(new CellDeactivatedEvent(newCell));

	// if we are adopting a cell that is actually a move and we are moving at the end, 'position' is now 'position--'
	// (or childrencount) as we have zero based arrays =)
	// Start:
	// cell0
	// cell1
	//
	// Move:
	// cell0 ----------\
	// cell1           |
	// [position=2] <--/
	//
	// As we have removed cell0 temporarily the parent has only one cell, so the actual target position is 1 and not 2
	// TODO: this may apply to more cases than moving at the end 

	//if (newCell.parent.getAdoptionURI()===this.cell.getAdoptionURI()) {
	if (newCell.parent===this.cell && position>this.cell.childrenCount()) {
		position = this.cell.childrenCount();	// logically equivalent to 'position--;'
	}

	// must be an orphan before adopting
	if (newCell.parent) {
		newCell.parent.remove(newCell);
	}

	this.cell.adopt(newCell, position);

	//this.cdr.markForCheck();

}


// UI method to highlight the cell
becomeActive(cell: Cell) {

	const activate = this.canBeDeleted && this.canBeModified;
	// console.log('[UI] CellComponent::becomeActive('+cell.URI+')');
	this.active = activate;
	this.activeReadonly = !activate;
	this.dragEnabled = this.canBeModified;	// can only be dragged if it's modifiable'
	// once we become active, selections are cleared, for instance to select the drag and drop destination
	this.events.service.publish(new CellSelectionClearEvent());

	//console.debug('[becomeActive]>%s, %s', this.cell.getAdoptionName(), this.cell.getAdoptionURI());

	//this.cdr.markForCheck();

}


// UI method to no longer be highlighted
becomeInactive(cell: Cell) {

	// console.log('[UI] CellComponent::becomeInactive('+cell.URI+')');
	this.active = false;
	this.activeReadonly = false;
	this.dragEnabled = false;

	//this.cdr.markForCheck();

}


// are we compatible with this element?
isCompatibleWith(element: FamilyMember): boolean {
	return this.cell.matches(element);
}


// it can be activated (for drag and drop, etc) if it's not a well
// if it's a snippet, we can always activate it, so it can be cloned
canBeActivated(): boolean {
	return !this.cell.cellModel.presentation.includes('COL-WELL') || this.snippet;
}


select(position: number) {

	if (position===this.position) {

		// if we were activated we deactivate ourselves and become selectable again
		if (this.active) {
			this.becomeInactive(this.cell);
		}

		// we were waiting for a selection and we've matched the position, so we select ourselves
		// and unsubscribe from selection as we are not eligible anymore
		console.log('[UI] CellComponent::select('+this.cell.name+'('+this.position+'))');
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
		console.log('[UI] CellComponent::select(out of bounds)');
	} else {
		this.clearSelection();	 // out of bounds, sorry, clear
	}

	//this.cdr.markForCheck();

}


/** This cell now can be selected or can bubble down selections, and can also be cleared */
subscribeToSelection() {

	this.selectionSubscription = this.register(this.events.service.of<CellSelectEvent>(CellSelectEvent)
										.subscribe(cs => this.select(cs.position))
	);
	this.subscribeToSelectionClear();  // if we are selectable we are also clearable

}


private cellPresentationIsIMG(): boolean {
	return this.cell.cellModel.getCellPresentationType()===CellModel.DEFAULT_PRESENTATION_TYPE;
}


getCellPresentation() {

	const pres = this.cell.getCellPresentation();
	if (pres.length > CellComponent._MAX_PRESENTATION_SIZE) {
		console.warn('Presentation may be too long for %s (%d)', this.cell.name, pres.length);
	}

	return pres;

}


private isEditable(): boolean {
	return (this.active || this.activeReadonly)
			&& this.canBeModified && !this.snippet && !this.cell.cellModel.presentation.includes('COL-WELL');
}


private remove() {

	let parent = this.cell.parent;
	if (parent) {
		parent.remove(this.cell);
	}

	//this.cdr.markForCheck();

}


// data that is being dragged (and potentially dropped)
private cellDragData() {

	let cellDragData: Cell;
	if (this.snippet) {	// If we are cloning, we deep clone the cell and remove the parent ref
						// as cloning will also clone the reference to the parent.
						// Then it's effectively an orphan when the adoption takes place
		cellDragData = this.cell.deepClone();
		delete cellDragData['parent'];
	} else {
		cellDragData = this.cell;
	}

	return cellDragData;

}


private doubleClick() {

	if (this.isEditable()) {
		this.events.service.publish(new CellEditEvent(this.cell));
	}

}


}

/*
 *	  Copyright 2019 Daniel Giribet
 *
 *	 Licensed under the Apache License, Version 2.0 (the 'License');
 *	 you may not use this file except in compliance with the License.
 *	 You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 *	 Unless required by applicable law or agreed to in writing, software
 *	 distributed under the License is distributed on an 'AS IS' BASIS,
 *	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	 See the License for the specific language governing permissions and
 *	 limitations under the License.
 */
