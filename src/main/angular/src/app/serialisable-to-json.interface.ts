/*
 *	  Copyright 2017 Daniel Giribet
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

// see arcane magic here https://github.com/Microsoft/TypeScript/issues/13462
//export interface SerialisableJSONStatic<T extends new (...args) => SerialisableJSON<T, J> , J> {
//	  
//fromJSON(json: J|string): SerialisableJSON<T, J>;
//	  
//}
//
//
//export interface SerialisableJSON<T extends new (...args) => any, J> {
//	  constructor: T
//}

/** Objects that are serialisable to a JSON-friendly format (for instance, a Date object is in fact a string
*	in JSON), the JSON-friendly format is represented in the interface J
*	See CellModel and Model for examples
*/
export interface SerialisableToJSON<T, J> {

fromJSON(json: J): SerialisableToJSON<T, J>;

toJSON():J;

}