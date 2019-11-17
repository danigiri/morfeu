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

package cat.calidos.morfeu.model.transform.injection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.transform.Transform;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.transform.injection.DaggerContentConverterComponent;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import dagger.Module;
import dagger.multibindings.ElementsIntoSet;
import dagger.multibindings.IntoMap;
import dagger.multibindings.Multibinds;
import dagger.multibindings.StringKey;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class TransformModule {

private static final int STRING_STATE = 0;
private static final int OBJECT_STATE = 1;


@Produces
public static List<String> parseTransforms(@Named("Transforms") String requestedTransforms) {
	
	LinkedList<String> transforms = new LinkedList<String>();
	String[] split = requestedTransforms.split(",");
	for (String t : split) {
		transforms.add(t);
	}

	return transforms;

}

@Produces 
Map<String, Transform<String, String>> stringToStringTransforms() {
	
	HashMap<String, Transform<String, String>> map = new HashMap<String, Transform<String, String>>(1);
	//map.put("str-replace", value)
	return map;

}


@Produces Map<String, Transform<Object, String>> objectToStringTransforms() {

	HashMap<String, Transform<Object, String>> map = new HashMap<String, Transform<Object, String>>(1);
	map.put("content-to-xml", (json) -> DaggerContentConverterComponent.builder().from((JsonNode)json).build().xml());
	map.put("content-to-yaml", (values) -> DaggerViewComponent.builder()
											.withTemplatePath("templates/transform/content-to-yaml.twig")
											.withValue(values)
											.build()
											.render());

	return map;
}


@Produces 
Map<String, Transform<String, Object>> stringToObjectTransforms() {
	
	HashMap<String, Transform<String, Object>> map = new HashMap<String, Transform<String, Object>>(1);
	map.put("string-to-json", (content) -> DaggerJSONParserComponent.builder().from(content).build().json().get());
	
	return map;
}


@Produces Map<String, Transform<Object, Object>> objectToObjectTransforms() {
	return new HashMap<String, Transform<Object, Object>>(0);
}


@Produces
public static Transform<String, Object> stringToObject(List<String> transforms, 
												Map<String, Transform<String, String>> stringToStringTransforms,
												Map<String, Transform<Object, String>> objectToStringTransforms,
												Map<String, Transform<String, Object>> stringToObjectTransforms,
												Map<String, Transform<Object, Object>> objectToObjectTransforms) 
														throws ConfigurationException {
	int state = STRING_STATE;
	Transform<String, Object> stringObjectIdentity = (s) -> s;
	Transform<String, Object> stringObject = stringObjectIdentity;

	return stringObject;

}


@Produces
public static Transform<Object, Object> objectToObject(List<String> transforms, 
												Map<String, Transform<String, String>> stringToStringTransforms,
												Map<String, Transform<Object, String>> objectToStringTransforms,
												Map<String, Transform<String, Object>> stringToObjectTransforms,
												Map<String, Transform<Object, Object>> objectToObjectTransforms) 
														throws ConfigurationException {
	int state = STRING_STATE;
	Transform<Object, Object> objectObjectIdentity = (s) -> s;
	Transform<Object, Object> objectObject = objectObjectIdentity;

	return objectObject;

}


@Produces
public static Transform<String, String> stringToString(List<String> transforms, 
												Map<String, Transform<String, String>> stringToStringTransforms,
												Map<String, Transform<Object, String>> objectToStringTransforms,
												Map<String, Transform<String, Object>> stringToObjectTransforms,
												Map<String, Transform<Object, Object>> objectToObjectTransforms) 
														throws ConfigurationException {

	// STATES: [string-string] and [string-object]
	// TRANSITIONS:
	// [string-string] -> [string-object], [string-object] -> [string-string], [string-string] -> [string-string]
	int state = STRING_STATE;
	Transform<String, String> stringIdentity = (s) -> s;
	Transform<String, String> stringString = stringIdentity;	// initial state is string
	Transform<String, Object> stringObject = null;

	for (String t : transforms) {

		String op = parseOperationNameFromTransform(t);
		Map<String, String> parameters = parseParametersFrom(t);	//TODO: use parameters to build this

		switch (state) {
		case STRING_STATE:

			if (stringToStringTransforms.containsKey(op)) {				// REMAIN AT STRING-STRING STATE
				Transform<String, String> transform = stringToStringTransforms.get(op);
				stringString = stringString.andThen(transform);
			} else if (stringToObjectTransforms.containsKey(op)) {			// TRANSITION TO STRING-OBJECT STATE
				Transform<String, Object> transform = stringToObjectTransforms.get(t);
				stringObject = stringString.andThen(transform); //transform.compose(stringIdentity);
				state = OBJECT_STATE;
			} else {
				throw new ConfigurationException("Broken transform: '"+op+"' cannot transition from string state");
			}
			break;

		case OBJECT_STATE:

			if (objectToObjectTransforms.containsKey(op)) {				// REMAIN AT STRING-OBJECT STATE
				Transform<Object, Object> transform = objectToObjectTransforms.get(op);
				stringObject = transform.compose(stringObject);
			} else if (objectToStringTransforms.containsKey(op)) {			// TRANSITION TO STRING-STRING STATE
				Transform<Object, String> transform = objectToStringTransforms.get(op);
				//stringIdentity = (s) -> s;
				stringString  = stringObject.andThen(transform);
				state = STRING_STATE;
			} else {
				throw new ConfigurationException("Broken transform: '"+op+"' cannot transition from obj state");
			}
			break;
		}
	}

	return stringString;

}


private static String parseOperationNameFromTransform(String t) throws ConfigurationException {

	String op = t;
	if (hasParameters(op)) {

		int paramsIndex = beginningOfParameters(op);
		if (paramsIndex==-1) {
			throw new ConfigurationException("Operation '"+op+"' not parsing correctly");
		}
		op = op.substring(0, paramsIndex);

	}
	
	return op;
	
}


private static boolean hasParameters(String t) {
	return t.endsWith("{");
}


private static int beginningOfParameters(String t) {
	return t.lastIndexOf("{");
}


private static Map<String, String> parseParametersFrom(String t) throws ConfigurationException {

	if (!hasParameters(t)) {
		return new HashMap<String, String>(0);
	}
	
	String paramString = t.substring(beginningOfParameters(t));
	JsonNode paramsJSON = null;
	try {
		paramsJSON = DaggerJSONParserComponent.builder().from(paramString).build().json().get();
	} catch (InterruptedException | ExecutionException | ParsingException e) {
		throw new ConfigurationException("Execution of parsing parameters of '"+t+"' did not go well", e);
	}

	Iterator<Entry<String, JsonNode>> fields = paramsJSON.fields();
	
	return StreamSupport.stream(Spliterators.spliteratorUnknownSize(fields, 0), false)
							.collect(Collectors.toMap(Entry::getKey, e -> e.getValue().toString()));

}

}
