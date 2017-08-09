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


import { Component, Inject, OnInit } from '@angular/core';

import { TreeNode } from 'angular-tree-component';


@Component({
    moduleId: module.id,
    selector: 'cell-model',
    template: `
        <span>{{ node.data.name }}</span> -  <span>{{ node.data.desc }}</span>
        `,

        styles:[`
    `]
})

export class CellModelComponent {
    
    
}
