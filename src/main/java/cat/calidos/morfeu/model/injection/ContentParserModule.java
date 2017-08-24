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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.Validable;
import cat.calidos.morfeu.model.XSDValidator;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.OrderedMap;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/** Module to validate and parse document content
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class ContentParserModule {

protected final static Logger log = LoggerFactory.getLogger(ContentParserModule.class);

@Produces
public static Validator produceValidator(Schema s) {
	
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



@Produces
public static Composite<Cell> produceContent(@Named("ContentURI") URI u, org.w3c.dom.Document xmldoc, Model m) throws ParsingException {
	
	// TODO: add root node attributes and value as cells
	//xmldoc.getAttributes()
	//xmldoc.getDocumentElement()
	
	LinkedList<Node> pendingNodes = new LinkedList<Node>();

	Node rootNode = xmldoc.getFirstChild();
	while (rootNode!=null) {
		pendingNodes.add(rootNode);
		rootNode = rootNode.getNextSibling();
	}
		
	List<CellModel> rootCellModels = m.getRootCellModels();
return contentCells(pendingNodes, u, rootCellModels);

	
	
}


private static Composite<Cell> contentCells(LinkedList<Node> pendingNodes, URI uri, List<CellModel> cellModels) throws ParsingException {

	//TODO: this is a bit repetitive from cellmodule, but let's leave it like this for the moment
	
	Composite<Cell> contentCells = new OrderedMap<Cell>();
	int cellIndex = 0;
	pendingNodes.stream().map(node -> {
		
		String name = node.getNodeName();
		Optional<CellModel> matchedCellModel = cellModels.stream().filter(cm -> cm.getName().equals(name)).findFirst();
		if (!matchedCellModel.isPresent()) {
			log.error("Could not match root ontent node '{}' with any cellmodel even tough content is valid", name);
			throw new RuntimeException("Node and model mismatch", new NullPointerException());
		}
		CellModel cellModel = matchedCellModel.get();
		
		URI cellURI;
		String proposedCellURI = uri+"/"+name;
		try {
			cellURI = new URI(proposedCellURI);
		} catch (URISyntaxException e) {
			log.error("Could not build URI of root content node '{}'", name);
			throw new RuntimeException("Node and model mismatch", new NullPointerException());
		}
		
		return DaggerCellComponent.builder()
				.withURI(cellURI)
				.fromNode(node)
				.withCellModel(cellModel)
				.builder()
				.createCell();
//		return name;
	}).forEach(cell -> contentCells.addChild(cell.getName()+"["+cellIndex+"]", cell));
	
	return contentCells;

}



@Produces
public static DocumentBuilderFactory produceDocumentBuilderFactory() {
	
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setNamespaceAware(true);
	
	return dbf;

}


@Produces
public static DocumentBuilder produceDocumentBuilder(DocumentBuilderFactory dbf, Schema s) throws ConfigurationException {

	//dbf.setSchema(s);
	DocumentBuilder db;
	try {
		db = dbf.newDocumentBuilder();
	} catch (ParserConfigurationException e) {
		throw new ConfigurationException("Problem when configuring the xml parsing system", e);
	}
	
	return db;

}


@Produces
public static SchemaFactory produceSchemaFactory() {
	
	SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	
	return sf;

}


@Produces
public static StreamSource produceStreamSource(@Named("FetchableModelURI") URI u) {
	return new StreamSource(u.toString());
}


@Produces
public static Schema produceSchema(SchemaFactory sf, StreamSource schemaSource) throws ParsingException {
	
	Schema schema;
	try {
		schema = sf.newSchema(schemaSource);
	} catch (SAXException e) {
		throw new ParsingException("Problem when reading the model schema", e);
	}

	return schema;

}


// notice this is a DOM Document and not a morfeu document
@Produces
public static org.w3c.dom.Document produceParsedContent(DocumentBuilder db, @Named("FetchableContentURI") URI u) 
		throws ParsingException, FetchingException {
	
	// TODO: we can probably parse with something faster than building into dom
	Document dom;
	String uri = u.toString();
	try {
		dom = db.parse(uri);
	} catch (SAXException e) {
		throw new ParsingException("Problem when parsing '"+uri+"'", e);
	} catch (IOException e) {
		throw new FetchingException("Problem when fetching '"+uri+"'", e);
	}

	return dom;

}


@Produces
public static DOMSource produceDOMSource(org.w3c.dom.Document xmldoc) {
	return new DOMSource(xmldoc);
}

@Produces
public static Validable xsdValidator(Validator v, DOMSource xmldom) {
	return new XSDValidator(v, xmldom);
}

}
