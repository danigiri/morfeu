// TEST - ROUTING . MODULE . TS

import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

import { ComponentsModule} from '../components/components.module';

import { CellEditorTestComponent} from '../components/cell-editor/cell-editor-test.component';
import { ContentTestComponent } from '../components/content/content-test.component';
import { PresentationTestComponent} from '../components/presentation/presentation-test.component';

const routes: Routes = [
						{path: 'cell-editor-test/:case_', component: CellEditorTestComponent},
						{path: 'presentation-test/:case_', component: PresentationTestComponent},
						{path: 'content-test/:case_', component: ContentTestComponent}
];

@NgModule({
	imports: [
				ComponentsModule,
				CommonModule,
				FormsModule,
				RouterModule.forChild(routes)
	],
	exports: [RouterModule],
	declarations: [
					CellEditorTestComponent,
					ContentTestComponent,
					PresentationTestComponent
	]
})

export class TestRoutingModule { }

/*
 *	  Copyright 2019 Daniel Giribet
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
