/* Credit for original idea should go to
*  http://www.processinginfinity.com/weblog/2016/08/18/MessageBus-Pattern-in-Angular2-TypeScript
*  As there is no license on the site we should be ok.
*/

import { map, filter } from 'rxjs/operators';

import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";

import { MorfeuEvent } from '../events/morfeu-event.class';

interface _Event {
	channel: string;
	data: any;
}

@Injectable()
export class EventService {

private event$: Subject<_Event>


constructor() {
	this.event$ = new Subject<_Event>();
}


public publish(event: MorfeuEvent): void {

	const channel_ = event.eventName;
	console.debug("\tSending event "+channel_+" -> ("+event.toString()+")");
	this.event$.next({ channel: channel_, data: event });

}


public of<T extends MorfeuEvent>(channel_: string): Observable<T> {

	console.debug("\tSubscribing to event "+channel_);	// we use a string to avoid problems with
														// dynamic minimised class names

	// this is ripe for optimization when we need it, hashing on the channel name for instance
	return this.event$.pipe(filter(m => m.channel===channel_), map(m => m.data));

}


}