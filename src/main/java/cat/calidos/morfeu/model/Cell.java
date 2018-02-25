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

package cat.calidos.morfeu.model;

import java.net.URI;
import java.util.Optional;

/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////////////////////////
public class Cell extends RemoteResource {

protected CellModel cellModel;
protected Optional<String> value;
protected boolean isSimple = true;

public Cell(URI u) {
	super(u);
}


public Cell(URI u, String name, String desc) {
	super(u, name, desc);
}


public Cell(URI u, String name, String desc, Optional<String> value, CellModel cm) {

	super(u, name, desc);

	this.value = value;
	this.cellModel = cm;

}


public CellModel getCellModel() {
	return this.cellModel;
}


public boolean isSimple() {
	return isSimple;
}


public boolean isComplex() {
	return !isSimple();
}


public ComplexCell asComplex() {
	throw new ClassCastException("Tried to access simple cell as complex ("+getName()+")");
}


public Optional<String> getValue() {
	return value;
}


}
