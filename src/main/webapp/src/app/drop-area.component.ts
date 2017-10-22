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
import { CellModel } from './cell-model.class';
import { Widget } from './widget.class';

import { CellActivatedEvent } from './events/cell-activated.event';
import { CellDeactivatedEvent } from './events/cell-deactivated.event';
import { CellDropEvent } from './events/cell-drop.event';
import { CellModelActivatedEvent } from './events/cell-model-activated.event';
import { CellModelDeactivatedEvent } from './events/cell-model-deactivated.event';
import { EventService } from './events/event.service';


@Component({
	moduleId: module.id,
	selector: 'drop-area',
	template: `
			<div class="drop-area" 
				 [class.drop-area-active]="active" 
				 [class.drop-area-inactive]="!active"
				 dnd-droppable
				 [dropEnabled]="active"
				 (onDropSuccess)="dropSuccess($event)"				 
				 ><small>{{position}}</small></div>
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

active: boolean = false;


constructor(eventService: EventService) {

    super(eventService);   

}
	

ngOnInit() {
    
    console.log("DropAreaComponent::ngOnInit()");
    
    // we check for null of parent as we're not getting the binding set at the beginning for some reason
    // IDEA: we could use the function of the drop enabled (gets cell as input) though it's less interactive
    this.subscribe(this.events.service.of( CellDeactivatedEvent )
            .subscribe(deactivated => {
                if (this.matchesCell(deactivated.cell)) {
                    //console.log("-> drop-area comp gets cell deactivated event for '"+deactivated.cell.name+"'");
                    this.becomeInactive();
                }
    }));
 
    this.subscribe(this.events.service.of( CellActivatedEvent )
            .subscribe( activated => {
                if (this.parent && this.parent.canAdopt(activated.cell)) {
                    //console.log("-> drop-area component '"+this.parent.getAdoptionName()+"' gets cell activated event for '"+activated.cell.name+"'");
                    this.becomeActive();
                }
    }));
    
    this.subscribe(this.events.service.of( CellModelDeactivatedEvent )
            .subscribe( d => {
                if (this.matchesCellmodel(d.cellModel)) {
                    //console.log("-> drop comp gets cellmodel deactivated event for '"+d.cellModel.name+"'");
                    this.becomeInactive();
                }
    }));
    
    this.subscribe(this.events.service.of( CellModelActivatedEvent )
            .subscribe( a => {
                if (this.matchesCellmodel(a.cellModel)) {
                    console.log("-> drop comp gets cellmodel activated event for '"+a.cellModel.name+"'");
                    this.becomeActive();
                }
    }));
    
    
}


becomeInactive() {
    this.active = false;
}


becomeActive() {
    this.active = true;
}


matchesCell(cell:Cell): boolean {
    return this.parent && this.parent.canAdopt(cell);
}


matchesCellmodel(cellModel:CellModel):boolean {
    return this.parent && this.parent.canAdopt(cellModel);
}

/** we drop here as we are only droppeable if we are active, and that's model validated */
dropSuccess($event: any) {

    console.log("[UI] DropAreaComponent::dropSuccess("+$event.dragData.URI+")");
    this.events.service.publish(new CellDropEvent($event.dragData, this.parent, this.position));
    
}

}
