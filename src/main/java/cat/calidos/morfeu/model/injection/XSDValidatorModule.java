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

package cat.calidos.morfeu.model.injection;

import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import cat.calidos.morfeu.model.Validable;
import cat.calidos.morfeu.model.XSDValidator;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class XSDValidatorModule {

protected final static Logger log = LoggerFactory.getLogger(XSDValidatorModule.class);

@Produces
public static Validable producerXSDValidator(Validator v, DOMSource xmldom) {
	return new XSDValidator(v, xmldom);
}


@Produces
public static Validator validator(Schema s) {
	
	Validator v = s.newValidator();
	// TODO: check if this is needed
	v.setErrorHandler(new ErrorHandler() {
	
	@Override
	public void warning(SAXParseException exception) throws SAXException {
		
		log.warn("Warning '{}' when parsing '{}'", exception.getMessage(), s.toString());
		throw exception;

	}
	
	
	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		
		log.error("Fatal problem '{}' when parsing '{}'", exception.getMessage(), s.toString());
		throw exception;
		
	}
	
	
	@Override
	public void error(SAXParseException exception) throws SAXException {
		
		log.error("Problem '{}' when parsing '{}'", exception.getMessage(), s.toString());
		throw exception;
		
	}
	});
	
	return v;
	
}

}
