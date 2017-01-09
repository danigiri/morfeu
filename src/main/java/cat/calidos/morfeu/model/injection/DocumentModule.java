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
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.xsom.parser.XSOMParser;

import dagger.producers.Producer;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule(subcomponents=HttpRequesterComponent.class, includes=ParserModule.class)
public class DocumentModule {

protected String uri;
// FIXME: for the life of me, I can't figure out why this is not injected, let's do the injection by hand, to keep sanity
@Inject ObjectMapper jsonMapper = ParserModule.provideJSONObjectMapper();
@Inject XSOMParser xsdParser = ParserModule.provideSchemaParser();


public DocumentModule(String uri) {
	this.uri = uri;
}

@Produces
public Document parse(Producer<HttpRequesterComponent.Builder> requesterComponentBuilder) throws Exception {
		try {
			assert jsonMapper!=null;
			HttpRequesterComponent component = requesterComponentBuilder.get().get()
					.httpRequesterModule(new HttpRequesterModule(uri))
					.build();
//			InputStream documentStream = component.fetchHttpData()
//					.get();
//			return jsonMapper.readValue(documentStream, Document.class);
			String v = component.fetchHttpDataAsString().get();
			System.err.println("***********"+v);
			return jsonMapper.readValue(v, Document.class);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			throw new Exception("Could not parse document '"+uri+"'", e);
		} catch (JsonMappingException e) {
			throw new Exception("Could not map json into object for '"+uri+"'", e);
		} catch (IOException e) {
			throw new Exception("Could not read json document '"+uri+"'", e);
		}
	return null;
//	try {
//		InputStream documentStream = remoteDocumentStream.get();
//		return jsonMapper.readValue(documentStream, Document.class);
//	} catch (ExecutionException e) {
//		throw new Exception("Could not get remote document at '"+uri+"'", e);
//	} catch (IOException e) {
//		throw new Exception("Could not read json document '"+uri+"'", e);
//	}
	
}


//@Produces
//@Named("CompleteDocument")
//public Document completeDocument(@Named a) {
//	
//}

}

