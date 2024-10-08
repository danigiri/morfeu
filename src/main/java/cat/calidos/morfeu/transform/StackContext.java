package cat.calidos.morfeu.transform;

import java.util.EmptyStackException;
import java.util.Stack;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class StackContext<T> implements Context<T, String> {

private static final int MAX_LINE_LENGTH = 49; // +1 for the final '|'

private Stack<Processor<T, String>>	stack	= new Stack<Processor<T, String>>();
private StringBuffer				output	= new StringBuffer();

@Override
public boolean empty() {
	return stack.empty();
}


@Override
public String output() {
	return output.toString();
}


@Override
public String setOutput(String o) {

	StringBuffer old = output;
	output = new StringBuffer().append(o);

	return old.toString();

}


@Override
public void appendToOutput(String toAppend) {
	output.append(toAppend);
}


@Override
public Processor<T, String> peek() throws EmptyStackException {
	return stack.peek();
}


@Override
public Processor<T, String> pop() throws EmptyStackException {
	return stack.pop();
}


@Override
public Processor<T, String> push(Processor<T, String> item) {
	return stack.push(item);
}


@Override
public String toString() {

	StringBuffer out = new StringBuffer();
	int height = stack.size();

	out.append("\n");
	out.append("⊏------------------------------------------------⊐\n");
	for (int i = 0; i < height; i++) {
		String stackLine = "|" + stack.elementAt(height - i - 1); // reverse
		stackLine = stackLine.substring(0, Math.min(stackLine.length(), MAX_LINE_LENGTH));
		out.append(stackLine);
		// we add padding if needed
		for (int p = stackLine.length(); p < MAX_LINE_LENGTH; p++) {
			out.append(" ");
		}
		out.append("|\n");
	}
	out.append("⊏------------------------------------------------⊐\n");

	return out.toString();

}

}

/*
 * Copyright 2019 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
