// SELECTABLE - WIDGET . CLASS . TS

import { Subscription } from "rxjs";

import { CellSelectionClearEvent } from "./events/cell-selection-clear.event";
import { EventListener } from "./events/event-listener.class";


export abstract class SelectableWidget extends EventListener {

selected = false;			// are we selected?

protected selectionSubscription: Subscription;
protected selectionClearSubscription: Subscription;


abstract select(position:number): void;


abstract subscribeToSelection(): void;


clearSelection() {

	this.unsubscribeFromSelection();
	this.unsubscribeFromSelectionClear();
	this.selected = false;

}


/** This cell is no longer eligible to be selected */
unsubscribeFromSelection() {

	if (this.selectionSubscription) {
		this.unsubscribe(this.selectionSubscription);
		this.selectionSubscription = undefined;
	}

}


subscribeToSelectionClear() {
	this.selectionClearSubscription = this.register(
											this.events.service.of<CellSelectionClearEvent>(CellSelectionClearEvent)
																.subscribe(() => this.clearSelection())
	);
}


unsubscribeFromSelectionClear() {

	if (this.selectionClearSubscription){
		this.unsubscribe(this.selectionClearSubscription);
		this.selectionClearSubscription = undefined;
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
