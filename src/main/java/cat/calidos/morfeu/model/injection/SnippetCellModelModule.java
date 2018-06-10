/*
 *    Copyright 2018 Daniel Giribet
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

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.problems.ConfigurationException;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class SnippetCellModelModule {


protected final static Logger log = LoggerFactory.getLogger(SnippetCellModelModule.class);

@Produces @Named("ModelURI")
public static URI modelURI(@Named("FilteredModelURI") URI uri) throws ConfigurationException {
	
	log.trace("Filtering cell model URI "+uri);
	
	String modelURI = uri.toString();
	int queryIndex = modelURI.indexOf("?filter=");
	if (queryIndex==-1) {
		throw new ConfigurationException("Filtered model uri did not have '?filter=' ("+modelURI+")");		
	}
	
	try {
		return new URI(modelURI.substring(0, queryIndex));
	} catch (URISyntaxException e) {
		log.error("Snippet model uri creation failed, could not remove query from "+modelURI);
		throw new ConfigurationException("Problem when creating effective model URI for snippet", e);
	}
			
}

@Produces @Named("SkipValidation")
public static Boolean skipValidation() {
	return true;
}


// return the uri of the snippet model so we can link it to the snippet content, at the moment we remove filter
@Produces @Named("CellModelFilter")
public static URI cellModelFilter(@Named("FilteredModelURI") URI uri) throws ConfigurationException {
	
	try {
		
		return new URI(uri.toString().replace("?filter=", ""));
		
	} catch (URISyntaxException e) {
		log.error("Snippet cell model filter creation failed");
		throw new ConfigurationException("Problem when creating the cell model filter for snippet", e);
	}

}

}
