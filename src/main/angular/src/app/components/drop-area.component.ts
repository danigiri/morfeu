// DROP - AREA . COMPONENT . TS

import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
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
import { UXEvent, UXEventType } from '../events/ux.event';
import { EventService } from '../services/event.service';

@Component({
	selector: 'drop-area',
	//changeDetection: ChangeDetectionStrategy.OnPush,
	template: `
			<div	class="drop-area" 
					[class.drop-area-active]="active && !selected && !forbidden"
					[class.drop-area-inactive]="!active && !forbidden"
					[class.drop-area-selected]="selected"
					[class.drop-area-forbidden]="forbidden"
					[class.drop-area-info]="info"
			><small>{{position}}</small>
				<small *ngIf="info">
					<strong *ngIf="active">[active]</strong><em *ngIf="!active">[inactive]</em>, selected={{selected}}, forbidden={{forbidden}}]
				</small>
			</div>
		`,
	styles:[`
				.drop-area {
					padding-top: 2px;
					padding-bottom: 2px;
				}
				.drop-area-active {
					padding-top: 0px;
					padding-bottom: 0px;
					border: 2px dotted #0f0;
					border-radius: 5px;
					opacity: 0.8;
				}
				.drop-area-inactive {
					opacity: 0.01;
				}
				.drop-area-selected {
					padding-top: 0px;
					padding-bottom: 0px;
					border: 2px dashed #00f;
					border-radius: 5px;
				}
				.drop-area-forbidden {
					padding-top: 0px;
					padding-bottom: 0px;
					border: 2px #f00;
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

active = false;
selected = false;			// are we selected?
info = false;
forbidden = false;

constructor(eventService: EventService, private cdr: ChangeDetectorRef) {
	super(eventService);
}


ngOnInit() {

	//console.debug("DropAreaComponent::ngOnInit()");

	// we check for null of parent as we're not getting the binding set at the beginning for some reason
	// IDEA: we could use the function of the drop enabled (gets cell as input) though it's less interactive
	this.register(this.events.service.of<CellDeactivatedEvent>(CellDeactivatedEvent)
			//.pipe(filter(deactivated => this.matchesCell(deactivated.cell)))
			.subscribe(deactivated => { 
										if (this.matchesCell(deactivated.cell)) {
											this.becomeInactive();
										} else {
										}
			})
					//console.log("-> drop-area comp gets cell deactivated event for '"+deactivated.cell.name+"'");

	);
 
	this.register(this.events.service.of<CellActivatedEvent>(CellActivatedEvent)
			//.pipe(filter(activated => this.parent && this.parent.canAdopt(activated.cell)))
			.subscribe(activated => {
										if (this.parent && this.parent.canAdopt(activated.cell, this.position)) {
											this.becomeActive()
										} else {
										}
			})
					// console.log("-> drop-area component '"+this.parent.getAdoptionName()+"' gets cell activated event for '"+activated.cell.name+"'");
	);

	this.register(this.events.service.of<CellModelDeactivatedEvent>(CellModelDeactivatedEvent)
			.subscribe(d => {
				if (this.matchesCellmodel(d.cellModel)) {
					//console.log("-> drop comp gets cellmodel deactivated event for '"+d.cellModel.name+"'");
					this.becomeInactive();
				} else {
				}
			})
	);

	this.register(this.events.service.of<CellModelActivatedEvent>(CellModelActivatedEvent)
			.pipe(filter(activated => activated.cellModel!==undefined))	// we only care for defined cell active models
			.subscribe(activated => {
										if (this.matchesCellmodel(activated.cellModel)) {
											this.becomeActive()
										} else {
										}
			})
	);

	this.register(this.events.service.of<CellDropEvent>(CellDropEvent)
			.pipe(filter(cd => this.selected && cd.newParent==undefined))
			.subscribe( cd => {
				console.log("-> drop comp gets cell drop event from '"+cd.cell.name+"'");
				this.performDropHere(cd.cell, this.parent);
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
	return this.parent && this.parent.canAdopt(cell, this.position);
}


matchesCellmodel(cellModel: CellModel): boolean {
	//console.debug("matching with %s, this.parent="+(this.parent&& true)+",canAdopt="+this.parent && this.parent.canAdopt(cellModel), cellModel.getAdoptionName());
	return this.parent && this.parent.canAdopt(cellModel, this.position);
}


performDropHere(cell:Cell, newParent: FamilyMember, droppedCellActive: boolean = true) {

	if (!cell || !newParent) {
		console.error('DropAreaComponent::performDropHere parameter issue ', cell, newParent);
	}

	// except on zero, we have to consider that the position array and the droparea array are not the same
	// therefore we substract one from the target position
	// TODO: to be determined if we want to substract from the last one
	// [0]
	// <thing(0)/>
	// [1]
	// <thing(1)/>
	// [2]
	// <thing(2)/>
	// [3]
	const newPosition = this.position===0 ? 0 : this.position-1;
	console.log('[UI] DropAreaComponent::performDropHere(%s, %i)', cell.URI, newPosition);
	this.events.service.publish(new CellDropEvent(cell, this.parent, newPosition, droppedCellActive));
	// the document is now dirty
	this.events.service.publish(new UXEvent(UXEventType.DOCUMENT_DIRTY));

	//this.cdr.markForCheck();

}

}

/*
 *	  Copyright 2019 Daniel Giribet
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
 