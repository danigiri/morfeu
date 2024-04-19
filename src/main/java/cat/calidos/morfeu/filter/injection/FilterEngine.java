package cat.calidos.morfeu.filter.injection;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.filter.Filter;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.Pair;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class FilterEngine<X,Y> {

private static final String PARAM_DELIMITER = ";";
private static final String PARAM_ESCAPE = "\\\\;";
private static final String PARAM_PLACEHOLDER = "___PLACEHOLDER_FOR_SEMICOLON_32141234t6453687923457";

private static final int X_TO_X_STATE = 0;
private static final int X_TO_Y_STATE = 1;


public static List<String> parseFilters(String requestedFilters) {

	LinkedList<String> transforms = new LinkedList<String>();

	if (requestedFilters.isEmpty()) {
		return transforms;
	}

	String escapedFilters = requestedFilters.replaceAll(PARAM_ESCAPE, PARAM_PLACEHOLDER);
	String[] split = escapedFilters.split(PARAM_DELIMITER);
	for (String t : split) {
		String finalTransform = t.replace(PARAM_PLACEHOLDER, PARAM_DELIMITER);
		transforms.add(finalTransform.trim());	// ensure no trailing whitespace
	}

	return transforms;

}


public static JsonNode parseParametersFrom(String f) throws ConfigurationException {
	String paramString = hasParameters(f) ? f.substring(beginningOfParameters(f)) : "{}";
	try {
		return DaggerJSONParserComponent.builder().from(paramString).build().json().get();
	} catch (InterruptedException | ExecutionException | ParsingException e) {
		throw new ConfigurationException("Execution of parsing parameters of '"+f+"' did not go well", e);
	}
}


public static String nameFromFilter(String filter) throws ConfigurationException {

	String filterName = filter;
	if (hasParameters(filterName)) {

		int paramsIndex = beginningOfParameters(filterName);
		int endOfParamsIndex = endOfParameters(filterName);
		if (paramsIndex==-1 || endOfParamsIndex==-1) {
			throw new ConfigurationException("Operation '"+filterName+"' not parsing correctly");
		}
		filterName = filterName.substring(0, paramsIndex);

	}

	return filterName;

}


public Filter<X, X> xToX(List<String> filters, 
							Map<String, Filter<X, X>> xToXFilters,
							Map<String, Filter<X, Y>> xToYFilters,
							Map<String, Filter<Y, X>> yToXFilters,
							Map<String, Filter<Y, Y>> yToYFilters)
											throws ConfigurationException {

	Pair<Filter<X, X>, Filter<X, Y>> output = stateMachine(filters, xToXFilters, xToYFilters, yToXFilters, yToYFilters);
	Filter<X, X> xToX = output.getLeft();
	if (xToX==null) {
		throw new ConfigurationException("Sequence of filters "+filters+" did not end as X to X");
	}

	return xToX;

}


public Filter<X, Y> xToY(List<String> filters, 
							Map<String, Filter<X, X>> xToXFilters,
							Map<String, Filter<X, Y>> xToYFilters,
							Map<String, Filter<Y, X>> yToXFilters,
							Map<String, Filter<Y, Y>> yToYFilters) 
				throws ConfigurationException {

	if (filters.isEmpty()) {
		throw new ConfigurationException("Cannot make an X to Y filter without any defined filters");
	}

	Pair<Filter<X, X>, Filter<X, Y>> output = stateMachine(filters, xToXFilters, xToYFilters, yToXFilters, yToYFilters);
	Filter<X, Y> xToY = output.getRight();
	if (xToY==null) {
		throw new ConfigurationException("Sequence of filters "+filters+" did not end as X to Y");
	}

	return xToY;

}


public Filter<X, X> identity() {
	return (o) -> o;
}


private Pair<Filter<X, X>, Filter<X, Y>> stateMachine(List<String> transforms, 
															Map<String, Filter<X, X>> xToXFilters,
															Map<String, Filter<X, Y>> xToYFilters, 
															Map<String, Filter<Y, X>> yToXFilters,
															Map<String, Filter<Y, Y>> yToYFilters)
													throws ConfigurationException {

	// STATES: [string-string] and [string-object]
	// TRANSITIONS:
	// [string-string] -> [string-object], [string-object] -> [string-string], [string-string] ->
	// [string-string]
	int state = X_TO_X_STATE;
	Filter<X, X> identity = identity();
	Filter<X, X> xToX = identity; // initial state x to x
	Filter<X, Y> xToY = null;
	for (String f : transforms) {

		String filterName = nameFromFilter(f);
		switch (state) {
		case X_TO_X_STATE:

			if (xToXFilters.containsKey(filterName)) { // REMAIN AT X-X STATE
				Filter<X, X> filter = xToXFilters.get(filterName);
				xToX = xToX.andThen(filter);
			} else if (xToYFilters.containsKey(filterName)) { // TRANSITION TO X-Y STATE
				Filter<X, Y> filter = xToYFilters.get(filterName);
				xToY = xToX.andThen(filter); // transform.compose(stringIdentity);
				state = X_TO_Y_STATE;
			} else {
				throw new ConfigurationException("Broken filter: '" + filterName + "' cannot transition from X state");
			}
			break;

		case X_TO_Y_STATE:

			if (yToYFilters.containsKey(filterName)) { // REMAIN AT X-Y STATE
				Filter<Y, Y> filter = yToYFilters.get(filterName);
				xToY = filter.compose(xToY);
			} else if (yToXFilters.containsKey(filterName)) { // TRANSITION TO X-X STATE
				Filter<Y, X> filter = yToXFilters.get(filterName);
				// stringIdentity = (s) -> s;
				xToX = xToY.andThen(filter);
				state = X_TO_X_STATE;
			} else {
				throw new ConfigurationException("Broken filter: '" + filterName + "' cannot transition from Y state");
			}
			break;
		}

	}

	if (state==X_TO_X_STATE) {	// we only care for the output state, not intermediate ones
		xToY = null;
	} else {
		xToX = null;
	}

	return new Pair<Filter<X, X>, Filter<X, Y>>(xToX, xToY);

}


private static boolean hasParameters(String filter) {
	return filter.contains("{") || filter.endsWith("}");
}

private static int endOfParameters(String filter) {
	return filter.lastIndexOf("}");
}


private static int beginningOfParameters(String filter) {
	return filter.indexOf("{");
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

