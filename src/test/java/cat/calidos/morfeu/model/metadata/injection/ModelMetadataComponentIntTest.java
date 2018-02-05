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
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSElementDecl;
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

private URI modelURI;
private String uri;
private XSSchema schema;
private XSSchemaSet schemaSet;


@Before
public void setup() throws Exception {

	uri = "target/test-classes/test-resources/models/test-model.xsd";
	modelURI = new URI(uri);
	schemaSet = parseSchemaFrom(modelURI);
	schema = schemaSet.getSchema(Model.MODEL_NAMESPACE);
	
}


@Test
public void testValue() {

	XSAnnotation annotation = schema.getAnnotation();
	Metadata meta = DaggerModelMetadataComponent.builder().from(annotation).withParentURI(modelURI).build().value();
	assertEquals("Description of test model", meta.getDesc());
	assertEquals(uri+"/test/row/col/data", meta.getURI().toString());

}


@Test
public void testTransformAttributes() {
	
	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, "test");
	XSAnnotation annotation = elem.getAnnotation();
	Metadata meta = DaggerModelMetadataComponent.builder().from(annotation).withParentURI(modelURI).build().value();
	assertFalse(meta.getAttributesFor("nonexistant").isPresent());
	
	assertTrue(meta.getAttributesFor("yaml-to-xml").isPresent());
	
	Set<String> ytxAttributes = meta.getAttributesFor("yaml-to-xml").get();
	assertNotNull(ytxAttributes);
	assertEquals("We should have two yaml-to-xml attributes", 2, ytxAttributes.size());
	assertTrue(ytxAttributes.contains("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""));
	assertTrue(ytxAttributes.contains("xmlns:mf=\"http://dani.calidos.com/morfeu/metadata\""));
	
}

@Test
public void testTransformDirectives() {

	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, "test");
	XSAnnotation annotation = elem.getAnnotation();
	Metadata meta = DaggerModelMetadataComponent.builder().from(annotation).withParentURI(modelURI).build().value();
	assertFalse(meta.getDirectivesFor("nonexistant").isPresent());

	assertTrue(meta.getDirectivesFor("xml-to-yaml").isPresent());

	Set<String> directives = meta.getDirectivesFor("xml-to-yaml").get();
	assertNotNull(directives);
	assertEquals("We should have one xml-to-yaml directive", 1, directives.size());

	String directive = directives.iterator().next();
	assertEquals("Wrong directives for xml-to-yaml",  "ATTRIBUTES-ONLY", directive);
	
}

}
