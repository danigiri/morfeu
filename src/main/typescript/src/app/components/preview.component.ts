// PREVIEW . COMPONENT . TS

import { Component, Input, OnInit } from "@angular/core";

import { ActivatedRoute, ParamMap } from "@angular/router";

import { Observable } from "rxjs";
import { switchMap } from 'rxjs/operators';

@Component({
	moduleId: module.id,
	selector: 'preview',
	template: `
			<div class="card">
				<div class="card-body" [style.background-color]="('#'+color) | safe: 'style'">
					<h2 class="card-title">{{id_}}</h2>
				</div>
			</div>
	`
})

export class PreviewComponent implements OnInit {

static readonly colorRegExp = new RegExp("^[0-9a-fA-F]{6}$");

id_: string;
color: string = "ffff00";


constructor(private route: ActivatedRoute) {}


ngOnInit() {

	this.route.paramMap.subscribe(params => { 
		this.id_ = params.get("id");
		if (params.has("color")) {
			let colorCandidate = params.get("color");
			if (PreviewComponent.colorRegExp.test(colorCandidate)) {
				this.color = colorCandidate;
				console.info("color="+this.color);
			}
		}
	});

}


}

/*
 *	  Copyright 2018 Daniel Giribet
 *
 *	 Licensed under the Apache License, Version 2.0 (the "License");
 *	 you may not use this file except in compliance with the License.
 *	 You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 *	 Unless required by applicable law or agreed to in writing, software
 *	 distributed under the License is distributed on an "AS IS" BASIS,
 *	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	 See the License for the specific language governing permissions and
 *	 limitations under the License.
 */
