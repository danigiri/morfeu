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

import { Component, Inject, OnInit, AfterViewInit, OnDestroy, QueryList, ViewChildren } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

import { HotkeysService, Hotkey } from 'angular2-hotkeys';

import { CellDocument } from './cell-document.class';
import { Cell } from './cell.class';
import { Content, ContentJSON } from './content.class';
import { FamilyMember } from './family-member.interface';
import { Model } from './model.class';
import { RemoteObjectService } from './services/remote-object.service';
import { SerialisableToJSON } from './serialisable-to-json.interface';

import { CellComponent } from './cell.component';
import { DropAreaComponent } from './drop-area.component';
import { Widget } from './widget.class';

import { CellActivateEvent } from './events/cell-activate.event';
import { CellDocumentLoadedEvent } from './events/cell-document-loaded.event';
import { CellDocumentSelectionEvent } from './events/cell-document-selection.event';
import { CellDragEvent } from './events/cell-drag.event';
import { CellSelectEvent } from './events/cell-select.event';
import { CellSelectionClearEvent } from './events/cell-selection-clear.event';
import { ContentRequestEvent } from './events/content-request.event';
import { StatusEvent } from './events/status.event';
import { EventService } from './events/event.service';


@Component({
	moduleId: module.id,
	selector: 'content',
	template: `
	<div class="panel panel-default" *ngIf="content">
		<div id="content" class="panel-body">
			<drop-area [parent]="model" position="0"></drop-area> 
			<cell *ngFor="let cell of content.children; let i=index" 
			    [parent]="content" 
			    [cell]="cell" [level]="1" 
			    [position]="i"
			    ></cell>
			<!-- TODO: static checks using the model and not what's already present (cells) -->
		</div>
	</div>
	`,
	styles:[`
		#content {}
	`],
	providers:[
	]
})


export class ContentComponent extends Widget implements OnInit, AfterViewInit, OnDestroy {
	
content: Content;
model: Model;

@ViewChildren(CellComponent) children: QueryList<CellComponent>;

private cellSelectionClearSubscription: Subscription;

private numberHotkey: Hotkey | Hotkey[];
private ctrlNumberHotkey: Hotkey | Hotkey[];
private commandHotkey: Hotkey | Hotkey[];


constructor(eventService: EventService,
           private hotkeysService: HotkeysService,
			@Inject("ContentService") private contentService: RemoteObjectService<Content, ContentJSON> ) {
	super(eventService);
}


ngOnInit() {

	console.log("ContentComponent::ngOnInit()");
	
	this.subscribe(this.events.service.of(CellDocumentSelectionEvent).filter(s => s.url==null).subscribe(
			selected => this.clearContent()
	));

	// if we load a problematic document we don't display anything
	this.subscribe(this.events.service.of(CellDocumentLoadedEvent)
			.filter(loaded => loaded.document.problem!=null && loaded.document.problem.length>0 )
			.subscribe(loadedProblematicDocument => this.clearContent()
	));
	
	this.subscribe(this.events.service.of(ContentRequestEvent).subscribe(
			requested => this.fetchContent(requested.url, requested.model)
	));
	
}

// we make sure we subscribe to new elements if we are waiting for selections at root level
ngAfterViewInit() {

    console.log("ContentComponent::ngAfterViewInit()")
    this.children.changes.subscribe(c => {
        this.subscribeChildrenToCellSelection();
     
    });

}

fetchContent(url:String, model:Model) {

	this.events.service.publish(new StatusEvent("Fetching content"));
	let contentURI = "/morfeu/content/"+url+"?model="+model.URI;
	this.contentService.get(contentURI, Content).subscribe( (content:Content) => {
		console.log("ContentComponent::fetchContent() Got content from Morfeu service ("+url+")");
		content.associateWith(model);
		this.displayContent(content);
	    //this.subscribeChildrenToCellSelection(); 
	    this.registerContentKeyShortcuts();
		this.events.ok();
	},
	error => {
		this.events.problem(error);
	},
	() =>	  this.events.service.publish(new StatusEvent("Fetching content", StatusEvent.DONE))
	);
	
}


displayContent(content: Content) {
	console.log("[UI] ContentComponent::displayContent()");
	this.content = content;
}


clearContent() {

    console.log("[UI] ContentComponent::clearContent()");
	this.registerContentKeyShortcuts();
	this.content = null;

}


numberPressed = (event: KeyboardEvent): boolean => {
    
    console.log("[UI] ContentComponent::numberPressed("+event.key+")");
    this.events.service.publish(new CellSelectEvent(parseInt(event.key, 10)));

    return false; // Prevent keyboard event from bubbling

}


ctrlNumberPressed = (event: KeyboardEvent): boolean => {
    
    console.log("[UI] ContentComponent::ctrlNumberPressed("+event.key+")");
//    this.events.service.publish(new CellSelectEvent(parseInt(event.key, 10)));

    return false; // Prevent keyboard event from bubbling

}



keyPressed = (event: KeyboardEvent): boolean => {
    
    // we first send a clear so all children will clear, if they are level one they will
    // subscribe themselves again
    console.log("[UI] ContentComponent::keyPressed("+event.key+")");
    if (event.key=="c") {
        this.events.service.publish(new CellSelectionClearEvent());
    } else if (event.key=="a") {
        this.events.service.publish(new CellActivateEvent());        
    } else if (event.key=="d") {
        this.events.service.publish(new CellDragEvent());        
    }

    return false; // Prevent keyboard event from bubbling

}


ngOnDestroy() {
    
    super.ngOnDestroy();
    this.unregisterContentKeyShortcuts();
    
}


private registerContentKeyShortcuts() {
    
    let numbers:string[] = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"];
    this.numberHotkey = this.hotkeysService.add(new Hotkey(numbers, this.numberPressed));
    let ctrlNumbers:string[] 
        = ["alt+0", "alt+1", "alt+2", "alt+3", "alt+4", "alt+5", "alt+6", "alt+7", "alt+8", "alt+9"];
    this.ctrlNumberHotkey = this.hotkeysService.add(new Hotkey(ctrlNumbers, this.ctrlNumberPressed));    
    let commands:string[] = ["c", "a", "d"]; 
    this.commandHotkey = this.hotkeysService.add(new Hotkey(commands, this.keyPressed)); 

}


private unregisterContentKeyShortcuts() {
    
    
    this.hotkeysService.remove(this.numberHotkey);   
    this.hotkeysService.remove(this.ctrlNumberHotkey);   
    this.hotkeysService.remove(this.commandHotkey);   

}


private subscribeChildrenToCellSelection () {
    console.log("Content::subscribeChildrenToCellSelection()");
    //FIXME: detect changes: https://angular.io/api/core/ViewChildren
    // the list of children views is only available ngAfterViewInit but we assume that
    // fetching the content will have been much slower
    // we ensure there were no previous selections, avoiding double or triple selects
    this.children.forEach(c => c.unsubscribeFromSelection());
    this.children.forEach(c => c.subscribeToSelection());
}


}

