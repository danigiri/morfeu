package cat.calidos.morfeu.utils;

import java.util.NoSuchElementException;


public class NamedPair<Z> extends Pair<Z, Z> {

private static final String	LEFT_DEFAULT_NAME	= "left";
private static final String	RIGHT_DEFAULT_NAME	= "right";

private String	leftName	= LEFT_DEFAULT_NAME;
private String	rightName	= RIGHT_DEFAULT_NAME;

public NamedPair(Z left, Z right) {
	super(left, right);
	this.leftName = LEFT_DEFAULT_NAME;
	this.rightName = RIGHT_DEFAULT_NAME;
}


public NamedPair(String leftName, Z left, String rightName, Z right) {
	super(left, right);
	if (leftName.equals(rightName)) {
		throw new IllegalArgumentException(
				"Can't have a named pair with same names (" + leftName + ")");
	}
	this.leftName = leftName;
	this.rightName = rightName;
}


public Z get(String name) {
	if (!(name.equals(leftName) || name.equals(rightName))) {
		throw new NoSuchElementException("Name not found (" + name + ")");
	}
	if (name.equals(leftName)) {
		return getLeft();
	} else {
		return getRight();
	}
}

}

/*
 * Copyright 2024 Daniel Giribet
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
