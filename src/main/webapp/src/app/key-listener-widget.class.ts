/*
 *    Copyright 2018 Daniel Giribet
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

import { KeyPressedEvent } from "./events/keypressed.event";
import { Widget } from "./widget.class";

export abstract class KeyListenerWidget extends Widget {
    
    
protected commandKeys: string[] = Array();
protected commandPressedSubscription: Subscription;
protected numberPressedSubscription: Subscription;
    
registerKeyPressedEvents() {
    
    this.commandPressedSubscription = this.subscribe(this.events.service.of( KeyPressedEvent )
            .filter( key => key.isCommand())
           .subscribe( key => {
               if (this.commandKeys.find( c => c===key.str)!=undefined) {
                   this.commandPressedCallback(key.str);
               } else {
                   this.commandNotRegisteredCallback(key.str);
               }
           })
    );
    
    
    
    this.numberPressedSubscription = this.subscribe(this.events.service.of( KeyPressedEvent )
            .filter( key => key.isNumber())
            .subscribe( key => this.numberPressedCallback(key.num))
    );

}

/** This will be called when a key registered in commandKeys is triggered */
abstract commandPressedCallback(command: string);

/** This will be called when a key is triggered that is not registered in commandKeys */
commandNotRegisteredCallback(command: string) {}

abstract numberPressedCallback(num: number);


unregisterKeyPressedEvents() {
        
        if (this.commandPressedSubscription) {
            this.unsubscribe(this.commandPressedSubscription);
        }
        if (this.numberPressedSubscription) {
            this.unsubscribe(this.numberPressedSubscription);
        }
        
}
    
    
}
