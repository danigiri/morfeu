// TEST - ROUTING . MODULE . TS

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ComponentsModule } from '../components/components.module';

import { ArrowContainerTestComponent } from '../components/arrow/arrow-container-test.component';
import { AttributeDataEditorTestComponent } 
	from '../components/attribute-data-editor/attribute-data-editor-test.component';
import { AttributeDataInfoTestComponent } from '../components/attribute-data-info/attribute-data-info-test.component';
import { BreadcrumbTestComponent } from '../components/breadcrumb/breadcrumb-test.component';
import { CellDataTestComponent } from '../components/cell-data/cell-data-test.component';
import { CellEditorTestComponent } from '../components/cell-editor/cell-editor-test.component';
import { CodeEditorTestComponent } from '../components/code-editor/code-editor-test.component';
import { ContentTestComponent } from '../components/content/content-test.component';
import { PresentationTestComponent } from '../components/presentation/presentation-test.component';
import { SnippetsListTestComponent } from '../components/snippets-list/snippets-list-test.component';

const routes: Routes = [
						{path: 'arrow-container-test/:case_', component: ArrowTestComponent},
						{path: 'attribute-data-editor-test/:case_', component: AttributeDataEditorTestComponent},
						{path: 'attribute-data-info-test/:case_', component: AttributeDataInfoTestComponent},
						{path: 'breadcrumb-test/:case_', component: BreadcrumbTestComponent},
						{path: 'cell-data/:case_', component: CellDataTestComponent},
						{path: 'cell-editor/:case_', component: CellEditorTestComponent},
						{path: 'code-editor/:case_', component: CodeEditorTestComponent},
						{path: 'presentation-test/:case_', component: PresentationTestComponent},
						{path: 'content/:case_', component: ContentTestComponent},
						{path: 'snippets-list/:case_', component: SnippetsListTestComponent}
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
					ArrowTestComponent,
					AttributeDataEditorTestComponent,
					AttributeDataInfoTestComponent,
					BreadcrumbTestComponent,
					CellDataTestComponent,
					CellEditorTestComponent,
					CodeEditorTestComponent,
					ContentTestComponent,
					PresentationTestComponent,
					SnippetsListTestComponent
	]
})

export class TestRoutingModule {}

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
