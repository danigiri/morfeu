package cat.calidos.morfeu.transform;


/** Generic traversal and transformation algorithm
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Transformer<T, O> {

private Context<Processor<T, O>, O> context;

public void Transformer(Context<Processor<T, O>, O> startingContext) {
	this.context = startingContext;
}


public O process() {

	while (!context.empty()) {
		//T current = context.pop();
		
	}

	return context.output();

}


}


/*
 *    Copyright 2019 Daniel Giribet
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

