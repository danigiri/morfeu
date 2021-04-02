// LINKS - TEST . COMPONENT . TS
import { Component } from '@angular/core';

import { CellDocument } from '../../cell-document.class';
import { Model } from '../../model.class';

import { Arrow } from './arrow.class';
import { Arrows } from './arrows.class';

import { _typesDocument } from '../../test/test.data';
import { TestComponent } from '../../test/test-component.class';

import { ContentRequestEvent } from '../../events/content-request.event';

@Component({
	selector: 'arrow-test',
	template: `
		<links *ngIf="arrows" [arrows]="arrows" [id]="'test'"></links>
		<content *ngIf="document"></content>
	`
})

export class LinksTestComponent extends TestComponent {

arrows: Arrows;
document: CellDocument;


protected test(case_: string): void {
	switch (case_) {
		case 'arrow' : this.showArrow(); break;
		case 'load' : this.showLinksOnLoad(); break;
		default: this.showLinksOnLoad();
	}
}


private showArrow() {

	console.debug('Show arrow test');
	this.arrows	= new Arrows();
	this.arrows.push(new Arrow(0,0, 128, 128));

}


private showLinksOnLoad() {

	console.debug('Show arrows on load test');
	this.document = this.createDocument(_typesDocument);
	this.loadModel(this.document.modelURI);

}


protected loadedModel(model: Model): void {
	this.events.service.publish(new ContentRequestEvent(this.document, model))
}


}

/*
 *	  Copyright 2021 Daniel Giribet
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