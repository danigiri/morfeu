// KEY PRESSED . EVENT . TS 

import { MorfeuEvent } from './morfeu-event.class';


export class KeyPressedEvent extends MorfeuEvent {


constructor(public str: string, public num?: number) {
	super('KeyPressedEvent');
}


public isNumber(): boolean {
	return this.num!==undefined;
}


public isCommand(): boolean {
	return !this.isNumber();
}


public override toString = (): string => {
	return "KeyPressedEvent:{ key:"+this.str+( (this.num) ? ", num:"+this.num : "" )+"}";
}


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