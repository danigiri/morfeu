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

private events$: Map<string, Subject<_Event>> = new  Map<string, Subject<_Event>>();
private eventCounter = 0;
private subscriptionCounter = 0;


public publish(event: MorfeuEvent): void {

	const k = event.eventName;
	//console.debug("\tSending event "+k+" -> ("+event.toString()+")");
	this.subject(k).next({ channel: k, data: event });
	this.eventCounter++;
}


public of<T extends MorfeuEvent>(type_: Constructor<T>): Observable<T> {

	const k = new type_().eventName;
	//console.debug("\tSubscribing to event "+k);
	return this.subject(k).map(m => m.data);

}


public incrementSubscriptions() {
	this.subscriptionCounter++;
}


public decrementSubscriptions() {
	this.subscriptionCounter--;
}


public subscriptionCount():number {
	return this.subscriptionCounter;
}


public eventCount(): number {
	return this.eventCounter;
}


private subject(k: string): Subject<_Event> {

	let e$ = this.events$.get(k);
	if (!e$) {
		e$ = new Subject<_Event>();
		this.events$.set(k, e$);
	}

	return e$;

}


}


interface Constructor<T extends MorfeuEvent> {
	new (...args: any[]): T;
}

