// SNIPPET . COMPONENT . TS

import { Component, Input, OnInit, ViewChildren } from "@angular/core";

import { CellDocument } from "../cell-document.class";
import { Content } from "../content.class";
import { Model } from "../model.class";

import { CellComponent } from "./cell.component";
import { SelectableWidget } from "../selectable-widget.class";
import { SnippetsListComponent } from "./snippets-list.component";

import { CellSelectEvent } from "../events/cell-select.event";
import { EventService } from "../services/event.service";

@Component({
	moduleId: module.id,
	selector: "snippet",
	template: `
		<a href="#" class="list-group-item list-group-item-action flex-column align-items-start snippet"
			[attr._position]="position"
			[class.snippet-selected]="selected"
		>
			<div class="d-flex justify-content-between">
				<h5 class="mb-1 snippet-name">{{snippet.name}}</h5>
				<!-- at the moment, snippets only contain one node-->
				<cell 
					[cell]="snippet.content" 
					[level]="0"
					[position]="0"
					[snippet]="true"
				></cell>
			</div>
		</a>
	`,
	styles: [`
	        .snippet {
	            /* border: 3px dashed #f00; */
	        }
			 .snippet-selected {
					border: 1px dashed #00f;
					border-radius: 5px;
			}
			.snippet-name {}
	`]
})

export class SnippetComponent extends SelectableWidget implements OnInit {

@Input() model: Model;
@Input() snippet: CellDocument;
@Input() position: number;
@Input() parent: SnippetsListComponent;	// so we can add ourselves to the children list

@ViewChildren(CellComponent) children: CellComponent[];

// active = false;
dragEnabled = false;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	console.log("SnippetComponent::ngOnInit()");

	// the idea here is that we set ourselves in the parent children list as this component is async
	this.parent.snippetComponents.push(null);				// as long as the array ops are reentrant
	this.parent.snippetComponents[this.position] = this;	// this will set the reference OK

}


//// SelectableWidget ////

select(position: number) {

	if (position===this.position) {

		// we were waiting for a selection and we've matched the position, so we select ourselves
		// and unsubscribe from selection as we are not eligible anymore
		console.log("[UI] SnippetComponent::select("+this.snippet.name+"("+this.position+"))");
		this.selected = true;
		this.unsubscribeFromSelection();
		this.children.forEach( c => c.subscribeToSelection());
	}

}


subscribeToSelection() {

	this.selectionSubscription = this.subscribe(this.events.service.of<CellSelectEvent>('CellSelectEvent')
				.subscribe(cs => this.select(cs.position))
	);
	this.subscribeToSelectionClear();  // if we are selectable we are also clearable

}


//unsubscribeFromSelection() {}

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
