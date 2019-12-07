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
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var Subject_1 = require("rxjs/Subject");
var ProblemService = (function () {
    function ProblemService() {
        this.problemSource = new Subject_1.Subject();
        this.announcedProblems$ = this.problemSource.asObservable();
    }
    ProblemService.prototype.reportProblem = function (p) {
        console.error("ProblemService::reportProblem(" + p + ")");
        this.problemSource.next(p);
    };
    ProblemService.prototype.clearProblem = function () {
        this.problemSource.next(null);
    };
    return ProblemService;
}());
ProblemService = __decorate([
    core_1.Injectable()
], ProblemService);
exports.ProblemService = ProblemService;
//# sourceMappingURL=problem.service.js.map