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

import com.sun.xml.xsom.XSType;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Type extends RemoteResource {

protected XSType xsType;

public Type(URI u, String name, XSType xsType) {

	super(u, name);
	
	this.xsType = xsType;
	
}


public boolean isSimple() {
	return xsType.isSimpleType();
}


public boolean isContentValid(Object content) {
	if (isSimple()) {
		//TODO: we're assuming we have a base type that can tell us how to validate
		XSType baseType = xsType.getBaseType();
		switch (baseType.getName()) {
		case "string":
			return content instanceof String;
		case "integer":
			return validateIntegers(content);
		default:
			return false;
		}
	} else {
		return false;
	}
}

protected boolean validateIntegers(Object content) {

	boolean isValid = true;
	Integer value = null;
	if (content instanceof Integer) {
		value  = (Integer) content;
	} else if (content instanceof String) {
		try { 
			value = Integer.parseInt((String) content);
		} catch (NumberFormatException e) {
			isValid = false;
		}
	} else {
		isValid = false;
	} 
	
	return isValid;
}


/* (non-Javadoc)
* @see java.lang.Object#toString()
*//////////////////////////////////////////////////////////////////////////////
@Override
public String toString() {
	return "["+name+", "+(isSimple() ? "simple]" : "complex]");
}




}
