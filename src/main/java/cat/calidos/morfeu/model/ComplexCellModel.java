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

package cat.calidos.morfeu.model;

import java.net.URI;
import java.util.List;

import cat.calidos.morfeu.problems.InternalException;

/** A Cell Model that has attributes (which cannot be repeated), order is considered important so using list and not set
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ComplexCellModel extends CellModel implements Composite<CellModel>, Attributes<CellModel> {


private Attributes<CellModel> attributes;
private Composite<CellModel> children;

public ComplexCellModel(URI u, String name, Type type, Attributes<CellModel> attributes, Composite<CellModel> children) {
	
	super(u, name, type);
	
	this.attributes = attributes;
	this.children = children;
}


@Override
public boolean hasAttributes() {
	return attributes.hasAttributes();
}


@Override
public boolean hasAttribute(String name) {
	return attributes.hasAttribute(name);
}


@Override
public List<CellModel> attributes() {
	return attributes.attributes();
}


@Override
public int size() {
	return attributes.size();
}


public CellModel attribute(int i) {
	
	try {
		return attributes.attribute(i);
	} catch (InternalException e) {
		throw new InternalException("Tried to get attribute "+i+" on cell model "+name+" of type "+type.getName(), e);
	}
	
}


public CellModel attribute(String name) {
	
	try {
		return attributes.attribute(name);
	} catch (InternalException e) {
		throw new InternalException("Cannot get attribute "+name+" at "+name+" of type "+type);
	}
	
}


public boolean addAttribute(String name, CellModel attribute) {
	return attributes.addAttribute(name, attribute);
}


@Override
public List<CellModel> clearAttributes() {
	return attributes.clearAttributes();
}


@Override
public boolean hasChildren() {
	return children.hasChildren();
}


@Override
public List<CellModel> children() {
	return children.children();
}


@Override
public CellModel child(int i) {
	return children.child(i);
}


@Override
public List<CellModel> clearChildren() {
	return children.clearChildren();
}


@Override
public CellModel child(String name) {

	// TODO Auto-generated method stub
	return null;
}


@Override
public boolean addChild(String key, CellModel c) {

	// TODO Auto-generated method stub
	return false;
}



}
