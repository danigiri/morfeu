/*
 *    Copyright 2016 Daniel Giribet
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
import java.util.stream.Stream;


/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
public class ComplexCell extends Cell implements Composite<Cell>, Attributes<Cell> {

protected Composite<Cell> children;
protected Attributes<Cell> attributes;


public ComplexCell(URI u, String name, String desc, String value, CellModel cm, Composite<Cell> children, Attributes<Cell> attributes) {
	
	super(u, name, value, desc, cm);
	
	this.children = children;
	this.attributes = attributes;

}



@Override
public List<Cell> asList() {
	return children.asList();
}


@Override
public Cell child(int i) {
	return children.child(i);
}


@Override
public List<Cell> clear() {
	return children.clear();
}


@Override
public boolean addChild(String name, Cell c) {
	return children.addChild(name, c);
}


@Override
public int size() {
	return children.size();
}


@Override
public Cell child(String name) {
	return children.child(name);
}


@Override
public boolean hasAttributes() {

	// TODO Auto-generated method stub
	return false;
}


@Override
public boolean hasAttribute(String name) {

	// TODO Auto-generated method stub
	return false;
}


@Override
public Cell attribute(int i) {
	return attributes.attribute(i);
}


@Override
public Cell attribute(String name) {
	return attributes.attribute(name);
}


@Override
public boolean addAttribute(String name, Cell a) {
	return attributes.addAttribute(name, a);
}

}
