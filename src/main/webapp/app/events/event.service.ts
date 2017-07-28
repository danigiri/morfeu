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


public publish<T>(event: T, subtype_?: string): void {

    const type_ = (<any>event.constructor).name+"^";
    const channel = (subtype_) ? type_+subtype_ : type_;
    console.log("\tSending event "+channel);
    this.event$.next({ channel: channel, data: event });

}


public of<T>(eventType: { new(...args: any[]): T }, subtype_?: string): Observable<T> {

    // if we are interested in all subtypes of a type of event, we also use starts with
    const type_ = (<any>eventType).name+"^";
    const channel = (subtype_) ? type_+subtype_ : type_;
    console.log("\tSubscribing to event "+channel);
    
    return this.event$.filter(m => m.channel.startsWith(channel,0)).map(m => m.data);

}




}