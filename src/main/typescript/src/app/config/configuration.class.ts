// CONFIGURATION . CLASS . TS

import { Inject, Injectable, isDevMode } from '@angular/core';
import { Params } from '@angular/router';

import { RemoteObjectService } from '../services/remote-object.service';
import { SerialisableToJSON } from '../serialisable-to-json.interface';

import { ConfigurationLoadedEvent } from '../events/configuration-loaded.event';
import { EventListener } from '../events/event-listener.class';
import { EventService } from '../services/event.service';


@Injectable()
export class Configuration extends EventListener implements SerialisableToJSON<Configuration, ConfigJSON>{

public static readonly BACKEND_PREF = '';

config: "default";
production: boolean = isDevMode(); 
catalogues = Configuration.BACKEND_PREF+'/test-resources/catalogues.json';
remoteEvents = Configuration.BACKEND_PREF+'/dyn/events';
saveFilters: string;														// filters to apply before saving
loadFilters: string;														// filters to apply before loading/parse
reloadOnSave = false;														// reload content on save


constructor(eventService: EventService, 
		@Inject("ConfigurationService") private configService: RemoteObjectService<Configuration, ConfigJSON>) {
	super(eventService);
}


loadConfigFrom(url: string) {

	console.debug("Loading configuration from '%s'", url);
	this.configService.get(url, Configuration).subscribe(
			loaded => this.overwriteWithConfig(loaded),
			error => console.error("Could not read the configuration url '%s', using defaults", url),
			() => this.events.service.publish(new ConfigurationLoadedEvent(this))
	);

}


overwriteWithParams(params: Params) {

	this.production = params.production!==undefined ? params.production : this.production;
	this.catalogues = params.catalogues!==undefined ? params.catalogues : this.catalogues;
	this.remoteEvents = params.remoteEvents!==undefined ? params.remoteEvents : this.remoteEvents;
	this.saveFilters = params.savefilters ?? this.saveFilters;
	this.loadFilters = params.loadFilters ?? this.loadFilters;
	this.reloadOnSave = params.reloadOnSave ?? this.reloadOnSave;

	return this;

}


overwriteWithConfig(config: Configuration) {

	//console.debug('Overwriting configuration with', config);
	this.production = config.production!==undefined ? config.production : this.production;
	this.catalogues = config.catalogues!==undefined ? config.catalogues : this.catalogues;
	this.remoteEvents = config.remoteEvents!==undefined ? config.remoteEvents : this.remoteEvents;
	this.saveFilters = config.saveFilters ?? this.saveFilters;
	this.loadFilters = config.loadFilters ?? this.loadFilters;
	this.reloadOnSave = config.reloadOnSave ?? this.reloadOnSave;

	return this;

}


/** given the paramters from the route, do we load from the url or just overwrite certain parts of the configuration? */
static bootstrapFromRouteParams(eventService: EventService, config: Configuration, params: Params) {
	if (params.config && params.config!==undefined) {
		console.log("Configuration to be bootstrapped from config url '%s'", params.config);
		config.loadConfigFrom(params.config);
	} else {
		let merged = config.overwriteWithParams(params);
		console.log("Configuration bootstrapped from defaults, firing config loaded event ^^");
		eventService.publish(new ConfigurationLoadedEvent(merged));
		console.debug('Configuration loaded event fired');
	}
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


public override toString = (): string => {
	return 'config='+this.config+', production='+this.production+',catalogues='+this.catalogues+',remoteEvents=';
}


}

//serialisable interface
export interface ConfigJSON {

config?: string;
production: boolean;
catalogues?: string;
remoteEvents?: string;
savefilters?: string;
reloadOnSave?: boolean;

}

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

