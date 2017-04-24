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

import { CatalogueListComponent } from './catalogue-list.component';
import { ProblemComponent } from './problem.component';
import { ProblemService } from './problem.service';


@Component({
    selector: 'my-app',
    template: `
      <div class="page-header">
          <h1>Morfeu application</h1>
      </div>
      
      <div class="container-fluid">
          <div class="row">
            <div class="col-md-4">
              <catalogue-list></catalogue-list>
            </div>
            <div class="col-md-8">

            </div>
          </div>
          <div class="row">
              <div class="col-md-12">
              <problem></problem>
              </div>
          </div>
      </div>


      `,
    providers:    [ProblemService                    
                     ]
                     })

export class AppComponent {
    

}
