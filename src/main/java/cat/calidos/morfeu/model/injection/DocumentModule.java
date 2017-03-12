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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.impl.client.CloseableHttpClient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ListenableFuture;
import com.sun.xml.xsom.parser.XSOMParser;

import dagger.producers.Producer;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class DocumentModule {


//@Produces
//public Document produceDocument(@Named("name") String name, 
//								@Named("desc") String desc, 
//								@Named("type") String type, 
//								@Named("modelURI") URI modelURI, 
//								@Named("docURI") URI docURI) throws Exception {
//	return new Document(name, desc, type, modelURI, docURI);
//}

@Produces 
Document produceDocument(URI uri) {
	return new Document("","","",uri, uri, uri);
}


@Produces
@Named("name")
String getName() {
	return null;
}


@Produces
@Named("desc")
String getDesc() {
	return null;
}


@Produces @Named("JSONDocumentStream")
InputStream fetchDocumentJSON(URI u, CloseableHttpClient c) throws ExecutionException {
	InputStream documentStream;
	try {
		documentStream = fetchRemoteStream(u, c).get();
	} catch (Exception e) {
		throw new ExecutionException("Problem fetching document with uri:"+u+" ("+e.getMessage()+")",e);
	}
	return documentStream;
}


@Produces @Named("BasicDocument")
Document fetchDocument(@Named("JSONDocumentStream") InputStream s, ObjectMapper mapper) 
		throws JsonParseException, JsonMappingException, IOException {
	return mapper.readValue(s, Document.class);
}


@Produces @Named("ModelURI")
URI modelURI(@Named("BasicDocument") Document doc) {
	return doc.getDocUri();
}


@Produces @Named("ModelStream")
ListenableFuture<InputStream> fetchDocumentModel(@Named("ModelURI") URI u, CloseableHttpClient c) {
	return fetchRemoteStream(u, c);
}


private ListenableFuture<InputStream> fetchRemoteStream(URI u, CloseableHttpClient c) {

	return DaggerHttpRequesterComponent.builder()
			.forURI(u)
			.withClient(c)
			.build()
			.fetchHttpData();
}


}
