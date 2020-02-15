// CONTENT PARSER MODULE . JAVA

package cat.calidos.morfeu.model.injection;

import java.net.URI;
import java.util.Optional;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import javax.inject.Named;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.OrderedMap;

/** Module to validate and parse document content
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class ContentParserModule {

protected final static Logger log = LoggerFactory.getLogger(ContentParserModule.class);


@Produces
public static Composite<Cell> produceContent(@Named("ContentURI") URI uri,
												@Named("ContentNode") Node node,
												@Named("CellModel") CellModel cellModel,
												@Named("CellModelFilter") Optional<URI> cellModelFilter)
								throws ParsingException {
	
	//FIXME: this is a quite repetitive from cellmodule, not following DRY	
	Composite<Cell> contentCells = new OrderedMap<Cell>();


	Cell cell = DaggerCellComponent.builder()
										.withURI(uri)
										.fromNode(node)
										.withCellModel(cellModel)
										.build()
										.createCell();
	if (!cellModelFilter.isPresent()) {	// we have a root node which is this cell, which is an empty root cell
		cell.setName(Document.ROOT_NAME);
	}
	contentCells.addChild(cell.getName(), cell);

	return contentCells;

}


// in this case, if we don't have a filter we return the whole document as, the cell structure in this case will contain
// an empty cell at the root to hold everything together
// If we have a filter, we are returning directly the first node as we do not have an empty cell node at the root
@Produces @Named("ContentNode")
public static Node contentRootNode(org.w3c.dom.Document xmldoc, 
									@Named("CellModelFilter") Optional<URI> cellModelFilter) {
	return cellModelFilter.isPresent() ? xmldoc.getFirstChild() : xmldoc;
}


@Produces
public static SchemaFactory produceSchemaFactory() {
	return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
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


@Produces
public static DOMSource produceDOMSource(org.w3c.dom.Document xmldoc) {
	return new DOMSource(xmldoc);
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
