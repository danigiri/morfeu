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

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import cat.calidos.morfeu.model.Attributes;
import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCell;
import cat.calidos.morfeu.model.Composite;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellModuleIntTest extends ModelTezt {


private Document document;
private URI modelURI;
private URI contentURI;


@Before
public void setup() throws Exception {
	
	String contentPath = "target/test-classes/test-resources/documents/document1.xml";
	contentURI = new URI(contentPath);
	String modelPath = "target/test-classes/test-resources/models/test-model.xsd";
	modelURI = new URI(modelPath);
		
	document = DaggerContentParserTeztComponent.builder()
												.content(contentURI)
												.fetchedContentFrom(contentURI)
												.model(modelURI)
												.withModelFetchedFrom(modelURI)
												.build()
												.parsedXMLDocument()
												.get();

}

@Test
public void testBuildCell() throws Exception {

	//TODO: complete test HEREHRE REHREHRE HERE 

}

@Test
public void testChildrenFrom() throws Exception {


	// we're expecting a fetchable relative path here
	CellModel testCellModel = cellModelFrom(modelURI, "test");	
	
	Composite<Cell> children = CellModule.childrenFrom(document.getDocumentElement(), contentURI, testCellModel);
	assertNotNull(children);
	assertEquals(1, children.size());
	Cell row = children.child(0);
	assertEquals("row", row.getName());
	assertEquals(contentURI+"/row", row.getURI().toString());
	assertEquals("row", row.getCellModel().getName());
	assertEquals("rowCell", row.getCellModel().getType().getName());
	assertTrue(row.isComplex());

	ComplexCell complexRow = ComplexCell.from(row);
	assertNotNull(complexRow);
	assertEquals(2, complexRow.children().size());
	assertEquals("col", complexRow.children().child(0).getName());
	assertEquals("col", complexRow.children().child(0).getCellModel().getName());
	assertEquals("colCell", complexRow.children().child(0).getCellModel().getType().getName());
	assertEquals("col", complexRow.children().child(1).getName());
	assertEquals("col", complexRow.children().child("col[0]").getName());
	assertEquals("col", complexRow.children().child("col[1]").getName());

}


@Test
public void testAttributesFrom() throws Exception {

	CellModel testCellModel = cellModelFrom(modelURI, "test");	

	Composite<Cell> children = CellModule.childrenFrom(document.getDocumentElement(), contentURI, testCellModel);

	Cell cell = children.child("row[0]").asComplex().children().child("col[0]").asComplex().children().child("test[0]");
	assertNotNull(cell);
	assertTrue(cell.isComplex());
	ComplexCell complexCell = cell.asComplex();
	
	Attributes<Cell> attributes = complexCell.attributes();
	assertNotNull(attributes);
	assertEquals(2, attributes.size());
	
	Cell number = attributes.attribute("number");
	assertNotNull(number);
	assertEquals("42", number.getValue());
	assertEquals("number", number.getCellModel().getName());
	assertEquals("numberField", number.getCellModel().getType().getName());
	
	Cell text = attributes.attribute("text");
	assertNotNull(text);
	assertEquals("blahblah", text.getValue());
	assertEquals("text", text.getCellModel().getName());
	assertEquals("textField", text.getCellModel().getType().getName());

}

}
