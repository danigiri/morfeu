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

import { CellHolder } from './cell-holder.interface';
import { Widget } from './widget.class';

import { CellActivatedEvent } from './events/cell-activated.event';
import { CellDeactivatedEvent } from './events/cell-deactivated.event';
import { EventService } from './events/event.service';

@Component({
    moduleId: module.id,
    selector: 'drop-area',
    template: `
            <div class="drop-area" 
                 [class.drop-area-active]="active" 
                 [class.drop-area-inactive]="!active"></div>
        `,
    styles:[`
                .drop-area {}
                .drop-area-active {
                    border: 3px dotted #f00;
                    opacity: 0.8;
                }
                .drop-area-inactive {
                    opacity: 0;
                }
            `],
    providers:[
    ]
})

export class CellComponent extends Widget {

@Input() parent: CellHolder;

active: boolean = false;;

    
}