// VARIABLE-PARSER . CLASS . TS

import { NameValue } from './name-value.interface';

export class VariableParser {


/** perform variable expansion if the variable is there, will also URL encode the variable content */
static expand(str: string, variable: string, data: string|NameValue[]): string {

	if (typeof data === 'string' && data.includes(variable)) {
		// noop to avoid infinite loops
		return str;
	}

	let out = str;
	// console.debug("str:%s, variable:%s, data:",str, variable, data);
	while (out.includes(variable)) {
		if (data===null || data===undefined) {	// this behaviour could be reverted to leave string unmodified
			out = out.replace(variable, '');
		} else if (typeof data === 'string') {	// we do a single variable replacement
			out = out.replace(variable, encodeURIComponent(data));
		} else {
			const values = (<NameValue[]> data).map(v => v.name+'='+encodeURIComponent(v.value)).join('&');
			out = out.replace(variable, values);
		}
	}

	return out;

}


/** expand arbitrarily named variables in a string, variables must be as ${varname} in input */
static expandVariables(str: string, data: NameValue[]): string {

	if (data===null || data===undefined) {
		return str;
	}

	let out = str;
	let varStart = out.indexOf('${');
	while (varStart>=0) {
		// let's look for the end of the variable reference

		const varEnd = out.indexOf('}');
		if (varEnd===-1) {
			console.error('Start of variable reference specified without corresponding "}" end');
			varStart = -1;	// exit the loop
		} else {
			// do the substitution
			const name = out.substring(varStart+2, varEnd);
			const dataEntry = data.find( a => a.name===name);
			const value = dataEntry!==undefined && dataEntry.value ? dataEntry.value : '';	// watch value not defined!
			out = VariableParser.expand(out, '${'+name+'}', value);
			varStart = out.indexOf('${', varStart);
		}

	}

	return out;

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
