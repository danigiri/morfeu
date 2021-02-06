// COMPLEX CELL . JAVA

package cat.calidos.morfeu.model;

import java.net.URI;
import java.util.Optional;


/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
public class ComplexCell extends Cell {

protected Composite<Cell> children;
protected Attributes<Cell> attributes;
private Attributes<Cell> internalAttributes;


public ComplexCell(URI u, 
					String name, 
					String desc, 
					Optional<String> value, 
					CellModel cm, 
					Composite<Cell> children, 
					Attributes<Cell> attributes,
					Attributes<Cell> internalAttributes) {

	super(u, name, desc, value, cm);

	this.children = children;
	this.attributes = attributes;
	this.internalAttributes = internalAttributes;
	this.isSimple = false;

}


public Composite<Cell> children() {
	return children;
}


public Attributes<Cell> attributes() {
	return attributes;
}


public Attributes<Cell> internalAttributes() {
	return internalAttributes;
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.model.Cell#asComplex()
*//////////////////////////////////////////////////////////////////////////////
@Override
public ComplexCell asComplex() {
	return this;
}


public static ComplexCell from(Cell c) {
	return (ComplexCell)c;
}


}

/*
 *    Copyright 2018 Daniel Giribet
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
