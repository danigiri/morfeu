/* Credit for original idea should go to
*  http://www.processinginfinity.com/weblog/2016/08/18/MessageBus-Pattern-in-Angular2-TypeScript
*  As there is no license on the site we should be ok.
*/

import { map, filter } from 'rxjs/operators';

import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";

import { Event } from '../events/event.interface';


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


public publish(event: Event): void {

	const channel_ = event.name();
	//console.debug("\tSending event "+channel_+" -> ("+event.toString()+")");
	this.event$.next({ channel: channel_, data: event });

}


public of<T extends Event>(eventType: { new(...args: any[]): T }): Observable<T> {

	const channel_ = Object.create(eventType.prototype).name();	// HACK
	//console.debug("\tSubscribing to event "+channel_);

	// this is ripe for optimization when we need it, hashing on the channel name for instance
	return this.event$.pipe(filter(m => m.channel===channel_), map(m => m.data));

}


}