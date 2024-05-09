// TREE-NODE . COMPONENT . TS


import {AfterViewInit, Component, Inject, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, ViewChild} from "@angular/core";


@Component({
	selector: "tree-node",
	template: `
	aaaaa
		<li class="list-group-item">
			<ng-content></ng-content>
		</li>
	`,

	styles:[`
				#model-info {}
				#model-name {}
				#model-desc {}
				#model-cell-model-list {}
	`]
})

export class TreeNodeComponent  implements OnInit, OnDestroy, OnChanges {

@Input() node: any;
@Input() expanded: boolean = false;
@Input() expandedAll: boolean = false;
showChildren: boolean = false;

constructor() {

}

ngOnInit() {

}

ngOnChanges(changes: SimpleChanges) {
	// this is called whenever Angular detects changes to the input properties.
	// this.showChildren = 'node' in changes && changes.node && 'children' in changes.node && Array.isArray(changes.node.children);
}

ngOnDestroy() {

}

}

/*
	<ng-container *ngIf="showChildren">
			<ul>
						<!--tree-root
							[nodes]="model.children">
							<ng-template #treeNodeTemplate let-node let-index="index">
								<cell-model [node]="node" [index]="index"></cell-model>
							</ng-template>
						/tree-root-->
						
			</ul>
	</ng-container>
	*/

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
