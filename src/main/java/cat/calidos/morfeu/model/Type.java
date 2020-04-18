// TYPE . JAVA

package cat.calidos.morfeu.model;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

import com.sun.xml.xsom.XSType;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Type extends RemoteResource {

private Optional<XSType> xsType;
private Optional<String> regex;
private Set<String> possibleValues;
private boolean isGlobal;
private Optional<Metadata> metadata;


public Type(URI u, String name) {

	super(u, name, "TYPE DESC");

	this.xsType = Optional.empty();
	this.isGlobal = false;
	this.metadata = Optional.empty();
	this.regex = Optional.empty();
}


public Type(URI u, String name, 			
			XSType xsType,
			String regex,
			Set<String> possibleValues,
			boolean global,
			Metadata metadata) {
	//TODO: extract description for types from annotation
	super(u, name, "TYPE DESC");

	this.xsType = Optional.of(xsType);
	this.regex = Optional.ofNullable(regex);
	this.possibleValues = possibleValues;
	this.isGlobal = global;
	this.metadata = Optional.of(metadata);

}


public boolean isSimple() {
	return xsType.isPresent() ? xsType.get().isSimpleType() : false;
}


public boolean isGlobal() {
	return isGlobal;
}


public Optional<String> getRegex() {
	return regex;
}


/**	@return all allowed values of elements of this type, empty set means all values allowed */
public Set<String> getPossibleValues() {
	return possibleValues;
}


public Metadata getMetadata() {
	return metadata.orElse(null);
}


public boolean isContentValid(Object content) {

	if (isSimple()) {
		//TODO: we're assuming we have a base type that can tell us how to validate
		XSType baseType = xsType.get().getBaseType();
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


@Override
public String toString() {
	return "["+name+", "+(isSimple() ? "simple]" : "complex]");
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


}

/*
 *    Copyright 2019 Daniel Giribet
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
