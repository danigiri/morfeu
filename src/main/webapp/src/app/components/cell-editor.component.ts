/*
 *    Copyright 2018 Daniel Giribet
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

import { Component, ElementRef, ViewChild, OnInit } from "@angular/core";

import { NgbModal, ModalDismissReasons} from "@ng-bootstrap/ng-bootstrap";

import { Cell } from "../cell.class";

import { Widget } from "../widget.class";

import { CellActivateEvent } from "../events/cell-activate.event";
import { CellActivatedEvent } from "../events/cell-activated.event";
import { CellEditEvent } from "../events/cell-edit.event";
import { EventService } from "../events/event.service";
import { UXEvent } from "../events/ux.event";

@Component({
    moduleId: module.id,
    selector: 'cell-editor',
    template: `
        <ng-template let-c="close" let-d="dismiss" #editor>
            <div id="cell-editor" class="modal-body">
            <cell-data [uri]="cell.URI" 
                        [cell]="cell" 
                        [cellModel]="cell.cellModel" 
                        [editor]="true"></cell-data>
            </div>
            <div class="modal-footer">
             <button type="button" class="btn btn-outline-secondary float-left" (click)="c('Discard')">Discard</button>
             <button type="button" class="btn btn-success float-right" (click)="c('Save')">Save</button>
            </div>
        </ng-template>
        `,
    styles:[`

`]
})


export class CellEditorComponent  extends Widget implements OnInit {
    
@ViewChild("editor") editor: ElementRef;
    
cell: Cell;
cellBackup: Cell;
editing: boolean = false;

constructor(eventService: EventService, private modalService: NgbModal) {
    super(eventService);
}


ngOnInit() {
    this.subscribe(this.events.service.of( CellEditEvent )
            .filter(edit => edit.cell!=undefined && !this.editing)
            .subscribe(edit => this.edit(edit.cell))
            );
}    


edit(cell: Cell) {

    this.editing = true;
    this.cellBackup = cell.deepClone();
    this.cell = cell;
    //this.events.service.publish(new CellActivatedEvent(this.cell));
    this.modalService.open(this.editor).result.then((result) => this.button(result), 
                                                    (reason) => this.outside());
    
}

button(result: any) {
    
    if (result=="Save") {
        console.log("[UI] Saved cell");
        // the document is now dirty
        this.events.service.publish(new UXEvent(UXEvent.DOCUMENT_DIRTY));
    } else {
        this.rollbackChanges();
    }
    this.clear();
}

outside() {
 
    console.log("[UI] Dismissed editor without saving");
    this.rollbackChanges();
    this.clear();
    
}


private rollbackChanges() {
    
    console.log("[UI] Discard changes");
    let position = this.cell.position;
    let parent = this.cell.parent;
    parent.removeChild(this.cell);    // TODO: slow but no need to change the interface for now
    parent.adopt(this.cellBackup, position);
    
    // reload 
    //this.events.service.publish(new CellActivateEvent());
    this.events.service.publish(new CellActivatedEvent(this.cellBackup));
    
}

private clear() {
    this.cell = undefined;
    this.cellBackup = undefined;
    this.editing = false;
}

}