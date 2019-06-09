// STATUS . COMPONENT . TS
import { Component, OnInit, OnDestroy } from "@angular/core";
import { Subscription } from "rxjs";
import { trigger, state, style, animate, transition } from "@angular/animations";

import { EventService } from "./services/event.service";
import { StatusEvent } from "./events/status.event";

@Component({
	moduleId: module.id,
	selector: "status",
	template: `<div id="status"
					[@visibilityChanged]="visibility"
					class="progress"
					(@visibilityChanged.done)="animationComplete($event)">
				<!-- FIXME: for some reason if using a variable percentage we crash -->
				<div *ngFor="let s of statuses"
					class="progress-bar progress-bar-info"
					style="width: 25%; min-width: 5em;">
					{{s.message}} {{s.percentage}}%
				</div>
			</div>
		`,
	styles: [`
		#status {}
	`],
	animations: [
		trigger("visibilityChanged", [
			state("shown" , style({ opacity: 1 })),
			state("hidden", style({ opacity: 0 })),
			transition("shown => hidden", animate("2s")),
			transition("hidden => shown", animate("0.3s"))
		])

	]
})

export class StatusComponent implements OnInit, OnDestroy {

readonly MAX_BARS = 4;
private readonly HIDDEN = 0;
private readonly ANIMATING_IN = 1;
private readonly SHOWN = 2;
private readonly ANIMATING_OUT = 3;

visibility = "hidden";
state = this.HIDDEN;
statuses: StatusEvent[];		// what is shown
pendingStatuses: StatusEvent[]; // what is pending to be shown given that statuses is full
eventSubscription: Subscription;

constructor(private eventService: EventService) {}

ngOnInit() {

	console.log("StatusComponent::constructor()");
	this.statuses = new Array();
	this.pendingStatuses = new Array();

	this.eventSubscription = this.eventService.of( StatusEvent ).subscribe( s => {
		// console.log("-> status component gets new status '"+s.message+"' ["+s.percentage+"]");
		this.newStatusReceived(s);
	});

}

protected newStatusReceived(s: StatusEvent) {

	switch (this.state) {
		case this.HIDDEN:		// initial state, so we add the status and start show animation
			this.addStatus(s);
			this.showAnimation();
			break;
		case this.ANIMATING_IN: // we're animating to show state, add new and cleanup, this means that we may
			this.addStatus(s);	// be clearing status events that are faster than the 'in animation though
			this.statuses = this.newStatusesAfterCleanup();
			break;
		case this.SHOWN:		// we're in full visible state, we add new status and cleanup, and then we
			this.addStatus(s);	// stay in this state if there are statuses not done, or go hiding otherwise
			const newStatuses = this.newStatusesAfterCleanup();
			if (newStatuses.length==0) {
				// we leave status as is so completed statuses are shown during animating out
				this.hideAnimation();
			} else {
				// console.log("\t StatusComponent::newStatusReceived() --> keep SHOWN");
				this.statuses = newStatuses;	// keep showing up-to-date-statuses
			}
			break;
		case this.ANIMATING_OUT: // we got new statuses, restart animation and go back to animating in state
			// console.log("\t StatusComponent::newStatusReceived() --> ANIMATING_OUT");
			this.addStatus(s);
			this.statuses = this.newStatusesAfterCleanup();
			this.showAnimation();
			break;
	}
}

protected addStatus(newStatus: StatusEvent) {

	let status = this.normaliseStatus(newStatus);
	// console.log("\t StatusComponent::addStatus('"+status.message+"', "+status.percentage+")");

	// The extra check is to avoid merging states that are the same but unrelated, to handle the following
	// event sequence: a(10), a(5), a(100), a(100)
	// As this is a series of two 'a' events with 10% and 100% plus another of two of 5% and 100% that are
	// interleaved in time
	const i = this.statuses.findIndex(s => s.message===status.message && s.percentage<status.percentage);
	if (i==-1) {	// this is a new status

		// console.log("\t StatusComponent::addStatus(%s, %i) new[%i]" ,status.message, status.percentage, i);
		status = this.adaptToBarSize(status);
		if (this.statuses.length==this.MAX_BARS) {
			this.addToPendingStatuses(status);
		} else {
			this.statuses.push(status);
		}

	} else {		// this is an update to an existing status

		// console.log("\t StatusComponent::addStatus(%s, %i) update[%i]", status.message,status.percentage,i);
		status = this.adaptToBarSize(status);
		this.statuses[i] = status;		

	}
	// console.log("\t StatusComponent::addStatus(%s, %i)" ,status.message, status.percentage);
		

}


// To make sure the cleaning is not out of synch with the bindings we use a promise here, as we do not know
// if the animation callback is called in a spot where this is affected, errors in the console confirm it
// is indeed the case, so we delay changing the bindings in a promise
animationComplete($event) {

	Promise.resolve(null).then(() => {
	// console.log("\t StatusComponent::animationComplete(%s, %s)", $event.fromState, $event.toState);
	if (this.state===this.ANIMATING_IN) {
		const newStatuses = this.newStatusesAfterCleanup();
		if (newStatuses.length==0) {
			// we leave status as is so completed statuses are shown during animating out
			// console.log("\t StatusComponent::animationComplete() --> ANIMATING_OUT");
			this.hideAnimation();
		} else {
			console.log("\t StatusComponent::animationComplete() --> SHOWN");
			this.state = this.SHOWN;
			this.statuses = newStatuses;
		}
	} else if (this.state==this.ANIMATING_OUT) {
		// console.log("\t StatusComponent::animationComplete() --> HIDDEN");
		this.statuses = Array();	// clear statuses for good and we're back to initial state
		this.state = this.HIDDEN;
	} else {
	   // we apparently receive the callback more than once, so we will get it in shown state, we ignore it
	   // console.log("\t StatusComponent::animationComplete() IN UNEXPECTED STATE (%s)", this.state);
	}
	});

}


protected newStatusesAfterCleanup(): StatusEvent[] {

	const newStatuses = this.statuses.filter( s => s.percentage<100 ); // >>
	let spaceForNewStatuses = this.MAX_BARS-this.statuses.length;
	if (spaceForNewStatuses>0) {
		while (spaceForNewStatuses-->0 && this.pendingStatuses.length>0) {
			newStatuses.push(this.pendingStatuses.splice(0,1)[0]);
		}
	}

	return newStatuses;

}


private showAnimation() {

	// console.log("\t StatusComponent::showAnimation() starts --> ANIMATING_IN");
	this.visibility = "shown";
	this.state = this.ANIMATING_IN;

}



private hideAnimation() {

	// console.log("\t StatusComponent::hideAnimation() starts --> ANIMATING_OUT");
	this.visibility = "hidden";
	this.state = this.ANIMATING_OUT;

}


private normaliseStatus(s: StatusEvent) {

	let percent = (s.percentage===null || s.percentage === undefined || s.percentage<0) ? 20 : s.percentage;
	percent = (percent>100) ? 100 : percent;

	return new StatusEvent(s.message, percent);

}


private adaptToBarSize(s: StatusEvent) {

	// normalise in respect to the bar
	//let percentage = Math.floor(s.percentage/this.MAX_BARS);
	// TODO: make the size a bit smaller depending on the percentage, in proportion to MAX / BAR SIZE

	return new StatusEvent(s.message, s.percentage);
}


private addToPendingStatuses(status: StatusEvent) {

	// see explanation for add above
	const i = this.pendingStatuses.findIndex(s => s.message===status.message && s.percentage>status.percentage);
	if (i===-1) {
		this.pendingStatuses[i] = status;
	} else {
		this.pendingStatuses.push(status);
	}

}


// TODO: move to event listener
ngOnDestroy() {
	this.eventSubscription.unsubscribe();
}

}

/*
 *	  Copyright 2018 Daniel Giribet
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
