package cat.calidos.morfeu.transform;


/** Generic traversal and transformation algorithm, used to convert form one type T to another O
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Converter<T, O> {

private Context<T, O> context;
private Processor<T, O> current;


public Converter(Context<T, O> startingContext) {
	this.context = startingContext;
}


/**	@return return the processed T context */
public O process() {

	while (!context.empty()) {
		current = context.pop();
		context.appendToOutput(current.output());
		context = current.generateNewContext(context);
	}

	return context.output();

}


@Override
public String toString() {

	StringBuffer out = new StringBuffer();
	out.append("⊏-  context.output: -----------------------------⊐\n");
	out.append(context.output().toString());
	out.append("⊏- current --------------------------------------⊐\n");
	out.append("*");
	out.append(current!=null ? current.toString() : "<null>");
	out.append("*\n");
	out.append("⊏- context: -------------------------------------⊐\n");
	out.append(context.toString());
	return out.toString();
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

