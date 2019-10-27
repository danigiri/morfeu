
import { take, delay, retryWhen } from 'rxjs/operators';
import { Injectable } from "@angular/core";
import { HttpClient, HttpErrorResponse} from "@angular/common/http";
import { Observable , BehaviorSubject } from 'rxjs';


/** This only creates plain JSON structures, not object instances, use RemoteObjectService instead */
@Injectable()
export class RemoteDataService {


constructor(private http: HttpClient) {}

getAll<T>(uri: string): Observable<T[]> {

	console.log("[SERVICE] RemoteDataService::getAll('%s')", uri);
	// TODO: handle errors here

	return this.http.get<T[]>(uri);

}


get<T>(uri: string): Observable<T> {

	console.log("[SERVICE] RemoteDataService::get('%s')", uri); 
	// TODO: handle errors with .catch here
	
	return this.http.get<T>(uri).pipe(retryWhen(errors => errors.pipe(delay(200),take(5))));
	//.concat(Observable.throw(new Error("Too many retries")))

}


getText(uri: string): Observable<String> {

	console.log("[SERVICE] RemoteDataService::getText('%s')", uri);

	return this.http.get(uri, {responseType: 'text'}).pipe(retryWhen(errors => errors.pipe(delay(200),take(5))));
	//.catch((err: HttpErrorResponse) => this.handleError(err.error));
}


post<T>(uri: string, content: any): Observable<T> {

	console.log("[SERVICE] RemoteDataService::post('%s')", uri);
	// TODO: handle errors here

	return this.http.post<T>(uri, content);

}


/*private handleError(error: HttpErrorResponse) {
  if (error.error instanceof ErrorEvent) {
    // A client-side or network error occurred. Handle it accordingly.
    console.error('An error occurred:', error.error.message);
  } else {
    // The backend returned an unsuccessful response code.
    // The response body may contain clues as to what went wrong,
    console.error(
      `Backend returned code ${error.status}, ` +
      `body was: ${error.error}`);
  }
	return "Error";
};
*/


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
