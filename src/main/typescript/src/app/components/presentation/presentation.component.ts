// PRESENTATION . COMPONENT . TS (NOT USED AT THE MOMENT)

import {AfterViewInit, Component, Inject, Input, OnChanges, SimpleChanges, SimpleChange} from '@angular/core';
import {Observable, Subject} from 'rxjs';


import {RemoteDataService} from '../../services/remote-data.service';

import {Cell} from '../../cell.class';
import {CellModel} from '../../cell-model.class';

import {EventListener} from '../../events/event-listener.class';
import {CellChangedEvent} from '../../events/cell-changed.event';
import {EventService} from '../../services/event.service';

@Component({
	moduleId: module.id,
	selector: "presentation",
	template: `
		<!-- TODO: add inner html type? -->
		<iframe *ngIf="this.getPresentationType()==='IFRAME'"
			class="cell cell-html"
			[src]="getPresentation() | safe: 'resourceUrl' "
		></iframe>
		<div class="cell cell-html" *ngIf="this.getPresentationType()==='HTML'">
			<div [innerHTML]="innerHTML$ | async | safe: 'html'"></div>
		</div>
		
	`,
})
// sets the ui to be too slow as the iframe blocks rendering
//	changeDetection: ChangeDetectionStrategy.OnPush

export class PresentationComponent extends EventListener implements AfterViewInit {

//private readonly CHANGED_LIMIT = 20;
private readonly UPDATE_MS = 200;


// if showing a cell with values or we are showing a cellmodel
@Input() cell?: Cell;
@Input() cellModel?: CellModel;

presentation: String;
innerHTML$?: Subject<String>;

private cellChangedCounter = 0;	// we use this to keep track


constructor(eventService: EventService, @Inject("RemoteDataService") private presentationService: RemoteDataService) {
	super(eventService);
}


ngAfterViewInit() {

	this.innerHTML$ = new Subject();
	this.updateInnerHTMLPresentation();

	this.subscribe(this.events.service.of(CellChangedEvent)
			.debounceTime(200)	// the ideal here would be to send one in N or the last one after a timeout
			//.pipe(debounce(() => timer(1000)))
			//.filter(() => (++this.cellChangedCounter) % this.CHANGED_LIMIT == 0)
			.subscribe(() => {
//				console.debug('.');
				this.updateInnerHTMLPresentation();	// FIXME: there is a potential race condition where this
			}));									// method calls pile up on each other on  the get text
}


private getPresentationType(): string {

	const cellModel = this.cell === undefined ? this.cellModel : this.cell.cellModel;

	return cellModel.getPresentationType();

}

private getPresentation(): string {
	return this.cell===undefined ? this.cellModel.getPresentation() : this.cell.getPresentation();
}


private updateInnerHTMLPresentation() {
	
const presentationURL = this.getPresentation(); //'/morfeu/dyn/preview/html/aaa;color=ff00ff';

	//console.debug('Getting presentation from %s', presentationURL);
	this.presentationService.getText(presentationURL).subscribe(
			html => {
				Promise.resolve(null)
						.then(() => this.innerHTML$.next(html));
			},
			error => {}
	);

}


}

/*
 *    Copyright 2019 Daniel Giribet
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
