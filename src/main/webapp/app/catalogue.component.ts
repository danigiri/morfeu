/*
 *    Copyright 2017 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import { Component, Input } from '@angular/core';

import { Widget } from './widget.class';
import { Catalogue } from './catalogue';
import { CatalogueService } from './catalogue.service';
import { Document } from './document.class';
import { DocumentService } from './document.service';
import { ProblemService } from './problem.service';


@Component({
	moduleId: module.id,
	selector: 'catalogue',
	template: `
    <div id="catalogue" class="panel panel-default" *ngIf="catalogue">
        <div class="panel-heading">
          <h4 id="catalogue-name" class="panel-title">{{catalogue.name}}</h4>
        </div>
        <div id="catalogue-desc" class="panel-body">
            {{catalogue.desc}}
        </div>
        <div class="panel-body">
	        <div id="document-list" class="list-group">
                <a *ngFor="let d of catalogue.documents"
                    href="#" 
                    class="document-list-entry list-group-item" 
                    [class.active]="d === currentDocument"
                    (click)="selectdocument(d)">
                    <!-- TODO: move kind attribute to a call to the document to remove coupling -->
                    {{d.name}} <span class="badge">{{d.kind}}</span>
                </a> 
        </div>
        </div>
      </div>
	`,
    styles:[`
    #catalogue {}
    #catalogue-name {}
    #catalogue-desc {}
    #document-list {}
    #document-list-entry {}
    `],
    providers:[
    ]
})
	
	//`
export class CatalogueComponent extends Widget {
	
catalogue: Catalogue;
currentDocument: Document;

constructor(private catalogueService : CatalogueService, 
            problemService: ProblemService, 
            private documentService: DocumentService) {
    super(problemService);
}


@Input() 
set selectedCatalogueUri(selectedCatalogueUri: string) {
    
    this.catalogueService.getCatalogue(selectedCatalogueUri)
    .subscribe(c => { 
        this.catalogue = c;
        this.allOK();
    },
    error => {
        this.reportProblem(error);
        this.catalogue = null;
    }
    );
    
}
 

selectdocument(d: Document) {
        
    console.log("Selected document="+d.uri);
    this.documentService.getDocument("/morfeu/documents/"+d.uri)
    .subscribe(d => {
        console.log("Got document from Morfeu service ("+d.name+")");
        this.currentDocument = d;
        this.documentService.setDocument(d);
        this.allOK();
    },
    error => {
        this.reportProblem(error);
        this.currentDocument = null;
    }
    );
    
}

}