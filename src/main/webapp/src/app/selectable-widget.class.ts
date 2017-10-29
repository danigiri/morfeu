/*
 *    Copyright 2017 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import { Subscription } from 'rxjs/Subscription';

import { Widget } from './widget.class';


export abstract class SelectableWidget extends Widget {
    
selected: boolean = false;          // are we selected?
 
protected selectionSubscription: Subscription;
protected selectionClearSubscription: Subscription;


abstract select(position:number);


clearSelection() {

    this.unsubscribeFromSelection();
    this.unsubscribeFromSelectionClear();
    this.selected = false;

}


abstract subscribeToSelection();


/** This cell is no longer eligible to be selected */
unsubscribeFromSelection() {

    if (this.selectionSubscription) {
        this.unsubscribe(this.selectionSubscription);
        this.selectionSubscription = undefined;
    }
    
}

abstract subscribeToSelectionClear();

unsubscribeFromSelectionClear() {
    
    if (this.selectionClearSubscription){
        this.unsubscribe(this.selectionClearSubscription);
        this.selectionClearSubscription = undefined;
    }
    
}


}