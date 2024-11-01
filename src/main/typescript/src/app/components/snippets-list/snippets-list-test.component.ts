// SNIPPETS - LIST - TEST . COMPONENT . TS

import { Inject, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { filter } from 'rxjs/operators';

import { CellDocument } from '../../cell-document.class';
import { Content, ContentJSON } from '../../content.class';
import { Model, ModelJSON } from '../../model.class';

import { SnippetsListComponent } from './snippets-list.component';

import { _model, _snippets } from '../../test/test.data';
import { TestComponent } from '../../test/test-component.class';

import { Configuration } from 'src/app/config/configuration.class';
import { SnippetDisplayedEvent } from '../../events/snippet-displayed.event';
import { SnippetsDisplayEvent } from 'src/app/events/snippets-display.event';
import { RemoteObjectService } from '../../services/remote-object.service';
import { RemoteDataService } from 'src/app/services/remote-data.service';
import { EventService } from '../../services/event.service';
import { Catalogue } from 'src/app/catalogue.class';
import { InfoModeEvent } from 'src/app/events/info-mode.event';

@Component({
	selector: 'snippets-list-test',
	template: `
		<snippets [display]=true></snippets>
		<key-capture></key-capture>
	`
})

export class SnippetsListTestComponent extends TestComponent implements OnInit {

snippets: CellDocument[];
model: Model;

@ViewChild(SnippetsListComponent) snippetsComponent: SnippetsListComponent;


constructor(eventService: EventService,
			config: Configuration,
			route: ActivatedRoute,
			@Inject("ContentService") protected override contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") protected override modelService: RemoteObjectService<Model, ModelJSON>,
			@Inject("RemoteJSONDataService") catalogueService: RemoteDataService,
		) {

	super(eventService, config, route, contentService, modelService, catalogueService);

	// we create in the constructor so the view will have values to inject and create the component
	this.snippets = [];
	// TODO: load from model instead of static as we are not testing the model
	this.model = this.createModel(_model);

}


ngOnInit() {

	// when we are finished loading we are ready for keypresses
	this.register(this.events.service.of<SnippetDisplayedEvent>(SnippetDisplayedEvent)
			.pipe(filter(displayed => displayed.position===this.snippetsComponent.currentSnippets().length-1))
			.subscribe(() => this.snippetsComponent.activateSnippetSelectingMode())
	);
}


protected test(case_: string): void {
	switch (case_) {
		default:
			this.display();
	}
}


private display() {
	console.debug('Testing catalogue 1 snippets...');
	this.loadCatalogue('/test-resources/catalogues/catalogue1.json');
}

protected override loadedCatalogue(catalogue: Catalogue): void {	
	this.snippets = catalogue.snippets;
	this.snippetsComponent.model = this.model;
	this.events.service.publish(new SnippetsDisplayEvent(catalogue.snippets, catalogue));
	// useful for development
	this.events.service.promiseToPublishDelayed(new InfoModeEvent(true),500);

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
