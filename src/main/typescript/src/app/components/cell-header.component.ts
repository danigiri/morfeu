import { Component, Input } from "@angular/core";
import { CellModel } from "../cell-model.class";

@Component({
	moduleId: module.id,
	selector: 'cell-header',
	template: `
			<h4 class="cell-header card-title card-header">
				{{cellModel.name}}
				[{{cellModel.minOccurs}}..<ng-container *ngIf="cellModel.maxOccurs && cellModel.maxOccurs!=-1">{{cellModel.maxOccurs}}</ng-container><ng-container *ngIf="!cellModel.maxOccurs || cellModel.maxOccurs==-1">∞</ng-container>]
					<span *ngIf="cell!=undefined" class="cell-data-source badge badge-pill badge-secondary float-secondary float-right">CELL</span>
					<span *ngIf="cell==undefined" class="cell-data-source badge badge-pill badge-dark float-dark float-right">MODEL</span>
			</h4>
			<div class="card-body">
				<p class="cell-header-desc card-subtitle">{{cellModel.desc}}<p>
				<p class="cell-header-uri card-text">URI: <span class="cell-data-uri text-muted">{{uri}}</span></p>
			</div>
	`,
	styles: [`
			.cell-data-header {}
			.cell-header-desc {}
			.cell-header-uri {}
	`]
})

export class CellHeaderComponent {

@Input() uri: string;
@Input() cellModel: CellModel;

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
