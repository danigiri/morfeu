import { Component, OnInit, Input, TemplateRef, ContentChild, Output, EventEmitter, OnChanges, SimpleChanges, ChangeDetectorRef } from '@angular/core';
import { TreeNodeDirective } from './tree-node.directive';

export interface TreeNode {
	children: TreeNode[]
}

@Component({
	selector: 'tree-node',
	template: `
	<li class="list-group-item" style="border: 0">
		<!-- current node -->
		<div class="container-fluid">
			<div class="row">
				<div class="col-1">
					<button 
						*ngIf="node.children && node.children.length > 0" 
						type="button" 
						class="btn"
						(click)="toggle()"
						>{{expandedButton()}}</button>
				</div>
				<div class="col">
					<ng-container *ngTemplateOutlet="nodeTemplate || template; context: { $implicit: node }"></ng-container>
				</div>
			</div>
		</div>
		<!-- children -->
		<ul *ngIf="expanded && node.children && node.children.length>0" class="list-group">
			<ng-container *ngFor="let n of node.children; index as i">
				<tree-node [node]="n" [index]="i" [level]="level+1" [template]="nodeTemplate || template" [expanded]="true"></tree-node>
			</ng-container>
		</ul>
	</li>
  `
})
export class TreeNodeComponent {
	
@Input() node: TreeNode & any;
@Input() template: TemplateRef<any>
@Input() index = 0;
@Input() level = 0;
@Input() expanded = false;

@ContentChild(TreeNodeDirective, { read: TemplateRef }) nodeTemplate: any;

expandedButton() {
	return this.expanded ? '-' : '+';
}


toggle() {
	Promise.resolve(null).then(() => this.expanded = !this.expanded);
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
