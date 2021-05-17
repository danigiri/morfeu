// DEBUG . COMPONENT . TS

import { Component, Input, OnInit } from '@angular/core';

import { fromEvent, Observable } from 'rxjs';

import { EventListener } from '../../events/event-listener.class';
import { InfoModeEvent } from '../../events/info-mode.event';
import { EventService } from '../../services/event.service';

@Component({
	selector: 'debug',
	template: `
				<p *ngIf="info || show" class="card-text"
				>
				<small><strong>x</strong>:{{mouseX}},<strong>y</strong>:{{mouseY}}</small>
				<small>{{subscriptionBreakdown()}}</small>
					<small>
						<strong>#events: {{eventCounter()}}, #subscriptions: {{subscriptionCounter()}}</strong>
					</small>
				</p>
	`,
})

export class DebugComponent extends EventListener implements OnInit {

info = false;
mouseX: number;
mouseY: number;

@Input() show: boolean = false;
@Input() trackMouse: boolean = false;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {
	this.register(this.events.service.of<InfoModeEvent>(InfoModeEvent).subscribe(mode => this.info = mode.active));
	if (this.trackMouse) {
		fromEvent(document.body, 'mousemove').subscribe({next:(e:MouseEvent) => this.mouseMove(e.pageX, e.pageY)});
	}
}


public eventCounter(): number {
	return this.events.service.eventCount();
}


public subscriptionCounter(): number {
	return this.events.service.subscriptionCount()
}

public subscriptionBreakdown():string {
	return this.events.service.toString();
}


private mouseMove(x: number, y: number) {

	this.mouseX = x;
	this.mouseY = y;

}


}

/*
 *    Copyright 2020 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
