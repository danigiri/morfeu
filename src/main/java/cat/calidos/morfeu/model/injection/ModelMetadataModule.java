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

package cat.calidos.morfeu.model.injection;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.sun.xml.xsom.XSAnnotation;

import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.problems.FetchingException;
import dagger.Module;
import dagger.Provides;


/** Model Metadata helper module to enrich the model definitions with useful information
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ModelMetadataModule {

protected final static Logger log = LoggerFactory.getLogger(ModelMetadataModule.class);
		
		
private static final String URI_FIELD = "mf:metadata@uri";
private static String DESC_FIELD = "mf:desc";
private static String PRESENTATION_FIELD = "mf:presentation";
private static String THUMB_FIELD = "mf:thumb";
//private static String UNDEFINED = "";


@Provides
Metadata provideMetadata(Optional<URI> uri,
						 @Named("desc") Optional<String> desc,
						 @Named("presentation") Optional<String> presentation,
						 @Named("thumb") Optional<String> thumb,
						 @Named("Fallback") @Nullable Metadata fallback) {
	
	if (fallback==null) {

		return new Metadata(uri, desc, presentation, thumb);
	
	} else {

		return new Metadata(uri, desc, presentation, thumb, fallback);
		
	} 
}


@Provides
Optional<URI> uri(@Nullable XSAnnotation annotation) {
	
	Optional<String> uriValue = contentOf(annotation, URI_FIELD);
	Optional<URI> optionalURI = Optional.empty();
	
	if (uriValue.isPresent()) {
		try {
			URI uri = DaggerURIComponent.builder().from(uriValue.get()).builder().uri().get();
			optionalURI = Optional.of(uri);
		} catch (Exception e) {
			// log the error and return empty for the moment
			// TODO: invalid URIs in metadata fail silently and should propagate an error
			log.error("Invalid uri in metadata '{}'",uriValue);
		}
	}
	
	return optionalURI;
	
}


@Provides @Named("desc")
Optional<String> desc(@Nullable XSAnnotation annotation) {
	return contentOf(annotation, DESC_FIELD);
}


@Provides @Named("presentation")
Optional<String> presentation(@Nullable XSAnnotation annotation) {
	return contentOf(annotation, PRESENTATION_FIELD);
}


@Provides @Named("thumb")
Optional<String> thumb(@Nullable XSAnnotation annotation) {
	return contentOf(annotation, THUMB_FIELD);
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
