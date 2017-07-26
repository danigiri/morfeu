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


/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
public class ComplexCell extends Cell implements Composite<Cell> {



public ComplexCell(URI u, String name, String desc, Composite<Cell> children) {
	
	super(u, name, desc);
	
	this.children = children;

}

protected Composite<Cell> children;


@Override
public List<Cell> children() {
	return children.children();
}


@Override
public Cell child(int i) {
	return children.child(i);
}


@Override
public List<Cell> clearChildren() {
	return children.clearChildren();
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


}
