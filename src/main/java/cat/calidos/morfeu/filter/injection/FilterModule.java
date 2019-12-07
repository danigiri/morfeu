// TRANSFORM MODULE . JAVA

package cat.calidos.morfeu.filter.injection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;
import cat.calidos.morfeu.filter.Filter;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.transform.injection.DaggerContentConverterComponent;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class FilterModule {

@Produces
public static Filter<String, String> stringToString(List<String> links,
									@Named("stringToString") Map<String, Filter<String, String>> strToStrFilters,
									@Named("objectToString") Map<String, Filter<Object, String>> objToStrFilters,
									@Named("stringToObject") Map<String, Filter<String, Object>> strToObjFilters,
									@Named("objectToObject") Map<String, Filter<Object, Object>> objToObjFilters)
											throws ConfigurationException {
	return new FilterEngine<String, Object>().xToX(links,
													strToStrFilters,
													strToObjFilters,
													objToStrFilters,
													objToObjFilters);
}


@Produces
public static Filter<String, Object> stringToObject(List<String> transforms,
									@Named("stringToString") Map<String, Filter<String, String>> strToStrFilters,
									@Named("objectToString") Map<String, Filter<Object, String>> objToStrFilters,
									@Named("stringToObject") Map<String, Filter<String, Object>> strToObjFilters,
									@Named("objectToObject") Map<String, Filter<Object, Object>> objToObjFilters)
											throws ConfigurationException {
	return new FilterEngine<String, Object>().xToY(transforms,
														strToStrFilters,
														strToObjFilters,
														objToStrFilters,
														objToObjFilters);
}


@Produces
public static Filter<Object, String> objectToString(List<String> transforms,
									@Named("stringToString") Map<String, Filter<String, String>> strToStrFilters,
									@Named("objectToString") Map<String, Filter<Object, String>> objToStrFilters,
									@Named("stringToObject") Map<String, Filter<String, Object>> strToObjFilters,
									@Named("objectToObject") Map<String, Filter<Object, Object>> objToObjFilters)
											throws ConfigurationException {
	return new FilterEngine<Object, String>().xToY(transforms,
														objToObjFilters,	// different order
														objToStrFilters,
														strToObjFilters,
														strToStrFilters
														);
}

@Produces
public static Filter<Object, Object> objectToObject(List<String> transforms,
									@Named("stringToString") Map<String, Filter<String, String>> strToStrFilters,
									@Named("objectToString") Map<String, Filter<Object, String>> objToStrFilters,
									@Named("stringToObject") Map<String, Filter<String, Object>> strToObjFilters,
									@Named("objectToObject") Map<String, Filter<Object, Object>> objToObjFilters)
											throws ConfigurationException {
	return new FilterEngine<Object, String>().xToX(transforms,
														objToObjFilters,	// different order
														objToStrFilters,
														strToObjFilters,
														strToStrFilters
														);
}


@Produces
public static List<String> transforms(@Named("Filters") String requestedFilters) {
	return FilterEngine.parseFilters(requestedFilters);
}


@Produces
public static Map<String, JsonNode> params(List<String> transforms) throws ConfigurationException {

	Map<String, JsonNode> params = new HashMap<String, JsonNode>(transforms.size());

	for (String t : transforms) {
		params.put(FilterEngine.nameFromFilter(t), FilterEngine.parseParametersFrom(t));
	}

	return params;
}


//TODO: move to domain-specific transform module please
@Produces @IntoMap @Named("objectToString")
@StringKey("content-to-xml")
Filter<Object, String> contentToXML() {
	return (json) -> DaggerContentConverterComponent.builder().from((JsonNode)json).build().xml();
}


//TODO: move to domain-specific transform module
@Produces @IntoMap @Named("objectToString")
@StringKey("content-to-yaml")
Filter<Object, String> contentToYAML() {
	return (values) -> DaggerViewComponent.builder()
											.withTemplatePath("templates/transform/content-to-yaml.twig")
											.withValue(values)
											.build()
											.render();
}


//TODO: move to domain-specific transform module
@Produces  @IntoMap @Named("stringToObject")
@StringKey("string-to-json")
Filter<String, Object> stringToJSON() {
	return (content) -> DaggerJSONParserComponent.builder().from(content).build().json().get();
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
