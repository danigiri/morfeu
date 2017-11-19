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
import java.util.List;

import org.junit.Test;

import cat.calidos.morfeu.model.BasicCellModel;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.ParsingException;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelModuleIntTest extends ModelTezt {

@Test
public void testParseModel() throws Exception {
	
	// TODO: see what we can do about these ugly maven specific paths
	URI modelURI = new URI("target/test-classes/test-resources/models/test-model.xsd");

	Model model = parseModelFrom(modelURI);
	assertEquals(modelURI, model.getURI());
	assertEquals("Description of test model", model.getDesc());
	
	List<CellModel> rootCellModels = model.getRootCellModels();
	assertNotNull(rootCellModels);
	assertEquals(1, rootCellModels.size());
	CellModel test = rootCellModels.get(0);
	assertEquals("test", test.getName());
	assertEquals("test-type", test.getType().getName());

}


@Test
public void testGlobalMetadata( ) {
		//HERE HERE HEREH HERHERHE 
}


@Test(expected = ParsingException.class)
public void testParseNonValidModel() throws Exception  {

	// TODO: see what we can do about these ugly maven specific paths

	System.err.println("Please ignore next ParsingException, it is expected as we are testing non valid schema");
	
	URI modelURI = new URI("target/test-classes/test-resources/models/nonvalid-model.xsd");
	parseModelFrom(modelURI);

}


}
