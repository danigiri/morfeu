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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import dagger.producers.Producer;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;
import javax.inject.Provider;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
* @author daniel giribet
* Handles parsing raw XML or YAML (hardcoded transformation)
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class URIToParsedModule {

protected final static Logger log = LoggerFactory.getLogger(URIToParsedModule.class);
		
//notice this is a DOM Document and not a morfeu document
@Produces
public static org.w3c.dom.Document produceDomDocument(DocumentBuilder db, 
													@Named("FetchableContentURI") URI uri, 
													@Named("FetchedEffectiveContent") InputStream effectiveContent) 
															throws ParsingException, FetchingException {
	
	// TODO: we can probably parse with something faster than building into dom
	try {
		return db.parse(effectiveContent);
	} catch (SAXException e) {
		log.error("Could not parse '{}' ({})", uri, e);
		throw new ParsingException("Problem when parsing '"+uri+"'", e);
	} catch (IOException e) {
		log.error("Could not fetch '{}' ({})", uri, e);
		throw new FetchingException("Problem when fetching '"+uri+"'", e);
	}

}


@Produces @Named("FetchedEffectiveContent") 
InputStream fetchedContentReady(@Named("FetchableContentURI") URI uri, 
								@Named("FetchedRawContent") Producer<InputStream> rawContentProvider,
								@Named("FetchedTransformedContent") Producer<InputStream> transformedContentProvider
								) throws FetchingException {

	// if uri ends with yaml, we transform it from yaml to xml, otherwise we just fetch it raw, assuming xml

	String name = FilenameUtils.getName(uri.getPath());

	try {
		if (name.endsWith("yaml")) {
		
				return transformedContentProvider.get().get();
		
		} else  {
	
			return rawContentProvider.get().get();
		
		}
	} catch (InterruptedException | ExecutionException e) {
		log.error("Could not complete executing fetch of '{}' ({}", uri, e);
		throw new FetchingException("Problem executing fetch '"+uri+"'", e);
	}

}


@Produces @Named("FetchedRawContent")
public static InputStream fetchedRawContent(@Named("FetchableContentURI") URI uri) throws FetchingException {

	// if uri is absolute we retrieve it, otherwise we assume it's a local relative file
	
	try {
		if (uri.isAbsolute()) {
			log.info("Fetching absolute content uri '{}' to parse", uri);
			return IOUtils.toInputStream(IOUtils.toString(uri, Config.DEFAULT_CHARSET), Config.DEFAULT_CHARSET);
		} else {
			log.info("Fetching relative content uri '{}' to parse, assuming file", uri);
			return FileUtils.openInputStream(new File(uri.toString()));
		}
	} catch (IOException e) {
		log.error("Could not fetch '{}' ({}", uri, e);
		throw new FetchingException("Problem when fetching '"+uri+"'", e);
	}
}


@Produces @Named("FetchedTransformedContent")
public static InputStream fetchedTransformedContent(@Named("FetchableContentURI") URI uri,
													@Named("FetchedRawContent") InputStream fetchedRawContent, 
													YAMLMapper mapper, 
													Producer<Model> model) 
							throws FetchingException, TransformException {

	// get the yaml and apply the transformation from yaml to xml
	
	try {
		
		JsonNode yaml = mapper.readTree(fetchedRawContent);
		Map<String, Object> values = new HashMap<String, Object>(2);
		values.put("yaml", yaml);
		values.put("cellmodels", model.get().get().getRootCellModels());
		values.put("case","yaml-to-xml");

		log.trace("Converting yaml to xml '{}'", uri);
		String transformedContent = DaggerViewComponent.builder()
			.withTemplate("templates/transform/content-yaml-to-xml.twig")
			.withValue(values)
			.build()
			.render();
		
		return IOUtils.toInputStream(transformedContent, Config.DEFAULT_CHARSET);
		
	} catch (IOException e) {
		log.error("Could not fetch yaml '{}' ({}", uri, e);
		throw new FetchingException("Problem when fetching yaml '"+uri+"'", e);
	} catch (InterruptedException | ExecutionException e) {
		log.error("Could not transform yaml to xml '{}' ({}", uri, e);
		throw new TransformException("Problem when transforming yaml to xml '"+uri+"'", e);
	}

}


@Produces
YAMLMapper yamlMapper() {
	return new YAMLMapper();
}

}