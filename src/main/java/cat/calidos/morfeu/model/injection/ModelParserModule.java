// MODEL PARSER MODULE . JAVA

package cat.calidos.morfeu.model.injection;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

import com.sun.xml.xsom.parser.AnnotationParserFactory;
import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.util.DomAnnotationParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import cat.calidos.morfeu.problems.ConfigurationException;

/**	Produce a bunch of XML parsing objects needed to parse content
* 	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class ModelParserModule {

protected final static Logger log = LoggerFactory.getLogger(ModelParserModule.class);


@Produces
public static SAXParserFactory produceSAXParserFactory() throws ConfigurationException {

	log.trace("[Producing saxParserFactory]");

	//TODO: double-check which parser to use that implements security like we want
	SAXParserFactory factory = SAXParserFactory.newInstance();
	factory.setNamespaceAware(true);
	try {
		// TODO: checkout how to ensure we can load includes but only from the same origin and stuff
		factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
	} catch (SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException e) {
		throw new ConfigurationException("Problem building schema parser", e);
	}

	return factory;

}


@Produces
public static XSOMParser produceSchemaParser(SAXParserFactory factory, 
												ErrorHandler parserErrorHandler, 
												AnnotationParserFactory annotationParserFactory) {

	log.trace("[Producing XSOMParser] with custom error handler and annotation parser");

	XSOMParser parser = new XSOMParser(factory);
	parser.setErrorHandler(parserErrorHandler);
	parser.setAnnotationParser(annotationParserFactory);

	return parser;

}


@Produces
public static ErrorHandler errorHandler() {
	return new LoggingSchemaParserErrorHandler();
}


@Produces
public static AnnotationParserFactory annotationParserFactory() {
	return new DomAnnotationParserFactory();
}


private static final class LoggingSchemaParserErrorHandler implements ErrorHandler {

	@Override
	public void warning(SAXParseException e) throws SAXException {
		log.warn("warning SAXParseException {} at {}", e.getMessage(), e.getSystemId());
		throw e;
	}
	
	
	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		log.error("Fatal problem SAXParseException {} at {}", e.getMessage(), e.getSystemId());
		throw e;
	}


	@Override
	public void error(SAXParseException e) throws SAXException {
		log.error("Problem SAXParseException {} at {}", e.getMessage(), e.getSystemId());
		throw e;
	}

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

