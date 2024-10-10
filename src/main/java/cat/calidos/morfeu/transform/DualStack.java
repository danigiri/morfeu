package cat.calidos.morfeu.transform;

import java.util.EmptyStackException;
import java.util.Stack;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DualStack<T1, T2> {

Stack<T1>	stackOne	= new Stack<T1>();
Stack<T2>	stackTwo	= new Stack<T2>();

boolean empty() {
	return stackOne.empty() && stackOne.empty();
}


T1 peekOne() throws EmptyStackException {
	return stackOne.peek();
}


T2 peeTwo() throws EmptyStackException {
	return stackTwo.peek();
}


T1 popOne() throws EmptyStackException {
	return stackOne.pop();
}


T2 popTwo() throws EmptyStackException {
	return stackTwo.pop();
}


T1 pushkOne(T1 item) {
	return stackOne.push(item);
}


T2 pushTwo(T2 item) {
	return stackTwo.push(item);
}


void push(	T1 itemOne,
			T2 itemTwo) {

	stackOne.push(itemOne);
	stackTwo.push(itemTwo);

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
