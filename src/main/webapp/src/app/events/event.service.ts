/* Credit for original idea should go to 
*  http://www.processinginfinity.com/weblog/2016/08/18/MessageBus-Pattern-in-Angular2-TypeScript
*  As there is no license on the site we should be ok.
*  Added support for subtypes of events
*/


import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs/Rx";


interface Event {
	channel: string;
	data: any;
}

@Injectable()
export class EventService {

private event$: Subject<Event>
	
constructor() {
	this.event$ = new Subject<Event>();
}


public publish<T>(event: T, subtype?: string): void {

	const type_ = (<any>event.constructor).name;
	const channel_ = (subtype) ? type_+subtype : type_;
	//console.log("\tSending event "+channel_+" -> ("+event.toString()+")");
	this.event$.next({ channel: channel_, data: event });

}


public of<T>(eventType: { new(...args: any[]): T }): Observable<T> {

	// by using starts with, we can have hierarchies of event types
	const channel_ = (<any>eventType).name;
	//console.log("\tSubscribing to event "+channel_);
	
	return this.event$.filter(m => m.channel.startsWith(channel_, 0)).map(m => m.data);

}


}