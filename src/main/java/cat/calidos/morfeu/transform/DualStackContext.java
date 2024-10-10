package cat.calidos.morfeu.transform;

import java.util.EmptyStackException;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DualStackContext<T1, T2> implements Context<DualStack<T1, T2>, String> {

// private DualStack<>

@Override
public boolean empty() {

	// TODO Auto-generated method stub
	return false;
}


@Override
public String output() {

	// TODO Auto-generated method stub
	return null;
}


@Override
public String setOutput(String o) {

	// TODO Auto-generated method stub
	return null;
}


@Override
public void appendToOutput(String output) {

	// TODO Auto-generated method stub

}


@Override
public Processor<DualStack<T1, T2>, String> peek() throws EmptyStackException {

	// TODO Auto-generated method stub
	return null;
}


@Override
public Processor<DualStack<T1, T2>, String> pop() throws EmptyStackException {

	// TODO Auto-generated method stub
	return null;
}


@Override
public Processor<DualStack<T1, T2>, String> push(Processor<DualStack<T1, T2>, String> item) {

	// TODO Auto-generated method stub
	return null;
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
