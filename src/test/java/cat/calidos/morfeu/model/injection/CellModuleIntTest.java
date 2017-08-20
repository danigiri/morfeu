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

import org.junit.Test;
import org.w3c.dom.Document;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Composite;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellModuleIntTest extends ModelTezt {


@Test
public void testBuildCell() throws Exception {

	//TODO: complete test HEREHRE REHREHRE HERE 
	
	String contentPath = "test-resources/documents/document1.xml";
	String fullContentPath = testAwareFullPathFrom(contentPath);
	String modelPath = "test-resources/models/test-model.xsd";
	String testAwareModelPath = testAwareFullPathFrom(modelPath);
	//CellModel testCellModel = cellModelFrom(modelURI, "test");	

//	Cell cell = CellModule.provideCellFrom(contentURI, "name", "desc", "value", testCellModel);
//	assertNotNull(cell);


}

@Test
public void testChildrenFrom() throws Exception {

	String contentPath = "target/test-classes/test-resources/documents/document1.xml";
	URI contentURI = new URI(contentPath);
	String modelPath = "target/test-classes/test-resources/models/test-model.xsd";
	URI modelURI = new URI(modelPath);
	
	// we're expecting a fetchable relative path here
	CellModel testCellModel = cellModelFrom(modelURI, "test");	
	
	Document document = DaggerContentParserTeztComponent.builder()
															.content(contentURI)
															.fetchedContentFrom(contentURI)
															.model(modelURI)
															.withModelFetchedFrom(modelURI)
															.build()
															.parsedXMLDocument()
															.get();
	
	
	Composite<Cell> children = CellModule.childrenFrom(document.getDocumentElement(), contentURI, testCellModel);
	assertNotNull(children);
	
}

}
