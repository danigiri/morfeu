// SNIPPETS - LIST . COMPONENT . TS

import { Component, AfterViewInit, Inject, Input, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';

import { NgbAccordion, NgbPanel, NgbPanelChangeEvent } from '@ng-bootstrap/ng-bootstrap';

import { RemoteDataService } from '../../services/remote-data.service';
import { RemoteObjectService } from '../../services/remote-object.service';

import { Configuration } from '../../config/configuration.class';

import { CellDocument } from '../../cell-document.class';
import { Content, ContentJSON } from '../../content.class';
import { Model } from '../../model.class';

import { KeyListenerWidget } from '../../key-listener-widget.class';
import { SnippetComponent } from './../snippet.component';

import { CellActivateEvent } from '../../events/cell-activate.event';
import { CellSelectEvent } from '../../events/cell-select.event';
import { CellSelectionClearEvent } from '../../events/cell-selection-clear.event';
import { SnippetDocumentRequestEvent } from '../../events/snippet-document-request.event';
import { StatusEvent } from '../../events/status.event';
import { EventService } from '../../services/event.service';
import { SnippetsDisplayEvent } from 'src/app/events/snippets-display.event';
import { CellModel } from 'src/app/cell-model.class';

@Component({
	selector: "snippets",
	template: `
		<div id="snippets" class="list-group">
			<ngb-accordion 
				*ngIf="snippetCategoryNames.length>0" 
				[closeOthers]="true"
			>
				<ngb-panel *ngFor="let c of snippetCategoryNames;let i=index" [id]="c" [title]="c">
					<ng-template ngbPanelContent>
						<snippet *ngFor="let s of snippetsByCategory.get(c); let j=index"
							class="list-group-item"
							[snippet]="s"
							[model]="normalisedModel"
							[position]="j"
							[parent]="this"
							(panelChange)="beforeToggle($event)"
						></snippet>
					</ng-template>
				</ngb-panel>
			</ngb-accordion>
		</div>
	`,
	styles: [`
		#snippets {}
	`]
})

export class SnippetsListComponent extends KeyListenerWidget implements AfterViewInit {

@Input() model: Model;

snippetStubs: CellDocument[];	 // stubs that come from the catalogue

@ViewChild(NgbAccordion) accordion: NgbAccordion;
snippetComponents: SnippetComponent[];				// this list is maintained manually and updated by the children

normalisedModel: Model;

snippetCategoryNames: string[] = [];
snippetsByCategory: Map<string, CellDocument[]>; // snippets grouped by categories
currentCategory: string;

protected override commandKeys: string[] = ["a"];		// activation keybinding
private snippetCategorySelectingMode = false;	// are we selecting categories
private snippetSelectingMode = false;			// or snippets?

protected snippetSubs: Subscription;


constructor(eventService: EventService,
			@Inject("RemoteJSONDataService") private snippetDocumentService: RemoteDataService,
			@Inject("SnippetContentService") private snippetContentService: RemoteObjectService<Content, ContentJSON>) {
	super(eventService);
}


ngAfterViewInit() {

	console.log("SnippetsListComponent::ngAfterViewInit()");
	this.snippetComponents = [];

	this.register(this.events.service.of<SnippetsDisplayEvent>(SnippetsDisplayEvent)
	.subscribe(display => this.fetchSnippets(display.snippets))
);
	//Promise.resolve(null).then(() => this.fetchSnippets());

}


// fetch all snippet documents
public fetchSnippets(stubs: CellDocument[]) {
	if (stubs.length>0) {
		this.snippetStubs = stubs;
		// we copy and normalise the model as we will link it with the snippets
		let MODEL:Model = Object.create(Model.prototype); // to simulate a static call
		const json = this.model.toJSON();
		this.normalisedModel = MODEL.fromJSON(json);
		this.normalisedModel.normaliseReferences();
		// we initialise the snippet structures
		this.snippetCategoryNames = [];
		this.snippetsByCategory = new Map<string, CellDocument[]>();
		this.snippetStubs.map(s => s.kind).forEach(c => {
															if (!this.snippetsByCategory.has(c)) {
																this.createSnippetCategory(c)
															}
		});

		this.events.service.publish(new StatusEvent("Fetching snippets"));
		// FIXME: WHY DO WE ASSIGN THE REGISTRATION TO THE STUBS? AND WHY WE REQUEST ONE ONLY?
		//this.snippetSubs = 
		this.register(this.events.service.of<SnippetDocumentRequestEvent>(SnippetDocumentRequestEvent)
										.subscribe(
											req => this.loadSnippetDocument(this.snippetStubs[req.index], req.index)
										)
		);
		this.events.service.publish(new SnippetDocumentRequestEvent(0));
	} else {
		console.warn('Empty snippets list from catalogue');
	}

}


public currentSnippets(): CellDocument[] {
	return this.snippetsByCategory.get(this.currentCategory) ?? [];
}


public beforeToggle($event: unknown) {
	const $e = $event as NgbPanelChangeEvent;
	if ($e.nextState) {
		this.currentCategory = $e.panelId;
	} else if (this.snippetSelectingMode || this.snippetCategorySelectingMode) {
		this.deactivateSnippetSelectingMode();		// if we close the tab and we were in selection mode we are not
													// able to select the snippet children anymore
		this.snippetCategorySelectingMode = true;	// but we want to be able to toggle the categories now

	}

}


private createSnippetCategory(category: string) {

	this.snippetCategoryNames.push(category);
	this.snippetsByCategory.set(category, []);

}


// load all snippet documents which in turn will be used to fetch all snippets
private loadSnippetDocument(snippetStub: CellDocument, index: number) {

	const uri = Configuration.BACKEND_PREF+snippetStub.uri;
	console.log("Loading snippet document %s", uri);
	this.register(this.snippetDocumentService.get<CellDocument>(uri).subscribe(
					snippetDoc => this.requestSnippetContent(snippetDoc, index),
					error => this.events.problem(error.message),
					() => {}
		)
	);

}


private requestSnippetContent(snippet: CellDocument, index: number) {

	const snippetURI = Configuration.BACKEND_PREF+'/dyn/snippets/'+snippet.contentURI+"?model="+snippet.modelURI;
	//console.debug("SnippetsListComponent::loadSnippetContent() Loading snippet content '%s'", snippetURI);
	this.register(
			this.snippetContentService.get(snippetURI, Content).subscribe( (snippetContent: Content) => {
				this.storeSnippetContent(snippet, snippetContent);
			},
			error => this.events.problem(error.message),	// error is of the type HttpErrorResponse
			() => {
				if (index<this.snippetStubs.length-1) {
					this.events.service.publish(new SnippetDocumentRequestEvent(index+1));
				} else {
					this.events.service.publish(new StatusEvent("Fetching snippets", StatusEvent.DONE));
					this.unsubscribe(this.snippetSubs);
					this.events.ok();	// this means we don't see errors for that long unfortunately
				}
			})
	);
}


private storeSnippetContent(snippet: CellDocument, snippetContent: Content) {

	// we set the document with the content, associate it with the model, as we modify the list the view will be
	// updated after this method
	snippet.content = snippetContent;
	console.debug("Stripping snippet content context %s and associating with model", snippet.contentURI);
	snippet.content = snippet.content.stripPrefixFromURIs(snippet.contentURI);
	snippet.content.associate(this.normalisedModel);

	const category = snippet.kind;
	if (!this.snippetsByCategory.has(category)) {	// not strictly needed, but to protect from the common error
		this.snippetsByCategory.set(category, []);	// of having the stubs out of sync of the snippet categories
		this.snippetCategoryNames.push(category);
		console.warn('category %s was in the snippet details but not on the catalogue', category);
	}
	this.snippetsByCategory.get(category).push(snippet);

	const defaultCategory = this.snippetCategoryNames[0];
	if (!this.accordion.isExpanded(defaultCategory) && category===defaultCategory) {
		this.currentCategory = defaultCategory;
		Promise.resolve(null).then(() => this.accordion.expand(defaultCategory));	// the toggle will do a next()
	}

}


//// KeyListenerWidget ////

override commandPressedCallback(command: string) {

	switch (command) {
		case "a":
		if (this.snippetSelectingMode) {
			console.log('[UI] SnippetsListComponent::activating current selection');
			this.events.service.publish(new CellActivateEvent());
		}
		break;
	}
}


override numberPressedCallback(num: number) {

	if (this.snippetSelectingMode) {
		console.debug("[UI] SnippetsListComponent::numberPressed(%i) [cellSelectingMode]", num);
		this.events.service.publish(new CellSelectEvent(num));
	} else if (this.snippetCategorySelectingMode) {
		const categories: NgbPanel[] = this.accordion.panels.toArray();
		if (num<categories.length) {
			console.debug('[UI] SnippetsListComponent::numberPressed(%i) [snippetCategorySelectingMode]', num);
			this.snippetCategorySelectingMode = false;
			this.snippetSelectingMode = true;
			// now we check if the category was not already toggled (no action needed then)
			const category = categories[num].id;
			if (!this.accordion.isExpanded(category)) {
				this.accordion.toggle(category);
				this.beforeToggle({panelId: category, nextState: true, preventDefault: undefined});
			}
			// only now can the children be selected, and we do it later so we have time to display
			Promise.resolve(null).then(() => this.subscribeChildrenToCellSelection());
		} else {
			console.debug("[UI] SnippetsListComponent::numberPressed(%i) [cellSelectingMode] out of bounds", num);
		}
	}
}


	override commandNotRegisteredCallback(command: string) {

	console.log("[UI] SnippetsListComponent::keyPressed(%s) not interested", command, this.snippetSelectingMode);
	this.deactivateSnippetSelectingMode();

}

//// KeyListenerWidget [end] ////


// we clear any existing selections, we subscribe our snippet children to selection and wait for numberic keypresses
activateSnippetSelectingMode() {

	console.log('[UI] SnippetsListComponent::activateSnippetSelectingMode()');

	this.events.service.publish(new CellSelectionClearEvent()); // clear any other subscriptions, happens in any case
	if (!this.snippetSelectingMode && !this.snippetCategorySelectingMode) {
		this.snippetCategorySelectingMode = true;
		this.registerKeyPressedEvents();	// we can now receive keypresses :)
	} else {
		// double toggle: we deselect the model
		this.snippetSelectingMode = false;
		this.snippetCategorySelectingMode = true;
	}

}


deactivateSnippetSelectingMode() {

	this.unregisterKeyPressedEvents();
	this.unsubscribeChildrenFromCellSelection();
	this.snippetSelectingMode = false;
	this.snippetCategorySelectingMode = false;

}




private subscribeChildrenToCellSelection () {

	console.log('SnippetsListComponent::subscribeChildrenToCellSelection() {%i}', this.snippetComponents.length);
	this.unsubscribeChildrenFromCellSelection();
	this.snippetComponents.forEach(sc => sc.subscribeToSelection());
}


private unsubscribeChildrenFromCellSelection() {

	console.log('SnippetsListComponent::unsubscribeChildrenToCellSelection() {%i}', this.snippetComponents.length);
	this.snippetComponents.forEach(sc => sc.unsubscribeFromSelection());

}


}

/*
 *	  Copyright 2024 Daniel Giribet
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
