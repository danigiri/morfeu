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

import java.io.IOException;

import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class XSDValidator implements Validable {


private Validator validator;
private DOMSource source;

public XSDValidator(Validator v, DOMSource s) {
	this.validator = v;
	this.source = s;
	
}

/* (non-Javadoc)
* @see cat.calidos.morfeu.model.Validable#validate()
*//////////////////////////////////////////////////////////////////////////////
@Override
public void validate() throws RuntimeException {

// FIXME: this complains strangely and fails to validate, though xmllint works well and validates
//	try {
////		validator.validate(source);
//	} catch (SAXException e) {
//		throw new RuntimeException("Issue validating '"+source.getSystemId()+"' with "+validator.toString(),e);
//	} catch (IOException e) {
//		throw new RuntimeException("IO issue validating '"+source.getSystemId()+"' with ",e);
//	}
}

}
