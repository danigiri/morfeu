// YAML CONVERTER INT TEST . JAVA

package cat.calidos.morfeu.transform;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.transform.injection.DaggerYAMLConverterComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class YAMLConverterIntTest extends TransformTezt {

private Model model;


@Before
public void setup() throws Exception {

	String docPath = "test-resources/documents/document1.json";
	Document doc = produceDocumentFromPath(docPath);
	model = doc.getModel();

}


@Test
public void testYAMLConverterRowCol() throws Exception {


	String yaml = 	"rows:\n" + 
					"  - \n" + 
					"    cols:\n" + 
					"      - \n" + 
					"        size: 12\n";
	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	JsonNode node = mapper.readTree(yaml);
	String transformed = DaggerYAMLConverterComponent.builder().from(node).given(model).build().xml();
	assertNotNull(transformed);
	//System.err.println(transformed);

	String xml =	"<test xsi:noNamespaceSchemaLocation=\"../models/test-model.xsd\" " +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
					"	<row>\n" + 
					"		<col size=\"12\">\n" + 
					"		</col>\n" + 
					"	</row>\n" +
					"</test>";

	compareWithXML(transformed, xml);

}



@Test
public void testYAMLConverterRowCols() throws Exception {

	String yaml = 	"rows:\n" + 
					"  - \n" + 
					"    cols:\n" + 
					"      - \n" + 
					"        size: 6\n"+
					"      - \n" + 
					"        size: 6\n";
	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	JsonNode node = mapper.readTree(yaml);
	String transformed = DaggerYAMLConverterComponent.builder().from(node).given(model).build().xml();
	assertNotNull(transformed);
	//System.err.println(transformed);

	String xml =	"<test xsi:noNamespaceSchemaLocation=\"../models/test-model.xsd\" " +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
					"	<row>\n" + 
					"		<col size=\"6\">\n" + 
					"		</col>\n" + 
					"		<col size=\"6\">\n" + 
					"		</col>\n" + 
					"	</row>\n" + 
					"</test>";	

	compareWithXML(transformed, xml);

}


@Test
public void testYAMLConverterData() throws Exception {

	String yaml = 	"rows:\n" + 
					"  - \n" + 
					"    cols:\n" + 
					"      - \n" + 
					"        size: 12\n" +
					"        data:\n" + 
					"          - \n" + 
					"            number: 1\n" + 
					"            text: blahblah1\n" +
					"          - \n" + 
					"            number: 2\n" + 
					"            text: blahblah2\n" +
					"          - \n" + 
					"            number: 3\n";

	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	JsonNode node = mapper.readTree(yaml);
	String transformed = DaggerYAMLConverterComponent.builder().from(node).given(model).build().xml();
	assertNotNull(transformed);
	System.err.println(transformed);

	String xml =	"<test xsi:noNamespaceSchemaLocation=\"../models/test-model.xsd\" " +
					"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
					"	<row>\n" + 
					"		<col size=\"12\">\n" +
					"			<data number=\"1\" text=\"blahblah1\" />\n" + 
					"			<data number=\"2\" text=\"blahblah2\" />\n" + 
					"			<data number=\"3\" />\n" + 
					"		</col>\n" + 
					"	</row>\n" +
					"</test>";

	compareWithXML(transformed, xml);

}


@Test
public void testYAMLConverterDatas() throws Exception {

	String yaml = 	"rows:\n" + 
					"  - \n" + 
					"    cols:\n" + 
					"      - \n" + 
					"        size: 12\n" + 
					"        data:\n" + 
					"          - \n" + 
					"            number: 42\n" + 
					"        data2:\n" + 
					"          - \n" + 
					"            number: 43\n" + 
					"            text: blahblah";

	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	JsonNode node = mapper.readTree(yaml);
	String transformed = DaggerYAMLConverterComponent.builder().from(node).given(model).build().xml();
	assertNotNull(transformed);
	System.err.println(transformed);

	String xml =	"<test xsi:noNamespaceSchemaLocation=\"../models/test-model.xsd\" " +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
					"	<row>\n" + 
					"		<col size=\"12\">\n" +
					"			<data number=\"42\" />\n" + 
					"			<data2 number=\"43\" text=\"blahblah\" />\n" + 
					"		</col>\n" + 
					"	</row>\n" +
					"</test>";

	compareWithXML(transformed, xml);

}


@Test
public void testYAMLConverterStuff() throws Exception {

	String yaml = 	"rows:\n" + 
					"  - \n" + 
					"    cols:\n" + 
					"      - \n" + 
					"        size: 12\n" + 
					"        stuff:\n" + 
					"          - Stuff content\n" + 
					"          - Stuff content 2\n" + 
					"          - |\n" + 
					"            Multiline stuff\n" + 
					"            content";

	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	JsonNode node = mapper.readTree(yaml);
	String transformed = DaggerYAMLConverterComponent.builder().from(node).given(model).build().xml();
	assertNotNull(transformed);
	//System.err.println(transformed);
	String xml =	"<test xsi:noNamespaceSchemaLocation=\"../models/test-model.xsd\" " +
					"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
					"	<row>\n" + 
					"		<col size=\"12\">\n" +
					"			<stuff>Stuff content</stuff>\n" + 
					"			<stuff>Stuff content 2</stuff>\n" + 
					"			<stuff>Multiline stuff\ncontent</stuff>\n" + 
					"		</col>\n" + 
					"	</row>\n" +
					"</test>";

	compareWithXML(transformed, xml);

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

