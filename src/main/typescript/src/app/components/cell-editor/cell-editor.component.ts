// CELL - EDITOR . COMPONENT . TS

import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { filter } from 'rxjs/operators';
import { NgForm } from '@angular/forms';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Cell } from '../../cell.class';
import { CellModel } from '../../cell-model.class';

import { CellActivatedEvent } from '../../events/cell-activated.event';
import { CellChangeEvent, CellChange } from '../../events/cell-change.event';
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


export class CellEditorComponent extends EventListener implements OnInit {

@ViewChild('editor') editor: ElementRef;
//@ViewChild('form_', {static: false}) ngForm: NgForm;
private form: NgForm;

cell: Cell;
cellBackup: Cell;
editing = false;
showAttributes = false;
showCategories = false;
categories: string[];
attributesByCategory: Map<string, CellModel[]>;
activeCategory:string = null;
canSave: boolean = false;


constructor(eventService: EventService, private modalService: NgbModal) {
	super(eventService);
}


ngOnInit() {

	// trigger cell edits from keyboard, clicking...
	this.register(this.events.service.of<CellEditEvent>(CellEditEvent)
			.pipe(filter(edit => edit.cell!==undefined && !this.editing))
			.subscribe(edit => this.edit(edit.cell))
	);

	// attributes or values are being edited (using the filter), so we act accordingly
	// we are not interested in the COMPLETED subevent given we generate it, it's for other components
	this.register(this.events.service.of<CellChangeEvent>(CellChangeEvent)
			.pipe(filter(changed => changed.what!==CellChange.COMPLETED))
			.pipe(filter(changed => this.editing))
			.subscribe(changed => this.editorChange(changed.attribute, changed.what, changed.valid))
	);

}


// we are unable to have this value injected as a viewchild so we have a direct method call
formCallback(f: NgForm) {

	// we only send an event when we are showing HTML and we need to handle presentation manually 
	if (this.cell?.cellModel.getCellPresentationType()==='HTML') {
		// we can optimise this if we compare this cell with the backup one, handle dirtiness from the fields, etc
		//console.debug('>Firing CellChangedEvent');
		//this.events.service.publish(new CellChangedEvent(this.cell));
	}

}


showValue() {
	return this.showText() || this.showCode();
}


showText() {
	return this.cell.cellModel.presentation.includes("TEXT"); // if we need to show the text area or not
}


showCode() {
	return this.cell.cellModel.presentation.includes("CODE"); // if we need to show the text area or not	
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
		const categoryCount = cellModel.getCategories().length;
		this.showAttributes = cellModel.attributes!==undefined && categoryCount<2;
		this.showCategories = cellModel.attributes!==undefined && categoryCount>1;

		// handle attribute categories if needed
		if (this.showCategories) {
			const defaultCategoryAttributes = cellModel.getAttributesInCategory(cellModel.category);
			const attributesWithNoCategory = cellModel.getAttributesInCategory(undefined);
			defaultCategoryAttributes.concat(attributesWithNoCategory);
			this.attributesByCategory = cellModel.getAttributesByCategory();
			this.attributesByCategory.set(cellModel.category, defaultCategoryAttributes);
			// default category goes first
			this.categories = cellModel.getCategories().filter(c => c!==cellModel.category);
			this.categories.unshift(cellModel.category);
			this.activeCategory = cellModel.category;
		}

		this.modalService.open(this.editor).result.then((result) => this.button(result), (reason) => this.outside());

	} else {
		console.debug("We're editing a cell-well so need to load a fragment of the content");
		this.events.service.publish(new ContentFragmentDisplayEvent(cell));
	}

}


//TODO: this is duplicated code, should refactor
cellPresentationIsIMG(): boolean {
	return this.cell?.cellModel.getCellPresentationType()===CellModel.DEFAULT_PRESENTATION_TYPE;
}


showPresentation() {
	return this.cell?.cellModel.presentation.startsWith("CELL");
}


getPresentation(): string {
	return this.cell===undefined ? this.cell.cellModel.getCellPresentation() : this.cell.getCellPresentation();
}



createValue() {

	console.log("[UI] Create new (empty|default) value for '%s'", this.cell.URI);
	Promise.resolve(null).then(() => {
		this.cell.createValue();
		const valid = this.cell.cellModel.validates(this.cell.value);
		this.events.service.publish(new CellChangeEvent(this.cell, CellChange.CREATED_VALUE, valid));
	});

}


modifiedValue(e: any) {

	const valid = this.cell.cellModel.validates(this.cell.value);
	this.events.service.publish(new CellChangeEvent(this.cell, CellChange.MODIFIED_VALUE, valid));
}


removeValue() {

	console.log("[UI] Removing value for '%s'", this.cell.URI);
	Promise.resolve(null).then(() => {
		this.cell.removeValue();
		this.events.service.publish(new CellChangeEvent(this.cell, CellChange.REMOVED_VALUE, true));
	});

}

select(category: string) {
	this.activeCategory = category;
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


private editorChange(attribute: Cell, what: CellChange, isValid: boolean) {

	let canBeSaved: boolean;
	switch(what) {
		case CellChange.INIT_ATTRIBUTE:
			canBeSaved = false;			// if valid, when loading we want to disable saving as no modifications have
			break;						// been made, if invalid (for some reason), then we still do not want saving
		case CellChange.ADD_ATTRIBUTE:
			this.cell.adopt(attribute);
			canBeSaved = isValid;
		break;
		case CellChange.MODIFIED_ATTRIBUTE:
			// no action needed, the attribute state is owned and changed by the child component
			// though in the future we may change this for coherence
			canBeSaved = isValid;
		break;
		case CellChange.REMOVE_ATTRIBUTE:
			this.cell.remove(attribute)
			canBeSaved = true;
		break;
		case CellChange.CREATED_VALUE:
			canBeSaved = isValid;
		break;
		case CellChange.MODIFIED_VALUE:
			canBeSaved = isValid;
		break;
		case CellChange.REMOVED_VALUE:
			canBeSaved = isValid;
		break;
		default:
			console.error('Unknown attribute change');
	}
	Promise.resolve(null).then(() => this.canSave = canBeSaved);
	// changes now are done, we notify any listeners that the cell is now in the state it should be
	// so we can do things like change the presentation, etc
	this.events.service.publish(new CellChangeEvent(this.cell, CellChange.COMPLETED, isValid, attribute));

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
