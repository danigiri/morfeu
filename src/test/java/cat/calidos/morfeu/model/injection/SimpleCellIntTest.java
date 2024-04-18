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

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.ComplexCell;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Composite;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SimpleCellIntTest extends ModelTezt {

private Composite<Cell> children;

@Test
public void testSimpleCell() throws Exception {
	
	String contentPath = "target/test-classes/test-resources/documents/document3.xml";
	URI contentURI = new URI(contentPath);
	String modelPath = "target/test-classes/test-resources/models/test-model.xsd";
	URI modelURI = new URI(modelPath);

	Document document = DaggerContentParserTeztComponent.builder()
															.content(contentURI)
															.fetchedContentFrom(contentURI)
															.model(modelURI)
															.withModelFetchedFrom(modelURI)
															.build()
															.parsedXMLDocument()
															.get();

	// we're expecting a fetchable relative path here
	ComplexCellModel testCellModel = cellModelFrom(modelURI, "test").asComplex();	
	children = CellModule.childrenFrom(document.getDocumentElement(), contentURI, testCellModel);
	assertEquals(1, children.size());

	ComplexCell col = children.child("row(0)").asComplex().children().child("col(0)").asComplex();
	assertEquals(4, col.children().size());

	Cell stuff = col.children().child("stuff(0)");
	assertTrue(stuff.isSimple());
	assertFalse(stuff.isComplex());
	assertTrue(stuff.getValue().isPresent());
	assertEquals("Stuff content", stuff.getValue().get());

}

}
