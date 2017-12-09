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
import { DropAreaSelectEvent } from './events/drop-area-select.event';
import { StatusEvent } from './events/status.event';
import { EventService } from './events/event.service';


@Component({
	moduleId: module.id,
	selector: 'content',
	template: `
	<div id="content" class="card" *ngIf="content">
		<div id="content" class="card-body">
			<drop-area [parent]="model" position="0"></drop-area> <!-- FIXME: this should activate and it doesn't -->
			<cell *ngFor="let cell of content.children; let i=index" 
			    [parent]="content" 
			    [cell]="cell" [level]="1" 
			    [position]="i"
			    ></cell>
			<!-- TODO: static checks using the model and not what's already present (cells) -->
		</div>
    </div>
<!-- THIS DISPLAYS AS IT SHOULD -->
<!--div class="container-fluid" style="border: 2px solid rgba(86, 62, 128, .2)">
  <div class="row" style="border: 2px solid rgba(86, 62, 128, .2)">
    <div class="col-4" style="border: 2px solid rgba(86, 62, 128, .2)">
      <img class="img-fluid" src="http://localhost:3000/assets/images/data-cell.svg" />
    </div>
    <div class="col-8" style="border: 2px solid rgba(86, 62, 128, .2)">
      <div class="row">
    <div class="col-6" style="border: 2px solid rgba(86, 62, 128, .2)">
      <img src="http://localhost:3000/assets/images/data-cell.svg" />
      <img src="http://localhost:3000/assets/images/data-cell.svg" />
    </div>
    <div class="col-6" style="border: 2px solid rgba(86, 62, 128, .2)">
      <img src="http://localhost:3000/assets/images/data-cell.svg" />
      <img src="http://localhost:3000/assets/images/data-cell.svg" />
    </div>

    </div>
  </div>
</div-->
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
private dropAreaSelectingMode: boolean = false;
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
	this.unregisterContentKeyShortcuts();
	this.content = null;

}


/** Notice we are falling back to charcodes dues to a chromium driver/selenium/selenide bug */
numberPressed = (event: KeyboardEvent): boolean => {
    
    console.log("[UI] ContentComponent::numberPressed(key:"+event.key+", charCode:"+event.charCode+")");
    let num:number = (event.key) ? parseInt(event.key, 10) : event.charCode-48;
    if (!this.dropAreaSelectingMode) {
        this.events.service.publish(new CellSelectEvent(num));
    } else {
        this.events.service.publish(new DropAreaSelectEvent(num));
        this.events.service.publish(new StatusEvent("Drop area selection mode", StatusEvent.DONE));
    }
    return false; // Prevent keyboard event from bubbling

}


/** Notice we are falling back to charcodes dues to a chromium driver/selenium/selenide bug */
keyPressed = (event: KeyboardEvent): boolean => {
    
    console.log("[UI] ContentComponent::keyPressed(key:"+event.key+", charCode:"+event.charCode+")");
    if (this.dropAreaSelectingMode) {
        console.log("[UI] ContentComponent::selection mode deactivated");        
        this.events.service.publish(new StatusEvent("Drop area selection mode", StatusEvent.DONE));
        this.dropAreaSelectingMode = false;
    }
    if ((event.key && event.key=="c") || (event.charCode && event.charCode==99)) {
        // we first send a clear so all children will clear, then back to registered in first level
        this.events.service.publish(new CellSelectionClearEvent());
        this.subscribeChildrenToCellSelection();
    } else if ((event.key && event.key=="a") || (event.charCode && event.charCode==97)) {
        this.events.service.publish(new CellActivateEvent());        
    } else if ((event.key && event.key=="'") || (event.charCode && event.charCode==39)) {
        console.log("[UI] ContentComponent::selection mode active for next numeric key");
        this.events.service.publish(new StatusEvent("Drop area selection mode"));
        this.dropAreaSelectingMode = true;
    } else if ((event.key && event.key=="d") || (event.charCode && event.charCode==100)) {
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
    let commands:string[] = ["c", "a", "'", "d"]; 
    this.commandHotkey = this.hotkeysService.add(new Hotkey(commands, this.keyPressed)); 

}


private unregisterContentKeyShortcuts() {
    
    
    this.hotkeysService.remove(this.numberHotkey);   
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

