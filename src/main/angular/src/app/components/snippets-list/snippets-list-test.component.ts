// SNIPPETS - LIST - TEST . COMPONENT . TS

import { Inject, Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { CellDocument } from '../../cell-document.class';
import { Content, ContentJSON } from '../../content.class';
import { Model, ModelJSON } from '../../model.class';

import { TestComponent } from '../../test/test-component.class';

import { RemoteObjectService } from '../../services/remote-object.service';

import { EventService } from '../../services/event.service';

@Component({
	selector: 'snippets-list-test',
	template: '<snippets [snippetStubs]="snippets" [model]="model"></snippets>'
})

export class SnippetsListTestComponent extends TestComponent {

snippets: CellDocument[];


constructor(eventService: EventService,
			route: ActivatedRoute,
			@Inject("ContentService") protected contentService: RemoteObjectService<Content, ContentJSON>,
			@Inject("ModelService") protected modelService: RemoteObjectService<Model, ModelJSON>) {

	super(eventService, route, contentService, modelService);

	// need to inject 

}



protected test(case_: string): void {
	switch (case_) {
		default:
			this.display();
	}
}


private display() {
	
}


}