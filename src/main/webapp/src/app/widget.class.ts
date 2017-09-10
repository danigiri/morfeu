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

import { OnDestroy } from '@angular/core';
import { Subscription }   from 'rxjs/Subscription';

import { EventService } from './events/event.service';
import { Events } from './events/events.class';


export class Widget implements OnDestroy {
    
protected events: Events;
private subscriptions: Subscription[];

protected constructor(private eventService: EventService) {

    this.events = new Events(eventService);
    this.subscriptions = [];

}


// at some point we will have to handle unsubscriptions more effectively
protected subscribe(s: Subscription): Subscription {
    
    this.subscriptions.push(s);
    return s;
    
}


protected unsubscribe(s:Subscription) {

   // unsusbscribe and then remove this subscription from the list
   s.unsubscribe();
   this.subscriptions = this.subscriptions.filter(sub => sub!==s);

}


ngOnDestroy() {
    this.subscriptions.forEach(s => s.unsubscribe());
}


}