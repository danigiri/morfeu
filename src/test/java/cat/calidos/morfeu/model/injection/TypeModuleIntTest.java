// TYPE MODULE INT TEST . JAVA

package cat.calidos.morfeu.model.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.Type;
import cat.calidos.morfeu.model.injection.TypeModule;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TypeModuleIntTest extends ModelTezt {

private XSSchemaSet schemaSet;
private Metadata emptyMedatada;
private URI modelURI;


@BeforeEach
public void setup() throws Exception {

	modelURI = new URI("target/test-classes/test-resources/models/test-model.xsd");
	schemaSet = parseSchemaFrom(modelURI);
	Map<String, String> emptyDefaultValues = new HashMap<String, String>(0);
	Map<String, Set<String>> directives = new HashMap<String, Set<String>>(0);
	Map<String, Set<String>> attributes = new HashMap<String, Set<String>>(0);
	Map<String, Set<String>> attributeCategories = new HashMap<String, Set<String>>(0);
	emptyMedatada = new Metadata(null,
									"desc",
									"PRESENTATION",
									"CELL-PRESENTATION",
									"IMG",
									"GET",
									"THUMB",
									null,
									Optional.empty(),
									"valueLocator",
									emptyDefaultValues,
									directives,
									attributes,
									null,
									attributeCategories
									);

}


@Test @DisplayName("Root test type")
public void testRootAnonymousType() throws Exception {

	XSElementDecl elementDecl = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, "test");

	XSType xsType = elementDecl.getType();
	Type type = TypeModule.type(modelURI, "cell-model-name", xsType, null, new HashSet<String>(), emptyMedatada);
	assertEquals("cell-model-name", type.getName());
	assertEquals("PRESENTATION", type.getMetadata().getPresentation());
	assertEquals("CELL-PRESENTATION", type.getMetadata().getCellPresentation());
	assertEquals("IMG", type.getMetadata().getCellPresentationType());
	assertEquals("THUMB", type.getMetadata().getThumb());
	assertFalse(type.isSimple());
	
		
	
//	attributeUses.iterator().forEachRemaining(au -> System.err.println(au.getDecl().getName()));
//	complexType.getSubtypes().iterator().forEachRemaining(e->System.err.println(e.getName()));
//	Collection<XSComponent> select = complexType.select("/test/*", new NamespaceContext() {
//	
//	@Override
//	public Iterator getPrefixes(String namespaceURI) {
//		
//		// TODO Auto-generated method stub
//		 ArrayList<String> arrayList = new ArrayList<String>(1);
//		 arrayList.add("");
//		return arrayList.iterator();
//	}
//	
//	
//	@Override
//	public String getPrefix(String namespaceURI) {
//		
//		// TODO Auto-generated method stub
//		return "";
//	}
//	
//	
//	@Override
//	public String getNamespaceURI(String prefix) {
//		
//		// TODO Auto-generated method stub
//		return "";
//	}
//	});
//	select.iterator().forEachRemaining(e->System.err.println(e.toString()));
	
//	complexType.getElementDecls().stream().forEach(e->System.err.println(e.getName()));
//	schemaSet.iterateElementDecls().forEachRemaining(e -> e.);
//	schemaSet.iterateModelGroupDecls().forEachRemaining(gd -> System.err.println(gd.getName()));
	
}


@Test @DisplayName("Test textField type")
public void testTextSimpleType() {

	XSType xsType = schemaSet.getType(Model.MODEL_NAMESPACE, "textField");
	Type type = TypeModule.type(modelURI, "not used default", xsType, null, new HashSet<String>(), emptyMedatada);
	assertEquals("textField", type.getName());
	assertTrue(type.isSimple());
	assertTrue(type.isContentValid("random string"));
	assertFalse(type.isContentValid(new Object()));

//	XSRestrictionSimpleType restriction = xsType.asSimpleType().asRestriction();
//	XSType baseType = xsType.getBaseType();

}


@Test @DisplayName("Test numberField type")
public void testIntegerSimpleType() {

	XSType xsType = schemaSet.getType(Model.MODEL_NAMESPACE, "numberField");
	Type type = TypeModule.type(modelURI, "not used default", xsType, null, new HashSet<String>(), emptyMedatada);
	assertEquals("numberField", type.getName());
	assertTrue(type.isSimple());

	validateInteger(type);

}


@Test @DisplayName("Test colField type")
public void testColFieldSimpleType() {

	XSType xsType = schemaSet.getType(Model.MODEL_NAMESPACE, "colField");
	Type type = TypeModule.type(modelURI, "not used default", xsType, null, new HashSet<String>(), emptyMedatada);
	assertEquals("colField", type.getName());
	assertTrue(type.isSimple());

	// notice that we are not writing a full validation as XML Schema will do this for us
	validateInteger(type);

}


@Test @DisplayName("Test empty type")
public void testEmptyType() {

	Type type = TypeModule.emptyType(modelURI, "defaultName");
	assertEquals("defaultName", type.getName());
	assertFalse(type.isSimple());
	assertNull(type.getMetadata());
	assertTrue(type.getRegex().isEmpty());

}



@Test @DisplayName("Test colorField type")
public void testColorType() {

	XSType xsType = schemaSet.getType(Model.MODEL_NAMESPACE, "colorField");
	String regex = TypeModule.regex(xsType);
	Type type = TypeModule.type(modelURI, "not used default", xsType, regex, new HashSet<String>(), emptyMedatada);
	assertAll("testing color",
		() -> assertEquals("colorField", type.getName()),
		() -> assertTrue(type.isSimple()),
		() -> assertTrue(type.getRegex().isPresent()),
		() -> assertEquals("[0-9a-fA-F]{6}", type.getRegex().get())
	);

}


@Test @DisplayName("Test list type")
public void testListType() {

	XSType xsType = schemaSet.getType(Model.MODEL_NAMESPACE, "listField");
	String regex = TypeModule.regex(xsType);
	HashSet<String> possibleValues = new HashSet<String>();
	possibleValues.add("A0");
	possibleValues.add("A1");
	possibleValues.add("A2");
	Type type = TypeModule.type(modelURI, "not used default", xsType, regex, possibleValues, emptyMedatada);
	assertAll("testing color",
		() -> assertEquals("listField", type.getName()),
		() -> assertTrue(type.isSimple()),
		() -> assertTrue(type.getRegex().isEmpty()),
		() -> assertEquals(3, type.getPossibleValues().size()),
		() -> assertTrue(type.getPossibleValues().contains("A0")),
		() -> assertTrue(type.getPossibleValues().contains("A1")),
		() -> assertTrue(type.getPossibleValues().contains("A2"))
	);

}




private void validateInteger(Type type) {

	assertFalse(type.isContentValid("random string"));
	assertFalse(type.isContentValid(new Object()));
	assertFalse(type.isContentValid(3.1416));
	assertFalse(type.isContentValid("3.1416"));
	assertTrue(type.isContentValid("0"));
	assertTrue(type.isContentValid("12"));
	assertTrue(type.isContentValid("-7"));
	assertTrue(type.isContentValid(0));
	assertTrue(type.isContentValid(11));
	assertTrue(type.isContentValid(-2));

}

}

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

