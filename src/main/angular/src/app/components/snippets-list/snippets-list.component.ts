// SNIPPETS - LIST . COMPONENT . TS

import { Component, AfterViewInit, Inject, Input, OnInit } from '@angular/core';
import { Observable, Subject, Subscription } from 'rxjs';

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

@Component({
	selector: "snippets",
	template: `
		<div id="snippets" class="list-group">
			<snippet *ngFor="let s of snippets | async; let i=index"
				class="list-group-item"
				[snippet]="s"
				[model]="normalisedModel"
				[position]="i"
				[parent]="this"
			></snippet>
		</div>
	`,
	styles: [`
		#snippets {}
	`]
})

export class SnippetsListComponent extends KeyListenerWidget implements AfterViewInit {

@Input() model: Model;
@Input() snippetStubs: CellDocument[];	 // stubs that come from the catalogue

normalisedModel: Model;
snippets: Observable<Array<CellDocument>>;			// snippets document observable, for async display
_snippets: Array<CellDocument>;						// snippets document list
_snippetsSubject: Subject<Array<CellDocument>>;		// snippets document subject, to push new documents into

protected commandKeys: string[] = ["a"];	// activation
private snippetSelectingMode = false;

protected snippetDocumentSubs: Subscription;

snippetComponents: SnippetComponent[];	// this will be populated by the snippets themselves


constructor(eventService: EventService,
			@Inject("RemoteJSONDataService") private snippetDocumentService: RemoteDataService,
			@Inject("SnippetContentService") private snippetContentService: RemoteObjectService<Content, ContentJSON>
			) {
	super(eventService);
}


ngAfterViewInit() {

	console.log("SnippetsListComponent::ngAfterViewInit()");
	this.snippetComponents = [];
	Promise.resolve(null).then(() => this.fetchSnippets());

}


// fetch all snippet documents
private fetchSnippets() {

	if (this.snippetStubs.length>0) {

		// we copy and normalise the model as we will link it with the snippets
		let MODEL:Model = Object.create(Model.prototype); // to simulate a static call
		this.normalisedModel = MODEL.fromJSON(this.model.toJSON());
		this.normalisedModel.normaliseReferences();

		// we initialise the snippet structures
		this._snippets = [];
		this._snippetsSubject = new Subject();
		this.snippets = this._snippetsSubject.asObservable();
		this.events.service.publish(new StatusEvent("Fetching snippets"));
		this.snippetDocumentSubs = this.register(this.events.service.of<SnippetDocumentRequestEvent>(SnippetDocumentRequestEvent)
										.subscribe(
											req => this.loadSnippetDocument(this.snippetStubs[req.index], req.index)
										)
		);
		this.events.service.publish(new SnippetDocumentRequestEvent(0));
	} else {
		console.warn('Empty snippets list from catalogue');
	}

}


// load all snippet documents which in turn will be used to fetch all snippets
private loadSnippetDocument(snippetStub: CellDocument, index: number) {

	const uri = Configuration.BACKEND_PREF+snippetStub.uri;
	console.log("Loading snippet document %s", uri);
	this.register(this.snippetDocumentService.get<CellDocument>(uri).subscribe(
					snippetDoc => this.loadSnippetContent(snippetDoc, index),
					error => this.events.problem(error.message),
					() => {}
		)
	);

}


private loadSnippetContent(snippet: CellDocument, index: number) {

	const snippetURI = Configuration.BACKEND_PREF+"/dyn/snippets/"+snippet.contentURI+"?model="+snippet.modelURI;
	console.debug("SnippetsListComponent::loadSnippetContent() Loading snippet content '%s'", snippetURI);
	this.register(
			this.snippetContentService.get(snippetURI, Content).subscribe( (snippetContent: Content) => {
				// we set the document with the content, associate it with the model
				snippet.content = snippetContent;
				console.debug("Stripping snippet content context %s", snippet.contentURI);
				snippet.content = snippet.content.stripPrefixFromURIs(snippet.contentURI);
				console.debug("Associating snippet content with model");
				snippet.content.associate(this.normalisedModel);

				// we have the content, so we can push the snippet document so it can de displayed
				this._snippets.push(snippet);
				this._snippetsSubject.next(this._snippets);
			},
			error => this.events.problem(error.message),	// error is of the type HttpErrorResponse
			() => {
				if (index<this.snippetStubs.length-1) {
					this.events.service.publish(new SnippetDocumentRequestEvent(index+1));
				} else {
					this.events.service.publish(new StatusEvent("Fetching snippets", StatusEvent.DONE));
					this.unsubscribe(this.snippetDocumentSubs);
					this.events.ok();	// this means we don't see errors for that long unfortunately
				}
			})
	);
}


//// KeyListenerWidget ////

commandPressedCallback(command: string) {

	switch (command) {
		case "a":
		if (this.snippetSelectingMode) {
			console.log("[UI] SnippetsListComponent::activating current selection");
			this.events.service.publish(new CellActivateEvent());
		}
		break;
	}
}


numberPressedCallback(num: number) {

	if (this.snippetSelectingMode) {
		console.log("[UI] SnippetsListComponent::numberPressed(%i) [cellSelectingMode]", num);
		this.events.service.publish(new CellSelectEvent(num));
	}

}


commandNotRegisteredCallback(command: string) {

	console.log("[UI] SnippetsListComponent::keyPressed(%s) not interested", command, this.snippetSelectingMode);
	this.deactivateSnippetSelectingMode();

}

//// KeyListenerWidget [end] ////


// we clear any existing selections, we subscribe our snippet children to selection and wait for numberic keypresses
activateSnippetSelectingMode() {

	console.log("[UI] SnippetsListComponent::activateSnippetSelectingMode()");

	this.events.service.publish(new CellSelectionClearEvent()); // clear any other subscriptions, happens in any case
	if (!this.snippetSelectingMode) {
		this.snippetSelectingMode = true;
		this.snippetSelectingMode = true;
		this.subscribeChildrenToCellSelection();
		this.registerKeyPressedEvents();	// we can now receive keypresses :)
	} else {
		// double toggle: we deselect the model
		this.snippetSelectingMode = false;
	}
}


deactivateSnippetSelectingMode() {

	this.unregisterKeyPressedEvents();
	this.unsubscribeChildrenFromCellSelection();
	this.snippetSelectingMode = false;

}


private subscribeChildrenToCellSelection () {

	console.log("SnippetsListComponent::subscribeChildrenToCellSelection()");
	this.unsubscribeChildrenFromCellSelection();
	this.snippetComponents.forEach( sc => sc.subscribeToSelection() );
}


private unsubscribeChildrenFromCellSelection() {

	console.log("SnippetsListComponent::subscribeChildrenToCellSelection()");
	this.snippetComponents.forEach( sc => sc.unsubscribeFromSelection() );

}


}

/*
 *	  Copyright 2019 Daniel Giribet
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
