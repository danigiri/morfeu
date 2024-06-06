import { Component, OnInit, Input, TemplateRef, ContentChild, Output, EventEmitter, OnChanges, SimpleChanges, ChangeDetectorRef, ViewChildren } from '@angular/core';
import { TreeNodeDirective } from './tree-node.directive';

export interface TreeNode {
	children: TreeNode[];
	index: number;
}

@Component({
	selector: 'tree-node',
	template: `
	<li class="list-group-item tree-node tree-node-index-{{index}} tree-node-level-{{level}}"
		[class.tree-node-collapsed]="!expanded"
		[class.tree-node-expanded]="expanded"
		style="border: 0">
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
			<ng-container *ngFor="let n of node.children">
				<tree-node [node]="n" [index]="n.index" [level]="level+1" [template]="nodeTemplate || template" [expanded]="true"></tree-node>
			</ng-container>
		</ul>
	</li>
  `
})
export class TreeNodeComponent {
	
@Input() node: TreeNode & any;
@Input() template: TemplateRef<any>
@Input() index: number = 0;
@Input() level = 0;
@Input() expanded = true;

@ContentChild(TreeNodeDirective, { read: TemplateRef }) nodeTemplate: any;
@ViewChildren(TreeNodeComponent) children: TreeNodeComponent[];

expandedButton() {
	return this.expanded ? '-' : '+';
}


toggle() {
	Promise.resolve(null).then(() => this.setExpanded(!this.expanded));
}


expand() {
	Promise.resolve(null).then(() => this.setExpanded(true));
}


expandAll() {
	Promise.resolve(null).then(() => this.setExpandedAll(true));
}


close() {
	Promise.resolve(null).then(() => this.expanded = false);
}


private setExpanded(value: boolean) {
	this.expanded = value;
}


private setExpandedAll(value: boolean) {
	let nodes: TreeNodeComponent[] = [this];
	while (nodes.length>0) {
		const n = nodes.pop();
		n.expanded = value;
		n.children?.forEach(c => nodes.push(c));
	}
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
