import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { MainComponent } from "./components/main.component";
import { PreviewComponent } from "./components/preview.component";


export const AppRoutes: Routes = [
									{ path: "preview/:id", component: PreviewComponent }
									,{ path: "", component: MainComponent }
									];


@NgModule({
	imports: [ RouterModule.forRoot(AppRoutes, { enableTracing: false }) ],
	exports: [ RouterModule ]
})

export class AppRoutingModule {}

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
