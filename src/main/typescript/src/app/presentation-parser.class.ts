// PRESENTATION-PARSER . CLASS . TS

import { NameValue } from "./name-value.interface";

export class PresentationParser {
	
/** perform parameter expansion */
static expand(str: string, variable: string, data: string|NameValue[]): string {

	if (data==null || data==undefined) {
		return str;
	}
	
	let out = str;
	if (out.includes(variable)) {
		if (typeof data === "string") {	  // we do a single variable replacement
			out = out.replace(variable, data);
		} else {
			const values = (<NameValue[]> data).map(v => v.name+"="+v.value).join("&");
			out = out.replace(variable, values);
		}
	}

	return out;

}


static expandVariables(str: string, data: NameValue[]): string {

	if (data==null || data==undefined) {
		return str;
	}

	let out = str;
	let varStart = out.indexOf("${");
	let maxAttributes = 0
	while (varStart>=0) {
		// let's look for the end of the variable reference

		let varEnd = out.indexOf("}");
		if (varEnd===-1) {
			console.error("Start of variable reference specified without corresponding '}' end");
			varStart = -1;	// exit the loop
		} else {
			// do the substitution
			let name = out.substring(varStart+2, varEnd);
			let dataEntry = data.find( a => a.name===name);
			let value = dataEntry!==undefined && dataEntry.value ? dataEntry.value : "";	// watch value not defined!
			out = PresentationParser.expand(out, "${"+name+"}", value);
			varStart = out.indexOf("${", varStart);
		}

	}

	return out;
	
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
