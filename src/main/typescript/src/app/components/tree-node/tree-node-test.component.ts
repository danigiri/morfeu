// TREE - NODE - TEST . COMPONENT . TS

import { Component } from "@angular/core";

import { Model } from "src/app/model.class";

import { TestComponent } from "src/app/test/test-component.class";
import { TreeNodeComponent } from "./tree-node.component";
import { _document1Document } from "src/app/test/test.data";

@Component({
	selector: 'tree-node-test',
	template: `
	<ul *ngIf="model" class="list-group">
		<tree-node *ngFor=" let m of model.children; index as i" [node]="m">
			<cell-model [cellModel]="m" [index]="i"></cell-model>
		</tree-node>
	</ul>
	`
})

export class TreeNodeTestComponent extends TestComponent {

	model?: Model;

protected override test(case_: string): void {
	console.debug('Show arrows on load test');
	let document = this.createDocument(_document1Document);
	this.loadModel(document.modelURI);
}

protected override loadedModel(model: Model): void {
	this.model = model;
	console.log('model loaded');
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
