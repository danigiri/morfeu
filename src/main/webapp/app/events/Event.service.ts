/* Credit should go to 
*  http://www.processinginfinity.com/weblog/2016/08/18/MessageBus-Pattern-in-Angular2-TypeScript
*  As there is no license on the site we should be ok
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

public publish<T>(event: T): void {
    const channel = (<any>event.constructor).name;
    this.event$.next({ channel: channel, data: event });
}

public of<T>(eventType: { new(...args: any[]): T }): Observable<T> {
    const channel = (<any>eventType).name;
    return this.event$.filter(m => m.channel === channel).map(m => m.data);
}

}