package cat.calidos.morfeu.model.injection;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cat.calidos.morfeu.model.Document;

public class DocumentIntTest extends IntT3st {


@Test
public void testProduceCompleteDocument() throws Exception {

	// This feels painful to write and using Dagger for complete integration black box tests seems fair
	// Document document = parseLocation("test-resources/documents/document1.json");
	// ModelModule.parseModel(new URI(document.getModelURI()), parserProducer);
	// when(modelComponentProvider.get().builder().model().get()).thenReturn(...);
	// DocumentModule.produceDocument(document, modelComponentProvider);

	String doc1Path = uriModuleForPath("test-resources/documents/document1.json");
	URIModule uriModule = new URIModule(doc1Path);
	DocumentComponent docComponent = DaggerDocumentComponent.builder().URIModule(uriModule).build();
	Document doc = docComponent.produceDocument().get();

	assertNotNull(doc);
	
	DocumentModuleTest.testDocument1(doc);
	
}

}