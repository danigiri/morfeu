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

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.google.common.util.concurrent.ListenableFuture;
import com.sun.xml.xsom.parser.XSOMParser;

import cat.calidos.morfeu.model.Model;
import dagger.producers.Produced;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelModuleTest {

@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

@Mock Produced<XSOMParser> parserProducer;

@Test
public void testParseModel() throws Exception {
	
	String location = "http://localhost:3000/test-resources/models/test-model.xsd";
	URI modelURI = new URI(location);
	InputStream stream = this.getClass().getClassLoader().getResourceAsStream(location);
	XSOMParser parser = DaggerParserComponent.builder().build().produceXSOMParser().get();
	
	when(parserProducer.get()).thenReturn(parser);
	
	Model model = ModelModule.parseModel(modelURI, stream, parserProducer);

	assertEquals(modelURI, model.getUri());
	
	
}

}
