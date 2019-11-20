// TRANSFORM MODULE . JAVA

package cat.calidos.morfeu.model.transform.injection;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import cat.calidos.morfeu.model.transform.Transform;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.transform.injection.DaggerContentConverterComponent;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class TransformModule {

@Produces
public static Transform<String, String> stringToString(List<String> transforms,
									@Named("stringToString") Map<String, Transform<String, String>> strToStrTransforms,
									@Named("objectToString") Map<String, Transform<Object, String>> objToStrTransforms,
									@Named("stringToObject") Map<String, Transform<String, Object>> strToObjTransforms,
									@Named("objectToObject") Map<String, Transform<Object, Object>> objToObjTransforms)
											throws ConfigurationException {
	return new TransformEngine<String,Object>().xToX(transforms,
														strToStrTransforms,
														strToObjTransforms,
														objToStrTransforms,
														objToObjTransforms);
}


@Produces
public static Transform<String, Object> stringToObject(List<String> transforms,
									@Named("stringToString") Map<String, Transform<String, String>> strToStrTransforms,
									@Named("objectToString") Map<String, Transform<Object, String>> objToStrTransforms,
									@Named("stringToObject") Map<String, Transform<String, Object>> strToObjTransforms,
									@Named("objectToObject") Map<String, Transform<Object, Object>> objToObjTransforms)
											throws ConfigurationException {
	return new TransformEngine<String,Object>().xToY(transforms,
														strToStrTransforms,
														strToObjTransforms,
														objToStrTransforms,
														objToObjTransforms);
}


@Produces
public static Transform<Object, String> objectToString(List<String> transforms,
									@Named("stringToString") Map<String, Transform<String, String>> strToStrTransforms,
									@Named("objectToString") Map<String, Transform<Object, String>> objToStrTransforms,
									@Named("stringToObject") Map<String, Transform<String, Object>> strToObjTransforms,
									@Named("objectToObject") Map<String, Transform<Object, Object>> objToObjTransforms)
											throws ConfigurationException {
	return new TransformEngine<Object,String>().xToY(transforms,
														objToObjTransforms,	// different order
														objToStrTransforms,
														strToObjTransforms,
														strToStrTransforms
														);
}

@Produces
public static Transform<Object, Object> objectToObject(List<String> transforms,
									@Named("stringToString") Map<String, Transform<String, String>> strToStrTransforms,
									@Named("objectToString") Map<String, Transform<Object, String>> objToStrTransforms,
									@Named("stringToObject") Map<String, Transform<String, Object>> strToObjTransforms,
									@Named("objectToObject") Map<String, Transform<Object, Object>> objToObjTransforms)
											throws ConfigurationException {
	return new TransformEngine<Object, String>().xToX(transforms,
														objToObjTransforms,	// different order
														objToStrTransforms,
														strToObjTransforms,
														strToStrTransforms
														);
}


@Produces
public static List<String> parseTransforms(@Named("Transforms") String requestedTransforms) {
	return TransformEngine.parseTransforms(requestedTransforms);
}


//TODO: move to domain-specific transform module
@Produces @IntoMap @Named("objectToString")
@StringKey("content-to-xml")
Transform<Object, String> contentToXML() {
	return (json) -> DaggerContentConverterComponent.builder().from((JsonNode)json).build().xml();
}


//TODO: move to domain-specific transform module
@Produces @IntoMap @Named("objectToString")
@StringKey("content-to-yaml")
Transform<Object, String> contentToYAML() {
	return (values) -> DaggerViewComponent.builder()
											.withTemplatePath("templates/transform/content-to-yaml.twig")
											.withValue(values)
											.build()
											.render();
}


//TODO: move to domain-specific transform module
@Produces  @IntoMap @Named("stringToObject")
@StringKey("string-to-json")
Transform<String, Object> stringToJSON() {
	return (content) -> DaggerJSONParserComponent.builder().from(content).build().json().get();
}


}

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
