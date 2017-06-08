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
import java.util.Optional;

import cat.calidos.morfeu.problems.InternalException;

/** A Cell Model that has attributes (which cannot be repeated), order is considered important so using list and not set
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ComplexCellModel extends CellModel implements Composite<CellModel>, Attributes<CellModel> {

private List<CellModel> attributes;


public ComplexCellModel(URI u, String name, Type type, List<CellModel> attributes) {
	
	super(u, name, type);
	
	this.attributes = attributes;
	
}


@Override
public boolean hasAttributes() {
	return !attributes.isEmpty();
}


public List<CellModel> getAttributes() {
	return attributes;
}


public CellModel attribute(int i) {
	
	int size = attributes.size();
	if (i>=size) {
		throw new InternalException("Cannot get attribute "+i+" at "+name+" of type "+type+" with only "+size+" attributes");
	}

	return attributes.get(i);
	
}


public CellModel attribute(String name) {

	Optional<CellModel> maybeCellModel = attributes.stream()
											.filter(cellModel -> cellModel.getName().equals(name)).findFirst();
	if (!maybeCellModel.isPresent()) {
		throw new InternalException("Cannot get attribute "+name+" at "+name+" of type "+type);
	}

	return maybeCellModel.get();
	
}


@Override
public List children() {

	// TODO Auto-generated method stub
	return null;
}


@Override
public CellModel child(int i) {

	// TODO Auto-generated method stub
	return null;
}


@Override
public List clearChildren() {

	// TODO Auto-generated method stub
	return null;
}


@Override
public int addChild(CellModel c) {

	// TODO Auto-generated method stub
	return 0;
}


@Override
public boolean hasChildren() {

	// TODO Auto-generated method stub
	return false;
}


}
