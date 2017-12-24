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


import { Component, OnInit } from "@angular/core";

import { CellModel } from "../cell-model.class";

import { Widget } from "../widget.class";

import { CellActivatedEvent } from "../events/cell-activated.event";
import { CellDeactivatedEvent } from "../events/cell-deactivated.event";
import { CellModelActivatedEvent } from "../events/cell-model-activated.event";
import { CellModelDeactivatedEvent } from "../events/cell-model-deactivated.event";
import { EventService } from "../events/event.service";


@Component({
	moduleId: module.id,
	selector: 'cell-model-info',
	template: `
		<div id="cell-model-info" class="card mt-2" *ngIf="cellModel">
		</div>
			   `,
	styles:[`
			#cell-model-info {}
	`]
})

export class CellModelInfoComponent extends Widget implements OnInit {
	
cellModel: CellModel;
	
constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {
	
	this.subscribe(this.events.service.of( CellActivatedEvent )
			.subscribe( activated => this.showCellModelInformation()
	));
	this.subscribe(this.events.service.of( CellActivatedEvent )
			.subscribe( activated => this.hidellCellModelInformation()
	));
	this.subscribe(this.events.service.of( CellModelActivatedEvent )
			.subscribe( activated => this.showCellModelInformation()
	));
	this.subscribe(this.events.service.of( CellModelDeactivatedEvent )
			.subscribe( activated => this.hidellCellModelInformation()
	));

}

showCellModelInformation() {
	
}

hidellCellModelInformation() {
	
}
}