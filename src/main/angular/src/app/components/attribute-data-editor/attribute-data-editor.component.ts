// ATTRIBUTE DATA EDITOR . COMPONENT . TS

import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';

import { Cell } from '../../cell.class';
import { CellModel } from '../../cell-model.class';

import { EventListener } from '../../events/event-listener.class';
import { CellChangeEvent, CellChange } from '../../events/cell-change.event';
import { EventService } from '../../services/event.service';

@Component({
	selector: 'attribute-data-editor',
	templateUrl: 'attribute-data-editor.component.html',
	styles: [`
				.attribute-data {}
				.attribute-not-present {}
				.attribute-data-editor {}
				.attribute-data-editor-identifier {
					text-decoration: underline;
				}
				.attribute-data-name {}
				.attribute-data-value {}
				.attribute-data-validation-warning {
					color: red !important;
				}
				.attribute-data-delete {}
				.attribute-data-add {}
				.attribute-data-boolean {}
	`]
})


export class AttributeDataEditorComponent extends EventListener implements OnInit {

@Input() cellModel: CellModel;
@Input() parentCell: Cell;
@Input() index: number;

isBoolean: boolean = false;
validates: boolean;
validationWarning: string;

private regexp: RegExp;

@ViewChild('input') input: ElementRef;


constructor(eventService: EventService) {
	super(eventService);
}


ngOnInit() {

	this.isBoolean = this.cellModel.presentation === CellModel.ATTR_BOOLEAN_PRESENTATION;
	this.regexp = this.cellModel.type_.regex ? new RegExp(this.cellModel.type_.regex) : undefined;
	// if we have a value at the beginning
	const attr = this.parentCell.getAttribute(this.cellModel.name);
	const valid = this.isValueOK(this.value);
	this.events.service.publish(new CellChangeEvent(this.parentCell, CellChange.INIT_ATTRIBUTE, attr, valid));
	if (!valid) {
		this.validationWarning = 'Should match '+this.cellModel.type_.regex;
	}

}


// do we have a value to show?
isPresent(): boolean {
	return this.parentCell.getAttribute(this.cellModel.name)!==undefined;
}


get value(): string {
	return this.parentCell.getAttribute(this.cellModel.name).value;
}


set value(v: string) {

	this.parentCell.getAttribute(this.cellModel.name).value = v;

}


get booleanValue(): boolean {
	return this.parentCell.getAttribute(this.cellModel.name).value==='true';
}


set booleanValue(v: boolean) {
	this.parentCell.getAttribute(this.cellModel.name).value = v===true ? 'true' : 'false';
}




// add current attribute with empty or default value
private add() {

	console.log("[UI] adding cell attribute ", this.cellModel.name);
	Promise.resolve(null).then(() => {
		const attr = this.cellModel.generateCell();
		//this.parentCell.adopt(attr);
		const valid = this.isValueOK(attr.value);	// default value could be valid from the start
		this.events.service.publish(new CellChangeEvent(this.parentCell, CellChange.ADD_ATTRIBUTE, attr, valid));
	});

}


private modified(e) {

	const attr = this.parentCell.getAttribute(this.cellModel.name);
	const valid = this.isValueOK(attr.value);
	this.events.service.publish(new CellChangeEvent(this.parentCell, CellChange.MODIFIED_ATTRIBUTE, attr, valid));

}


// delete current value
private delete() {

	console.log("[UI] deleting cell attribute ", this.cellModel.name);
	Promise.resolve(null).then(() => {
	//	this.parentCell.remove(this.parentCell.getAttribute(this.cellModel.name))
		const attr = this.parentCell.getAttribute(this.cellModel.name);
		this.events.service.publish(new CellChangeEvent(this.parentCell, CellChange.REMOVE_ATTRIBUTE, attr));
	});

}


private isValueOK(v: string) {

	const result = this.regexp.exec(v);

	return result?.length===1 && result[0].length===v.length;	// a single match and for the whole thing

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
