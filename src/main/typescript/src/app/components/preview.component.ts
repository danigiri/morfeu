
import { Component, Input } from "@angular/core";

@Component({
	moduleId: module.id,
	selector: 'preview',
	template: `
			<div class="card">
				<div class="card-body">
					<h1 class="card-title">PREVIEW</h1>
				</div>
			</div>
	`,
	styles: [`
			.cell-data-header {}
			.cell-header-desc {}
			.cell-header-uri {}
	`]
})

export class PreviewComponent {}

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
