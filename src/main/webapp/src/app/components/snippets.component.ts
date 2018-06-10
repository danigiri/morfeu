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

import { Component, AfterViewInit, Inject, Input, OnInit } from "@angular/core";
import { Subscription } from "rxjs";

import { RemoteObjectService } from "../services/remote-object.service";

import { CellDocument, CellDocumentJSON } from "../cell-document.class";

import { KeyListenerWidget } from "../key-listener-widget.class";

import { SnippetsRequestEvent } from "../events/snippets-request.event";
import { SnippetRequestEvent } from "../events/snippet-request.event";
import { StatusEvent } from "../events/status.event";
import { EventService } from "../events/event.service";

@Component({
	moduleId: module.id,
	selector: "snippets",
	template: `xxx
	`,

	styles:[`

	`]
})

export class SnippetsComponent extends KeyListenerWidget implements AfterViewInit {

@Input() snippetStubs: CellDocument[];	 // stubs that come from the catalogue

snippets: CellDocument[];

protected snippetSubscription: Subscription;

	
constructor(eventService: EventService,
			@Inject("SnippetsService") private snippetService: RemoteObjectService<CellDocument, CellDocumentJSON>,
			) {
	super(eventService);
}


ngAfterViewInit() {
	Promise.resolve(null).then(() => this.fetchSnippets()); 
}


// fetch all snippets
private fetchSnippets() {

	if (this.snippetStubs.length>0) {
		this.events.service.publish(new StatusEvent("Fetching snippets"));
		this.snippetSubscription = this.subscribe(this.events.service.of(SnippetRequestEvent).subscribe(
				loaded => this.loadSnippet(this.snippetStubs[loaded.index], loaded.index)
		));
		this.events.service.publish(new SnippetRequestEvent(0));
    }

}


// TODO: this needs to fetch the document and then we extract the content from it =)
private loadSnippet(snippet: CellDocument, index: number) {
   
	let snippetURI = "/morfeu/dyn/snippets/"+snippet.contentURI+"?model="+snippet.modelURI;
	console.log("Loading snippet %s", snippetURI);
	this.snippetService.get(snippetURI, CellDocument).subscribe(
			s => {
				this.snippets.push(s);
			},
			error => {
				this.events.problem(error.message);
			},
			() => {
				if (index<this.snippetStubs.length) {
					this.events.service.publish(new SnippetRequestEvent(index+1));
				} else {
					this.unsubscribe(this.snippetSubscription);
					this.events.ok();	// this means we don't see errors for that long unfortunately
				}
			}
	);

}


commandPressedCallback(command: string) {}


numberPressedCallback(num: number) {}

}


