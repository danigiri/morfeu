/*
 *    Copyright 2017 Daniel Giribet
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

package cat.calidos.morfeu.model.metadata.injection;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.sun.xml.xsom.XSAnnotation;

import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.model.metadata.injection.DaggerMetadataAnnotationComponent;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

/** Model Metadata helper module to enrich the model definitions with useful information
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ModelMetadataModule {

protected final static Logger log = LoggerFactory.getLogger(ModelMetadataModule.class);

private static final String URI_FIELD = "mf:metadata@uri";
private static final String DEFAULT_URI = ".";
private static final String METADATA = "";	// empty at the moment

//private static final String METADATA = "#metadata";
private static final String DESC_FIELD = "mf:desc";
private static final String PRESENTATION_FIELD = "mf:presentation";
private static final String CELL_PRESENTATION_FIELD = "mf:cell-presentation";
private static final String THUMB_FIELD = "mf:thumb";
private static final String DEFAULT_VALUE_FIELD = "mf:default-value";

@Provides
Metadata provideMetadata(URI uri,
						@Named("desc") Optional<String> desc,
						@Named("presentation") Optional<String> presentation,
						@Named("cellPresentation") Optional<String> cellPresentation,
						@Named("thumb") Optional<String> thumb,
						Map<String, String> defaultValues,
						@Named("Fallback") @Nullable Metadata fallback) {
	
	if (fallback==null) {

		return new Metadata(uri, desc, presentation, cellPresentation, thumb, defaultValues);

	} else {

		return new Metadata(uri, desc, presentation, cellPresentation, thumb, defaultValues, fallback);

	} 
}


@Provides
URI uri(@Nullable XSAnnotation annotation,
	    @Nullable @Named("ParentURI") URI parentURI,
	    @Named("DefaultURI") Lazy<URI> defaultURI) {
	
	Optional<String> uriValue = contentOf(annotation, URI_FIELD);
	URI uri = null;

	if (uriValue.isPresent()) {
		// in the metadata we have explicitly the URI
		try {
			uri = DaggerURIComponent.builder().from(parentURI+"/"+uriValue.get()+METADATA).builder().uri().get();
		} catch (Exception e) {
			// log the error and return empty for the moment
			// TODO: invalid URIs in metadata fail silently and should probably propagate an error
			log.error("Invalid uri in metadata '{}', using default uri",uriValue);
			uri = defaultURI.get();
		}

	} else {
		uri = defaultURI.get();
	}
	
	return uri;
	
}


// we derive the URI from the parent
@Provides @Named("DefaultURI")
URI defaultURI(@Nullable @Named("ParentURI") URI parentURI) {
	
	URI uri = null;
	try {
		String uriVal = parentURI!=null ? parentURI.toString()+"/"+DEFAULT_URI+METADATA : DEFAULT_URI+METADATA;
		uri = DaggerURIComponent.builder()
				.from(uriVal)
				.builder()
				.uri()
				.get();
	} catch (Exception e) {
		// DEFAULT URI SHOULD NOT FAIL
		log.error("Really? Default URI for metadata fails - epic fail");		
	}
	
	return uri;
	
}

@Provides @Named("desc")
Optional<String> desc(@Nullable XSAnnotation annotation) {
	return contentOf(annotation, DESC_FIELD);
}


@Provides @Named("presentation")
Optional<String> presentation(@Nullable XSAnnotation annotation) {
	return contentOf(annotation, PRESENTATION_FIELD);
}


@Provides @Named("cellPresentation")
Optional<String> cellPresentation(@Nullable XSAnnotation annotation) {
	return contentOf(annotation, CELL_PRESENTATION_FIELD);
}


@Provides @Named("thumb")
Optional<String> thumb(@Nullable XSAnnotation annotation) {
	return contentOf(annotation, THUMB_FIELD);
}



@Provides
Map<String, String> defaultValues(@Nullable XSAnnotation annotation, @Named("Fallback") @Nullable Metadata fallback) {

	// we extract the default values from the metadata annotation
	List<Node> nodeValues = DaggerMetadataAnnotationComponent.builder()
							.from(annotation)
							.andTag(DEFAULT_VALUE_FIELD)
							.build()
							.values();
	HashMap<String, String> defaultValues = new HashMap<String, String>();
	// set the fallback ones first if any, as they will be overwritten by any local annotation values
	if (fallback!=null) {
		defaultValues.putAll(fallback.getDefaultValues());
	}
	for (Node n : nodeValues) {

		String defaultValue = n.getTextContent();
		Node nameItem = n.getAttributes().getNamedItem("name");
		if (nameItem!=null) {
			String name = nameItem.getNodeValue();
			if (name.startsWith(Metadata.DEFAULT_VALUE_PREFIX)) {
				defaultValues.put(name, defaultValue)	;	// default value for attribute
			} else {
				log.warn("Ignoring a metadata default that doesn't have a name starting with '@' ({})", n);
			}
		} else {
			defaultValues.put(null, defaultValue); 		// this is the default value for the cell
		}
		
	}
	
	return defaultValues;
	
}

//reverse breadth-first search, as the dom annotation parser adds all sibling nodes in reverse order
private static Optional<String> contentOf(@Nullable XSAnnotation annotation, String tag) {

	List<Node> nodeValues = DaggerMetadataAnnotationComponent.builder()
																.from(annotation)
																.andTag(tag)
																.build()
																.values();

	//TODO: we assume the first value of the tag is the one we want, careful with nested stuff
	
	return !nodeValues.isEmpty() ? Optional.of(nodeValues.get(0).getTextContent()) : Optional.empty();

}


}
