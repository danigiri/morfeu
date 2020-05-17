// UX . EVENT . TS

import { MorfeuEvent } from './morfeu-event.class';

export class UXEvent extends MorfeuEvent {


constructor(public readonly type: UXEventType, public readonly payload?: any) {
	super('UXEvent');
}


public toString = () : string => {

	let message: string;
	switch (this.type) {
		case UXEventType.DOCUMENT_DIRTY:
			message = "DOCUMENT_DIRTY";
			break;
		default:
			message = "UNKNOWN";
			break;
	}

	return "UXEvent:{'"+message+"'}";

}


}

export enum UXEventType {
	DOCUMENT_DIRTY = 100,
	TOGGLE_COLLAPSABLE = 200,
	START_DRAG = 301,
	END_DRAG = 302
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