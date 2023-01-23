import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';  // new angular 5 http client module

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
	ComponentsModule,
	HttpClientModule,
	DndModule.forRoot(),
	NgbModule,
],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
