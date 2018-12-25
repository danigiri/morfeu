// CONFIGURATION . CLASS . TS

import { Inject } from "@angular/core";
import { Params, ActivatedRoute } from "@angular/router";

import { environment } from "../../environments/environment";

import { RemoteDataService } from "../services/remote-data.service";

import { ConfigurationLoadedEvent } from "../events/configuration-loaded.event";
import { EventListener } from "../events/event-listener.class";
import { EventService } from "../services/event.service";

export class Configuration extends EventListener {

config: "default";
production: boolean = true;
catalogues = "/morfeu/test-resources/catalogues.json";
remoteEvents = "/morfeu/dyn/events";


constructor(eventService?: EventService, @Inject("RemoteJSONDataService") private configService?: RemoteDataService ) {
	super(eventService);
}


loadRemoteConfigFrom(url: string) {
	
	console.debug("Loading configuration from '%s'", url);
	this.configService.get<ConfigJSON>(url).subscribe(
			c => {},
			error => {
				console.error("Could not read the configuration url '%s'", url);
				this.events.service.publish(new ConfigurationLoadedEvent(new Configuration()));
			},
			() => {}
	);

	
}


static from(params: Params): Configuration {

	let config = new Configuration();
	config.production = params.production!==undefined ? params.production : environment.production;
	config.catalogues = params.catalogues!==undefined ? params.catalogues : config.catalogues;
	config.remoteEvents = params.remoteEvents!==undefined ? params.remoteEvents : config.remoteEvents;

	return config;

}


}


export class ConfigJSON {

schema: number;
config?: string;
production: boolean;
catalogues?: string;
events?: string;

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

