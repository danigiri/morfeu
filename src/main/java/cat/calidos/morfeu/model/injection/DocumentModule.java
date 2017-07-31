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
import cat.calidos.morfeu.model.Validable;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.MorfeuUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;
import javax.inject.Provider;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/** TODO: ensure all this is actually asynchronous
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule(subcomponents={ModelSubcomponent.class, ValidatorComponent.class})
public class DocumentModule extends RemoteModule {

protected final static Logger log = LoggerFactory.getLogger(DocumentModule.class);
		
		
@Produces
public static Document produceDocument(@Named("NormalisedDocument") Document doc, 
											  Provider<ModelSubcomponent.Builder> modelComponentProvider,
											  Provider<ValidatorComponent.Builder> validatorComponentProvider) throws Exception {

	// FIXME: what exception should we throw here?
	Model model;
	try {
		 model = modelComponentProvider.get().builder().model().get();
	} catch (Exception e) {
		throw new ExecutionException("Problem with model of document '"+doc.getName()+"' with model: '"+doc.getModelURI()+"'",e);
	}
	doc.setModel(model);
		
	Validable validator = validatorComponentProvider.get().builder().validator().get();
	doc.setValidator(validator);
	
	return doc;
	
}


@Produces @Named("JSONDocumentStream")
public static InputStream fetchDocumentJSON(URI uri, CloseableHttpClient client) throws FetchingException {

	try {
		
		return fetchRemoteStream(uri, client).get();
		
	} catch (InterruptedException | ExecutionException e) {
		throw new FetchingException("Problem while fetching '"+uri+"'", e);
	}

	
}


@Produces @Named("ParsedDocument")
public static Document parseDocument(URI uri, @Named("JSONDocumentStream") InputStream docStream, ObjectMapper mapper) 
		throws ParsingException, FetchingException {
	
	try {
		
		return mapper.readerForUpdating(new Document(uri)).readValue(docStream);
		
	} catch (JsonProcessingException jpe) {
		throw new ParsingException("Problem with the json format of '"+uri+"'", jpe);
	} catch (IOException ioe) {
		throw new FetchingException("Problem fetching '"+uri+"'", ioe);
	}

}


@Produces @Named("NormalisedDocument")
public static Document normaliseDocumentURIs(@Named("ParsedDocument") Document doc,
											 @Named("PrefixURI") URI prefix,
											 @Named("FetchableModelURI") URI fetchableModelURI, 
											 @Named("ContentURI") URI contentURI) {
	
	log.trace("[DocumentModule::normaliseDocumentURIs prefix={}, model={} content={}]", prefix, fetchableModelURI, contentURI);
	
	doc.setFetchableModelURI(fetchableModelURI);
	doc.setContentURI(contentURI);
	doc.setPrefix(prefix);
		
	return doc;
	
}


@Produces  @Named("PrefixURI")
public static URI documentPrefix(@Named("ParsedDocument") Document doc, 
								 @Named("Prefix") String prefix) throws ParsingException {
	URI prefixURI = null;
	if (prefix==null || prefix.length()==0) {
		// we make a best effort to guess the prefix
		String uri = doc.getURI().toString();
		try {
			int index = uri.lastIndexOf("/");
			if (index==-1) {
				throw new ParsingException("Problem guessing prefix as no / found on '"+uri+"'",new IndexOutOfBoundsException());
			}
			prefixURI = new URI(uri.substring(0, index+1));
		} catch (URISyntaxException e) {
			throw new ParsingException("Problem guessing prefix of '"+uri+"'", e);
		}
	} else {
		try {
			prefixURI = new URI(prefix);
		} catch (URISyntaxException e) {
			throw new ParsingException("Problem with invalid URI of prefix '"+prefix+"'", e);
		}
	}
	
	return prefixURI;
	
}


// this is the basic model uri, usually relative and easy to read and understand, cannot necesarily be fetched from
// the runtime context
@Produces @Named("ModelURI")
public static URI modelURI(@Named("ParsedDocument") Document doc) {
	return doc.getModelURI();
}


// this is a model uri that is absolute and fetchable, guaranteed to be reachable from any runtime context
@Produces @Named("FetchableModelURI")
public static URI fetchableModelURI(@Named("PrefixURI") URI prefix, @Named("ParsedDocument") Document doc) throws ParsingException {
	return DocumentModule.makeAbsoluteURIIfNeeded(prefix, doc.getModelURI());
}


@Produces @Named("ContentURI")
public static URI contentURI(@Named("PrefixURI") URI prefix, @Named("ParsedDocument") Document doc) throws ParsingException {
	return DocumentModule.makeAbsoluteURIIfNeeded(prefix, doc.getContentURI());
}


// if the uri is absolute we don't modify it, if prefix is a file://, we don't modify it either, otherwise we prepend the prefix
private static URI makeAbsoluteURIIfNeeded(URI prefix, URI uri) throws ParsingException {
	
	URI finalURI = null;
	if (!uri.isAbsolute() && prefix.getScheme()!=null && !prefix.getScheme().equals("file")) {
		try {
			finalURI = new URI(prefix+uri.toString());
		} catch (URISyntaxException e) {
			throw new ParsingException("Problem composing absolute urls with prefix:'"+prefix+"', and:'"+uri+"'",e);
		}
	} else {
		finalURI = uri;
	}
	
	return finalURI;

}


}
