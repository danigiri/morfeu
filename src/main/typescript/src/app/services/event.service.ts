/* Credit for original idea should go to
*  http://www.processinginfinity.com/weblog/2016/08/18/MessageBus-Pattern-in-Angular2-TypeScript
*  As there is no license on the site we should be ok. It has been modified and streamlined from the original code.
*/

import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";
import { map } from 'rxjs/operators';

import { MorfeuEvent } from '../events/morfeu-event.class';

interface _Event {
	channel: string;
	data: any;
}

@Injectable()
export class EventService {

private events$: Map<string, Subject<_Event>> = new Map<string, Subject<_Event>>();
private eventCounter = 0;


/** @param event publish this event */
public publish(event: MorfeuEvent) {

	const k = event.eventName;
	console.debug("\tSending event "+k+" -> ("+event.toString()+")");
	this.subject(k).next({ channel: k, data: event });
	this.eventCounter++;

}


promiseToPublish(event: MorfeuEvent) {
	Promise.resolve(null).then(() => {
		const k = event.eventName;
		console.debug("\tSending event "+k+" -> ("+event.toString()+")");
		this.subject(k).next({ channel: k, data: event });
		this.eventCounter++;
	});
}


public of<T extends MorfeuEvent>(type_: Constructor<T>): Observable<T> {

	const k = new type_().eventName;
	//console.debug("\tSubscribing to event "+k);
	return this.subject(k).pipe(map(m => m.data));

}


public eventCount(): number {
	return this.eventCounter;
}


public subscriptionCount(): number {

	let count = 0;
	this.events$.forEach((s: Subject<_Event>) => count +=s.observers.length);

	return count;
}


public toString = (): string => {

	let out = "{\n";
	this.events$.forEach((s: Subject<_Event>, k: string) => out+='"'+k+'":'+(s.observers.length+',').padStart(3)+"\n");

	return out+"\n}";
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

