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

import { Component, ElementRef, ViewChild, OnInit } from "@angular/core";

import { NgbModal, ModalDismissReasons} from "@ng-bootstrap/ng-bootstrap";

import { Cell } from "../cell.class";

import { Widget } from "../widget.class";

import { CellActivateEvent } from "../events/cell-activate.event";
import { CellActivatedEvent } from "../events/cell-activated.event";
import { CellEditEvent } from "../events/cell-edit.event";
import { EventService } from "../events/event.service";
import { UXEvent } from "../events/ux.event";

@Component({
	moduleId: module.id,
	selector: 'cell-editor',
	template: `
		<ng-template let-c="close" let-d="dismiss" #editor>
			<div id="cell-editor" class="card mt-2 modal-body">
				<cell-header [uri]="cell.URI" [cellModel]="cell.cellModel"></cell-header>
				<div class="card-body">
					<form>
						<textarea *ngIf="cell.value!=undefined && showValue()"
							class="cell-editor-value form-control"
							rows="3"
							name="{{cell.cellModel.name}}.value"
							attr.aria-label="{{cell.cellModel.name}}.value" 
							attr.aria-describedby="{{cell.cellModel.desc}} value" 
							[(ngModel)]="cell.value"></textarea>
							<!-- create new value button -->
							<img  *ngIf="cell.value==undefined && showValue()"
								id="cell-editor-create-value-button"
								class="btn btn-outline-danger float-right" 
								src="assets/images/open-iconic/plus.svg" 
								(click)="createValue()"
								/>
							<!-- remove value button -->
							<img  *ngIf="cell.value!=undefined && showValue()"
								id="cell-editor-remove-value-button"
								class="btn btn-outline-danger float-right" 
								src="assets/images/open-iconic/circle-x.svg" 
								(click)="removeValue()"
								/>

						<ul class="list-group list-group-flush" *ngIf="cell.cellModel.attributes">
							<attribute-data-editor *ngFor="let a of cell.cellModel.attributes; let i = index" 
								[parentCell]="cell" 
								[cellModel]="a"
								[index]="i"
								></attribute-data-editor>
						</ul>
					</form>
					<img *ngIf="showPresentation()" class="card-img-bottom" src="{{getPresentation()}}" alt="Image representation of the cell" />
				</div>
				<div class="modal-footer card-footer">
					<button id="cell-editor-discard-button"
							type="button" 
							class="btn btn-outline-secondary float-left" 
							(click)="c('Discard')">Discard</button>
					<button id="cell-editor-save-button" 
							type="button" 
							class="btn btn-success float-right" 
							(click)="c('Save')">Save</button>
				</div>
			</div>
		</ng-template>
		`,
	styles:[`
			#cell-editor {}
			#cell-editor-discard-button {}
			#cell-editor-save-button {}
			#cell-editor-create-value-button {}
			#cell-editor-remove-value-button {}
			.cell-editor-value {}
`]
})


export class CellEditorComponent  extends Widget implements OnInit {

@ViewChild("editor") editor: ElementRef;

cell: Cell;
cellBackup: Cell;
editing: boolean = false;

constructor(eventService: EventService, private modalService: NgbModal) {
	super(eventService);
}


ngOnInit() {
	this.subscribe(this.events.service.of( CellEditEvent )
			.filter(edit => edit.cell!=undefined && !this.editing)
			.subscribe(edit => this.edit(edit.cell))
			);
}


private showValue() {
	return this.cell.cellModel.presentation.includes("TEXT"); // if we need to show the text area or not
}

private edit(cell: Cell) {

	console.log("[UI] Discard changes at position "+cell.position);
	this.editing = true;
	this.cellBackup = cell.deepClone();
	this.cell = cell;

	//this.events.service.publish(new CellActivatedEvent(this.cell));
	this.modalService.open(this.editor).result.then((result) => this.button(result), 
													(reason) => this.outside());
	
}


private showPresentation() {
	return this.cell.cellModel.presentation.startsWith("CELL");
}


private getPresentation(): string {
	return this.cell==undefined ? this.cell.cellModel.getPresentation() : this.cell.getPresentation();
}


private button(result: any) {
	
	if (result=="Save") {
		console.log("[UI] Saved cell");
		// the document is now dirty
		this.events.service.publish(new UXEvent(UXEvent.DOCUMENT_DIRTY));
	} else {
		this.rollbackChanges();
	}
	this.clear();
}


private outside() {
 
	console.log("[UI] Dismissed editor without saving");
	this.rollbackChanges();
	this.clear();
	
}


private rollbackChanges() {
	
	let position = this.cell.position;
	let parent = this.cell.parent;

	console.log("[UI] Discard changes at position "+position);
	parent.remove(this.cell);	  // TODO: slow but no need to change the interface for now
	this.cellBackup.parent = undefined; // backup must be an orphan for it to be adopted
	parent.adopt(this.cellBackup, position);
	
	// reload ?
	//this.events.service.publish(new CellActivateEvent());
	this.events.service.publish(new CellActivatedEvent(this.cellBackup));

}


private clear() {
	this.cell = undefined;
	this.cellBackup = undefined;
	this.editing = false;
}

}