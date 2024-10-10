/*
 * Copyright 2018 Daniel Giribet
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

package cat.calidos.morfeu.model.injection;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import dagger.BindsOptionalOf;
import dagger.producers.Producer;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import cat.calidos.morfeu.model.NullValidator;
import cat.calidos.morfeu.model.Validable;
import cat.calidos.morfeu.model.XSDValidator;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public abstract class ValidatorModule {

protected final static Logger log = LoggerFactory.getLogger(ValidatorModule.class);

@Produces
public static Validable produceValidator(	@Named("SkipValidation") Optional<Boolean> skipValidation,
											@Named("XSDValidator") Producer<Validable> xsdValidator,
											@Named("NullValidator") Producer<Validable> nullValidator)
		throws InterruptedException, ExecutionException {
	return skipValidation.isPresent() && skipValidation.get().booleanValue()
			? nullValidator.get().get() : xsdValidator.get().get();

}


@Named("SkipValidation") @BindsOptionalOf
abstract Boolean skipValidation();


@Produces @Named("XSDValidator")
public static Validable xsdValidator(	Validator v,
										DOMSource xmldom) {
	return new XSDValidator(v, xmldom);
}


@Produces @Named("NullValidator")
public static Validable produceNullValidator() { // will be used if skipValidation == true
	return new NullValidator();
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
