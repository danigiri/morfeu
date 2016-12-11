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
     <div id="catalogue-list" class="panel panel-default">
      <div class="panel-heading">
        <h4 class="panel-title">Catalogues</h4>
      </div>
      <div class="panel-body">
        <ul class="list-group" *ngFor="let catalogue of catalogues">
            <li class="catalogue-entry list-group-item">
              <button type="button" class="btn btn-secondary">{{catalogue.name}}</button>
            </li>
        </ul>
      </div>
    </div>
    `,
    styles:[`
    #catalogue-list {
        width: auto;
    height: auto;
    margin-left: 10px;
    margin-top: 3px;
    }
    .catalogue-entry {}
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