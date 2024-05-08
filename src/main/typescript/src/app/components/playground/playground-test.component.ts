// PLAYGROUND - TEST . COMPONENT . TS

import { Component } from '@angular/core';

import { CellDocument } from '../../cell-document.class';
import { Model } from '../../model.class';

import { _readonlyDocument, _document1Document, _document3Document, _document5Document } from '../../test/test.data';

import { TestComponent } from '../../test/test-component.class';


@Component({
	selector: 'playground-test',
	template: `
	<div class="card">
		<div class="card-body">
			<h1 class="card-title">Morfeu application</h1>
			<div class="container-fluid">
				<div class="row">
					<div class="col-2">
						catalogue goes here
					</div>
					<div class="col-8">	
						<!-- content starts here -->
						<img src="/assets/images/test-cell.svg" />
						<div class="container">
							<div class="row"> <!-- well drop area -->
								<div class="col-12">
									<div class="mydroparea">
										<small>&nbsp;       0       &nbsp;</small>
									</div>
								</div>
							</div> <!-- well drop area end -->
							<div class="row"> <!-- row image for the row that comes next-->
								<div class="col-12">
									<img src="/assets/images/row-cell.svg" />
								</div>
							</div>
						  	<div class="row myrow">
							    <div class="col-4 mycol">
							    	<div> <!-- cell -->
								    	<img src="/assets/images/stuff-cell.svg" />
					    			</div>
							    </div>
							    <div class="col-8 mycol">
									<div class="row"> <!-- row image for the row that comes next-->
										<div class="col-12">
											<img src="/assets/images/row-cell.svg" />
										</div>
									</div>
								    <div class="row myrow">
									    <div class="col-6 mycol">
								    	<div> <!-- cell -->
									    	<img src="/assets/images/stuff-cell.svg" />
										</div>
										<div> <!-- cell -->
											<img src="/dyn/preview/svg/data2.svg?__header=data2&number=42&text=blahblah"/>
										</div>
										<div class="col-6 mycol">
									    	<div> <!-- cell -->
											    <img src="/assets/images/stuff-cell.svg" />
											</div>
									    	<div> <!-- cell -->
											    <img src="/assets/images/stuff-cell.svg" />
											</div>
										</div>
									</div>
								</div>
							</div>
				    	</div> <!-- top level container -->
					</div>
				</div>
				<div class="row"> <!-- main row drop area -->
					<div class="col-12">
						<div class="mydroparea">
							<small>&nbsp;       0     x  &nbsp;</small>
						</div>
					</div>
				</div>
			</div>
			<!-- content ends here -->
		<div class="col-2">
			model goes here
		</div>
	</div>
	`,
	styles: [`
	     	.myrow { border: 1px solid red;}
	    	.mycol { border: 1px solid blue;}
	    	.mydroparea {
	    		background-color: grey;
	    	}
	         `]
})

export class PlaygroundTestComponent extends TestComponent {

document: CellDocument;

protected test(case_: string) {
	console.debug('Drag and Drop 1 test')
	this.document = this.createDocument(_document1Document);
	this.loadModel(this.document.modelURI);
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