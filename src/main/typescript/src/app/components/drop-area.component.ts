// DROP - AREA . COMPONENT . TS

import { ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import { filter } from 'rxjs/operators';

import { FamilyMember } from '../family-member.interface';
import { Cell } from '../cell.class';
import { CellModel } from '../cell-model.class';
import { SelectableWidget } from '../selectable-widget.class';

import { CellActivatedEvent } from '../events/cell-activated.event';
import { CellDeactivatedEvent } from '../events/cell-deactivated.event';
import { CellDropEvent } from '../events/cell-drop.event';
import { CellModelActivatedEvent } from '../events/cell-model-activated.event';
import { CellModelDeactivatedEvent } from '../events/cell-model-deactivated.event';
import { CellSelectionClearEvent } from '../events/cell-selection-clear.event';
import { DropAreaSelectEvent } from '../events/drop-area-select.event';
import { InfoModeEvent } from '../events/info-mode.event';
import { EventService } from '../services/event.service';
import { UXEvent } from '../events/ux.event';
import { Adoption } from '../adoption.class';

@Component({
	selector: 'drop-area',
	//changeDetection: ChangeDetectionStrategy.OnPush,
	template: `
			<div	class="drop-area" 
					[class.drop-area-active]="active && !selected" 
					[class.drop-area-inactive]="!active"
					[class.drop-area-selected]="selected"
					[class.drop-area-info]="info"
					dnd-droppable
					[dropEnabled]="active"
					 (onDropSuccess)="dropSuccess($event)"
			>
			<small>{{position}}</small>
				<small *ngIf="info">
					<strong *ngIf="active">[active]</strong><em *ngIf="!active">[inactive]</em>, selected={{selected}}]
					<p *ngIf="adoptionMap">{{adoptionMap}}</p>
				</small>
			</div>
		`,
	styles:[`
				.drop-area {
/*
					padding-top: 2px;
					padding-bottom: 2px;
*/
				}
				.drop-area-active {
					padding-top: 0px;
					padding-bottom: 0px;
					border: 1px dotted #080;
					border-radius: 5px;
					opacity: 0.8;
				}
				.drop-area-inactive {
					border: 1px solid white;
					opacity: 0.01;
				}
				.drop-area-selected {
					padding-top: 0px;
					padding-bottom: 0px;
					border: 1px dashed #00f;
					border-radius: 5px;
				}
				.drop-area-info {
					padding-top: 2px;
					padding-bottom: 2px;
					background-color: #eaeaea;
					opacity: 0.8;
				}
			`]
})


export class DropAreaComponent extends SelectableWidget implements OnInit {

@Input() parent: FamilyMember;
@Input() position: number;

override selected = false;			// are we selected?
active = false;
info = false;
adoptionMap: string = null;


constructor(eventService: EventService, private cdr: ChangeDetectorRef) {
	super(eventService);
}


ngOnInit() {

	//console.debug("DropAreaComponent::ngOnInit()");

	// we check for null of parent as we're not getting the binding set at the beginning for some reason
	// IDEA: we could use the function of the drop enabled (gets cell as input) though it's less interactive
	this.register(this.events.service.of<CellDeactivatedEvent>(CellDeactivatedEvent)
			.pipe(filter(deactivated => this.matchesCell(deactivated.cell)))
			.subscribe(() => this.becomeInactive())
					//console.log("-> drop-area comp gets cell deactivated event for '"+deactivated.cell.name+"'");

	);
 
	this.register(this.events.service.of<CellActivatedEvent>(CellActivatedEvent)
			.pipe(filter(activated => this.parent && this.canAdoptCell(activated.cell)))
			.subscribe(() => this.becomeActive())
					// console.log("-> drop-area component '"+this.parent.getAdoptionName()+"' gets cell activated event for '"+activated.cell.name+"'");
	);

	// for deactivation we only do a match for cell model, which is faster and simpler
	this.register(this.events.service.of<CellModelDeactivatedEvent>(CellModelDeactivatedEvent)
			.subscribe(d => {
				if (this.matchesCellmodel(d.cellModel)) {
					//console.log("-> drop comp gets cellmodel deactivated event for '"+d.cellModel.name+"'");
					this.becomeInactive();
				}
			})
	);

	// the event for cell model activation generates an new cell which we can use for adoption conmpatibility
	this.register(this.events.service.of<CellModelActivatedEvent>(CellModelActivatedEvent)
			.pipe(filter(activated => activated.cellModel && this.canAdoptCell(activated.newCell)))
			.subscribe(() => this.becomeActive())
	);

	this.register(this.events.service.of<CellDropEvent>(CellDropEvent)
			.pipe(filter(cd => this.selected && cd.newParent==undefined))
			.subscribe( cd => {
				console.log("-> drop comp gets cell drop event from '"+cd.cell.name+"'");
				this.performDropHere(cd.cell, this.parent, this.position);
			})
	);

	this.register(this.events.service.of<InfoModeEvent>(InfoModeEvent).subscribe( mode => this.info = mode.active));

}


select(position:number) {

	if (this.active && position==this.position) {

		console.log("[UI] DropAreaComponent::select("+this.parent.getURI()+"["+this.position+"])");
		this.selected = true;
		this.unsubscribeFromSelection();

		// We temporarily unsubscribe from clear, send a clear event and re-subscribe
		// This means we are the only ones selected now (previous parent will be unselected, for instance)
		this.unsubscribeFromSelectionClear();
		this.events.service.publish(new CellSelectionClearEvent());
		this.subscribeToSelectionClear();

	} else if (this.parent && position>=this.parent.childrenCount()) {
		console.log("[UI] DropAreaComponent::select(out of bounds)");
	} else {
		this.clearSelection();	// out of bounds, sorry, clear
	}

	this.cdr.markForCheck();

}


subscribeToSelection() {

	this.selectionSubscription = this.register(this.events.service.of<DropAreaSelectEvent>(DropAreaSelectEvent)
			.subscribe(das => this.select(das.position))
	);
	this.subscribeToSelectionClear();  // if we are selectable we are also clearable

}


becomeInactive() {
	this.active = false;
	//this.cdr.markForCheck();
}


becomeActive() {
	this.active = true;
	//this.cdr.markForCheck();

}


matchesCell(cell: Cell): boolean {
	this.adoptionMap = this.info ? Adoption.adoptionMapToString(this.parent?.canAdoptMap(cell)) : null;
	return this.parent && this.parent.canAdopt(cell);
}


matchesCellmodel(cellModel: CellModel): boolean {
	//console.debug("matching with %s, this.parent="+(this.parent&& true)+",canAdopt="+this.parent && this.parent.canAdopt(cellModel), cellModel.getAdoptionName());
	return this.canAdoptCell(cellModel.generateCell());
}


//  cell can be a moved cell or a new cell from a model
canAdoptCell(cell: Cell): boolean {
	this.adoptionMap = this.info ? Adoption.adoptionMapToString(this.parent?.canAdoptMap(cell, this.position)) : null;
	return this.parent.canAdopt(cell, this.position);
}


/** we drop here as we are only droppeable if we are active, and that's model validated */
dropSuccess($event: any) {
	this.performDropHere($event.dragData, this.parent, this.position);
}


performDropHere(cell:Cell, newParent: FamilyMember, newPosition: number) {

	if (!cell || !newParent || newPosition===undefined) {
		console.error('DropAreaComponent::performDropHere parameter issue ',cell, newParent, newPosition);
	}

	console.log("[UI] DropAreaComponent::dropSuccess("+cell.URI+")");
	this.events.service.publish(new CellDropEvent(cell, this.parent, this.position));
	// the document is now dirty
	this.events.service.publish(new UXEvent(UXEvent.DOCUMENT_DIRTY));

	//this.cdr.markForCheck();

}


}

/*
 *	  Copyright 2024 Daniel Giribet
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
