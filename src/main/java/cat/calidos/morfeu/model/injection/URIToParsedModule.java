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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import dagger.producers.Producer;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.parboiled.support.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import cat.calidos.morfeu.filter.injection.DaggerFilterComponent;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.transform.injection.DaggerYAMLConverterComponent;
import cat.calidos.morfeu.utils.Config;

/**
* @author daniel giribet
* Handles parsing raw XML or YAML (hardcoded transformation)
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class URIToParsedModule {

private static final String JSON_EXTENSION = "json";
private static final String YAML_EXTENSION = "yaml";
protected final static Logger log = LoggerFactory.getLogger(URIToParsedModule.class);

//notice this is a DOM Document and not a morfeu document
@Produces
public static org.w3c.dom.Document produceDomDocument(DocumentBuilder db, 
													@Named("FetchableContentURI") URI uri, 
													@Named("EffectiveContent") InputStream effectiveContent)
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
InputStream fetchedContentReady(@Named("Filename") String filename,
								@Named("ContentURI") URI uri,
								@Nullable @Named("FetchableContentURI") URI fetchableURI,
								@Named("FetchedRawContent") Producer<InputStream> rawContentProvider,
								@Named("FetchedTransformedContent") Producer<InputStream> transformedContentProvider
								) throws FetchingException {

	// if uri ends with yaml, we transform it from yaml to xml, otherwise we just fetch it raw, assuming xml

	InputStream effectiveContent;

	try {
		if (filename.endsWith(YAML_EXTENSION) || filename.endsWith(JSON_EXTENSION)) {
			effectiveContent = transformedContentProvider.get().get();
		} else  {
			effectiveContent =  rawContentProvider.get().get();
		}
	} catch (InterruptedException | ExecutionException e) {
		log.error("Could not complete executing fetch of '{}' ({})", uri, e);
		throw new FetchingException("Problem executing fetch '"+uri+"'", e);
	}

	return effectiveContent;

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
													@Named("FetchedRawContent") Producer<InputStream> fetchedRawContent,
													@Named("IsYAML") boolean isYAML,
													Producer<ObjectMapper> mapperJSON,
													Producer<YAMLMapper> mapperYAML,
													Producer<Model> model) 
							throws FetchingException, TransformException {

	// get the yaml and apply the transformation from yaml to xml

	try {

			log.trace("Converting yaml to xml '{}'", uri);
			ObjectMapper mapper = isYAML ? mapperYAML.get().get() : mapperJSON.get().get();
			JsonNode yaml = mapper.readTree(fetchedRawContent.get().get());
			String xml = DaggerYAMLConverterComponent.builder().from(yaml).given(model.get().get()).build().xml();

			log.trace("Transformed yaml to xml '{}'", xml);

			return  IOUtils.toInputStream(xml, Config.DEFAULT_CHARSET);

	} catch (IOException e) {
		log.error("Could not fetch yaml '{}' ({})", uri, e.getMessage());
		throw new FetchingException("Problem when fetching yaml '"+uri+"'", e);
	} catch (Exception e) {
		log.error("Could not transform yaml to xml '{}' ({}", uri, e);
		throw new TransformException("Problem when transforming yaml to xml '"+uri+"'", e);
	}

}



@Produces @Named("EffectiveContent")
public static InputStream filteredContent(@Named("FetchableContentURI") URI uri,
											@Nullable @Named("Filters") String filters,
											@Named("FetchedEffectiveContent") InputStream fetchedEffectiveContent) 
							throws FetchingException {

	InputStream content;

	if (filters!=null && !filters.isBlank()) {
		try {
				String raw = IOUtils.toString(fetchedEffectiveContent, Config.DEFAULT_CHARSET);
				String filtered = DaggerFilterComponent.builder()
														.filters(filters)
														.build()
														.stringToString()
														.get()
														.apply(raw);
				content = IOUtils.toInputStream(filtered, Config.DEFAULT_CHARSET);
		} catch (Exception e) {
			log.error("Could not filter '{}' ({})", uri, e.getMessage());
			throw new FetchingException("Problem when filtering '"+uri+"'", e);
		}
	} else {
		content = fetchedEffectiveContent;
	}

	return content;

}


@Produces @Named("Filename")
String filename(@Named("FetchableContentURI") URI uri) {
	return FilenameUtils.getName(uri.getPath());
}

@Produces @Named("IsYAML")
boolean isYAML(@Named("Filename") String filename) {
	return filename.endsWith(YAML_EXTENSION);
}


}
