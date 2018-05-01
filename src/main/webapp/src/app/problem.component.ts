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

import { Component, OnInit } from '@angular/core';
import { Subscription }	  from 'rxjs/Subscription';

import { Widget } from './widget.class';

import { EventService } from './events/event.service';
import { ProblemEvent } from './events/problem.event';


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


export class ProblemComponent extends Widget implements OnInit	{
	
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