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

import { Params, ActivatedRoute } from "@angular/router";

import { environment } from "../environments/environment";

export class Configuration {


constructor() {}

production: boolean;
catalogues = "/morfeu/test-resources/catalogues.json";

static merge(params: Params): Configuration {

	let config = new Configuration();
	config.production = params.production!==undefined ? params.production : environment.production;
	config.catalogues = params.catalogues!==undefined ? params.catalogues : config.catalogues;

	return config;

}

}