/*
 *	  Copyright 2018 Daniel Giribet
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

import { Component, Input, OnInit } from "@angular/core";

import { CellDocument } from "../cell-document.class";
import { Content } from "../content.class";
import { Model } from "../model.class";

import { SelectableWidget } from "../selectable-widget.class";

import { EventService } from "../events/event.service";

@Component({
	moduleId: module.id,
	selector: "snippet",
	template: `
		<a href="#" class="list-group-item list-group-item-action flex-column align-items-start snippet">
        		<div class="d-flex justify-content-between">
                  <h5 class="mb-1">{{snippet.name}}</h5>
				<cell *ngFor="let cell of snippet.content.children; let i=index" 
					[parent]="snippet.content" 
					[cell]="cell" [level]="0" 
					[position]="i"
					[snippet]="true"
				></cell>
            </div>
		</a>
`,
	styles:[`
	        .snippet {
	            /* border: 3px dashed #f00; */
	        }
	`]
})

export class SnippetComponent extends SelectableWidget implements OnInit {

@Input() model: Model;
@Input() snippet: CellDocument;


active: boolean = false;
dragEnabled: boolean = false;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {
	console.log("SnippetComponent::ngOnInit()");
}

becomeActive() {

	this.active = true;
	this.dragEnabled = true;

}


becomeInactive() {

	this.active = false;
	
}


//// SelectableWidget ////

select(position:number) {}


subscribeToSelection() {}

}