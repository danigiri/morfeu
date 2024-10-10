// SNIPPET INT TEST . JAVA

package cat.calidos.morfeu.model.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Composite;


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
	Composite<Cell> content = DaggerSnippetParserComponent.builder()
			.content(new URI(contentPath))
			.fetchedContentFrom(new URI(fullContentPath))
			.modelFiltered(new URI(modelPath))
			.withModelFetchedFrom(new URI(testAwareModelPath))
			.build()
			.content()
			.get();
	assertNotNull(content);
	assertEquals(1, content.size(), "There should be only one stuff snippet");

	Cell stuff = content.child(0);
	assertNotNull(stuff);

	Optional<String> stuffValue = stuff.getValue();
	assertTrue(stuffValue.isPresent());
	assertEquals("Stuff content", stuffValue.get());

	CellModel stuffModel = stuff.getCellModel();
	assertEquals("stuff", stuffModel.getName());

}

}
