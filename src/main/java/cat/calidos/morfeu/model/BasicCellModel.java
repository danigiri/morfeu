/*
 * Copyright 2017 Daniel Giribet
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

package cat.calidos.morfeu.model;

import java.net.URI;
import java.util.Optional;
import java.util.OptionalInt;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class BasicCellModel extends RemoteResource implements CellModel {

private Type				type;
private int					minOccurs;
private OptionalInt			maxOccurs;
private Optional<String>	defaultValue;
private boolean				isAttribute;
protected boolean			isSimple	= true;
private Optional<String>	category;
private Metadata			metadata;
private boolean				isReference;
private Optional<CellModel>	reference;

public BasicCellModel(	URI u,
						String name,
						String desc,
						Type type,
						int minOccurs,
						int maxOccurs,
						boolean isAttribute,
						Optional<String> defaultValue,
						Optional<String> category,
						Metadata meta) {

	super(u, name, desc);

	this.type = type;
	this.minOccurs = minOccurs;
	this.maxOccurs = maxOccurs == CellModel.UNBOUNDED ? OptionalInt.empty()
			: OptionalInt.of(maxOccurs);
	this.isAttribute = isAttribute;
	this.defaultValue = defaultValue;
	this.category = category;
	this.metadata = meta;
	this.isReference = false;
	this.reference = Optional.empty();

}


public BasicCellModel(	URI u,
						String name,
						String desc,
						Type type,
						int minOccurs,
						int maxOccurs,
						boolean isAttribute,
						Optional<String> defaultValue,
						Optional<String> category,
						Metadata meta,
						CellModel ref) {

	this(u, name, desc, type, minOccurs, maxOccurs, isAttribute, defaultValue, category, meta);

	this.isReference = true;
	this.reference = Optional.of(ref);

}


/*
 * (non-Javadoc)
 * 
 * @see cat.calidos.morfeu.model.CellModelI#isSimple()
 *//////////////////////////////////////////////////////////////////////////////
@Override
public boolean isSimple() {
	return isSimple;
}


@Override
public boolean isComplex() {
	// this is implemented as jtwig is unable to use a default interface method
	return !isSimple();
}


@Override
public boolean isAttribute() {
	return isAttribute;
}


/*
 * (non-Javadoc)
 * 
 * @see cat.calidos.morfeu.model.CellModelI#asComplex()
 *//////////////////////////////////////////////////////////////////////////////
@Override
public ComplexCellModel asComplex() {
	throw new ClassCastException(
			"Tried to access simple cell model as complex (" + getName() + ")");
}


/*
 * (non-Javadoc)
 * 
 * @see cat.calidos.morfeu.model.CellModelI#getType()
 *//////////////////////////////////////////////////////////////////////////////
@Override
public Type getType() {
	return type;
}


/*
 * (non-Javadoc)
 * 
 * @see cat.calidos.morfeu.model.CellModel#getMinOccurs()
 *//////////////////////////////////////////////////////////////////////////////
@Override
public int getMinOccurs() {
	return minOccurs;
}


/*
 * (non-Javadoc)
 * 
 * @see cat.calidos.morfeu.model.CellModel#getMaxOccurs()
 *//////////////////////////////////////////////////////////////////////////////
@Override
public OptionalInt getMaxOccurs() {
	return maxOccurs;
}


@Override
public Optional<String> getCategory() {
	return category;
}


@Override
public Metadata getMetadata() {
	return metadata;
}


/*
 * (non-Javadoc)
 * 
 * @see cat.calidos.morfeu.model.CellModel#getDefaultValue()
 *//////////////////////////////////////////////////////////////////////////////
@Override
public Optional<String> getDefaultValue() {
	return defaultValue;
}


/*
 * (non-Javadoc)
 * 
 * @see cat.calidos.morfeu.model.CellModelI#isReference()
 *//////////////////////////////////////////////////////////////////////////////
@Override
public boolean isReference() {
	return isReference;
}


/*
 * (non-Javadoc)
 * 
 * @see cat.calidos.morfeu.model.CellModel#getReference()
 *//////////////////////////////////////////////////////////////////////////////
@Override
public Optional<CellModel> getReference() {

	return reference;

}


/*
 * (non-Javadoc)
 * 
 * @see java.lang.Object#toString()
 *//////////////////////////////////////////////////////////////////////////////
@Override
public String toString() {
	return "[" + name + ", (" + type + ")"
			+ (defaultValue.isPresent() ? ", default:" + defaultValue.get() : "") + "]";
}

}
