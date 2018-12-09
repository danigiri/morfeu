// REMOTE - EVENT . SERVICE . TS


import { Inject, Injectable, OnDestroy } from "@angular/core";
import { Subscription } from "rxjs";

import { Configuration } from "../configuration.class";

import { RemoteDataService } from "../services/remote-data.service";

import { ConfigurationLoadedEvent } from "../events/configuration-loaded.event";
import { EventService } from "./event.service";


export class RemoteEventResponse {

result: string;
desc: string;

}


@Injectable()
export class RemoteEventService extends EventService implements OnDestroy {

private configuration: Configuration;
private configurationSubscription: Subscription;


constructor(private eventService: EventService,
			@Inject("RemoteJSONDataService") private remoteEventService: RemoteDataService ) {

	super();

	this.configurationSubscription = eventService.of(ConfigurationLoadedEvent).subscribe(
			loaded => {
						console.debug("Remote Event Service loaded the configuration");
						this.configuration = loaded.configuration;
			}
	);

}


/** After publishing the event internally, we send it to the server */
publish<T>(event: T, subtype?: string): void {

	this.eventService.publish(event, subtype);

	let eventURL = this.configuration.events+"/"+event
	
	// we iterate through our attributes
	
	
	console.debug("Calling server event '%s'", eventURL);
	
	this.remoteEventService.get<RemoteEventResponse>(eventURL).subscribe(
			response => console.debug("Server responded to event with '%s' - '%s'", response.result, response.desc),
			error => console.error("Server responded to event with an error '%'", error)
	);
	
}


ngOnDestroy() {
	this.configurationSubscription.unsubscribe();
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
