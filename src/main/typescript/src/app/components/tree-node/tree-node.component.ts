import { Component, OnInit, Input, TemplateRef, ContentChild, Output, EventEmitter } from '@angular/core';
import { TreeNodeDirective } from './tree-node.directive';

export interface TreeNode {
	children : TreeNode[]
}

@Component({
  selector: 'tree-node',
  template: `
	<li class="list-group-item">
		<ng-container *ngTemplateOutlet="nodeTemplate || template; context: { $implicit: node }"></ng-container>
			<ul *ngIf="node.children && node.children.length>0" class="list-group">
				<ng-container *ngFor="let n of node.children; index as i">
					<tree-node [node]="n" [index]="i" [level]="level+1" [template]="nodeTemplate || template"></tree-node>
				</ng-container>
		</ul>
	</li>
  `
})
export class TreeNodeComponent {

@Input() node: TreeNode & any;
@Input() template: TemplateRef<any>
@Input() index: number = 0;
@Input() level: number = 0;

 @ContentChild( TreeNodeDirective, { read: TemplateRef } ) nodeTemplate:any;

}
