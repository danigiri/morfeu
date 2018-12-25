// CONFIGURATION . CLASS . TS

import { Inject } from "@angular/core";
import { Params, ActivatedRoute } from "@angular/router";

import { environment } from "../../environments/environment";

import { RemoteObjectService } from "../services/remote-object.service";
import { SerialisableToJSON } from "../serialisable-to-json.interface";

import { ConfigurationLoadedEvent } from "../events/configuration-loaded.event";
import { EventListener } from "../events/event-listener.class";
import { EventService } from "../services/event.service";

export class Configuration extends EventListener implements SerialisableToJSON<Configuration, ConfigJSON>{

config: "default";
production: boolean = environment.production;
catalogues = "/morfeu/test-resources/catalogues.json";
remoteEvents = "/morfeu/dyn/events";


constructor(eventService: EventService, 
		@Inject("ConfigurationService") private configService: RemoteObjectService<Configuration, ConfigJSON>) {
	super(eventService);
}


loadConfigFrom(url: string) {
	
	console.debug("Loading configuration from '%s'", url);
	this.configService.get(url, Configuration).subscribe(
			c => {
				
				
			},
			error => console.error("Could not read the configuration url '%s', using defaults", url),
			() => this.events.service.publish(new ConfigurationLoadedEvent(this))
	);

	
}


overwriteWithParams(params: Params) {

	this.production = params.production!==undefined ? params.production : environment.production;
	this.catalogues = params.catalogues!==undefined ? params.catalogues : this.catalogues;
	this.remoteEvents = params.remoteEvents!==undefined ? params.remoteEvents : this.remoteEvents;

	return this;

}


overwriteWithConfig(config: Configuration) {

	this.production = config.production!==undefined ? config.production : this.production;
	this.catalogues = config.catalogues!==undefined ? config.catalogues : this.catalogues;
	this.remoteEvents = config.remoteEvents!==undefined ? config.remoteEvents : this.remoteEvents;

	return this;

}

////SerialisableToJSON ////

toJSON(): ConfigJSON {
	return Object.assign({}, this);
}


fromJSON(json: ConfigJSON|string): Configuration {
	
	if (typeof json === "string") {

		return JSON.parse(json, Configuration.reviver);
		
	} else {
	
		let config = Object.create(Configuration.prototype);
		config = Object.assign(config, json);
	
		return config;
		
	}
	
}


static reviver(key: string, value: any): any {
	return key === "" ? Object.create(Configuration.prototype).fromJSON(value) : value;
}


}

//serialisable interface
export interface ConfigJSON {

config?: string;
production: boolean;
catalogues?: string;
remoteEvent?: string;

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

