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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import javax.inject.Named;

import cat.calidos.morfeu.model.transform.Transform;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.ParsingException;
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
	return new HashMap<String, Transform<String, String>>(0);
}
@Produces Map<String, Transform<Object, String>> objectToStringTransforms() {
	
	HashMap<String, Transform<Object, String>> map = new HashMap<String, Transform<Object, String>>(0);
	map.put("content-to-xml", (json) -> DaggerViewComponent.builder()
										.withTemplate("templates/transform/content-json-to-xml.twig")
										.withValue(json)
										.build()
										.render()
	);
	
	return map;
}


@Produces 
Map<String, Transform<String, Object>> stringToObjectTransforms() {
	
	HashMap<String, Transform<String, Object>> map = new HashMap<String, Transform<String, Object>>(0);
	map.put("string-to-json", (content) -> DaggerJSONParserComponent.builder().from(content).build().json().get());
	
	return map;
}

@Produces Map<String, Transform<Object, Object>> objectToObjectTransforms() {
	return new HashMap<String, Transform<Object, Object>>(0);
}




@Produces
public static Transform<String, String> transform(List<String> transforms, 
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
		switch (state) {
		case STRING_STATE:

			if (stringToStringTransforms.containsKey(t)) {				// REMAIN AT STRING-STRING STATE
				Transform<String, String> transform = stringToStringTransforms.get(t);
				stringString = stringString.andThen(transform);
			} else if (stringToObjectTransforms.containsKey(t)) {			// TRANSITION TO STRING-OBJECT STATE
				Transform<String, Object> transform = stringToObjectTransforms.get(t);
				stringObject = stringString.andThen(transform); //transform.compose(stringIdentity);
				state = OBJECT_STATE;
			} else {
				throw new ConfigurationException("Broken transform: '"+t+"' cannot transition from string state");
			}
			break;

		case OBJECT_STATE:

			if (objectToObjectTransforms.containsKey(t)) {				// REMAIN AT STRING-OBJECT STATE
				Transform<Object, Object> transform = objectToObjectTransforms.get(t);
				stringObject = transform.compose(stringObject);
			} else if (objectToStringTransforms.containsKey(t)) {			// TRANSITION TO STRING-STRING STATE
				Transform<Object, String> transform = objectToStringTransforms.get(t);
				//stringIdentity = (s) -> s;
				stringString  = stringObject.andThen(transform);
				state = STRING_STATE;
			} else {
				throw new ConfigurationException("Broken transform: '"+t+"' cannot transition from obj state");
			}
			break;
		}
	}
	
	return stringString;

}


}
