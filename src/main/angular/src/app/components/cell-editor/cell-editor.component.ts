// CELL - EDITOR . COMPONENT . TS

import { filter } from 'rxjs/operators';
import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Subscription } from 'rxjs';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Cell } from '../../cell.class';
import { CellModel } from '../../cell-model.class';

import { CellActivatedEvent } from '../../events/cell-activated.event';
import { CellChangedEvent } from '../../events/cell-changed.event';
import { CellEditEvent } from '../../events/cell-edit.event';
import { ContentFragmentDisplayEvent } from '../../events/content-fragment-display.event';
import { EventListener } from '../../events/event-listener.class';
import { UXEvent } from '../../events/ux.event';
import { EventService } from '../../services/event.service';

@Component({
	selector: 'cell-editor',
	templateUrl: './cell-editor.component.html',
	styles: [`
				#cell-editor {}
				#cell-editor-discard-button {}
				#cell-editor-save-button {}
				#cell-editor-create-value-button {}
				#cell-editor-remove-value-button {}
				.cell-editor-value {}
				.cell-editor-category {}
				.cell-editor-category-link {}
	`]
})


export class CellEditorComponent extends EventListener implements OnInit, OnDestroy {

@ViewChild('editor') editor: ElementRef;
//@ViewChild('form_', {static: false}) ngForm: NgForm;
private form: NgForm;

private formSubscription: Subscription;

cell: Cell;
cellBackup: Cell;
editing = false;
showAttributes = false;
showCategories = false;
categories: string[];
defaultCategoryAttributes: CellModel[];
attributesByCategory: Map<string, CellModel[]>;

constructor(eventService: EventService, private modalService: NgbModal) {
	super(eventService);
}


ngOnInit() {

	this.subscribe(this.events.service.of<CellEditEvent>(CellEditEvent)
			.pipe(filter(edit => edit.cell!==undefined && !this.editing))
			.subscribe(edit => this.edit(edit.cell))
	);

}


// we are unable to have this value injected as a viewchild so we have a direct method call
formCallback(f: NgForm) {

	// we only send an event when we are showing HTML and we need to handle presentation manually 
	if (this.cell.cellModel.getCellPresentationType()==='HTML') {
		// we can optimise this if we compare this cell with the backup one, handle dirtiness from the fields, etc
		//console.debug('>Firing CellChangedEvent');
		//this.events.service.publish(new CellChangedEvent(this.cell));
	}

}


ngOnDestroy() {

	if (this.formSubscription) {
		this.formSubscription.unsubscribe();
	}

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
		const cellModel = this.cell.cellModel;

		// do we show all attributes or do we do it by category
		this.showAttributes = cellModel.attributes!==undefined && cellModel.category===undefined;

		// handle attribute categories if needed
		this.showCategories =  cellModel.attributes!==undefined && cellModel.category!==undefined;
		if (this.showCategories) {
			this.defaultCategoryAttributes = cellModel.getAttributesInCategory(cellModel.category);
			this.attributesByCategory = cellModel.getAttributesByCategory();
			this.categories = cellModel.getCategories().filter(c => c!==cellModel.category);
			const attributesWithNoCategory = cellModel.getAttributesInCategory(undefined);
			this.defaultCategoryAttributes.concat(attributesWithNoCategory);
		}

		this.modalService.open(this.editor).result.then((result) => this.button(result), (reason) => this.outside());

	} else {
		console.debug("We're editing a cell-well so need to load a fragment of the content");
		this.events.service.publish(new ContentFragmentDisplayEvent(cell));
	}

}


//TODO: this is duplicated code, should refactor
private cellPresentationIsIMG(): boolean {
	return this.cell.cellModel.getCellPresentationType()===CellModel.DEFAULT_PRESENTATION_TYPE;
}


private showPresentation() {
	return this.cell.cellModel.presentation.startsWith("CELL");
}


private getPresentation(): string {
	return this.cell===undefined ? this.cell.cellModel.getCellPresentation() : this.cell.getCellPresentation();
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
	Promise.resolve(null).then(() => {
		this.cell.createValue();
		this.events.service.publish(new CellChangedEvent(this.cell));
	});

}


private removeValue() {

	console.log("[UI] Removing value for '%s'", this.cell.URI);
	Promise.resolve(null).then(() => {
		this.cell.removeValue();
		this.events.service.publish(new CellChangedEvent(this.cell));
	});

}


private modified(e) {
	this.events.service.publish(new CellChangedEvent(this.cell));
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
