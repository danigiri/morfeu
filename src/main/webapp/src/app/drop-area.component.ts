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

import { Cell } from './cell.class';
import { CellHolder } from './cell-holder.interface';
import { Widget } from './widget.class';

import { CellActivatedEvent } from './events/cell-activated.event';
import { CellDeactivatedEvent } from './events/cell-deactivated.event';
import { EventService } from './events/event.service';


@Component({
	moduleId: module.id,
	selector: 'drop-area',
	template: `
			<div class="drop-area" 
				 [class.drop-area-active]="active" 
				 [class.drop-area-inactive]="!active">&nbsp;</div>
		`,
	styles:[`
				.drop-area {
	                padding-top: 1px;
	                padding-bottom: 1px;
                    border: 2px dotted #000;
                    border-radius: 5px;
				}
				.drop-area-active {
					border: 2px dotted #0f0;
					opacity: 0.8;
				}
				.drop-area-inactive {
					opacity: 0;
				}
			`],
	providers:[
	]
})


export class DropAreaComponent extends Widget implements OnInit {

@Input() parent: CellHolder;

active: boolean = false;;


constructor(eventService: EventService) {
    super(eventService);   
}
	

ngOnInit() {
    console.log("DropAreaComponent::ngOnInit()");
    
    this.subscribe(this.events.service.of( CellDeactivatedEvent )
            .subscribe(deactivated => {
                if (this.parent.canHaveAsChild(deactivated.cell)) {
                    console.log("-> drop-area comp gets cell deactivated event for '"+deactivated.cell.name+"'");
                    this.becomeInactive(deactivated.cell);
                }
    }));
 
    this.subscribe(this.events.service.of( CellActivatedEvent )
            .subscribe( activated => {
                if (this.parent.canHaveAsChild(activated.cell)) {
                    console.log("-> drop-area component gets cell activated event for '"+activated.cell.name+"'");
                    this.becomeActive(activated.cell);
                }
    }));
    
}


becomeInactive(cell:Cell) {

    console.log("[UI] DropAreaComponent::becomeInactive()");
    this.active = false;

}


becomeActive(cell: Cell) {

    console.log("[UI] DropAreaComponent::becomeActive()");
    this.active = true;
    
}

}
