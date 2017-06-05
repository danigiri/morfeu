/*
 *    Copyright 2017 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */


import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import 'rxjs/add/operator/map';

import { Catalogue } from './catalogue';

@Injectable()
export class CatalogueService {
    
catalogues: Observable<Catalogue[]>;
 
constructor(private http: Http) {}
    
getAll(uri:string) : Observable<Catalogue[]> {
    console.log("CatalogueService::getAll("+uri+")"); 
    
    return this.http.get(uri)
        .map(response => response.json());
        //TODO: handle errors with .catch here
}
    
    getCatalogue(uri:string) : Observable<Catalogue> {
        
        console.log("CatalogueService::getCatalogue("+uri+")"); 
    
        return this.http.get(uri)
            .map(response => response.json());
    
    }
    
}