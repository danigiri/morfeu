// ATTRIBUTE - DATA - INFO . COMPONENT . TS

import { Component, Input } from '@angular/core';

import { Cell } from "../../cell.class";
import { CellModel } from "../../cell-model.class";

@Component({
	selector: "attribute-data-info",
	templateUrl: './attribute-data-info.component.html',
		styles:[`
				.attribute-data {}
				.attribute-data-info {}
				.attribute-data-name {}
				.attribute-data-identifier {
					text-decoration: underline;
				}
				.attribute-data-value {}
				.attribute-data-boolean {}
				.attribute-data-type-name {}
				.attribute-data-from-model {}
		`]
})

export class AttributeDataInfoComponent {

@Input() isFromCell: boolean;	 // if it's form a cell and we have no value we skip this attribute
@Input() isFromModel: boolean;
@Input() parentCell?: Cell;	   // optional, only when showing information of cell (and not just a cell model)
@Input() cellModel: CellModel;	// always there
@Input() index: number;

isBoolean: boolean = false;


ngOnInit() {

	this.isBoolean = this.cellModel.presentation === CellModel.ATTR_BOOLEAN_PRESENTATION;

}


// do we have a value to show?
hasValue_(): boolean {
	return this.parentCell?.getAttribute(this.cellModel.name)!=undefined;
}


private isIdentifier(): boolean {
	return this.parentCell?.cellModel.identifier!=undefined 
			&& this.parentCell.cellModel.identifier==this.cellModel;
}


private getValue(): string {
	return this.parentCell?.getAttribute(this.cellModel.name).value;
}


hasDesc(): boolean {
	return this.cellModel.desc!=undefined || this.cellModel.type_.desc!=undefined;
}


getDesc(): string {
	return (this.cellModel.desc && this.cellModel.desc.length!=0) 
			? this.cellModel.desc : this.cellModel.type_.desc;
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
