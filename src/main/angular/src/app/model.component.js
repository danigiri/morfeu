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
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var model_class_1 = require("./model.class");
var widget_class_1 = require("./widget.class");
var remote_object_service_1 = require("./services/remote-object.service");
var cell_document_selection_event_1 = require("./events/cell-document-selection.event");
var event_service_1 = require("./events/event.service");
var model_request_event_1 = require("./events/model-request.event");
var status_event_1 = require("./events/status.event");
var ModelComponent = (function (_super) {
    __extends(ModelComponent, _super);
    function ModelComponent(eventService, modelService) {
        var _this = _super.call(this, eventService) || this;
        _this.modelService = modelService;
        return _this;
    }
    ModelComponent.prototype.ngOnInit = function () {
        var _this = this;
        console.log("ModelComponent::ngOnInit()");
        this.subscribe(this.events.service.of(cell_document_selection_event_1.CellDocumentSelectionEvent).filter(function (s) { return s.url == null; }).subscribe(function (selected) { return _this.clearModel(); }));
        this.subscribe(this.events.service.of(model_request_event_1.ModelRequestEvent).subscribe(function (requested) {
            return _this.loadModel(requested.url);
        }));
    };
    ModelComponent.prototype.loadModel = function (uri) {
        var _this = this;
        this.events.service.publish(new status_event_1.StatusEvent("Fetching model"));
        var modelURI = "/morfeu/models/" + uri;
        this.modelService.get(modelURI, model_class_1.Model).subscribe(function (model) {
            console.log("ModelComponent::loadModel() Got model from Morfeu service (" + model.name + ")");
            _this.diplayModel(model); // not firing a load event yet if not needed
            _this.events.ok();
        }, function (error) {
            _this.events.problem(error);
        }, function () { return _this.events.service.publish(new status_event_1.StatusEvent("Fetching model", status_event_1.StatusEvent.DONE)); });
    };
    ModelComponent.prototype.clearModel = function () {
        console.log("[UI] ModelComponent::clearModel()");
        this.model = null;
    };
    ModelComponent.prototype.diplayModel = function (m) {
        console.log("[UI] ModelComponent::diplayModel(" + m.name + ")");
        this.model = m;
    };
    return ModelComponent;
}(widget_class_1.Widget));
ModelComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'model',
        template: "\n    <div id=\"model-info\" class=\"panel panel-info\" *ngIf=\"model\">\n        <div class=\"panel-heading\">\n            <h4 id=\"model-name\" class=\"panel-title\">Model: {{model.name}}</h4>\n        </div>\n        <div class=\"panel-body\">\n            <div class=\"panel panel-info\">\n              <div id=\"model-desc\" class=\"panel-body\">\n                {{model.desc}}\n              </div>\n            </div>\n            \n        </div>\n    </div>\n    ",
        styles: ["\n                #model-info {}\n                #model-name {}\n                #model-desc {}\n    "]
    }),
    __param(1, core_1.Inject("ModelService")),
    __metadata("design:paramtypes", [event_service_1.EventService,
        remote_object_service_1.RemoteObjectService])
], ModelComponent);
exports.ModelComponent = ModelComponent;
//# sourceMappingURL=model.component.js.map