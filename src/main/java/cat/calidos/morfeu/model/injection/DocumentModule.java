// DOCUMENT MODULE . JAVA

package cat.calidos.morfeu.model.injection;


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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.utils.injection.RemoteModule;


/** TODO: ensure all this is actually asynchronous
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule(subcomponents={ModelSubcomponent.class, ContentParserSubcomponent.class})
public class DocumentModule extends RemoteModule {

protected final static Logger log = LoggerFactory.getLogger(DocumentModule.class);


@Produces
public static Document produceDocument(@Named("NormalisedDocument") Document doc, 
										Provider<ModelSubcomponent.Builder> modelComponentProvider,
										Provider<ContentParserSubcomponent.Builder> contentParserComponentProvider) 
												throws ExecutionException {

	// FIXME: what exception should we throw here?
	Model model;
	try {

		model = modelComponentProvider.get().builder().model().get();
		doc.setModel(model);
		ContentParserSubcomponent contentParser = contentParserComponentProvider.get().builder();
		doc.setValidator(contentParser.validator().get());
		doc.validate();	// if this does not throw an exception, it means content is valid
		doc.setContent(contentParser.content().get());

	} catch (Exception e) {
		throw new ExecutionException("Problem with model of document '"+doc.getName()+"' with model: '"+doc.getModelURI()+"'",e);
	}
	
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
											 @Named("ContentURI") URI contentURI,
											 @Named("FetchableContentURI") URI fetchableContentURI
											 ) {
	
	log.trace("[DocumentModule::normaliseDocumentURIs prefix={}, model={} content={}]", prefix, fetchableModelURI, contentURI);
	
	doc.setFetchableModelURI(fetchableModelURI);
	doc.setContentURI(contentURI);
	doc.setFetchableContentURI(fetchableContentURI);
	doc.setPrefix(prefix);
		
	return doc;
	
}


@Produces @Named("PrefixURI")
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


@Produces @Named("SkipValidation")
public static Boolean skipValidation(@Named("ParsedDocument") Document doc) {
	return doc.skipValidation();
}


//this is a model uri that is absolute and fetchable, guaranteed to be reachable from any runtime context
@Produces @Named("FetchableModelURI")
public static URI fetchableModelURI(@Named("PrefixURI") URI prefix, @Named("ModelURI") URI uri) 
					throws ParsingException {
	return MorfeuUtils.makeAbsoluteURIIfNeeded(prefix, uri);
}


@Produces @Named("ContentURI")
public static URI contentURI(@Named("ParsedDocument") Document doc) {
	return doc.getContentURI();
}


//this is a content uri that is absolute and fetchable, guaranteed to be reachable from any runtime context
@Produces @Named("FetchableContentURI")
public static URI fetchableContentURI(@Named("PrefixURI") URI prefix, @Named("ParsedDocument") Document doc) throws ParsingException {
	return MorfeuUtils.makeAbsoluteURIIfNeeded(prefix, doc.getContentURI());
}


}


/*
 *    Copyright 2019 Daniel Giribet
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

