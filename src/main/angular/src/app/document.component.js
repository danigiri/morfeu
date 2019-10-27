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
"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var widget_class_1 = require("./widget.class");
var event_service_1 = require("./events/event.service");
var status_event_1 = require("./events/status.event");
var DocumentComponent = (function (_super) {
    __extends(DocumentComponent, _super);
    function DocumentComponent(eventService, documentService) {
        var _this = _super.call(this, eventService) || this;
        _this.documentService = documentService;
        return _this;
    }
    DocumentComponent.prototype.ngOnInit = function () {
        var _this = this;
        console.log("DocumentComponent::ngOnInit()");
        this.documentSubscription = this.events.service.of(DocumentSelectionEvent).subscribe(function (selected) {
            if (selected.url != null) {
                _this.loadDocument(selected.url);
            }
            else {
                _this.clear();
            }
        });
        this.documentSubscription = this.events.service.of(DocumentLoadedEvent).subscribe(function (loaded) { return _this.display(loaded.document); });
    };
    DocumentComponent.prototype.loadDocument = function (url) {
        var _this = this;
        //this.events.service.publish(new DocumentSelectionEvent(null));  // we don't have a document now
        this.events.service.publish(new status_event_1.StatusEvent("Fetching document"));
        // notice we're using the enriched url here, as we want to display the JSON enriched data
        this.documentService.getDocument("/morfeu/documents/" + url).subscribe(function (d) {
            console.log("Got document from Morfeu service (" + d.name + ")");
            _this.events.service.publish(new DocumentLoadedEvent(d)); // now we have it =)
            _this.events.ok();
        }, function (error) {
            _this.events.problem(error);
            _this.events.service.publish(new DocumentSelectionEvent(null));
            _this.document = null;
        }, function () { return _this.events.service.publish(new status_event_1.StatusEvent("Fetching document", status_event_1.StatusEvent.DONE)); });
    };
    DocumentComponent.prototype.display = function (d) {
        console.log("-> document component gets Document (" + d.name + ")");
        this.document = d;
        if (d.problem == null || d.problem != "") {
            this.events.problem(d.problem);
        }
    };
    DocumentComponent.prototype.clear = function () {
        console.log("-> document component gets null document (no document selected)");
        this.document = null;
    };
    DocumentComponent.prototype.ngOnDestroy = function () {
        this.documentSubscription.unsubscribe();
    };
    return DocumentComponent;
}(widget_class_1.Widget));
DocumentComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'document',
        template: "\n    <div id=\"document-info\" class=\"panel panel-default\" *ngIf=\"document\">\n        <div class=\"panel-heading\">\n          <h4 id=\"document-name\" class=\"panel-title\">{{document.name}} <span class=\"badge\">{{document.kind}}</span></h4>\n            \n        </div>\n        <div class=\"panel-body\">\n            <span id=\"document-desc\">{{document.desc}}</span>\n            <span id=\"document-valid\" *ngIf=\"document.valid\" class=\"label label-success\">VALID</span>\n            <span id=\"document-valid\" *ngIf=\"!document.valid\" class=\"label label-danger\">NON VALID</span>\n        \n        </div>\n    </div>\n    ",
        styles: ["\n            #document-info {}\n            #document-name {}\n            #document-desc {}\n            #document-valid {}\n\n   "]
    }),
    __metadata("design:paramtypes", [event_service_1.EventService, Object])
], DocumentComponent);
exports.DocumentComponent = DocumentComponent;
//# sourceMappingURL=document.component.js.map