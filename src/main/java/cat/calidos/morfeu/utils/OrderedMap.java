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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cat.calidos.morfeu.model.Attributes;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.problems.InternalException;


/** We need a map that keeps insertion order
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class OrderedMap<T> implements Composite<T>, Attributes<T> {

private static final String ATTRIBUTE = "attribute";
private static final String CHILD = "child";

private Map<String, T> values;
private List<String>keys;

public OrderedMap() {

	this.values = new HashMap<String,T>();
	this.keys = new ArrayList<String>();

}


public OrderedMap(int initialCapacity) {

	this.values = new HashMap<String,T>(initialCapacity);
	this.keys = new ArrayList<String>(initialCapacity);

}

public  OrderedMap(String name, T value) {

	this(1);

	this.addAttribute(name, value);

}


@Override
public boolean hasChildren() {
	return hasAttributes();
}


@Override
public boolean hasAttributes() {

	return !keys.isEmpty();
}


@Override
public boolean hasAttribute(String name) {
	return values.containsKey(name);
}


@Override
public int size() {
	return values.size();
}


@Override
public T attribute(int i) {
	return get(i, ATTRIBUTE);
}


@Override
public T attribute(String name) {
	return get(name, ATTRIBUTE);
}


@Override
public boolean addAttribute(String name, T a) {
	
	boolean isOld = hasAttribute(name);
	if (!isOld) {
		keys.add(name);
	}
	values.put(name, a);
	
	return !isOld;
	
}


@Override
public List<T> clear() {

	List<T> oldAttributes = asList();
	values.clear();
	keys.clear();
	
	return oldAttributes;
	
}


@Override
public List<T> asList() {

	List<T> asList = new ArrayList<T>(keys.size());
	keys.forEach(k -> asList.add(values.get(k)));

	return asList;

}



@Override
public Stream<T> stream() {
	return asList().stream();
}


@Override
public T child(int i) {
	return get(i, CHILD);
}


@Override
public T child(String name) {
	return get(name, CHILD);
}


@Override
public boolean addChild(String key, T c) {
	return addAttribute(key,c);		// reuse implementation
}




/* (non-Javadoc)
* @see java.lang.Object#toString()
*//////////////////////////////////////////////////////////////////////////////
@Override
public String toString() {

	StringBuilder s = new StringBuilder("{\n");
	// inefficent but only used in debugging
	keys.forEach(k -> s.append(k +":" +values.get(k) + ","));
	s.append("}");
	return s.toString();
}


//@Override
//public Stream<T> stream() {
//	return values.values().stream();
//}


private T get(int i, String what) throws InternalException {

	int size = keys.size();
	if (i>=size) {
		throw new InternalException("Tried to get '"+what+"' at position "+i+" when we only have "+size);
	}
	
	return values.get(keys.get(i));
}


private T get(String name, String what) throws InternalException {

	if (values.containsKey(name)) {
		return values.get(name);
	} else {
		throw new InternalException("Cannot get '"+what+"' named '"+name+"'");
	}
}


}
