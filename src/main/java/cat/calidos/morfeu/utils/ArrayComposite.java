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

package cat.calidos.morfeu.utils;

import java.util.ArrayList;
import java.util.List;

import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.problems.InternalException;

/**
* @author daniel giribet
 * @param <T>
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ArrayComposite<T> implements Composite<T> {

private ArrayList<T> list;


public ArrayComposite() {
	list = new ArrayList<T>();
}


public ArrayComposite(int initialCapacity) {
	list = new ArrayList<T>(initialCapacity);
}


@Override
public List<T> children() {
	return list;
}


@Override
public T child(int i) {
	
	int size = list.size();
	if (i>=size) {
		throw new InternalException("Tried to get item "+i+" on a list of "+size);
	}
	
	return list.get(i);
	
}


@Override
public List<T> clearChildren() {

	List<T> oldList = list;
	list = new ArrayList<T>();
	
	return oldList;
	
}


@Override
public int addChild(T c) {
	
	list.add(c);

	return list.size();
	
}


@Override
public boolean hasChildren() {
	return !list.isEmpty();
}

}
