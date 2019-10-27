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
"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var cell_model_class_1 = require("./cell-model.class");
//interface Model extends SerialisableJSON<typeof Model, ModelJSON>;
//export class Model implements SerialisableJSON<typeof Model, ModelJSON> {
var Model = (function () {
    function Model(schema, name, desc, valid) {
        this.schema = schema;
        this.name = name;
        this.desc = desc;
        this.valid = valid;
        this.cellModels = [];
    }
    // check out this excellent post http://choly.ca/post/typescript-json/ to find out how to deserialize objects
    //normalise() {
    //    this.cellModels = this.cellModels.map(cm => cm.normalised());
    //}
    Model.prototype.toJSON = function () {
        return Object.assign({}, this, { cellModels: this.cellModels.map(function (cm) { return cm.toJSON(); }) });
    };
    Model.prototype.fromJSON = function (json) {
        if (typeof json === 'string') {
            return JSON.parse(json, Model.reviver);
        }
        else {
            var model = Object.create(Model.prototype);
            return Object.assign(model, json, { cellModels: json.cellModels.map(function (cm) { return cell_model_class_1.CellModel.fromJSON(cm); }) });
        }
    };
    Model.reviver = function (key, value) {
        return key === "" ? (Object.create(Model.prototype)).fromJSON(value) : value;
    };
    return Model;
}());
exports.Model = Model;
//# sourceMappingURL=model.class.js.map