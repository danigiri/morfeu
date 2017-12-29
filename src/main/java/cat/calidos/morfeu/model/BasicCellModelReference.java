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

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class BasicCellModelReference extends BasicCellModel implements CellModelReference {

private CellModel reference;

//public BasicCellModelReference(String name, CellModel ref) {
//	super(ref.getURI(), name, ref.getDesc(), ref.getType(), ref.getMinOccurs(), ref.getMaxOccurs(), ref.getMetadata());
//
//	this.reference = ref;
//	this.isReference = true;
//
//}


public BasicCellModelReference(URI u, String name,  int minOccurs, int maxOccurs, Metadata metadata, CellModel ref) {

	super(u, name, metadata.getDesc(), ref.getType(), minOccurs, maxOccurs, ref.getDefaultValue(), metadata);

	this.reference = ref;
	this.isReference = true;
	
}


@Override
public boolean isSimple() {
	return reference.isSimple();
}


// TODO: candidate to remove as it's not needed
@Override
public Type getType() {
	return reference.getType();
}


@Override
public boolean isWeak() {
	return false;
}


@Override
public CellModel reference() {
	return this.reference;
}


@Override
public CellModelReference asReference() {
	return this;
}


@Override
public ComplexCellModel asComplex() {
	throw new ClassCastException("Tried to access reference cell model as complex ("+getName()+")");
}




}
