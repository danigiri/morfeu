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

import { Component, Input, OnInit } from "@angular/core";

import { Cell } from "../cell.class";
import { CellModel } from "../cell-model.class";

@Component({
	moduleId: module.id,
	selector: 'attribute-info',
	template: `
		<li class="list-group-item attribute-info" [class.list-group-item-secondary]="!hasValue">
			<span class="font-weight-bold attribute-info-name">{{cellModel.name}}<ng-container *ngIf="cellModel.minOccurs==1">*</ng-container>:</span>
	        <span *ngIf="hasValue">{{getValue()}}</span>
			<span class="text-muted attribute-info-type-name float-right">({{cellModel.type_.name}})</span>
		</li>
		`,
		styles:[`
                .attribute-info {}
                .attribute-info-name {}
		        .attribute-info-type-name {}
		`]
})

export class AttributeInfoComponent {

hasValue: boolean = false;
@Input() cell?: Cell;    // optional, only when showing information of cell (and not just a cell model)
@Input() cellModel: CellModel;


ngOnInit() {
    this.hasValue = this.hasValue_();
}


// do we have a value to show?
private hasValue_(): boolean {
    return this.cell 
            && this.cell.attributes && this.cell.attributes.find(a => a.name==this.cellModel.name)!=undefined;
}


getValue(): string {
    return this.cell.attributes.find(a => a.name==this.cellModel.name).value;
}

hasDesc(): boolean {
    return this.cellModel.desc!=undefined || this.cellModel.type_.desc!=undefined;
}


getDesc(): string {
    return (this.cellModel.desc && this.cellModel.desc.length!=0) 
            ? this.cellModel.desc : this.cellModel.type_.desc;
}

}
