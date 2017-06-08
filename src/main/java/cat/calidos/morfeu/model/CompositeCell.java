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

import cat.calidos.morfeu.utils.BasicComposite;

/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
public class CompositeCell extends Cell implements Composite<Cell> {

public CompositeCell(URI u, String name, String desc) {
	super(u, name);
	this.desc = desc;
}

protected BasicComposite<Cell> children;


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
public int addChild(Cell c) {
	return children.addChild(c);
}


@Override
public boolean hasChildren() {
	return children.hasChildren();
}
	
}
