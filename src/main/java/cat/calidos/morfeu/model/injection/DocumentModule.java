/*
 *    Copyright 2016 Daniel Giribet
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

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.Model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;
import javax.inject.Provider;

import org.apache.http.impl.client.CloseableHttpClient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/** TODO: make all this asynchronous
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule(subcomponents=ModelComponent.class)
public class DocumentModule extends RemoteModule {


@Produces
public static Document produceDocument(@Named("BasicDocument") Document doc, Provider<ModelComponent.Builder> modelComponentProvider) throws Exception {

	Model model = modelComponentProvider.get().builder().model().get();
	doc.setModel(model);
		
	return doc;
	
}

//@Produces 
//Document produceDocument(@Named("name") String name, URI uri) {
//	return new Document(name, ,"",uri, uri, uri);
//}


@Produces @Named("JSONDocumentStream")
public static InputStream fetchDocumentJSON(URI u, CloseableHttpClient c) throws ExecutionException {
	InputStream documentStream;
	try {
		documentStream = fetchRemoteStream(u, c).get();
	} catch (Exception e) {
		throw new ExecutionException("Problem fetching document with uri:"+u+"",e);
	}
	return documentStream;
}


@Produces @Named("BasicDocument")
public static Document parseDocument(URI u, @Named("JSONDocumentStream") InputStream s, ObjectMapper mapper) 
		throws JsonParseException, JsonMappingException, IOException {
	return mapper.readerForUpdating(new Document(u)).readValue(s);
}


@Produces @Named("ModelURI")
public static URI modelURI(@Named("BasicDocument") Document doc) {

	//TODO: the problem here is that urls need to be absolute in the JSON, which is a PITA, we need to autodetect this
	URI modelUri = doc.getModelURI();
	return modelUri;

}


}
