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

/** TODO: ensure all this is actually asynchronous
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule(subcomponents=ModelComponent.class)
public class DocumentModule extends RemoteModule {


@Produces
public static Document produceDocument(@Named("BasicDocument") Document doc, Provider<ModelComponent.Builder> modelComponentProvider) throws Exception {

	Model model;
	try {
		 model = modelComponentProvider.get().builder().model().get();
	} catch (Exception e) {
		throw new ExecutionException("Problem fetching model of document '"+doc.getName()+"' from uri: '"+doc.getModelURI()+"'",e);
	}
	doc.setModel(model);
		
	return doc;
	
}


@Produces @Named("JSONDocumentStream")
public static InputStream fetchDocumentJSON(URI uri, CloseableHttpClient client) throws ExecutionException {
	InputStream documentStream;
	try {
		documentStream = fetchRemoteStream(uri, client).get();
	} catch (Exception e) {
		throw new ExecutionException("Problem fetching document with uri: '"+uri+"'",e);
	}
	return documentStream;
}


@Produces @Named("BasicDocument")
public static Document parseDocument(URI uri, @Named("JSONDocumentStream") InputStream docStream, ObjectMapper mapper) 
		throws JsonParseException, JsonMappingException, IOException {
	return mapper.readerForUpdating(new Document(uri)).readValue(docStream);
}


@Produces @Named("ModelURI")
public static URI modelURI(@Named("BasicDocument") Document doc) {

	return doc.getModelURI();

}


@Produces @Named("ContentURI")
public static URI contentURI(@Named("BasicDocument") Document doc) {

	return doc.getContentURI();

}


}
