// ATTRIBUTE DATA EDITOR . COMPONENT . TS

import { Component, Input } from '@angular/core';
import { zip } from 'rxjs/operators';

import { Arrows } from './arrows.class';

@Component({
	selector: 'arrows',
	templateUrl: './arrows.component.svg'
})

export class ArrowsComponent {

@Input() id: string;
@Input() arrows: Arrows;

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
