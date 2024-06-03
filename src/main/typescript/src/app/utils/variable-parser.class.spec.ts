// VARIABLE - PARSER . CLASS . SPEC . TS

import { NameValue } from "./name-value.interface";
import { VariableParser } from "./variable-parser.class";

describe('variable-parser.class', () => {


	it('should not replace variable if not found', () => {
		expect(VariableParser.expand('not here', 'var', 'value')).toBe('not here');
	});

	it('should replace variables if found', () => {
		expect(VariableParser.expand('var', 'var', 'value')).toBe('value');
		expect(VariableParser.expand('var var', 'var', 'value')).toBe('value value');
		expect(VariableParser.expand('var', 'var', null)).toBe('');

	});

	it('should replace variables with encoding', () => {
		expect(VariableParser.expand('var', 'var', 'value here')).toBe('value%20here');

	});

	it('should replace name values if found', () => {
		const empty: NameValue[] = [];
		expect(VariableParser.expand('var', 'var', empty)).toBe('');
		const nameValue: NameValue[] = [{name: "name", value: "value"}];
		expect(VariableParser.expand('var', 'var', nameValue)).toBe('name=value');
		const nameValues: NameValue[] = [{name: "name", value: "value"}, {name: "name2", value: "value2"}];
		expect(VariableParser.expand('var', 'var', nameValues)).toBe('name=value&name2=value2');
	});
	
	it('should not replace variable if data includes variable name', () => {
		expect(VariableParser.expand('var', 'var', 'var')).toBe('var');
		expect(VariableParser.expand('var', 'var', 'variable')).toBe('var');
	});

	it('should expand variables', () => {
		expect(VariableParser.expandVariables('not here',  [])).toBe('not here');
		expect(VariableParser.expandVariables('${name}',  [])).toBe('');
		const nameValue: NameValue[] = [{name: "name", value: "value"}];
		expect(VariableParser.expandVariables('${name}',  nameValue)).toBe('value');
	});


});

/*
 *	Copyright 2024 Daniel Giribet
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