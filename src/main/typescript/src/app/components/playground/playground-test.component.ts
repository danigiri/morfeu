// PLAYGROUND - TEST . COMPONENT . TS

import { Component } from '@angular/core';

import { TestComponent } from '../../test/test-component.class';


@Component({
	selector: 'playground-test',
	template: `
	<div class="container text-center">
	  <div class="row myrow">
	    <div class="col-4 mycol">
			<img src="http://localhost:3000/assets/images/stuff-cell.svg" />
			<img src="localhost:3000/dyn/preview/svg/data2.svg?__header=data2&number=42&text=blahblah"/>
	    </div>
	    <div class="col-8 mycol">
		    <div class="row myrow">
		    <div class="col-6 mycol">col</div>
		    <div class="col-6 mycol">col</div>
		    </div>
	    </div>
	  </div>
	</div>
	`,
	styles: [
	     	'.myrow { border: 1px solid red;}',
	    	'.mycol { border: 1px solid blue;}',
	         ]
})

export class PlaygroundTestComponent extends TestComponent {

protected test(case_: string) {
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