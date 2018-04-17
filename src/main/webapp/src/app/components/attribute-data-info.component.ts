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
	selector: 'attribute-data-info',
	template: `
		<li *ngIf="isFromModel || (isFromCell && hasValue)" 
			class="attribute-data attribute-data-info list-group-item"
			[class.list-group-item-secondary]="isFromModel"
			>{{parentCell.identifier}}
			<span class="font-weight-bold attribute-data-name"
			    [class.attribute-data-identifier]="isIdentifier()"
			>{{cellModel.name}}<ng-container *ngIf="cellModel.minOccurs==1">*</ng-container>:</span>
			<span *ngIf="hasValue" class="attribute-data-value">{{getValue()}}</span>
			<span class="text-muted attribute-data-type-name float-right">({{cellModel.type_.name}})</span>
		</li>
		`,
		styles:[`
				.attribute-data {}
				.attribute-data-info {}
                .attribute-data-name {}
                .attribute-data-identifier {
                    text-decoration: underline;
                }
				.attribute-data-value {}
				.attribute-data-type-name {}
				.attribute-data-from-model {}
		`]
})

export class AttributeDataInfoComponent {

@Input() isFromCell: boolean;	 // if it's form a cell and we have no value we skip this attribute
@Input() isFromModel: boolean;
@Input() parentCell?: Cell;	   // optional, only when showing information of cell (and not just a cell model)
@Input() cellModel: CellModel;
hasValue: boolean;

ngOnInit() {
	this.hasValue = this.hasValue_();
}


// do we have a value to show?
private hasValue_(): boolean {
	return this.parentCell && this.parentCell.attributes 
			&& this.parentCell.attributes.find(a => a.name==this.cellModel.name)!=undefined;
}


private isIdentifier(): boolean {
    return this.parentCell!=undefined && this.parentCell.cellModel.identifier!=undefined 
    && this.parentCell.cellModel.identifier==this.cellModel;
}

getValue(): string {
	return this.parentCell.attributes.find(a=> a.name==this.cellModel.name).value;
}


hasDesc(): boolean {
	return this.cellModel.desc!=undefined || this.cellModel.type_.desc!=undefined;
}


getDesc(): string {
	return (this.cellModel.desc && this.cellModel.desc.length!=0) 
			? this.cellModel.desc : this.cellModel.type_.desc;
}

}
