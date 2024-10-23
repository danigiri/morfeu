// TEST.  MODULE . TS

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ComponentsModule } from '../components/components.module';
import { TestRoutingModule } from './test-routing.module';
import { ConfigJSON, Configuration } from '../config/configuration.class';
import { RemoteObjectService } from '../services/remote-object.service';

@NgModule({
	declarations: [],
	imports: [
		ComponentsModule,
		CommonModule,
		NgbModule,
		TestRoutingModule,
	],
	providers: [
			Configuration,
			{provide: "ConfigurationService",
			useFactory: (http: HttpClient) => (new RemoteObjectService<Configuration, ConfigJSON>(http)),
			deps: [HttpClient]
			}
	]
})

export class TestModule {}

/*
 *	  Copyright 2024 Daniel Giribet
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