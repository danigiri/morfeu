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
import java.util.Optional;

/** A Cell Model that has attributes (which cannot be repeated), order is considered important so using list and not set
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ComplexCellModel extends BasicCellModel {

private boolean areChildrenOrdered;
private Attributes<CellModel> attributes;
private Composite<CellModel> children;


/** Create a complex cell model from scratch
*	@param u URL
*	@param name of the cell model
*	@param desc short description
*	@param type associated type
*	@param minOccurs minimum number of occurrences (0, 1, 2...)
*	@param maxOccurs max number of occurrences
*	@param defaultValue
*	@param category
*	@param areChildrenOrdered
*	@param meta
*	@param attributes
*	@param children
*/
public ComplexCellModel(URI u,
						String name,
						String desc,
						Type type,
						int minOccurs,
						int maxOccurs,
						Optional<String> defaultValue,
						Optional<String> category,
						boolean areChildrenOrdered,
						Metadata meta,
						Attributes<CellModel> attributes,
						Composite<CellModel> children) {

	super(u, name, desc, type, minOccurs, maxOccurs, false, defaultValue, category, meta);

	this.attributes = attributes;
	this.areChildrenOrdered = areChildrenOrdered;
	this.children = children;
	this.isSimple = false;

}


public ComplexCellModel(URI u, 
						String name,
						String desc,
						Type type,
						int minOccurs,
						int maxOccurs,
						Optional<String> defaultValue,
						Optional<String> category,
						Metadata meta,
						Attributes<CellModel> attributes, 
						ComplexCellModel ref) {

	super(u, name, desc, type, minOccurs, maxOccurs, false, defaultValue, category, meta, ref);

	this.attributes = attributes;	// attributes are the same as the reference, but may have different metadata
	this.areChildrenOrdered = ref.areChildrenOrdered;
	this.children = null;			// we will use the ones from the reference to ensure they are the same
	this.isSimple = false;

}


public Attributes<CellModel> attributes() {
	return attributes;
}


public void setAttributes(Attributes<CellModel> attributes) {
	this.attributes = attributes;	
}


public Composite<CellModel> children() {
	return isReference() ? getReference().get().asComplex().children() : children;
}


public void setChildren(Composite<CellModel> children) {
	this.children = children;
}


public boolean areChildrenOrdered() {
	return areChildrenOrdered;
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.model.CellModel#asComplexCellModel()
*//////////////////////////////////////////////////////////////////////////////
@Override
public ComplexCellModel asComplex() {
	return this;
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.model.CellModel#toString()
*//////////////////////////////////////////////////////////////////////////////
@Override
public String toString() {

	StringBuilder sb = new StringBuilder(super.toString());
	if (attributes!=null && attributes.size()>0) {
		sb.append("\n\tattributes:\n");
		sb.append(attributes.toString());
	}
	if (children!=null && !isReference() && children.size()>0) {
		// TODO: replace \ns with \n\t's here for correct indendation
		sb.append("\nchildren("+children.size()+"):\n");
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
