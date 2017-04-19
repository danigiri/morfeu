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

package cat.calidos.morfeu.model.injection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.parser.XSOMParser;

import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;
import dagger.producers.Produced;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelModuleTest {

@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

@Mock Produced<XSOMParser> parserProducer;

@Test
public void testParseModel() throws Exception {
	
	// TODO: see what we can do about these ugly maven specific paths
	URI modelURI = new URI("target/test-classes/test-resources/models/test-model.xsd");
	Model model = parseURI(modelURI);

	assertEquals(modelURI, model.getUri());
	assertEquals(4, model.getComplextTypeCount());
	
}


@Test(expected = ParsingException.class)
public void testParseNonValidModel() throws Exception  {

	// TODO: see what we can do about these ugly maven specific paths

	System.err.println("Please ignore next ParsingException, it is expected as we are testing non valid schema");
	
	URI modelURI = new URI("target/test-classes/test-resources/models/nonvalid-model.xsd");
	parseURI(modelURI);

}


private Model parseURI(URI u) 
		throws ConfigurationException, InterruptedException, ExecutionException, ParsingException, FetchingException {

	XSOMParser parser = DaggerParserComponent.builder().build().produceXSOMParser().get();
	when(parserProducer.get()).thenReturn(parser);
	
	Model model = ModelModule.parseModel(u, parserProducer);

	return model;
}


}
