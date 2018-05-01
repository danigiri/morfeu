/*
 *	  Copyright 2017 Daniel Giribet
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

import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { HttpModule } from "@angular/http";
import { HttpClientModule } from "@angular/common/http";  // new angular 5 http client module

import { DndModule } from "ng2-dnd";
import { HotkeyModule } from "angular2-hotkeys";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { TreeModule } from "angular-tree-component";

import { AppComponent } from "./app.component";
import { AttributeDataEditorComponent } from "./components/attribute-data-editor.component";
import { AttributeDataInfoComponent } from "./components/attribute-data-info.component";
import { CatalogueListComponent } from "./catalogue-list.component";
import { CatalogueComponent } from "./catalogue.component";
import { CollapsibleComponent } from "./components/collapsible.component";
import { ContentComponent } from "./content.component";
import { CellComponent } from "./cell.component";
import { CellEditorComponent } from "./components/cell-editor.component";
import { CellDocumentComponent } from "./cell-document.component";
import { CellModelComponent } from "./cell-model.component";
import { CellDataComponent } from "./components/cell-data.component";
import { DropAreaComponent } from "./drop-area.component";
import { KeyCaptureComponent } from "./components/key-capture.component";
import { ModelAreaComponent } from "./components/model-area.component";
import { ModelComponent } from "./components/model.component";
import { ProblemComponent } from "./problem.component";
import { StatusComponent } from "./status.component";

@NgModule({
  imports:		[ 
				  NgbModule.forRoot()
				  ,BrowserModule
				  ,BrowserAnimationsModule
				  ,CommonModule
				  ,DndModule.forRoot()
				  ,FormsModule
				  ,HttpModule
				  ,HttpClientModule
				  ,HotkeyModule.forRoot()
				  ,TreeModule
				  ],
  declarations: [ AppComponent
				  ,AttributeDataEditorComponent
				  ,AttributeDataInfoComponent
				  ,CatalogueListComponent 
				  ,CatalogueComponent
				  ,CollapsibleComponent
				  ,ContentComponent 
				  ,CellComponent
				  ,CellEditorComponent
				  ,CellDocumentComponent
				  ,CellDataComponent
				  ,CellModelComponent
				  ,DropAreaComponent
				  ,KeyCaptureComponent
				  ,ModelAreaComponent
				  ,ModelComponent
				  ,ProblemComponent 
				  ,StatusComponent 
				],
  providers:	[],
  bootstrap:	[ AppComponent ]
})

export class AppModule { }
