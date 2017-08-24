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
import java.util.Iterator;
import java.util.List;

import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSSchemaSet;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Model extends RemoteResource {

protected URI fetchableURI;
protected XSSchemaSet schema;
protected List<CellModel> roots;
public static final String MODEL_NAMESPACE = "";


public Model(URI u, String desc, URI fetchableURI, XSSchemaSet s, List<CellModel> rootTypes) {

	// TODO: fetch description from annotation
	super(u, u.getPath().substring(u.getPath().lastIndexOf("/")).substring(1), desc);

	this.schema = s;
	this.roots = rootTypes;
	this.fetchableURI = fetchableURI;
	
}


public List<CellModel> getRootCellModels() {
	return roots;
}


public int getComplextTypeCount() {
	
	//	XSSchema modelSchema = schema.getSchema("http://dani.calidos.com/morfeu/model");
	Iterator<XSComplexType> typeIterator = schema.iterateComplexTypes();
	int c = 0;
	while (typeIterator.hasNext()) {
		typeIterator.next();
		c++;
	}
	
	return c;
}


public URI getFetchableURI() {
	return fetchableURI;
}


}
