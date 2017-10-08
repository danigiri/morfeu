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

import { FamilyMember } from './family-member.interface';
import { Cell } from './cell.class';
import { Widget } from './widget.class';

import { CellActivatedEvent } from './events/cell-activated.event';
import { CellDeactivatedEvent } from './events/cell-deactivated.event';
import { CellModelActivatedEvent } from './events/cell-model-activated.event';
import { CellModelDeactivatedEvent } from './events/cell-model-deactivated.event';
import { EventService } from './events/event.service';


@Component({
	moduleId: module.id,
	selector: 'drop-area',
	template: `
			<div class="drop-area" 
				 [class.drop-area-active]="active_" 
				 [class.drop-area-inactive]="!active_"
				 dnd-droppable
				 [allowDrop]="isActive"
				 (onDropSuccess)="dropSuccess($event)"				 
				 >{{position}}</div>
		`,
	styles:[`
				.drop-area {
	                padding-top: 10px;
	                padding-bottom: 10px;
				}
				.drop-area-active {
                    padding-top: 8px;
                    padding-bottom: 8px;
					border: 2px dotted #0f0;
					opacity: 0.8;
				}
				.drop-area-inactive {
					opacity: 0.01;
				}
			`],
	providers:[
	]
})


export class DropAreaComponent extends Widget implements OnInit {

@Input() parent: FamilyMember;
@Input() position: number;

active_: boolean = false;


constructor(eventService: EventService) {

    super(eventService);   

}
	

ngOnInit() {
    
    console.log("DropAreaComponent::ngOnInit()");
    
    // we check for null of parent as we're not getting the binding set at the beginning for some reason
    
    this.subscribe(this.events.service.of( CellDeactivatedEvent )
            .subscribe(deactivated => {
                if (this.parent && this.parent.canAdopt(deactivated.cell)) {
                    console.log("-> drop-area comp gets cell deactivated event for '"+deactivated.cell.name+"'");
                    this.becomeInactive();
                }
    }));
 
    this.subscribe(this.events.service.of( CellActivatedEvent )
            .subscribe( activated => {
                if (this.parent && this.parent.canAdopt(activated.cell)) {
                    console.log("-> drop-area component '"+this.parent.getAdoptionName()+"' gets cell activated event for '"+activated.cell.name+"'");
                    this.becomeActive();
                }
    }));
    
    this.subscribe(this.events.service.of( CellModelDeactivatedEvent )
            .subscribe( d => {
                if (this.parent && this.parent.canAdopt(d.cellModel)) {
                    console.log("-> drop comp gets cellmodel deactivated event for '"+d.cellModel.name+"'");
                    this.becomeInactive();
                }
    }));
    
    this.subscribe(this.events.service.of( CellModelActivatedEvent )
            .subscribe( a => {
                if (this.parent && this.parent.canAdopt(a.cellModel)) {
                    console.log("-> drop comp gets cellmodel activated event for '"+a.cellModel.name+"'");
                    this.becomeActive();
                }
    }));
    
    
}


becomeInactive() {
    console.log("[UI] DropAreaComponent::becomeInactive()");
    //this.active_ = false;
    this.active_ = true;
}


becomeActive() {
    this.active_ = true;
}


isActive():boolean {
    //console.log("[UI] DropAreaComponent::isActive("+this.active_+")");
    if (this.active_===undefined) {
        this.active_=false;
    }
    //   return true;
    return this.active_;
}


dropSuccess($event: any) {

    console.log("[UI] DropAreaComponent::dropSuccess("+$event.dragData.URI+")");

}

}
