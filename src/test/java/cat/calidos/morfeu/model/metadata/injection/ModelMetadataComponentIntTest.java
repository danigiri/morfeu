// MODEL METADATA COMPONENT INT TEST . JAVA

package cat.calidos.morfeu.model.metadata.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Set;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.injection.ModelTezt;

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
	assertEquals(uri+"/test/row/col/stuff", meta.getURI().toString());

}


@Test
public void testTransformAttributes() {

	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, "test");
	XSAnnotation annotation = elem.getAnnotation();
	Metadata meta = DaggerModelMetadataComponent.builder().from(annotation).withParentURI(modelURI).build().value();
	assertTrue(meta.getAttributesFor("nonexistant").isEmpty());

	Set<String> ytxAttributes = meta.getAttributesFor("yaml-to-xml");
	assertNotNull(ytxAttributes);
	assertEquals(2, ytxAttributes.size(), "We should have two yaml-to-xml attributes");
	assertTrue(ytxAttributes.contains("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""));
	assertTrue(ytxAttributes.contains("xsi:noNamespaceSchemaLocation=\"../models/test-model.xsd\""));

}


@Test
public void testTransformDirectives() {

	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, "test");
	XSAnnotation annotation = elem.getAnnotation();
	Metadata meta = DaggerModelMetadataComponent.builder().from(annotation).withParentURI(modelURI).build().value();
	assertTrue(meta.getDirectivesFor("nonexistant").isEmpty());

	Set<String> directives = meta.getDirectivesFor("obj-to-yaml");
	assertNotNull(directives);
	assertEquals(1, directives.size(), "We should have one xml-to-yaml directive");

	String directive = directives.iterator().next();
	assertEquals("ATTRIBUTES-ONLY", directive, "Wrong directives for xml-to-yaml");

}


@Test @DisplayName("Test value locator")
public void testValueLocator() throws Exception {

	Metadata locator = cellModelFrom(modelURI, "test").asComplex()
														.children()
														.child("row")
														.asComplex()
														.children()
														.child("col")
														.asComplex()
														.children()
														.child("types")
														.asComplex()
														.attributes()
														.attribute("locator")
														.getMetadata();
	assertNotNull(locator);

	assertAll("test locator stuff",
		() -> assertTrue(locator.getValueLocator().isPresent()),
		() -> assertEquals("/test/**/stuff", locator.getValueLocator().get())

	);

}



}

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
