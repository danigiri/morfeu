package cat.calidos.morfeu.utils;

/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Pair<X, Y> {

private X	left;
private Y	right;

public Pair(X left, Y right) {
	this.left = left;
	this.right = right;
}


public X getLeft() { return left; }


public Y getRight() { return right; }


public static <X, Y> Pair<X, Y> of(	X left,
									Y right) {
	return new Pair<X, Y>(left, right);
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
