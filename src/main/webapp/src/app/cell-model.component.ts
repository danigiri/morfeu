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


import { Component, Input, OnInit } from '@angular/core';

import { TreeNode } from 'angular-tree-component';


@Component({
    moduleId: module.id,
    selector: 'cell-model',
    template: `
        <div id="{{node.data.id}}" class="cell-model-entry cell-model-level-{{node.level}}">
            <span class="cell-model-name">{{ node.data.name }}</span> - <span class="cell-model-desc">{{ node.data.desc }}</span>
        </div>
        `,

        styles:[`
                .cell-model-entry {}
                .cell-model-name {}
                .cell-model-desc {}
                .cell-model-level-1 {}
                .cell-model-level-2 {}
                .cell-model-level-3 {}
                .cell-model-level-4 {}
                .cell-model-level-5 {}
                .cell-model-level-6 {}
                .cell-model-level-7 {}
                .cell-model-level-8 {}
                .cell-model-level-9 {}
    `]
})

export class CellModelComponent {

@Input() node: TreeNode;
@Input() index: number;
    
}
