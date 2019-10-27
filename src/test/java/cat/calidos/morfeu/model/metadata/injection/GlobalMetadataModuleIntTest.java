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

package cat.calidos.morfeu.model.metadata.injection;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;

import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.model.metadata.injection.GlobalModelMetadataModule;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class GlobalMetadataModuleIntTest extends ModelTezt {


private URI modelURI;
private String uri;


@Before
public void setup() throws Exception {

	uri = "target/test-classes/test-resources/models/test-model.xsd";
	modelURI = new URI(uri);

}


@Test
public void testGlobalMetadataFromModel() throws Exception {

	XSSchemaSet schemaSet = parseSchemaFrom(modelURI);
	XSSchema schema = schemaSet.getSchema(Model.MODEL_NAMESPACE);
	XSAnnotation annotation = schema.getAnnotation();

	Map<URI, Metadata> globalMetadata = GlobalModelMetadataModule.provideGlobalModelMetadata(annotation, modelURI);
	assertNotNull("global metadata parser should not return null", globalMetadata);
	assertEquals("global metadata should have 4 entries", 4, globalMetadata.size());

	URI dataURI = new URI(uri+"/test/row/col/data");
	Metadata dataMetadata = globalMetadata.get(dataURI);	
	assertNotNull("global metadata parser should return data cell metadata", dataMetadata);
	assertEquals(dataURI.toString(), dataMetadata.getURI().toString());
	assertEquals("Globally provided description of 'data'", dataMetadata.getDesc());
	assertEquals("assets/images/data-thumb.svg", dataMetadata.getThumb());
	assertEquals("assets/images/data-cell.svg", dataMetadata.getCellPresentation());
	Map<String, String> defaultValues = dataMetadata.getDefaultValues();
	assertEquals("We should have 1 default attribute value in global data meta", 1, defaultValues.size());
	String expectedDefaultValue = "Default value for text (from global)";
	String actualDefaultValue = defaultValues.get(Metadata.DEFAULT_VALUE_PREFIX+"text");
	assertEquals("Incorrec text attribute default value from global metadata", expectedDefaultValue, actualDefaultValue);

	URI data2URI = new URI(uri+"/test/row/col/data2");
	Metadata data2Metadata = globalMetadata.get(data2URI);	
	assertEquals(data2URI.toString(), data2Metadata.getURI().toString());
	assertNotNull("global metadata parser should return data2 cell metadata", data2Metadata);
	assertEquals("assets/images/data2-thumb.svg", data2Metadata.getThumb());
	assertEquals("/morfeu/dyn/preview/svg/data2.svg?__header=$_NAME&$_ATTRIBUTES", data2Metadata.getCellPresentation());
	assertTrue("data2 global metadata has no defaults defined", data2Metadata.getDefaultValues().isEmpty());
	
}

}
