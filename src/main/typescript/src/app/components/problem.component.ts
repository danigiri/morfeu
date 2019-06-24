// PROBLEM . COMPONENT . TS

import { Component, OnInit } from '@angular/core';
import { Subscription }	  from 'rxjs';

import { EventListener } from '../events/event-listener.class';
import { ProblemEvent } from '../events/problem.event';
import { EventService } from '../services/event.service';


@Component({
	moduleId: module.id,
	selector: 'problem',
	template: `
		<div id="problem" *ngIf="problem" class="alert alert-danger" role="alert">{{problem}}</div>	   
		`,
	styles:[`
	 #problem {}
	`]
})
//`


export class ProblemComponent extends EventListener implements OnInit	{
	
problem: any;

constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {
	console.log("ProblemComponent::ngOnInit()");
	this.subscribe(this.events.service.of( ProblemEvent ).subscribe( p => {
		if ( p.message != null ) {
			console.log( "-> ProblemComponent gets problem"+p.message );
		} else {
			console.log( "-> ProblemComponent clears problem" );
		}
			this.problem = p.message;
	}));
	
}

}