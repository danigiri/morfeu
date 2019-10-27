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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
private static final String CELL_PRESENTATION_TYPE_FIELD = "mf:cell-presentation@type";
private static final String CELL_PRESENTATION_METHOD_FIELD = "mf:cell-presentation@method";
private static final String THUMB_FIELD = "mf:thumb";
private static final String DEFAULT_VALUE_FIELD = "mf:default-value";
private static final String IDENTIFIER_FIELD = "mf:identifier@name";
private static final String IDENTIFIER_FIELD_PREFIX = "@";


// serialisation metadata definitions
private static final String TRANSFORM_TAG = "mf:transform";
private static final String TRANSFORM_TYPE_ATTR = "type";
private static final String ATTRIBUTE_TYPE = "attribute";
private static final String DIRECTIVE_TYPE = "directive";
private static final String TRANSFORM_CASE_ATTR = "case";


@Provides
public static Metadata provideMetadata(URI uri,
										@Named("desc") Optional<String> desc,
										@Named("presentation") Optional<String> presentation,
										@Named("cellPresentation") Optional<String> cellPresentation,
										@Named("cellPresentationType") Optional<String> cellPresentationType,
										@Named("cellPresentationMethod") Optional<String> cellPresentationMethod,
										@Named("thumb") Optional<String> thumb,
										@Named("identifier") Optional<String> identifier,
										Map<String, String> defaultValues,
										@Named("Directives") Map<String, Set<String>> directives,
										@Named("Attributes") Map<String, Set<String>> attributes) {
	return new Metadata(uri,
						desc,
						presentation,
						cellPresentation,
						cellPresentationType,
						cellPresentationMethod,
						thumb,
						identifier, 
						defaultValues, 
						directives, 
						attributes);
}


@Provides
public static URI uri(@Nullable XSAnnotation annotation,
						@Nullable @Named("ParentURI") URI parentURI,
						@Named("DefaultURI") Lazy<URI> defaultURI) {
	
	Optional<String> uriValue = annotationTaggedAs(annotation, URI_FIELD);
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
public static URI defaultURI(@Nullable @Named("ParentURI") URI parentURI) {

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
public static Optional<String> desc(@Nullable XSAnnotation annotation) {
	return annotationTaggedAs(annotation, DESC_FIELD);
}


@Provides @Named("presentation")
public static Optional<String> presentation(@Nullable XSAnnotation annotation) {
	return annotationTaggedAs(annotation, PRESENTATION_FIELD);
}


@Provides @Named("cellPresentation")
public static Optional<String> cellPresentation(@Nullable XSAnnotation annotation) {
	return annotationTaggedAs(annotation, CELL_PRESENTATION_FIELD);
}


@Provides @Named("cellPresentationType") Optional<String> cellPresentationType(@Nullable XSAnnotation annotation) {
	return annotationTaggedAs(annotation, CELL_PRESENTATION_TYPE_FIELD);
}


@Provides @Named("cellPresentationMethod") Optional<String> cellPresentationMethod(@Nullable XSAnnotation annotation) {
	return annotationTaggedAs(annotation, CELL_PRESENTATION_METHOD_FIELD);
}


@Provides @Named("thumb")
public static Optional<String> thumb(@Nullable XSAnnotation annotation) {
	return annotationTaggedAs(annotation, THUMB_FIELD);
}


@Provides @Named("identifier")
public static Optional<String> identifier(@Nullable XSAnnotation annotation) {	
	
	Optional<String> identifier = annotationTaggedAs(annotation, IDENTIFIER_FIELD);
	if (identifier.isPresent() && identifier.get().startsWith(IDENTIFIER_FIELD_PREFIX)) {
		identifier = Optional.of(identifier.get().substring(IDENTIFIER_FIELD_PREFIX.length()));
	} 

	return identifier;

}


@Provides
public static Map<String, String> defaultValues(@Nullable XSAnnotation annotation) {

	// we extract the default values from the metadata annotation
	List<Node> nodeValues = DaggerMetadataAnnotationComponent.builder()
							.from(annotation)
							.andTag(DEFAULT_VALUE_FIELD)
							.build()
							.values();
	HashMap<String, String> defaultValues = new HashMap<String, String>();
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
			defaultValues.put(null, defaultValue); 		// this is the default value for the cell content
		}
		
	}
	
	return defaultValues;
	
}


// list of <mf:transform> nodes
@Provides @Named("TransformNodes")
public static List<Node> serializeNodes(@Nullable XSAnnotation annotation) {
	return DaggerMetadataAnnotationComponent.builder().from(annotation).andTag(TRANSFORM_TAG).build().values();
}


@Provides @Named("Directives")
public static Map<String, Set<String>> directives(@Named("TransformNodes") List<Node> transformNodes) {
	return groupSerializeTagsByCaseFilterBy(DIRECTIVE_TYPE, transformNodes);
}


@Provides @Named("Attributes") 
Map<String, Set<String>> attributes(@Named("TransformNodes") List<Node> transformNodes) {
	return groupSerializeTagsByCaseFilterBy(ATTRIBUTE_TYPE, transformNodes);
}


private static Optional<String> annotationTaggedAs(@Nullable XSAnnotation annotation, String tag) {

	List<Node> nodeValues = DaggerMetadataAnnotationComponent.builder()
																.from(annotation)
																.andTag(tag)
																.build()
																.values();

	//TODO: we assume the first value of the tag is the one we want, careful with nested stuff
	return !nodeValues.isEmpty() ? Optional.of(nodeValues.get(0).getTextContent()) : Optional.empty();

}


private static Map<String, Set<String>> groupSerializeTagsByCaseFilterBy(String type, List<Node> nodeValues) {
	
	Map<String, Set<String>> groups = new HashMap<String, Set<String>>(0);
	// first we get all the serialize tags of the given type
	Stream<Node> attributeNodes = nodeValues.stream()
									.filter(Node::hasAttributes)
									.filter(nv -> {
													Node typeNode = nv.getAttributes()
																		.getNamedItem(TRANSFORM_TYPE_ATTR);
													return typeNode!=null && typeNode.getTextContent().equals(type);
									});

	// next we group them by cases
	attributeNodes.filter(an -> an.getAttributes().getNamedItem(TRANSFORM_CASE_ATTR)!=null)
					.forEach(an -> {
									String case_ = an.getAttributes().getNamedItem(TRANSFORM_CASE_ATTR).getTextContent();
									if (!groups.containsKey(case_)) {
										groups.put(case_, new HashSet<String>());
									}
									groups.get(case_).add(an.getTextContent());
	});
	
	return groups;
	
}


}
