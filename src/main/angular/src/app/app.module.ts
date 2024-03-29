import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {ReactiveFormsModule} from "@angular/forms";
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { DndModule } from './dnd/dnd.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { PipesModule } from './pipes/pipes.module';
import { CatalogueListComponent } from './components/catalogue-list.component';
import { CatalogueComponent } from './components/catalogue.component';
import { CellDocumentComponent } from './components/cell-document.component';
import { CellModelComponent } from './components/cell-model/cell-model.component';
import { CollapsibleComponent } from './components/collapsible.component';
import { ComponentsModule } from './components/components.module';
import { DebugComponent } from './components/debug/debug.component';
import { MainComponent } from './main.component';
import { ModelAreaComponent } from './components/model-area.component';
import { ModelComponent } from './components/model.component';
import { ProblemComponent } from './components/problem.component';
import { StatusComponent } from './components/status.component';



@NgModule({
  declarations: [
    AppComponent,
	CatalogueListComponent,
	CatalogueComponent,
	CollapsibleComponent,
	CellDocumentComponent,
	CellModelComponent,
	MainComponent,
	ModelAreaComponent,
	ModelComponent,
	ProblemComponent,
	StatusComponent,
	
],
imports: [
	AppRoutingModule,
    BrowserModule,
	BrowserAnimationsModule,
	CommonModule,
	ComponentsModule,
	HttpClientModule,
	DndModule.forRoot(),
	NgbModule,
	PipesModule,
	ReactiveFormsModule
],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule { }

/*
 *	  Copyright 2023 Daniel Giribet
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
