// ARROW - TEST . COMPONENT . TS
import { Component } from '@angular/core';

import { Arrow } from './arrow.class';

import { _types } from '../../test/test.data';
import { TestComponent } from '../../test/test-component.class';

@Component({
	selector: 'arrow-test',
	template: `
		<arrow *ngIf="arrows.length>0" [arrows]="arrows"></arrow>
	`
})

export class ArrowsTestComponent extends TestComponent {

public arrows: Arrow[] = [];


protected test(case_: string): void {
	switch (case_) {
		case 'arrow' : this.showArrow(); break;
		default: this.showArrow();
	}
}


private showArrow() {
	const w = 720;
	const h = 480;
	Promise.resolve(null).then(() => this.arrows.push(new Arrow(4, 4, w/4+10, h/4, w/2-10, h/2-10, 0.5, 1, 1)));
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