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

import { SnippetDisplayedEvent } from '../../events/snippet-displayed.event';
import { RemoteObjectService } from '../../services/remote-object.service';
import { EventService } from '../../services/event.service';

@Component({
	selector: 'snippets-list-test',
	template: `
		<snippets [snippetStubs]="snippets" [model]="model"></snippets>
		<key-capture></key-capture>
	`
})

export class SnippetsListTestComponent extends TestComponent implements OnInit {

snippets: CellDocument[];
model: Model;

@ViewChild(SnippetsListComponent) snippetsComponent: SnippetsListComponent;


constructor(eventService: EventService,
			route: ActivatedRoute,
			@Inject("ContentService") protected contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") protected modelService: RemoteObjectService<Model, ModelJSON>) {

	super(eventService, route, contentService, modelService);

	// we create in the constructor so the view will have values to inject
	this.snippets = [];
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
	Promise.resolve(null).then(() => {
										this.snippets = this.createSnippets(_snippets);	// a bit forced but works
										this.snippetsComponent.snippetStubs = this.snippets;
										this.snippetsComponent.fetchSnippets();
	});
}


}