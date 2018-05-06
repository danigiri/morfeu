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

import { NameValue } from "./name-value.interface";

export class PresentationParser {
	
/** perform parameter expansion */
static expand(str: string, variable: string, data: NameValue|NameValue[]): string {
	
	let out = str;
	if (out.includes(variable)) {
		if (typeof data =='string') {
			
		}
	}
	
	return out;
	
}


}