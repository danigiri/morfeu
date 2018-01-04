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
import { Observable } from 'rxjs/Observable';

import { TreeNode } from 'angular-tree-component';


import { Cell } from './cell.class';
import { CellModel } from './cell-model.class';
import { FamilyMember } from './family-member.interface';
import { Widget } from './widget.class';

import { CellActivatedEvent } from './events/cell-activated.event';
import { CellDeactivatedEvent } from './events/cell-deactivated.event';
import { CellModelActivatedEvent } from './events/cell-model-activated.event';
import { CellModelDeactivatedEvent } from './events/cell-model-deactivated.event';
import { EventService } from './events/event.service';

@Component({
	moduleId: module.id,
	selector: 'cell-model',
	template: `
		<div id="{{node.data.id}}" 
			 class="cell-model-entry cell-model-level-{{node.level}}">
			<img 
				src={{getThumb()}} 
				class="cell-model-thumb img-fluid" 
				[class.cell-model-active]="active" 
				[class.cell-model-selected]="node.data.widget.selected"
				(mousedown)="clickDown(node.data)" 
				(mouseup)="clickUp(node.data)"
				(mouseenter)="clickDown(node.data)" 
				(mouseleave)="clickUp(node.data)"
				dnd-draggable 
	            [dragEnabled]="dragEnabled"
	            (onDragEnd)="dragEnd(node.data)"
                [dragData]="node.data.generateCell()"                             
					/>
                <!-- -->
			<span class="cell-model-name"><small>{{ node.data.name }}</small></span>
		</div>
		`,

	styles:[`
			.cell-model-entry {}
			.cell-model-name {}
			.cell-model-desc {}
			.cell-model-thumb {
				border: 1px solid transparent;	/* So when changed to highlighted, active, it doesn't move */
			}
			.cell-model-active {
				border: 1px solid #f00;
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
	`]
})

export class CellModelComponent extends Widget implements OnInit {

@Input() node: TreeNode;
@Input() index: number;
	
active:boolean = false;
dragEnabled:boolean = false;

constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	console.log("CellModelComponent::ngOnInit()");

	this.subscribe(this.events.service.of( CellDeactivatedEvent )
			.filter(deactivated => this.isCompatibleWith(deactivated.cell))
			.subscribe( deactivated => {
				//console.log("-> cell-model comp gets cell deactivated event for '"+deactivated.cell.name+"'");
				this.becomeInactive(deactivated.cell);
	}));

	this.subscribe(this.events.service.of( CellActivatedEvent )
			.filter(activated => this.isCompatibleWith(activated.cell))
			.subscribe( activated => {
				//console.log("-> cell-model component gets cell activated event for '"+activated.cell.name+"'");
				this.becomeActive(activated.cell);
	}));
	
	// this will come from the selectableCellModelWidget via shortcuts
	this.subscribe(this.events.service.of( CellModelActivatedEvent )
            .filter( activated => activated.cellModel==this.node.data && !this.active)
            .subscribe( activated => this.becomeActive(null)) 
	);
}


becomeActive(cell: Cell) {

	//console.log("[UI] CellModelComponent::becomeActive()");
	this.active = true;
	this.dragEnabled = this.node.data.canGenerateNewCell();
	console.log("[UI] CellModelComponent::becomeActive(dragEnabled:%s)", this.dragEnabled);
}


becomeInactive(cell: Cell) {

	//console.log("[UI] CellModelComponent::becomeInactive()");
	this.active = false;
	this.dragEnabled = false;
	
}


clickDown(cellModel:CellModel) {

	this.becomeActive(null);
	this.events.service.publish(new CellModelActivatedEvent(cellModel));

}


clickUp(cellModel:CellModel) {
	
	this.becomeInactive(null);
	this.events.service.publish(new CellModelDeactivatedEvent(cellModel));	  

}


dragEnd(cellModel:CellModel) {
    
    console.log("[UI] CellModelComponent::dragEnd()");
    this.becomeInactive(null);
    
}

isCompatibleWith(element:FamilyMember): boolean {
	return this.node.data.matches(element);
}


getThumb():string {
	return (this.node.data.thumb=='DEFAULT') ? "assets/images/cell-thumb.svg" : this.node.data.thumb;
}

}
