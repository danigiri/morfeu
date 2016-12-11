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
import { CatalogueComponent } from './catalogue.component';

@Component({
  selector: 'my-app',
  template: `
      <div class="page-header">
          <h1>Partikle application</h1>
      </div>
      
      <div class="container">
          <div class="row">
            <div class="col-sm-4">
              <catalogue-list></catalogue-list>
              <catalogue></catalogue>
              </div>
            <div class="col-sm-8">
            <div class="navbar">
  <div class="navbar-inner">
    <a class="brand" href="#">Title</a>
    <ul class="nav">
      <li class="active"><a href="#">Home</a></li>
      <li><a href="#">Link</a></li>
      <li><a href="#">Link</a></li>
    </ul>
  </div>
</div>
            </div>
          </div>
      </div>


      `
})

export class AppComponent {
    

}
