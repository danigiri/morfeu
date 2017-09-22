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
import { Widget } from './widget.class';

import { CellActivatedEvent } from './events/cell-activated.event';
import { CellDeactivatedEvent } from './events/cell-deactivated.event';
import { EventService } from './events/event.service';

@Component({
	moduleId: module.id,
	selector: 'cell-model',
	template: `
		<div id="{{node.data.id}}" 
		     class="cell-model-entry cell-model-level-{{node.level}}">
			<img *ngIf="node.data.thumb=='DEFAULT'; else thumb" 
				 src="assets/images/cell-thumb.svg" 
				 class="cell-model-thumb" 
				 [class.cell-model-active]="active" 
					/>
			<ng-template #thumb>
			<img src="{{node.data.thumb}}" 
			     class="cell-model-thumb" 
			     [class.cell-model-active]="active" 
			/>
			</ng-template>
			<span class="cell-model-name">{{ node.data.name }}</span>
		</div>
		`,

		styles:[`
				.cell-model-entry {}
				.cell-model-name {}
				.cell-model-desc {}
				.cell-model-thumb {}
				.cell-model-active {
					border: 1px solid #f00;
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

export class CellModelComponent extends Widget {

@Input() node: TreeNode;
@Input() index: number;
	
active:boolean = false;

constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	console.log("CellModelComponent::ngOnInit()");

	this.subscribe(this.events.service.of( CellDeactivatedEvent )
			.filter(deactivated => this.matchesWith(deactivated.cell))
			.subscribe( deactivated => {
				console.log("-> cell-model comp gets cell deactivated event for '"+deactivated.cell.name+"'");
				this.becomeInactive(deactivated.cell);
	}));

	this.subscribe(this.events.service.of( CellActivatedEvent )
			.filter(activated => this.matchesWith(activated.cell))
			.subscribe( activated => {
				console.log("-> cell-model component gets cell activated event for '"+activated.cell.name+"'");
				this.becomeActive(activated.cell);
	}));
	
}


becomeInactive(cell: Cell) {

	console.log("[UI] CellModelComponent::becomeInactive()");
	this.active = false;
	
}


becomeActive(cell: Cell) {

	console.log("[UI] CellModelComponent::becomeActive()");
	this.active = true;
	
}


matchesWith(cell:Cell): boolean {
    return cell.name==this.node.data.name && cell.cellModelURI==this.node.data.URI
}

}
