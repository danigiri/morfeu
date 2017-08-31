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


import { Component, Input, OnInit, OnDestroy } from '@angular/core';

import { Cell } from './cell.class';
import { Widget } from './widget.class';

import { EventService } from './events/event.service';


@Component({
    moduleId: module.id,
    selector: 'cell',
    template: `
            <ng-container *ngIf="cell.cellModel.presentation=='CELL'; else well">
                <img src="assets/images/cell.svg" class="img-fluid cell-img" alt="{{cell.name}}"/>
            </ng-container>
            <ng-template #well>
        <div class="{{cellClass()}}">
                <cell *ngFor="let c of cell.children" [cell]="c"></cell>
        </div>
            </ng-template>
    `,
    styles:[`
    .cell-img {
        width: 100%;
        height: auto;
    }
    .show-grid [class^=col-] {
        padding-top: 10px;
        padding-bottom: 10px;
        background-color: #ddd;
        background-color: rgba(86, 62, 128, .15);
        border: 1px solid #ccc;
        border: 1px solid rgba(86, 62, 128, .2)
    }

    `],
    providers:[
    ]
})

export class CellComponent extends Widget implements OnInit {

@Input() cell:Cell;
    
constructor(eventService: EventService) {
    super(eventService);
}


ngOnInit() {

    console.log("CellComponent::ngOnInit()");
}    
        
cellClass() {
    if (this.cell.cellModel.presentation=="WELL") {
        return "container-fluid";
    } else if (this.cell.cellModel.presentation=="ROW-WELL") {
        return "row show-grid";
   } else if (this.cell.cellModel.presentation=="COLUMN-WELL") {
       return "col-md-"+this.cell.columnFieldValue();
   } else {
        return "";
    }
}



}