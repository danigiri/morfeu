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
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sun.xml.xsom.parser.XSOMParser;

import dagger.Module;
import dagger.Provides;
import dagger.producers.Produced;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;
import dagger.producers.Production;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class DocumentModule {

protected URI uri;
protected ObjectMapper jsonMapper;
protected XSOMParser parser;
protected XSOMParser xsdParser;

@Inject
public DocumentModule(URI uri, ObjectMapper jsonMapper, XSOMParser xsdParser) {
	this.uri = uri;
	this.jsonMapper = jsonMapper;
	this.xsdParser = xsdParser;
}

@Produces
@Named("BareDocument")
public Document parse(@Named("DocumentJSON") Produced<InputStream> remoteDocumentStream) throws Exception {
	try {
		InputStream documentStream = remoteDocumentStream.get();
		return jsonMapper.readValue(documentStream, Document.class);
	} catch (ExecutionException e) {
		throw new Exception("Could not get remote document at '"+uri+"'", e);
	} catch (JsonParseException e) {
		throw new Exception("Could not parse document '"+uri+"'", e);
	} catch (JsonMappingException e) {
		throw new Exception("Could not map json into object for '"+uri+"'", e);
	} catch (IOException e) {
		throw new Exception("Could not read json document '"+uri+"'", e);
	}
	
}


//@Produces
//@Named("CompleteDocument")
//public Document completeDocument(@Named a) {
//	
//}

}

