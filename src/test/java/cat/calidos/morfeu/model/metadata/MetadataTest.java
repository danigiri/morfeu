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

package cat.calidos.morfeu.model.metadata;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import cat.calidos.morfeu.model.Metadata;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MetadataTest {


@Test
public void testMergeMetadata() throws Exception {
	
	URI priorityURI = new URI("priority.xsd");
	HashMap<String, String> priorityDefaultValues = new HashMap<String, String>(1);
	priorityDefaultValues.put("@a", "priority-default-a");
	Map<String, Set<String>> directives = new HashMap<String, Set<String>>(0);
	Map<String, Set<String>> attributes = new HashMap<String, Set<String>>(0);
	Metadata priorityMetadata = new Metadata(priorityURI, "descA", "A", "ACP", "DEFAULT", priorityDefaultValues, directives, attributes);

	URI metadataURI = new URI("priority.xsd");
	HashMap<String, String> metadataDefaultValues = new HashMap<String, String>(2);
	metadataDefaultValues.put("@a", "meta-default-a");
	metadataDefaultValues.put("@b", "meta-default-b");
	Map<String, Set<String>> directives2 = new HashMap<String, Set<String>>(0);
	Map<String, Set<String>> attributes2 = new HashMap<String, Set<String>>(0);
	Metadata metadata = new Metadata(metadataURI, "descB", "B", "BCP", "THUMB", metadataDefaultValues, directives2, attributes2);

	URI mergedURI = new URI("foo.xsd");
	Metadata merged = Metadata.merge(mergedURI, priorityMetadata, metadata);
	
	assertEquals(mergedURI, merged.getURI());
	assertEquals("descA", merged.getDesc());
	assertEquals("A", merged.getPresentation());
	assertEquals("ACP", merged.getCellPresentation());
	assertEquals("THUMB", merged.getThumb());
	
	Map<String, String> mergedDefaultValues = merged.getDefaultValues();
	assertEquals(2, mergedDefaultValues.size());
	assertEquals("priority-default-a", mergedDefaultValues.get("@a"));
	assertEquals("meta-default-b", mergedDefaultValues.get("@b"));
	
}

}
