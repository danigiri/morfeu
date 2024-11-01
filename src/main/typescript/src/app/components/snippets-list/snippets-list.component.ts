// SNIPPETS - LIST . COMPONENT . TS

import { Component, Inject, Input, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';

import { NgbAccordionDirective } from '@ng-bootstrap/ng-bootstrap';

import { RemoteDataService } from '../../services/remote-data.service';
import { RemoteObjectService } from '../../services/remote-object.service';

import { Configuration } from '../../config/configuration.class';

import { CellDocument } from '../../cell-document.class';
import { Content, ContentJSON } from '../../content.class';
import { Model } from '../../model.class';

import { KeyListenerWidget } from 'src/app/key-listener-widget.class';
import { SnippetComponent } from './../snippet/snippet.component';

import { CellActivateEvent } from '../../events/cell-activate.event';
import { CellDocumentClearEvent } from 'src/app/events/cell-document-clear.event';
import { CellSelectEvent } from '../../events/cell-select.event';
import { CellSelectionClearEvent } from '../../events/cell-selection-clear.event';
import { InfoModeEvent } from 'src/app/events/info-mode.event';
import { SnippetDocumentRequestEvent } from '../../events/snippet-document-request.event';
import { StatusEvent } from '../../events/status.event';
import { SnippetsDisplayEvent } from 'src/app/events/snippets-display.event';
import { ModelDisplayEvent } from 'src/app/events/model-display.event';
import { EventService } from '../../services/event.service';

@Component({
	selector: "snippets",
	templateUrl: "./snippets-list.component.html",
	styles: [`
		#snippets {}
		#snippets-loading-status {}
		.snippet-category {}
		.snippet-category-name {}
	`]
})

export class SnippetsListComponent extends KeyListenerWidget implements OnInit { // AfterViewInit {

private static readonly SNIPPET_CATEGORY_POSTFIX = '-snippet-category';
	
@Input() display = false;

model: Model;

snippetStubs: CellDocument[];	 // stubs that come from the catalogue

@ViewChild('snippetsAccordion') accordion: NgbAccordionDirective;
snippetComponents: SnippetComponent[];				// this list is maintained manually and updated by the children

normalisedModel: Model;

snippetCategoryNames: string[] = [];
snippetsByCategory: Map<string, CellDocument[]>; // snippets grouped by categories, source of truth
snippetsByCategoryDisplay: Map<string, CellDocument[]>; // using this to display and force change detection

currentCategory: string;

protected override commandKeys: string[] = ["a"];		// activation keybinding
snippetCategorySelectingMode = false;	// are we selecting categories
snippetSelectingMode = false;			// or snippets?
completedLoading = false;

info = false;

protected snippetSubscription: Subscription;


constructor(eventService: EventService,
			@Inject("RemoteJSONDataService") private snippetDocumentService: RemoteDataService,
			@Inject("SnippetContentService") private snippetContentService: RemoteObjectService<Content, ContentJSON>) {
	super(eventService);
}


ngOnInit() {

	console.log("SnippetsListComponent::ngOnInit()");
	this.snippetComponents = [];

	// when we clear the curernt document, we do not show the snippets anymore
	this.register(this.events.service.of<CellDocumentClearEvent>(CellDocumentClearEvent)
			.subscribe(() => this.clear())
	);

	// once the model has loaded, we set it, as we need to link it 
	this.register(this.events.service.of<ModelDisplayEvent>(ModelDisplayEvent)
			.subscribe(display => this.model = display.model)
	);

	this.register(this.events.service.of<SnippetsDisplayEvent>(SnippetsDisplayEvent)
		.subscribe(display => this.fetchSnippets(display.snippets))
	);
	
	this.register(this.events.service.of<InfoModeEvent>(InfoModeEvent).subscribe(mode => this.info = mode.active));

	//Promise.resolve(null).then(() => this.fetchSnippets());

}


// fetch all snippet documents
public fetchSnippets(stubs: CellDocument[]) {
	if (!this.completedLoading && stubs.length>0) {
		this.snippetStubs = stubs;
		// we copy and normalise the model as we will link it with the snippets
		let MODEL:Model = Object.create(Model.prototype); // to simulate a static call
		const json = this.model.toJSON(); // TOOD: this is costly and we could cache it
		this.normalisedModel = MODEL.fromJSON(json);
		this.normalisedModel.normaliseReferences();
		// we initialise the snippet structures
		this.snippetCategoryNames = [];
		this.snippetsByCategory = new Map<string, CellDocument[]>();
		this.snippetsByCategoryDisplay = new Map<string, CellDocument[]>();
		this.snippetStubs.map(s => s.kind).forEach(c => {
															if (!this.snippetsByCategory.has(c)) {
																this.createSnippetCategory(c)
															}
		});

		this.events.service.publish(new StatusEvent("Fetching snippets"));
		this.snippetSubscription = 
			this.register(this.events.service.of<SnippetDocumentRequestEvent>(SnippetDocumentRequestEvent)
										.subscribe(
											req => this.loadSnippetDocument(this.snippetStubs[req.index], req.index)
										)
		);
		this.events.service.publish(new SnippetDocumentRequestEvent(0));
	} else if (this.completedLoading) {
		console.debug('Already loaded snippets, skipping reload');
	 } else  {
		console.warn('Empty snippets list from catalogue');
	}

}


public currentSnippets(): CellDocument[] {
	return this.snippetsByCategory.get(this.currentCategory) ?? [];
}

//public snippetsByCategory(category: string): CellDocument[] {
//	
//}

public beforeToggle(category: string, nextState: boolean) {
	// const $e = $event as NgbPanelChangeEvent;
	if (nextState) {
		this.currentCategory = category;
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
	// console.debug("SnippetsListComponent::loadSnippetContent() Loading snippet content [%d]'%s'",index, snippetURI);
	this.register(
			this.snippetContentService.get(snippetURI, Content).subscribe( (snippetContent: Content) => {
				this.storeSnippetContent(snippet, snippetContent);
			},
			error => this.events.problem(error.message),	// error is of the type HttpErrorResponse
			() => {
				if (index<this.snippetStubs.length-1) {
					this.events.service.publish(new SnippetDocumentRequestEvent(index+1));
				} else {
					console.debug('SnippetsListComponent: completed loading all snippets');
					this.snippetsByCategoryDisplay = this.snippetsByCategory;
					this.events.service.publish(new StatusEvent("Fetching snippets", StatusEvent.DONE));
					this.completedLoading = true; // finished, do not load snippets again
					this.events.ok();	// this means we don't see intermediate errors for that long unfortunately
					this.unsubscribe(this.snippetSubscription);
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
	const categoryID = defaultCategory+SnippetsListComponent.SNIPPET_CATEGORY_POSTFIX;
	if (!this.accordion.isExpanded(categoryID) && category===defaultCategory) {
		this.currentCategory = defaultCategory;
		Promise.resolve(null).then(() => this.accordion.expand(categoryID));	// the toggle will do a next()
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
		const categories: string[] = this.snippetCategoryNames; //NgbPanel[] = this.accordion.panels.toArray();
		if (num<categories.length) {
			console.debug('[UI] SnippetsListComponent::numberPressed(%i) [snippetCategorySelectingMode]', num);
			this.snippetCategorySelectingMode = false;
			this.snippetSelectingMode = true;
			// now we check if the category was not already toggled (no action needed then)
			const category = categories[num]; //.id;
			const categoryId = category + SnippetsListComponent.SNIPPET_CATEGORY_POSTFIX;
			if (!this.accordion.isExpanded(categoryId)) {
				this.accordion.toggle(categoryId);
				this.beforeToggle(category,true);
				// we lose info status here, we could reactivate it here if needed
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

	console.log('[UI] SnippetsListComponent::activateSnippetSelectingMode() ******************');

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


private clear(): void {
	console.debug('SnippetsListComponent::clear')
	this.model = null;
	this.snippetStubs = []
	this.snippetCategoryNames = [];
	this.snippetsByCategory = null;
	this.completedLoading = false;
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
