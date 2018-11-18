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

package cat.calidos.morfeu.model.metadata;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cat.calidos.morfeu.model.Metadata;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MetadataTest {

private URI mergedURI;
private Metadata merged;

@Before
public void setup() throws Exception {

	URI priorityURI = new URI("priority.xsd");
	HashMap<String, String> priorityDefaultValues = new HashMap<String, String>(1);
	priorityDefaultValues.put("@a", "priority-default-a");
	Map<String, Set<String>> directives = new HashMap<String, Set<String>>(1);
	HashSet<String> directivesA = new HashSet<String>(1);
	directivesA.add("directive-A");
	directives.put("directives", directivesA);
	HashSet<String> directivesA2 = new HashSet<String>(1);
	directivesA2.add("directive-A2");
	directives.put("directives2", directivesA2);
	Map<String, Set<String>> attributes = new HashMap<String, Set<String>>(1);
	Metadata priorityMetadata = new Metadata(priorityURI, 
												"descA",
												"A", 
												"ACP", 
												"IMG", 
												"DEFAULT", 
												"idA", 
												priorityDefaultValues, 
												directives, 
												attributes);

	URI metadataURI = new URI("priority.xsd");
	HashMap<String, String> metadataDefaultValues = new HashMap<String, String>(2);
	metadataDefaultValues.put("@a", "meta-default-a");
	metadataDefaultValues.put("@b", "meta-default-b");
	directives = new HashMap<String, Set<String>>(0);
	HashSet<String> directivesB = new HashSet<String>(1);
	directivesB.add("directive-B");
	directives.put("directives", directivesB);
	Map<String, Set<String>> attributes2 = new HashMap<String, Set<String>>(0);
	Metadata metadata = new Metadata(metadataURI, 
										"descB", 
										"B", 
										"BCP", 
										"BCPTYPE", 
										"THUMB", 
										"idB", 
										metadataDefaultValues, 
										directives, 
										attributes2);

	mergedURI = new URI("foo.xsd");
	merged = Metadata.merge(mergedURI, priorityMetadata, metadata);

}


@Test
public void testMergeMetadataBasic() {

	assertEquals(mergedURI, merged.getURI());
	assertEquals("descA", merged.getDesc());
	assertEquals("A", merged.getPresentation());
	assertEquals("ACP", merged.getCellPresentation());
	assertEquals("BCPTYPE", merged.getCellPresentationType());
	assertEquals("THUMB", merged.getThumb());

	assertTrue(merged.getIdentifier().isPresent());
	assertEquals("idA", merged.getIdentifier().get());

	Map<String, String> mergedDefaultValues = merged.getDefaultValues();
	assertEquals(2, mergedDefaultValues.size());
	assertEquals("priority-default-a", mergedDefaultValues.get("@a"));
	assertEquals("meta-default-b", mergedDefaultValues.get("@b"));

}


@Test
public void testMergeMetadataDirectives() {

	Set<String> directives = merged.getDirectivesFor("directives");
	assertNotNull(directives);
	assertEquals(2, directives.size());
	assertTrue(directives.contains("directive-A"));
	assertTrue(directives.contains("directive-B"));

	directives = merged.getDirectivesFor("directives2");
	assertNotNull(directives);
	assertEquals(1, directives.size());
	assertTrue(directives.contains("directive-A2"));

}

}
