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

package cat.calidos.morfeu.model.metadata.injection;

import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Test;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;

import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.injection.ModelTezt;
import cat.calidos.morfeu.model.metadata.injection.DaggerModelMetadataComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelMetadataComponentIntTest extends ModelTezt {


@Test
public void testValue() throws Exception {
	
	String uri = "target/test-classes/test-resources/models/test-model.xsd";
	URI modelURI = new URI(uri);
	XSSchemaSet schemaSet = parseSchemaFrom(modelURI);
	
	XSSchema schema = schemaSet.getSchema(Model.MODEL_NAMESPACE);
	XSAnnotation annotation = schema.getAnnotation();
	
	Metadata meta = DaggerModelMetadataComponent.builder().from(annotation).withParentURI(modelURI).build().value();
	assertEquals("Description of test model", meta.getDesc());
	assertEquals(uri+"/test/row/col/data", meta.getURI().toString());

}

}