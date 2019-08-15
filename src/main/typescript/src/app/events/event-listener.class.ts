// EVENT - LISTENER . CLASS . TS

import {OnDestroy} from "@angular/core";
import {Subscription} from "rxjs";

import {EventService} from "../services/event.service";
import {RemoteEventService} from "../services/remote-event.service";
import {Events} from "./events.class";


export class EventListener implements OnDestroy {

protected events: Events;
private subscriptions: Subscription[];


constructor(private eventService: EventService, private remoteEventService?: RemoteEventService) {

	this.events = new Events(this.eventService, this.remoteEventService);
	this.subscriptions = [];

}


// at some point we will have to handle unsubscriptions more effectively
subscribe(s: Subscription): Subscription {

	this.subscriptions.push(s);
	return s;

}


unsubscribe(s: Subscription) {

	if (!s) {
		console.error("Trying to unsubscribe an undefined subscription");
	}
	// unsusbscribe and then remove this subscription from the list
	s.unsubscribe();
	this.subscriptions = this.subscriptions.filter(sub => sub!==s);

}


subscriptionCount(): number {
	return this.subscriptions.length;
}


ngOnDestroy() {
	this.subscriptions.forEach(s => s.unsubscribe());
}


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
