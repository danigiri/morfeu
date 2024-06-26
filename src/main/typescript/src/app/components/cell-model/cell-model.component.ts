// CELL - MODEL . COMPONENT . TS

import { Component, Input, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

//import { TreeNode } from '@circlon/angular-tree-component';

import { CellModel } from '../../cell-model.class';
import { FamilyMember } from '../../family-member.interface';
import { SelectableWidget } from '../../selectable-widget.class';

import { CellActivatedEvent } from '../../events/cell-activated.event';
import { CellDeactivatedEvent } from '../../events/cell-deactivated.event';
import { CellDropEvent } from '../../events/cell-drop.event';
import { CellModelActivatedEvent } from '../../events/cell-model-activated.event';
import { CellModelDeactivatedEvent } from '../../events/cell-model-deactivated.event';
import { CellSelectEvent } from '../../events/cell-select.event';
import { CellSelectionClearEvent } from '../../events/cell-selection-clear.event';
import { NewCellFromModelEvent } from '../../events/new-cell-from-model.event';
import { EventService } from '../../services/event.service';
import { CellSelectionReadyEvent } from 'src/app/events/cell-selection-ready.event';

@Component({
	selector: 'cell-model',
	templateUrl: './cell-model.component.html',

	styles: [`
			.cell-model-entry {}
			.cell-model-name {}
			.cell-model-desc {}
			.cell-model-thumb {
				border: 1px solid transparent;	/* So when changed to highlighted, active, it doesn't move */
			}
			.cell-model-active {
				border: 1px solid #00f000;
			}
			 .cell-model-selected {
					border: 1px dashed #00f;
					border-radius: 5px;
			}
			.cell-model-level-1 {}
			.cell-model-level-2 {}
			.cell-model-level-3 {}
			.cell-model-level-4 {}
			.cell-model-level-5 {}
			.cell-model-level-6 {}
			.cell-model-level-7 {}
			.cell-model-level-8 {}
			.cell-model-level-9 {}
			.cell-model-position-0 {}
			.cell-model-position-1 {}
			.cell-model-position-2 {}
			.cell-model-position-3 {}
			.cell-model-position-4 {}
			.cell-model-position-5 {}
			.cell-model-position-6 {}
			.cell-model-position-7 {}
			.cell-model-position-8 {}
			.cell-model-position-9 {}
	`]
})

export class CellModelComponent extends SelectableWidget implements OnInit {

//@Input() node: TreeNode;
@Input() position: number;
@Input() cellModel: CellModel;

active = false;
dragEnabled = false;

private activationSubscription: Subscription;
private newCellSubscription: Subscription;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	console.log("CellModelComponent::ngOnInit()");

	// establish the relationship between the cellmodel and the component
//	this.cellModel = this.node.data as CellModel;
//	this.cellModel.component = this;

	// this is activation due to a cell that is compatible with this model being activated
	this.register(this.events.service.of<CellDeactivatedEvent>(CellDeactivatedEvent)
		.pipe(filter(deactivated => this.isCompatibleWith(deactivated.cell)))
		.subscribe(() => this.becomeInactive(true))
				// console.log("-> cell-model comp gets cell deactivated event for '"+deactivated.cell.name+"'");
	);

	this.register(this.events.service.of<CellActivatedEvent>(CellActivatedEvent)
			.pipe(filter(activated => this.isCompatibleWith(activated.cell)))
			.subscribe(() => this.becomeActive(true))
				// console.log("-> cell-model component gets cell activated event for '"+activated.cell.name+"'");
	);

	// an outsider component (like a keyboard shortcut) wants to activate this selected cell model
	this.register(this.events.service.of<CellModelActivatedEvent>(CellModelActivatedEvent)
	.pipe(filter(activated => this.selected && activated.cellModel == null))
	.subscribe(() => {
			console.log('-> selected cell model comp gets model activation event and proceeds to focus :)');
			this.becomeActive(false);
		})
	);
	// a different cell model was activated and we are active at this moment
	this.register(this.events.service.of<CellModelActivatedEvent>(CellModelActivatedEvent)
			.pipe(filter(activated => this.active && activated.cellModel && activated.cellModel !== this.cellModel))
			.subscribe(() => {
				console.log('-> cell model comp gets cell model activated event from someone else, as active, clear');
				this.becomeInactive(false);
			})
	);

	this.register(this.events.service.of<CellSelectionReadyEvent>(CellSelectionReadyEvent)
		.pipe(filter(e => !e.ready && this.isCompatibleWith(e.target)))
		.subscribe(() => this.unsubscribeFromSelection())
	);

	this.register(this.events.service.of<CellSelectionReadyEvent>(CellSelectionReadyEvent)
			.pipe(filter(e => e.ready && this.isCompatibleWith(e.target)))
			.subscribe(() => this.subscribeToSelection())
	);
}


becomeActive(fromCell: boolean) {

	// console.log("[UI] CellModelComponent::becomeActive()");
	this.active = true;
	this.dragEnabled = this.cellModel.canGenerateNewCell();
	if (!fromCell) {   // if we become active from a cell, we do not need to propagate the activation
		this.events.service.publish(new CellModelActivatedEvent(this.cellModel));
	}
	this.subscribeToNewCellFromModel();
	// console.log("[UI] CellModelComponent::becomeActive(dragEnabled:%s)", this.dragEnabled);

}


becomeInactive(fromCell: boolean) {

	// console.log("[UI] CellModelComponent::becomeInactive()");
	this.active = false;
	this.dragEnabled = false;
	this.unsubscribeToNewCellFromModel();
	this.events.service.publish(new CellModelDeactivatedEvent(this.cellModel));

}


select(position: number) {

	if (position===this.position) {

		// if we were activated we deactivate ourselves and become selectable again
		if (this.active) {
			this.becomeInactive(false);
		}

		console.log("[UI] CellModelComponent::select("+this.cellModel.name+"("+this.position+"))");
		this.selected = true;
		this.unsubscribeFromSelection();
		// cleverly, we now subscribe to cellmodel activation events that may be triggered by shortcuts
		//this.subscribeToActivation(); WE ARE ALWAYS SUBSCRIBED TO ACTIVATION, THIS SHOULD NOT BE NEEDED

		// We temporarly unsubscribe from clear, send a clear event and re-subscribe
		// This means we are the only ones selected now (previous parent will be unselected, for instance)
		this.unsubscribeFromSelectionClear();
		this.events.service.publish(new CellSelectionClearEvent()); // warning: resets model state variables
		this.subscribeToSelectionClear();

		this.cellModel.children.forEach(c => this.events.service.publish(new CellSelectionReadyEvent(c)));

		// TODO: implement out of bounds handling for cell-models
//	  } else if (this.cellModel.parent && position>=this.cell.parent.childrenCount()) {
//		  console.log("[UI] SelectableCellModelWidget::select(out of bounds)");
	} else {
		this.clearSelection();	 // out of bounds, sorry, clear
	}

}


subscribeToSelection() {

	//console.debug('Subscribed to selection: '+this.cellModel.name+'('+this.position+')');
	this.selectionSubscription = this.register(this.events.service.of<CellSelectEvent>(CellSelectEvent)
										.subscribe(cs => this.select(cs.position))
	);
	this.subscribeToSelectionClear();  // if we are selectable we are also clearable

}


override unsubscribeFromSelection() {

	super.unsubscribeFromSelection();
	if (this.activationSubscription) {	// if we were selectable we may have been activable as well
		this.unsubscribe(this.activationSubscription);
	}

}



clickDown() {
	this.becomeActive(false);
	// this.events.service.publish(new CellModelActivatedEvent(cellModel));
}


clickUp() {
	this.becomeInactive(false);
	// this.events.service.publish(new CellModelDeactivatedEvent(cellModel));
}


dragEnd() {

	console.log("[UI] CellModelComponent::dragEnd()");
	this.becomeInactive(false);

}


isCompatibleWith(element: FamilyMember): boolean {
	return this.cellModel.matches(element);
}


getThumb(): string {
	return (this.cellModel.thumb=='DEFAULT') ? "assets/images/cell-thumb.svg" : this.cellModel.thumb;
}


/*private subscribeToActivation() {

	console.log("[UI] CellModelComponent::subscribeToActivation("+this.cellModel.name+")");
	this.activationSubscription = this.register(this.events.service.of<CellModelActivatedEvent>(CellModelActivatedEvent)
		.pipe(filter( activated => activated.cellModel==undefined && this.selected))	 // no cell model
		.subscribe(() => this.becomeActive(false) )
	);

}


private unsubscribeFromActivation() {

	if (this.activationSubscription) {
		this.unsubscribe(this.activationSubscription);
	}

}

*/
private subscribeToNewCellFromModel() {

	if (!this.newCellSubscription) {
		this.newCellSubscription = this.register(this.events.service.of<NewCellFromModelEvent>(NewCellFromModelEvent) 
				.subscribe( nc => {
					if (this.active && this.cellModel.canGenerateNewCell()) {
						console.log("-> cell model widget gets new cell event and will try to create one :)");
						this.events.service.publish(new CellDropEvent(this.cellModel.generateCell()));
					}
				})
		);
	}
}


private unsubscribeToNewCellFromModel() {

	if (this.newCellSubscription) {
		this.unsubscribe(this.newCellSubscription);
		this.newCellSubscription = undefined;
	}

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
 