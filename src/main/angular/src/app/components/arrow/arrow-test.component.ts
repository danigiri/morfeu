// ARROW - TEST . COMPONENT . TS
import { Component, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Arrow } from './arrow.class';
import { Content, ContentJSON } from '../../content.class';
import { Model, ModelJSON } from '../../model.class';

import { RemoteObjectService } from '../../services/remote-object.service';

import { TestComponent } from '../../test/test-component.class';

import { EventService } from '../../services/event.service';

@Component({
	selector: 'arrow-test',
	template: `
	<div><arrow *ngIf="arrow_" [arrow]="arrow_"></arrow></div>`
})

export class ArrowTestComponent extends TestComponent {

public arrow_: Arrow;


protected test(case_: string): void {
	switch (case_) {
		case 'arrow' : this.showArrow(); break;
		default: this.showArrow();
	}
}


private showArrow() {
	Promise.resolve(null).then(() => this.arrow_ = new Arrow(4, 4, 720/4+10, 480/4, 720/2-10, 480/2-10, 0.5, 1, 1));
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