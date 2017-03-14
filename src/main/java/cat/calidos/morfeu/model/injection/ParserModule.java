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


import dagger.Module;
import dagger.Provides;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class ParserModule {


@Produces
public static SAXParserFactory provideSAXParserFactory() throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
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
public static XSOMParser provideSchemaParser(SAXParserFactory factory) {
	
    XSOMParser parser = new XSOMParser(factory);
    //parser.setErrorHandler(new ErrorHandler()); TODO: handle parse errors more gracefully
    
    parser.setErrorHandler(new SchemaParserErrorHandler());
    return parser;
    
}


@Produces
public static ObjectMapper provideJSONObjectMapper() {
	ObjectMapper mapper = new ObjectMapper();
	return mapper;	//TODO: check if it is necessary to 'provide' default constructor objects
}


private static final class SchemaParserErrorHandler implements ErrorHandler {

@Override
public void warning(SAXParseException exception) throws SAXException {
	
	// TODO Auto-generated method stub
	
}


@Override
public void fatalError(SAXParseException exception) throws SAXException {
	
	// TODO Auto-generated method stub
	
}


@Override
public void error(SAXParseException exception) throws SAXException {
	
	// TODO Auto-generated method stub
	
}
}


}
