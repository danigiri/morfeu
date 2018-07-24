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

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cat.calidos.morfeu.model.BasicCellModel;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.ParsingException;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelIntTest extends ModelTezt {

private URI modelURI;
private Model model;


@Before
public void setup () throws Exception {

	// TODO: see what we can do about these ugly maven specific paths
	modelURI = new URI("target/test-classes/test-resources/models/test-model.xsd");
	model = parseModelFrom(modelURI);
}


@Test
public void testParseModel() throws Exception {
	
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
public void testGlobalMetadata() {

	ComplexCellModel col = model.getRootCellModels()
								.get(0)
								.asComplex()
								.children()
								.child("row")
								.asComplex()
								.children()
								.child("col")
								.asComplex();

	CellModel data2 = col.children().child("data2");
	assertEquals("Globally provided description of 'data2'", data2.getMetadata().getDesc());
	assertEquals("assets/images/data2-thumb.svg", data2.getMetadata().getThumb());

	CellModel data = col.children().child("data");
	assertEquals("Globally provided description of 'data'", data.getMetadata().getDesc());
	assertEquals("assets/images/data-thumb.svg", data.getMetadata().getThumb());

}


@Test
public void testMetadataDirectives() {
	
	ComplexCellModel col = model.getRootCellModels()
			.get(0)
			.asComplex()
			.children()
			.child("row")
			.asComplex()
			.children()
			.child("col")
			.asComplex();

	Metadata meta = col.children().child("data").getMetadata();
	Map<String, Set<String>> directives = meta.getDirectives();
	assertNotNull(directives);
	assertEquals(2, directives.size());
	assertTrue(directives.containsKey("obj-to-yaml"));
	assertTrue(directives.get("obj-to-yaml").contains("LISTS-NO-PLURAL"));
	assertTrue(directives.containsKey("yaml-to-xml"));
	assertTrue(directives.get("yaml-to-xml").contains("LISTS-NO-PLURAL"));
	
	Metadata meta2 = col.children().child("data2").getMetadata();

}


@Test(expected = ParsingException.class)
public void testParseNonValidModel() throws Exception  {

	// TODO: see what we can do about these ugly maven specific paths

	System.err.println("Please ignore next ParsingException, it is expected as we are testing non valid schema");
	
	URI modelURI = new URI("target/test-classes/test-resources/models/nonvalid-model.xsd");
	parseModelFrom(modelURI);

}


}