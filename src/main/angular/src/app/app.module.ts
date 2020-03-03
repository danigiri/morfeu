// APP . MODULE . TS

import { NgModule, Injector } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';  // new angular 5 http client module

import { DndModule } from 'ng2-dnd';
import { HotkeyModule } from 'angular2-hotkeys';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TreeModule } from 'angular-tree-component';

import { ComponentsModule } from './components/components.module';
import { AppRoutingModule, AppRoutes } from './app-routing.module';
import { PipesModule } from './pipes/pipes.module';

import { AppComponent } from './app.component';
import { AttributeDataInfoComponent } from './components/attribute-data-info.component';
import { CatalogueListComponent } from './components/catalogue-list.component';
import { CatalogueComponent } from './components/catalogue.component';
import { CollapsibleComponent } from './components/collapsible.component';
import { ContentComponent } from './components/content/content.component';
import { CellComponent } from './components/cell.component';
import { CellDocumentComponent } from './components/cell-document.component';
import { CellModelComponent } from './components/cell-model.component';
import { CellDataComponent } from './components/cell-data.component';
import { DropAreaComponent } from './components/drop-area.component';
import { KeyCaptureComponent } from './components/key-capture.component';
import { MainComponent } from './main.component';
import { ModelAreaComponent } from './components/model-area.component';
import { ModelComponent } from './components/model.component';
import { ProblemComponent } from './components/problem.component';
import { SnippetComponent } from './components/snippet.component';
import { SnippetsListComponent } from './components/snippets-list.component';
import { StatusComponent } from './components/status.component';

@NgModule({
	imports: [
				AppRoutingModule,
				BrowserModule,
				BrowserAnimationsModule,
				CommonModule,
				ComponentsModule,
				DndModule.forRoot(),
				FormsModule,
				HttpClientModule,
				HotkeyModule.forRoot(),
				NgbModule, 
				PipesModule,
				TreeModule.forRoot()
	],
	declarations: [
					AppComponent
					, AttributeDataInfoComponent
					, CatalogueListComponent
					, CatalogueComponent
					, CollapsibleComponent
					, ContentComponent
					, CellComponent
					, CellDocumentComponent
					, CellDataComponent
					, CellModelComponent
					, DropAreaComponent
					, KeyCaptureComponent
					, MainComponent
					, ModelAreaComponent
					, ModelComponent
					, ProblemComponent
					, SnippetComponent
					, SnippetsListComponent
					, StatusComponent 
	],
	bootstrap:	[AppComponent]
})

export class AppModule {}

/*
 *	  Copyright 2018 Daniel Giribet
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

