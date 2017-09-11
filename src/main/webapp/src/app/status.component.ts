/*
 *	  Copyright 2017 Daniel Giribet
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

import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription }	  from 'rxjs/Subscription';
import { trigger, state, style, animate, transition } from '@angular/animations';
  
import { EventService } from './events/event.service';
import { StatusEvent } from './events/status.event';


@Component({
	moduleId: module.id,
	selector: 'status',
	template: `
			  <div [@visibilityChanged]="visibility" id="status" class="progress">
				  <!-- FIXME: for some reason if using a variable percentage we crash -->
				  <div *ngFor="let s of statuses" 
					  class="progress-bar progress-bar-info" 
					  style="width: 25%; min-width: 5em;">
					{{s.message}}
				  </div>
			  </div>  
		`,
	styles:[`
	 #status {}

	`],
	animations: [
		trigger('visibilityChanged', [
			state('shown' , style({ opacity: 1 })), 
			state('hidden', style({ opacity: 0 })),
			transition('shown => hidden', animate('2s')),
			transition('hidden => shown', animate('.1s'))
		])
	
	]
})

export class StatusComponent implements OnInit, OnDestroy {
 
MAX_BARS = 4;
	
visibility = 'hidden';
statuses: StatusEvent[];
pendingStatuses : StatusEvent[];
eventSubscription: Subscription;

constructor(private eventService: EventService) {}
	
	
ngOnInit() {
		
	//*ngIf="statuses.length!=0"
	console.log("StatusComponent::constructor()");
	this.statuses = new Array();
	this.pendingStatuses = new Array();
	
	this.eventSubscription = this.eventService.of( StatusEvent ).subscribe( s => {
		console.log("-> status component gets new status '"+s.message+"' ["+s.percentage+"]");
		this.updateStatus(s);
	});
	
}


protected updateStatus(s: StatusEvent) {

	console.log("\t StatusComponent::updateStatus('"+s.message+"', "+s.percentage+")");

	let status = this.normaliseStatus(s);

	let i = this.statuses.findIndex(s => s.message===status.message);
	if (i==-1) {	// this is a new status
		// we only add it if it's not complete
		if (status.percentage<100) {
			status = this.adaptToBarSize(status);
			if (this.statuses.length==this.MAX_BARS) {
				this.addToPendingStatuses(status);
			} else {
				this.statuses.push(status);
			}
		}
				
	} else {		// this is an update to an existing status

		if (status.percentage<100) {
			status = this.adaptToBarSize(status);
			this.statuses[i] = status;
		} else {
			this.statuses.splice(i,1);
			if (this.pendingStatuses.length>0) {
				this.statuses.push(this.pendingStatuses.splice(0,1)[0]);
			}
		}
		
	}

	if (this.statuses.length==0) {
		this.visibility = 'hidden';
		console.log("\t StatusComponent::updateStatus('"+status.message+"', "+status.percentage+") [hiding]");
	} else {
		this.visibility = 'shown';		  
		console.log("\t StatusComponent::updateStatus('"+status.message+"', "+status.percentage+") [showing]");
	}
	console.log("\t StatusComponent::updateStatus('"+status.message+"', "+status.percentage+") [FINISHED]");
	
}


private normaliseStatus(s: StatusEvent) {
	
	let percentage = (s.percentage==null || s.percentage == undefined || s.percentage<0) ? 10 : s.percentage;
	percentage = (percentage>100) ? 100 : percentage;

	return new StatusEvent(s.message, percentage);

}


private adaptToBarSize(s: StatusEvent) {
	
	// normalise in respect to the bar
	let percentage = Math.floor(s.percentage/this.MAX_BARS);
	
	return new StatusEvent(s.message, percentage);
}


private addToPendingStatuses(status: StatusEvent) {

	let i = this.pendingStatuses.findIndex(s => s.message===status.message);
	if (i==-1) {
		this.pendingStatuses[i] = status;
	} else {
		this.pendingStatuses.push(status);
	}

}

	
ngOnDestroy() {
	this.eventSubscription.unsubscribe();
}


}
