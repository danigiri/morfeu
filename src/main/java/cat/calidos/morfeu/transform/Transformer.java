package cat.calidos.morfeu.transform;


/** Generic traversal and transformation algorithm
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Transformer<T, O> {

private Context<T, O> context;


public void Transformer(Context<T, O> startingContext) {
	this.context = startingContext;
}


public O process() {

	while (!context.empty()) {
		Processor<T, O> current = context.pop();

	}

	return context.output();

}


@Override
public String toString() {

	// TODO Auto-generated method stub
	return super.toString();
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

