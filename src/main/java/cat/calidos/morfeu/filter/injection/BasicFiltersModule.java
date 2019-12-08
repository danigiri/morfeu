// BASIC FILTERS MODULE . JAVA

package cat.calidos.morfeu.filter.injection;

import java.util.Map;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.filter.Filter;
import cat.calidos.morfeu.problems.TransformException;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class BasicFiltersModule {

protected final static Logger log = LoggerFactory.getLogger(BasicFiltersModule.class);


@Produces @IntoMap @Named("stringToString")
@StringKey("identity")
Filter<String, String> stringIdentity() {
	return s -> s;
}


@Produces @IntoMap @Named("stringToString")
@StringKey("uppercase")
Filter<String, String> uppercase() {	// mainly for testing
	return s -> s.toUpperCase();
}


@Produces @IntoMap @Named("stringToString")
@StringKey("lowercase")
Filter<String, String> lowercase() {	// mainly for testing
	return s -> s.toLowerCase();
}


@Produces @IntoMap @Named("stringToString")
@StringKey("replace")
Filter<String, String> replace(Map<String, JsonNode> params) throws TransformException {

	if (!params.containsKey("replace")) {
		return (v) -> "replace filter did not get parameters";
	}
	JsonNode filterParameters = params.get("replace");
	if (!filterParameters.has("from") || !filterParameters.has("to")) {
		String message = "replace filter did not get proper parameters (from: and/or to:)";
		log.error(message);
		return (v) -> message;
	}
	JsonNode fromNode = filterParameters.get("from");
	JsonNode toNode = filterParameters.get("to");
	if (!fromNode.isTextual() || !toNode.isTextual()) {
		String message = "Incorrect parameters in replace, 'from' and 'to' param values should both be strings";
		log.error(message);
		return (v) -> message;
	}
	String from = fromNode.asText();
	String to = toNode.asText();

	return s -> s.replaceAll(from, to);

}



@Produces @IntoMap @Named("objectToString")
@StringKey("to-string")
Filter<Object, String> toString_() {
	return (o) -> o.toString();
}



@Produces @IntoMap @Named("objectToObject")
@StringKey("identity")
 Filter<Object, Object> iobjectIdentity() {
	return o -> o;
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

