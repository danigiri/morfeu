/*
 *	  Copyright 2016 Daniel Giribet
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

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';  // new angular 5 http client module

import { DndModule } from 'ng2-dnd';
import { HotkeyModule } from 'angular2-hotkeys';
import { TreeModule } from 'angular-tree-component';

import { AppComponent }	  from './app.component';
import { CatalogueListComponent } from './catalogue-list.component';
import { CatalogueComponent } from './catalogue.component';
import { ContentComponent } from './content.component';
import { CellComponent } from './cell.component';
import { CellDocumentComponent } from './cell-document.component';
import { CellModelComponent } from './cell-model.component';
import { DropAreaComponent } from './drop-area.component';
import { ModelComponent } from './model.component';
import { ProblemComponent } from './problem.component';
import { StatusComponent } from './status.component';

@NgModule({
  imports:		[ BrowserModule
				  ,BrowserAnimationsModule
				  ,CommonModule
                  ,HttpModule
                  ,HttpClientModule
				  ,TreeModule
				  ,DndModule.forRoot()
				  ,HotkeyModule.forRoot()
				  ],
  declarations: [ AppComponent
				  ,CatalogueListComponent 
				  ,CatalogueComponent
				  ,ContentComponent 
				  ,CellComponent
				  ,CellDocumentComponent
                  ,CellModelComponent
                  ,DropAreaComponent
				  ,ModelComponent
				  ,ProblemComponent 
				  ,StatusComponent 
				],
  providers:	[
				 ],
  bootstrap:	[ AppComponent ]
})

export class AppModule { }
