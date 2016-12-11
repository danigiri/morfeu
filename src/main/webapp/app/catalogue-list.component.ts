/*
 *    Copyright 2016 Daniel Giribet
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

import { Component } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Catalogue } from './catalogue';
import { CatalogueService } from './catalogue.service';

@Component({
    moduleId: module.id,
    selector: 'catalogue-list',
    template: `
    <h2>catalogue-list</h2>
        <div id="catalogue-list">
            <li *ngFor="let catalogue of catalogues">
               <span class="catalogue-entry">{{catalogue.name}}</span>
            </li>
        </div>
    `,
    styles:[`
    #catalogue-list {
            border: 1px solid #000000;
            border-radius: 4px;
            padding: 10px;
    }
    .catalogue-entry {
        
    }
    `],
    providers: [CatalogueService]
    })
    
export class CatalogueListComponent {
    
    catalogues: Catalogue[];
    
constructor(private catalogueService : CatalogueService){}

ngOnInit() {
    this.catalogueService.getAll()
    .subscribe(c => this.catalogues = c);
}

}