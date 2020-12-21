
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';	// new angular 5 http client
import { KeyboardShortcutsModule } from 'ng-keyboard-shortcuts';
import { NgModule } from '@angular/core';

import { DndModule } from '../dnd/dnd.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { PipesModule } from '../pipes/pipes.module';

import { AttributeDataEditorComponent } from './attribute-data-editor/attribute-data-editor.component';
import { AttributeDataInfoComponent } from './attribute-data-info/attribute-data-info.component';
import { BreadcrumbComponent } from './breadcrumb/breadcrumb.component';
import { CellComponent } from './cell.component';
import { CellDataComponent } from './cell-data/cell-data.component';
import { CellEditorComponent } from './cell-editor/cell-editor.component';
import { CellHeaderComponent } from './cell-header.component';
import { CodeEditorComponent } from './code-editor/code-editor.component';
import { ContentComponent } from './content/content.component';
import { DropAreaComponent } from './drop-area.component';
import { KeyCaptureComponent } from '../components/key-capture.component';
import { PresentationComponent } from './presentation/presentation.component';
import { SnippetComponent } from './snippet.component';
import { SnippetsListComponent } from './snippets-list/snippets-list.component';

import { RemoteDataService } from '../services/remote-data.service';
import { RemoteObjectService } from '../services/remote-object.service';

import { Content, ContentJSON } from '../content.class';
import { Model, ModelJSON } from '../model.class';

import { EventService } from '../services/event.service';
import { RemoteEventService } from '../services/remote-event.service';

@NgModule({
	imports: [
				CommonModule,
				DndModule,
				KeyboardShortcutsModule,
				NgbModule,
				FormsModule,
				PipesModule
	],
	declarations: [
					AttributeDataEditorComponent,
					AttributeDataInfoComponent,
					BreadcrumbComponent,
					CellComponent,
					CellDataComponent,
					CellEditorComponent,
					CellHeaderComponent,
					CodeEditorComponent,
					ContentComponent,
					DropAreaComponent,
					KeyCaptureComponent,
					PresentationComponent,
					SnippetComponent,
					SnippetsListComponent
	],
	exports: [
					AttributeDataEditorComponent,
					AttributeDataInfoComponent,
					BreadcrumbComponent,
					CellComponent,
					CellDataComponent,
					CellEditorComponent,
					CellHeaderComponent,
					CodeEditorComponent,
					ContentComponent,
					KeyCaptureComponent,
					PresentationComponent,
					SnippetComponent,
					SnippetsListComponent
	],
	providers: [
				{provide: "RemoteJSONDataService",
					useFactory: (http: HttpClient) => (new RemoteDataService(http)),
					deps: [HttpClient]
				},
				{
					provide: "ContentService",
					useFactory: (http: HttpClient) => (new RemoteObjectService<Content, ContentJSON>(http)),
					deps: [HttpClient]
				},
				EventService,
				{
					provide: "ModelService",
					useFactory: (http: HttpClient) => (new RemoteObjectService<Model, ModelJSON>(http)),
					deps: [HttpClient]
				},
				{
					provide: "RemoteDataService",
					useFactory: (http: HttpClient) => (new RemoteDataService(http)),
					deps: [HttpClient]
				},
				RemoteEventService,
				{provide: "SnippetContentService",
					useFactory: (http: HttpClient) => (new RemoteObjectService<Content, ContentJSON>(http)),
					deps: [HttpClient]
				}
	]

})

export class ComponentsModule {}

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
