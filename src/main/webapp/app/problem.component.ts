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

import { Component, Input, OnDestroy } from '@angular/core';
import { Subscription }   from 'rxjs/Subscription';

import { ProblemService } from './problem.service';


@Component({
    moduleId: module.id,
    selector: 'problem',
    template: `
        <div id="problem" *ngIf="problem" class="alert alert-danger" role="alert">{{problem}}</div>        
        `,
    styles:[`
     #problem {}

`]
})
//`

export class ProblemComponent implements OnDestroy {
    
problem: any;
problemSubscription: Subscription;

constructor(private problemService: ProblemService) {
    
    console.log("ProblemComponent::constructor()");
    this.problemSubscription = problemService.announcedProblems$.subscribe(
            p => {
                console.log("***** service gets problem *****");
                this.problem = p;
                
            }
    );
}


ngOnDestroy() {
    //TODO: prevent memory leak when component destroyed (from the angular docs, is this needed?)
    this.problemSubscription.unsubscribe();
}
}