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

import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import 'rxjs/add/operator/map';

import { SerialisableToJSON } from '../serialisable-to-json.interface';

/** This class leverages the SerialisableJSON interface so we can invoke the method to conver to a rich
*	object with its expected methods from a JSON-formatted string
*/
@Injectable()
export class RemoteObjectService<T extends SerialisableToJSON<T, J>, J> {


constructor(private http:Http) {}

// with an array this will not work
//getAll(uri: string, type_: Constructor<T>): Observable<T[]> {
//	  
//	  console.log("[SERVICE] RemoteObjectService::getAll("+uri+")"); 
//	  //TODO: handle errors with .catch here
//	  return this.http.get(uri).map(response => <T[]>response.json());
//
//}

/** we have to explicitly pass the class we're expecting */
get(uri: string, type_: Constructor<T>): Observable<T> {

	console.log("[SERVICE] RemoteObjectService::get("+uri+")"); 
	//TODO: handle errors with .catch here
	return this.http.get(uri).map(response => <T>createInstance(type_).fromJSON(response.text()));

}

}


interface Constructor<T> {
	new (...args: any[]): T;
}


function createInstance<T extends SerialisableToJSON<T, J>, J>(type_: Constructor<T>): T {
	return new type_();
}


