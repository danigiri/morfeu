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

import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import dagger.producers.Produced;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelTezt {

protected static final String MODEL_NAMESPACE = "";

@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
@Mock Produced<XSOMParser> parserProducer;

protected Model parseURI(URI u) throws ConfigurationException, InterruptedException, ExecutionException, ParsingException, FetchingException {
		
	XSSchemaSet schemaSet = parseSchemaFrom(u);
		
	//TODO: complete model tests
	return ModelModule.model(u, schemaSet, null);
		
}

protected XSSchemaSet parseSchemaFrom(URI uri)
		throws InterruptedException, ExecutionException, ConfigurationException, ParsingException, FetchingException {

	XSOMParser parser = DaggerParserComponent.builder().build().produceXSOMParser().get();
	when(parserProducer.get()).thenReturn(parser);
		
	XSSchemaSet schemaSet = ModelModule.parseModel(uri, parserProducer);

	return schemaSet;

}

	
}
