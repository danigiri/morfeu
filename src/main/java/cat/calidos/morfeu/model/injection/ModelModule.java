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

import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;

import org.apache.http.impl.client.CloseableHttpClient;
import org.xml.sax.SAXException;

import com.google.common.util.concurrent.ListenableFuture;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

import cat.calidos.morfeu.model.Model;
import dagger.producers.Produced;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule 
public class ModelModule extends RemoteModule {

@Produces @Named("ModelStream")
ListenableFuture<InputStream> fetchModel(@Named("ModelURI") URI u, CloseableHttpClient c) {
	return fetchRemoteStream(u, c);
}

@Produces
Model parseModel(@Named("ModelURI") URI u, @Named("ModelStream") InputStream stream, Produced<XSOMParser> parserProducer) throws SAXException, ExecutionException {
	XSOMParser parser = parserProducer.get();
	parser.parse(u.toString());
	XSSchemaSet schemaSet = parser.getResult();
	//schemaSet.getSchema()
	return new Model(u, schemaSet);

}

}
