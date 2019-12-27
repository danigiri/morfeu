// COLLAPSIBLE . COMPONENT . TS

import {filter} from 'rxjs/operators';
import {Component, Input} from "@angular/core";

import {EventListener} from "../events/event-listener.class";
import {EventService} from "../services/event.service";
import {UXEvent} from "../events/ux.event";


@Component({
	moduleId: module.id,
	selector: 'collapsible',
	template: `
		<!-- TODO: have header be a template and not an attribute, so we can have more than text -->
		<div id="{{id_}}" class="card">
			<ng-container [ngSwitch]="headerSize">
				<ng-container *ngSwitchCase="1">
					 <h1 class="card-header {{headerClass}}">{{header}}</h1>
				</ng-container>
				<ng-container *ngSwitchCase="2">
					<h2 class="card-header {{headerClass}}">{{header}}</h2>
				</ng-container>
				<ng-container *ngSwitchCase="3">
					<h3 class="card-header {{headerClass}}">{{header}}</h3>
				</ng-container>
				<ng-container *ngSwitchCase="4">
					<h4 class="card-header {{headerClass}} {{toggleClass()}} btn-block collapsible-header"
						attr.aria-controls="{{id_}}-region" 
						attr.aria-expanded="!folded"
						role="button"
						(click)="toggle()"
						>
						 <ng-container *ngIf="folded"><img src="assets/images/open-iconic/caret-right.svg" alt="expand"/></ng-container>
						 <ng-container *ngIf="!folded"><img src="assets/images/open-iconic/caret-bottom.svg" alt="collapse"/></ng-container> 
						 {{header}}
					</h4>
				</ng-container>
				<ng-container *ngSwitchCase="5">
					<h5 class="card-header {{headerClass}}">{{header}}</h5>
				</ng-container>
			</ng-container>
			<div id="{{id_}}-region" role="region" class="card-body {{bodyClass}}" *ngIf="!folded">
				<ng-content></ng-content>
			</div>
		</div>
		`,
		styles:[`
				.folded {}
				.unfolded {}
				.collapsible-header {
					cursor: pointer;
				}
		`],
		providers: [
					]
})

export class CollapsibleComponent extends EventListener {

@Input() id_: string;
@Input() header: string;
@Input() headerClass: string = "";
@Input() headerSize: number = 4;
@Input() bodyClass: string = "";
@Input() folded: boolean;


constructor(eventService: EventService) {

	super(eventService);   

}


ngOnInit() {
	this.subscribe(this.events.service.of<UXEvent>(UXEvent)
			.pipe(	filter(e => e.type==UXEvent.TOGGLE_COLLAPSABLE),
					filter(e => e.payload && (e.payload==="*" || e.payload===this.id_)))
			.subscribe(() => this.toggle() )
	);
}


fold() {
	this.folded = true;
}


unfold() {
	this.folded = false;
}


toggle() {

	console.log("[UI] toggling collapsible '%s'", this.folded);
	this.folded = !this.folded;
}


toggleClass():string {
	return this.folded ? "folded" : "unfolded";
} 


}

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
