// CONTENT - TEST . COMPONENT . TS

import { AfterViewInit, Component, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { TestComponent } from '../../test/test-component.interface';

@Component({
	selector: 'content-test',
	template: `

	`
})

export class ContentTestComponent implements AfterViewInit, TestComponent {

constructor(private route: ActivatedRoute) {}

ngAfterViewInit() {

	this.route.paramMap.subscribe(params => this.load(params.get('case_')));
}


load(case_: string) {
	switch (case_) {
		//case 'post' : this.loadPOST(); break;
		default: this.readonly();
	}
}


private readonly() {



}

}