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

import cat.calidos.morfeu.model.injection.ModelMetadataComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class BasicCellModel extends RemoteResource implements CellModel {

protected Type type;
protected boolean isSimple = true;
protected String presentation;
protected String thumb;
protected boolean isReference = false;


public BasicCellModel(URI u, String name, String desc, Type type, String presentation, String thumb) {
	//TODO: come up with a useful description or leave empty
	super(u, name, desc);
	
	this.type = type;
	this.presentation = presentation;
	this.thumb = thumb;
	
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.model.CellModelI#isSimple()
*//////////////////////////////////////////////////////////////////////////////
@Override
public boolean isSimple() {
	return isSimple;
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.model.CellModelI#isComplex()
*//////////////////////////////////////////////////////////////////////////////
@Override
public boolean isComplex() {
	return !isSimple();
}

/* (non-Javadoc)
* @see cat.calidos.morfeu.model.CellModelI#asComplex()
*//////////////////////////////////////////////////////////////////////////////
@Override
public ComplexCellModel asComplex() {
	throw new ClassCastException("Tried to access simple cell model as complex ("+getName()+")");
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.model.CellModelI#getType()
*//////////////////////////////////////////////////////////////////////////////
@Override
public Type getType() {
	return type;
}

/* (non-Javadoc)
* @see cat.calidos.morfeu.model.CellModelI#isReference()
*//////////////////////////////////////////////////////////////////////////////
@Override
public boolean isReference() {
	return isReference;
}

/* (non-Javadoc)
* @see cat.calidos.morfeu.model.CellModelI#getPresentation()
*//////////////////////////////////////////////////////////////////////////////
@Override
public String getPresentation() {
	return (presentation.equals(ModelMetadataComponent.UNDEFINED)) ? type.getPresentation() : presentation;
}


@Override
public String getThumb() {
	return (thumb.equals(ModelMetadataComponent.UNDEFINED)) ? type.getThumb() : thumb;
}


/* (non-Javadoc)
* @see java.lang.Object#toString()
*//////////////////////////////////////////////////////////////////////////////
@Override
public String toString() {
	return "["+name+", ("+type+")]";
}


@Override
public CellModelReference asReference() {
	throw new ClassCastException("Tried to access cell model as a reference ("+getName()+")");
}




}
