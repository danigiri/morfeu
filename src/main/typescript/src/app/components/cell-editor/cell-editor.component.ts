// CELL - EDITOR . COMPONENT . TS

import {filter} from 'rxjs/operators';
import {Component, ElementRef, ViewChild, OnInit} from '@angular/core';

import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';

import {Cell} from '../../cell.class';
import {CellModel} from '../../cell-model.class';

import {CellActivateEvent} from '../../events/cell-activate.event';
import {CellActivatedEvent} from '../../events/cell-activated.event';
import {CellEditEvent} from '../../events/cell-edit.event';
import {ContentFragmentDisplayEvent} from '../../events/content-fragment-display.event';
import {EventListener} from '../../events/event-listener.class';
import {UXEvent} from '../../events/ux.event';
import {EventService} from '../../services/event.service';

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
							rows="textAreaRows()"
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
							<img *ngIf="cell.value!=undefined && showValue()"
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
					<!-- presentation goes here -->
					<img *ngIf="showPresentation() && cellPresentationIsIMG()"
						class="card-img-bottom" src="{{getPresentation()}}"
						alt="Image representation of the cell" />
					<presentation *ngIf="showPresentation() && !cellPresentationIsIMG()" [cell]="cell" ></presentation>
				</div>
				<div class="modal-footer card-footer">
					<button id="cell-editor-discard-button"
						type="button"
						class="btn btn-outline-secondary float-left"
						(click)="c('Discard')"
					>Discard</button>
					<button id="cell-editor-save-button"
						type="button"
						class="btn btn-success float-right"
						(click)="c('Save')"
					>Save</button>
				</div>
			</div>
		</ng-template>
		`,
	styles: [`
			#cell-editor {}
			#cell-editor-discard-button {}
			#cell-editor-save-button {}
			#cell-editor-create-value-button {}
			#cell-editor-remove-value-button {}
			.cell-editor-value {}
`]
})


export class CellEditorComponent extends EventListener implements OnInit {

@ViewChild("editor", {static: false}) editor: ElementRef;

cell: Cell;
cellBackup: Cell;
editing = false;

constructor(eventService: EventService, private modalService: NgbModal) {
	super(eventService);
}


ngOnInit() {
	this.subscribe(this.events.service.of(CellEditEvent)
			.pipe(filter(edit => edit.cell!==undefined && !this.editing))
			.subscribe(edit => this.edit(edit.cell))
	);
}


private showValue() {
	return this.cell.cellModel.presentation.includes("TEXT"); // if we need to show the text area or not
}


private textAreaRows(): number {
	return this.cell.cellModel.presentation.includes("ONELINE") ? 1 : 3;
}


private edit(cell: Cell) {

	console.log("[UI] Edit at position %s [%s]", cell.position, cell.cellModel.presentation);

	// if we are not a CELL-WELL we can edit directly

	if (cell.cellModel.presentation!=="CELL-WELL") {
		this.editing = true;
		this.cellBackup = cell.deepClone();
		this.cell = cell;
		this.modalService.open(this.editor).result.then((result) => this.button(result), (reason) => this.outside());
	} else {
		console.debug("We're editing a cell-well so need to load a fragment of the content");
		this.events.service.publish(new ContentFragmentDisplayEvent(cell));
	}

}


//TODO: this is duplicated code, should refactor
private cellPresentationIsIMG(): boolean {
	return this.cell.cellModel.getPresentationType()===CellModel.DEFAULT_PRESENTATION_TYPE;
}


private showPresentation() {
	return this.cell.cellModel.presentation.startsWith("CELL");
}


private getPresentation(): string {
	return this.cell===undefined ? this.cell.cellModel.getPresentation() : this.cell.getPresentation();
}


private button(result: string) {

	if (result==="Save") {
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

	const position = this.cell.position;
	const parent = this.cell.parent;

	console.log("[UI] Discard changes at position "+position);
	parent.remove(this.cell);	  // TODO: slow but no need to change the interface for now
	this.cellBackup.parent = undefined; // backup must be an orphan for it to be adopted
	parent.adopt(this.cellBackup, position);

	// reload ?
	// this.events.service.publish(new CellActivateEvent());
	this.events.service.publish(new CellActivatedEvent(this.cellBackup));

}


private clear() {

	this.cell = undefined;
	this.cellBackup = undefined;
	this.editing = false;

}


private createValue() {

	console.log("[UI] Create new (empty|default) value for '%s'", this.cell.URI);
	Promise.resolve(null).then(() => this.cell.createValue());

}


private removeValue() {

	console.log("[UI] Removing value for '%s'", this.cell.URI);
	Promise.resolve(null).then(() => this.cell.removeValue());

}


}

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
