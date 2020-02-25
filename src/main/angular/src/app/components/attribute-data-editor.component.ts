// ATTRIBUTE DATA EDITOR . COMPONENT . TS

import {Component, ElementRef, Input, ViewChild} from '@angular/core';

import {Cell} from '../cell.class';
import {CellModel} from '../cell-model.class';

import {EventListener} from '../events/event-listener.class';
import {CellChangedEvent} from '../events/cell-changed.event';
import {EventService} from '../services/event.service';

@Component({
	moduleId: module.id,
	selector: 'attribute-data-editor',
	template: `
		<li [class.attribute-data]="isPresent()"
			[class.attribute-not-present]="!isPresent()"
			class="attribute-data-editor list-group-item"
			[attr._index]="index"
		>
			<div *ngIf="isPresent()" class="input-group input-group-sm mb-3">
				<div class="input-group-prepend">
					<span class="attribute-data-name input-group-text"
						[class.attribute-data-editor-identifier]="parentCell.cellModel.identifier==cellModel"
						id="{{cellModel.name}}"
					>{{cellModel.name}}<ng-container *ngIf="cellModel.minOccurs==1">*</ng-container>:</span>
				</div>
				<input type="text"
					class="attribute-data-value form-control"
					attr.aria-label="{{cellModel.name}}"
					attr.aria-describedby="{{cellModel.desc}}"
					[(ngModel)]="this.value"
					(change)="modified($event)"
					(input)="modified($event)"
				/>
				<div *ngIf="cellModel.minOccurs==0" class="input-group-append">
					<img class="btn float-right attribute-data-delete"
						src="assets/images/open-iconic/circle-x.svg"
						(click)="delete()"
					/>
				</div>
			</div>
			<div *ngIf="!isPresent()" class="list-group-item">
				<small class="attribute-data-name">{{cellModel.name}}</small>
				<span class="badge float-right">
					<img class="btn float-right attribute-data-add"
						src="assets/images/open-iconic/plus.svg"
						(click)="add()"
					/>
				</span>
			</div>
		</li>
	`,
	styles: [`
				.attribute-data {}
				.attribute-not-present {}
				.attribute-data-editor {}
				.attribute-data-editor-identifier {
					text-decoration: underline;
				}
				.attribute-data-name {}
				.attribute-data-value {}
				.attribute-data-delete {}
				.attribute-data-add {}
	`]
})


export class AttributeDataEditorComponent extends EventListener {

@Input() cellModel: CellModel;
@Input() parentCell: Cell;
@Input() index: number;

@ViewChild('input') input: ElementRef;


constructor(eventService: EventService) {
	super(eventService);
}


// do we have a value to show?
isPresent(): boolean {
	return this.parentCell && this.parentCell.attributes
		&& this.parentCell.attributes.find(a => a.name===this.cellModel.name)!==undefined;
}


get value(): string {
	return this.parentCell.attributes.find(a=> a.name===this.cellModel.name).value;
}


set value(v: string) {

	let attributeCell = this.parentCell.attributes.find(a => a.name===this.cellModel.name);
	attributeCell.value = v;

}

// add current attribute with empty or default value
private add() {

	console.log("[UI] adding cell attribute ", this.cellModel.name);
	Promise.resolve(null).then(() => {
		this.parentCell.adopt(this.cellModel.generateCell());
		this.events.service.publish(new CellChangedEvent(this.parentCell));
	});

}


private modified(e) {
	this.events.service.publish(new CellChangedEvent(this.parentCell));
}


// delete current value
private delete() {

	console.log("[UI] deleting cell attribute ", this.cellModel.name);
	Promise.resolve(null).then(() => {
		this.parentCell.remove(this.parentCell.attributes.find(a => a.name===this.cellModel.name))
		this.events.service.publish(new CellChangedEvent(this.parentCell));
	});

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
