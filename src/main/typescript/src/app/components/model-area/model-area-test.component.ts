// MODEL - AREA - TEST . COMPONENT . TS

import { Component, Inject, AfterViewInit } from '@angular/core';

import { CellDocument } from 'src/app/cell-document.class';
import { Model } from 'src/app/model.class';

import { _document1Document } from 'src/app/test/test.data';
import { TestComponent } from 'src/app/test/test-component.class';

import { ModelRequestEvent } from 'src/app/events/model-request.event';
import { Catalogue } from 'src/app/catalogue.class';
import { CatalogueLoadedEvent } from 'src/app/events/catalogue-loaded.event';

@Component({
	selector: 'model-area-test',
	template: '<model-area></model-area>'
})

export class ModelAreaTestComponent extends TestComponent {

document: CellDocument;

protected test(case_: string) {
	this.model();
}


private model() {
	console.debug('Load model test')
	this.document = this.createDocument(_document1Document);
	this.events.service.publish(new ModelRequestEvent(this.document));
	this.loadCatalogue('/test-resources/catalogues/catalogue1.json');
}


protected override loadedModel(model: Model): void {}


protected override loadedCatalogue(catalogue: Catalogue): void {
	this.events.service.publish(new CatalogueLoadedEvent(catalogue));
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
