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
public class ComplexCellModel extends CellModel {


private Attributes<CellModel> attributes;
private Composite<CellModel> children;

public ComplexCellModel(URI u, String name, String desc, Type type, Attributes<CellModel> attributes, Composite<CellModel> children) {
	
	super(u, name, desc, type);
	
	this.attributes = attributes;
	this.children = children;
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.model.CellModel#isComplex()
*//////////////////////////////////////////////////////////////////////////////
@Override
public boolean isComplex() {
	return true;
}



/* (non-Javadoc)
* @see cat.calidos.morfeu.model.CellModel#asComplexCellModel()
*//////////////////////////////////////////////////////////////////////////////
@Override
public ComplexCellModel asComplexCellModel() {
	return this;
}


public Attributes<CellModel> attributes() {
	return attributes;
}


public Composite<CellModel> children() {
	return children;
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.model.CellModel#toString()
*//////////////////////////////////////////////////////////////////////////////
@Override
public String toString() {

	StringBuilder sb = new StringBuilder(super.toString());
	if (attributes.size()>0) {
		sb.append("\n\tattributes:\n");
		sb.append(attributes.toString());
	}
	if (children.size()>0) {
		// TODO: replace \ns with \n\t's here for correct indendation
		sb.append("\nchildren:\n");
		sb.append(children.toString());
	}

	return sb.toString();

}


public static ComplexCellModel from(CellModel m) {
	
	if (m.isComplex()) {
		return (ComplexCellModel)m;
	} else {
		throw new IllegalArgumentException("Tried to extract complex cell model from a simple one ("+m.getName()+"");
	}
	
}

}
