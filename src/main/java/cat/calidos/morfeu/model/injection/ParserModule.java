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

package cat.calidos.morfeu.model.injection;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.xsom.parser.XSOMParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class ParserModule {

protected final static Logger log = LoggerFactory.getLogger(ParserModule.class);
		
@Produces
public static SAXParserFactory produceSAXParserFactory() throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {

	log.trace("Producing saxParserFactory");
	
	//TODO: double-check which parser to use that implements security like we want
	SAXParserFactory factory = SAXParserFactory.newInstance();
	factory.setNamespaceAware(true);
//    try {
    	// TODO: checkout how to ensure we can load includes but only from the same origin and stuff
    	factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
//	} catch (SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	return factory;
}


@Produces
public static XSOMParser produceSchemaParser(SAXParserFactory factory) {
	
	log.trace("Producing XSOMParser");

    XSOMParser parser = new XSOMParser(factory);
    parser.setErrorHandler(new SchemaParserErrorHandler());
//    parser.setEntityResolver(new EntityResolver() {
//	
//	@Override
//	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
//		
//		// TODO Auto-generated method stub
//		System.err.println(publicId+","+systemId);
//		return null;
//	}
//	});
    
    return parser;
    
}


@Produces
public static ObjectMapper produceJSONObjectMapper() {
	
	log.trace("Producing ObjectMapper");
	return new ObjectMapper();	//TODO: check if it is necessary to 'provide' default constructor objects
	
}


private static final class SchemaParserErrorHandler implements ErrorHandler {

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
