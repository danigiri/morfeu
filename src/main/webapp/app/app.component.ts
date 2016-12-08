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
      <h1>Partikle Editor</h1>
      <catalogue-list></catalogue-list>
      <catalogue></catalogue>
      `
})

export class AppComponent {
    

}
