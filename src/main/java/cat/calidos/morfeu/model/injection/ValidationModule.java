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

package cat.calidos.morfeu.model.injection;

import java.io.IOException;
import java.net.URI;

import javax.inject.Named;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.xml.utils.DefaultErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
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
public class ValidationModule {

final static Logger log = LoggerFactory.getLogger(ValidationModule.class);

@Produces
public static DocumentBuilderFactory produceDcoumentBuilderFactory() {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setNamespaceAware(true);
	return dbf;
}


@Produces
public static DocumentBuilder produceDocumentBuilder(DocumentBuilderFactory dbf, Schema s) throws ParserConfigurationException {

	//dbf.setSchema(s);
	DocumentBuilder db = dbf.newDocumentBuilder();
	
	return db;

}


@Produces
public static SchemaFactory produceSchemaFactory() {
	
	SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	
	return sf;

}

@Produces
public static StreamSource produceStreamSource(@Named("ModelURI") URI u) {
	return new StreamSource(u.toString());
}


@Produces
public static Schema produceSchema(SchemaFactory sf, StreamSource schemaSource) throws SAXException {
	
	Schema schema = sf.newSchema(schemaSource);

	return schema;

}


@Produces
public static Validator produceValidator(Schema s) {
	
	Validator v = s.newValidator();
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


@Produces
public static Document produceXMLDocument(DocumentBuilder db, @Named("ContentURI") URI uri) throws SAXException, IOException {
	
	// TODO: we can probably parse with something faster than building into dom
	Document dom = db.parse(uri.toString());

	return dom;

}

@Produces
public static DOMSource produceDOMSource(Document xmldoc) {
	return new DOMSource(xmldoc);
}

@Produces
public static Validable xsdValidator(Validator v, DOMSource xmldom) {
	return new XSDValidator(v, xmldom);
}

}
