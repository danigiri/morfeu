package cat.calidos.morfeu.model.injection;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.xml.sax.SAXParseException;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.utils.MorfeuUtils;

public class DocumentIntTest extends ModelInjectionTezt {


@Test
public void testProduceCompleteDocument() throws Exception {

	// This feels painful to write and using Dagger for complete integration black box tests seems fair
	// Document document = parseLocation("test-resources/documents/document1.json");
	// ModelModule.parseModel(new URI(document.getModelURI()), parserProducer);
	// when(modelComponentProvider.get().builder().model().get()).thenReturn(...);
	// DocumentModule.produceDocument(document, modelComponentProvider);

	Document doc = produceDocumentFromPath("test-resources/documents/document1.json");
	assertNotNull(doc);
	
	DocumentModuleTest.testDocument1(doc);
	
}


@Test
public void testNonValidContentDocument() throws Exception {
	
	try {
		
		produceDocumentFromPath("test-resources/documents/document-with-nonvalid-content.json");

	} catch (ExecutionException e) {
		
		Throwable rootCause = MorfeuUtils.findRootCauseFrom(e);
		assertEquals(ValidationException.class, rootCause.getClass());
		assertTrue(rootCause.getMessage().contains("Invalid content"));
		
	}
}


@Test
public void testNotFoundModelDocument() throws Exception {
	
	try {

		System.err.println("Please ignore next FileNotFoundException, it is expected");
		produceDocumentFromPath("test-resources/documents/document-with-notfound-model.json");

	} catch (ExecutionException e) {
	
		Throwable rootCause = MorfeuUtils.findRootCauseFrom(e);
		assertEquals(FetchingException.class, rootCause.getClass());
	
	}
}


@Test
public void testNonValidModelDocument() throws Exception {
	
	try {

		System.err.println("Please ignore next ParsingException, it is expected as the model is not valid");
		produceDocumentFromPath("test-resources/documents/document-with-nonvalid-model.json");

	} catch (ExecutionException e) {
		
		Throwable rootCause = MorfeuUtils.findRootCauseFrom(e);
		assertEquals(ParsingException.class, rootCause.getClass());
		assertTrue(rootCause.getMessage().contains("undefined"));
		
	}
	
}



}