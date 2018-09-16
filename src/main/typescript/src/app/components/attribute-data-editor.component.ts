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

import { Component, Input, OnInit } from "@angular/core";

import { Cell } from "../cell.class";
import { CellModel } from "../cell-model.class";

@Component({
	moduleId: module.id,
	selector: "attribute-data-editor",
	template: `
		<li class="attribute-data attribute-data-editor list-group-item" [attr._index]="index">
			<div *ngIf="hasValue_()" class="input-group input-group-sm mb-3">
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
				/>
				<div *ngIf="cellModel.minOccurs==0" class="input-group-append">
					<img class="btn float-right attribute-data-delete"
						src="assets/images/open-iconic/circle-x.svg"
						(click)="delete()"
					/>
				</div>
			</div>
			<div *ngIf="!hasValue_()" class="list-group-item disabled">
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


export class AttributeDataEditorComponent {

@Input() cellModel: CellModel;
@Input() parentCell: Cell;
@Input() index: number;


// do we have a value to show?
private hasValue_(): boolean {
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

// delete current value
private delete() {

	console.log("[UI] deleting cell attribute ", this.cellModel.name);
	Promise.resolve(null).then(() =>
		this.parentCell.remove(this.parentCell.attributes.find(a => a.name===this.cellModel.name)));
}


// add current attribute with empty or default value
private add() {
	console.log("[UI] adding cell attribute ", this.cellModel.name);
	Promise.resolve(null).then(() => this.parentCell.adopt(this.cellModel.generateCell()));
}

}