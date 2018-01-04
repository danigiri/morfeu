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

import { RemoteDataService } from "./services/remote-data.service";
import { RemoteObjectService } from "./services/remote-object.service";
import { OperationResult } from "./services/operation-result.class";
import { SerialisableToJSON } from "./serialisable-to-json.interface";

import { CellComponent } from './cell.component';
import { DropAreaComponent } from './drop-area.component';
import { HotkeyWidget } from './hotkey-widget.class';

import { CellActivateEvent } from './events/cell-activate.event';
import { CellDocumentLoadedEvent } from './events/cell-document-loaded.event';
import { CellDocumentSelectionEvent } from './events/cell-document-selection.event';
import { CellDragEvent } from './events/cell-drag.event';
import { CellSelectEvent } from './events/cell-select.event';
import { CellSelectionClearEvent } from './events/cell-selection-clear.event';
import { ContentRefreshedEvent } from './events/content-refreshed.event';
import { ContentRequestEvent } from './events/content-request.event';
import { ContentSaveEvent } from './events/content-save.event';
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
			    [cell]="cell" [level]="0" 
			    [position]="i"
			    ></cell>
			<!-- TODO: static checks using the moel and not what's already present (cells) -->
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


export class ContentComponent extends HotkeyWidget implements OnInit, AfterViewInit, OnDestroy {
	
content: Content;
model: Model;

@ViewChildren(CellComponent) childrenCellComponents: QueryList<CellComponent>;

private cellSelectionClearSubscription: Subscription;
private cellSelectingMode: boolean = false;
private dropAreaSelectingMode: boolean = false;


constructor(eventService: EventService,
           protected hotkeysService: HotkeysService,
			@Inject("ContentService") private contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("RemoteJSONDataService") private contentSaverService: RemoteDataService
            ) {
	super(eventService, hotkeysService);
}


ngOnInit() {

	console.log("ContentComponent::ngOnInit()");
	
	this.subscribe(this.events.service.of(CellDocumentSelectionEvent).filter(s => s.url==null).subscribe(
			selected => this.clearContent()
	));

	// if we load a problematic document we don't display anything
	this.subscribe(this.events.service.of(CellDocumentLoadedEvent)
			.filter(loaded => loaded.document.hasProblem() )
			.subscribe(loadedProblematicDocument => this.clearContent()
	));
	
	this.subscribe(this.events.service.of(ContentRequestEvent).subscribe(
			requested => this.fetchContentFor(requested.document, requested.model)
	));
	
	this.subscribe(this.events.service.of(ContentSaveEvent).subscribe(
	       save => this.saveContent(save.document)
	));
}


// we make sure we subscribe to new elements if we are waiting for selections at root level
ngAfterViewInit() {

    console.log("ContentComponent::ngAfterViewInit()")
    this.childrenCellComponents.changes.subscribe(c => {
        if (this.cellSelectingMode) {
            this.subscribeChildrenToCellSelection();
            // if we send the vent immediately in the binding changing callback we'll probably be affecting the
            // component binding values after they have been read, we trigger it outside the callback then:
            Promise.resolve(null).then(()=>this.events.service.publish(new ContentRefreshedEvent(this.content)));
        }
    });

}


fetchContentFor(document_: CellDocument, model:Model) {

	this.events.service.publish(new StatusEvent("Fetching content"));
	let uri = document_.contentURI;
	let contentURI = "/morfeu/content/"+uri+"?model="+model.URI;
	this.contentService.get(contentURI, Content).subscribe( (content:Content) => {
		console.log("ContentComponent::fetchContent() Got content from Morfeu service ('%s')", uri);
		// we associate the content with the document and the model so it al fits together
		document_.content = content;
		content.associateWith(model);
		this.displayContent(content);
		this.events.ok();
	},
	error => this.events.problem(error.message),    // error is of the type HttpErrorResponse
	() =>	  this.events.service.publish(new StatusEvent("Fetching content", StatusEvent.DONE))
	);
	
}


displayContent(content: Content) {
	console.log("[UI] ContentComponent::displayContent()");
	this.content = content;
	this.cellSelectingMode = true;
    this.registerContentKeyShortcuts();
}


clearContent() {

    console.log("[UI] ContentComponent::clearContent()");
    this.cellSelectingMode = false;
	this.unregisterKeyShortcuts();
	this.content = null;

}


saveContent(document_:CellDocument) {
    
    this.events.service.publish(new StatusEvent("Saving content"));
    let postURI = "/morfeu/content/"+document_.contentURI+"?model="+document_.model.getURI();
    let content = document_.content.toJSON();
    console.log("ContentComponent::saveContent('%s')", postURI);
  
    this.contentSaverService.post<OperationResult>(postURI, content).subscribe(op => {  // YAY!
                console.log("ContentComponent::saveContent: saved in %s milliseconds ", op.operationTime);
                // reloading would go here
                //this.events.service.publish(new CellDocumentSelectionEvent(document_.uri));
            },
            error => this.events.problem(error.message),     // error is of the type HttpErrorResponse
            () => this.events.service.publish(new StatusEvent("Saving content", StatusEvent.DONE))
    );
}

/** Notice we are falling back to charcodes dues to a chromium driver/selenium/selenide bug */
numberPressed = (event: KeyboardEvent): boolean => {
    
    let num = this.translateNumberKeyboardEvent(event);
    console.log("[UI] ContentComponent::numberPressed(%i)", num);
    if (!this.dropAreaSelectingMode && this.cellSelectingMode) {
        this.events.service.publish(new CellSelectEvent(num));
    } else if (this.dropAreaSelectingMode){
        this.events.service.publish(new DropAreaSelectEvent(num));
        this.events.service.publish(new StatusEvent("Drop area selection mode", StatusEvent.DONE));
    }
    
    return false; // Prevent keyboard event from bubbling

}


/** Notice we are falling back to charcodes dues to a chromium driver/selenium/selenide bug */
keyPressed = (event: KeyboardEvent): boolean => {
    
    let command = this.translateCommandKeyboardEvent(event);
    console.log("[UI] ContentComponent::keyPressed(%s)", command);
    if (this.dropAreaSelectingMode) {
        console.log("[UI] ContentComponent::selection mode deactivated");        
        this.events.service.publish(new StatusEvent("Drop area selection mode", StatusEvent.DONE));
        this.dropAreaSelectingMode = false;
    }
    
    let captured = false;
    switch (command) {
        case "c":
            // we first send a clear so all children will clear, then back to registered in first level
            this.events.service.publish(new CellSelectionClearEvent());
            this.cellSelectingMode = true;
            this.subscribeChildrenToCellSelection();
            captured = true;
            break;
        case "m":       //FIXME: this is never called, it's owned by the model component
            this.dropAreaSelectingMode = false;
            this.cellSelectingMode = false;
            captured = false;    // this needs to 
            break;
        case "a":
            if (this.cellSelectingMode || this.dropAreaSelectingMode) {
                this.events.service.publish(new CellActivateEvent());
                captured = true;
            }
            break;
        case "'":
            console.log("[UI] ContentComponent::selection mode active for next numeric key");
            this.events.service.publish(new StatusEvent("Drop area selection mode"));
            this.dropAreaSelectingMode = true;
            this.cellSelectingMode = false;
            captured = true;
            break;
        case "d":
            this.events.service.publish(new CellDragEvent());
            captured = true;
            break;
    }

    return !captured;

}


ngOnDestroy() {
    
    super.ngOnDestroy();
    this.unregisterKeyShortcuts();
    
}


private registerContentKeyShortcuts() {
    
    let numbers:string[] = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"];
    this.registerNumberHotkey(new Hotkey(numbers, this.numberPressed));
    let commands:string[] = ["c", "a", "'", "d"]; 
    this.registerCommandHotkey(new Hotkey(commands, this.keyPressed)); 

}


private subscribeChildrenToCellSelection () {
    console.log("Content::subscribeChildrenToCellSelection()");
    //FIXME: detect changes: https://angular.io/api/core/ViewChildren
    // the list of children views is only available ngAfterViewInit but we assume that
    // fetching the content will have been much slower
    // we ensure there were no previous selections, avoiding double or triple selects
    this.unsubscribeChildrenFromCellSelection();
    this.childrenCellComponents.forEach(c => c.subscribeToSelection());
}


private unsubscribeChildrenFromCellSelection () {
    this.childrenCellComponents.forEach(c => c.unsubscribeFromSelection());
}

}

