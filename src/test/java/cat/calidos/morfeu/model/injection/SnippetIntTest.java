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
import java.util.Optional;

import org.junit.Test;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.Validable;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SnippetIntTest extends ModelTezt {


@Test
public void testSnippet() throws Exception {
	
	String contentPath = "test-resources/snippets/stuff.xml";
	String fullContentPath = testAwareFullPathFrom(contentPath);
	String modelPath = "target/test-classes/test-resources/models/test-model.xsd?filter=/test/row/col/stuff";
	String testAwareModelPath = testAwareFullPathFrom("test-resources/models/test-model.xsd");
	Composite<Cell> content = DaggerSnippetComponent.builder()
														.content(new URI(contentPath))
														.fetchedContentFrom(new URI(fullContentPath))
														.modelFiltered(new URI(modelPath))
														.withModelFetchedFrom(new URI(testAwareModelPath))
														.build()
														.content()
														.get();
	assertNotNull(content);
	assertEquals("There should be only one stuff snippet", 1, content.size());
	
	Cell stuff = content.child(0);
	assertNotNull(stuff);
	
	Optional<String> stuffValue = stuff.getValue();
	assertTrue(stuffValue.isPresent());
	assertEquals("Stuff content", stuffValue.get());
	
	CellModel stuffModel = stuff.getCellModel();
	assertEquals("stuff", stuffModel.getName());

}

}
