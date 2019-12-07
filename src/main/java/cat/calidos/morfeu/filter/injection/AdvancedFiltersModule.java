package cat.calidos.morfeu.filter.injection;

import java.io.IOException;
import java.util.Map;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;
import cat.calidos.morfeu.filter.Filter;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;
import cat.calidos.morfeu.utils.injection.MapperModule;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule(includes=MapperModule.class)
public class AdvancedFiltersModule {

protected final static Logger log = LoggerFactory.getLogger(AdvancedFiltersModule.class);


@Produces @IntoMap @Named("stringToString")
@StringKey("yaml-to-json")
Filter<String, String> yamlToJSON(ObjectMapper jsonMapper, YAMLMapper yamlMapper) {
	return yaml -> {
		try {

			// we are using the dirty trick of converting to YAML and then converting to JSON, works ^^
			return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(yamlMapper.readTree(yaml));

		} catch (IOException e) {
			throw new TransformException("Problem when transforming yaml", e);
		}
	};
}


//TODO: move to domain-specific transform module
@Produces @IntoMap @Named("objectToString")
@StringKey("apply-template")
Filter<Object, String> applyTemplate(Map<String, JsonNode> params) {

	if (!params.containsKey("apply-template")) {
		return (values) -> "APPLY TEMPLATE NEEDS PARAMETERS";
	}
	JsonNode filterParameters = params.get("apply-template");
	if (!filterParameters.has("template")) {
		return (values) -> "APPLY TEMPLATE HAS NO TEMPLATE PARAMETER";
	}
	JsonNode templateNode = filterParameters.get("template");
	if (!templateNode.isTextual()) {
		log.error("Incorrect parameters in apply-template, 'template' param value should be a string");
		return (values) -> "APPLY TEMPLATE PARAM 'template' IS NOT A STRING";
	}
	String template = templateNode.asText();

	return (values) -> {
		return DaggerViewComponent.builder().withTemplatePath(template).withValue(values).build().render();
	};

}


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

