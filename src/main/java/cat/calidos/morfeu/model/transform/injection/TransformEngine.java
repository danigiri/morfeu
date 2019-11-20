package cat.calidos.morfeu.model.transform.injection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.transform.Transform;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.Pair;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformEngine<X,Y> {

private static final int X_TO_X_STATE = 0;
private static final int X_TO_Y_STATE = 1;


public static List<String> parseTransforms(String requestedTransforms) {

	LinkedList<String> transforms = new LinkedList<String>();
	String[] split = requestedTransforms.split(",");
	for (String t : split) {
		transforms.add(t);
	}

	return transforms;

}


public Transform<X, X> xToX(List<String> transforms, 
							Map<String, Transform<X, X>> xToXTransforms,
							Map<String, Transform<X, Y>> xToYTransforms,
							Map<String, Transform<Y, X>> yToXTransforms,
							Map<String, Transform<Y, Y>> yToYTransforms)
											throws ConfigurationException {

	Pair<Transform<X, X>, Transform<X, Y>> output = stateMachine(transforms, 
																	xToXTransforms, 
																	xToYTransforms, 
																	yToXTransforms, 
																	yToYTransforms);
	Transform<X, X> xToX = output.getLeft();
	if (xToX==null) {
		throw new ConfigurationException("Sequence of transforms "+transforms+" did not end as X to X");
	}

	return xToX;

}


public Transform<X, Y> xToY(List<String> transforms, 
							Map<String, Transform<X, X>> xToXTransforms,
							Map<String, Transform<X, Y>> xToYTransforms,
							Map<String, Transform<Y, X>> yToXTransforms,
							Map<String, Transform<Y, Y>> yToYTransforms) 
				throws ConfigurationException {

	if (transforms.isEmpty()) {
		throw new ConfigurationException("Cannot make an X to Y transformation without any transforms");
	}

	Pair<Transform<X, X>, Transform<X, Y>> output = stateMachine(transforms, 
																		xToXTransforms, 
																		xToYTransforms, 
																		yToXTransforms, 
																		yToYTransforms);
	Transform<X, Y> xToY = output.getRight();
	if (xToY==null) {
		throw new ConfigurationException("Sequence of transforms "+transforms+" did not end as X to Y");
	}

	return xToY;

}


public Transform<X, X> identity() {
	return (o) -> o;
}


private Pair<Transform<X, X>, Transform<X, Y>> stateMachine(List<String> transforms, 
															Map<String, Transform<X, X>> xToXTransforms,
															Map<String, Transform<X, Y>> xToYTransforms, 
															Map<String, Transform<Y, X>> yToXTransforms,
															Map<String, Transform<Y, Y>> yToYTransforms)
													throws ConfigurationException {

	// STATES: [string-string] and [string-object]
	// TRANSITIONS:
	// [string-string] -> [string-object], [string-object] -> [string-string], [string-string] ->
	// [string-string]
	int state = X_TO_X_STATE;
	Transform<X, X> identity = identity();
	Transform<X, X> xToX = identity; // initial state x to x
	Transform<X, Y> xToY = null;
	for (String t : transforms) {

		String op = parseOperationNameFromTransform(t);
		Map<String, String> parameters = parseParametersFrom(t);	// TODO: use parameters to build this

		switch (state) {
		case X_TO_X_STATE:

			if (xToXTransforms.containsKey(op)) { // REMAIN AT X-X STATE
				Transform<X, X> transform = xToXTransforms.get(op);
				xToX = xToX.andThen(transform);
			} else if (xToYTransforms.containsKey(op)) { // TRANSITION TO X-Y STATE
				Transform<X, Y> transform = xToYTransforms.get(t);
				xToY = xToX.andThen(transform); // transform.compose(stringIdentity);
				state = X_TO_Y_STATE;
			} else {
				throw new ConfigurationException("Broken transform: '" + op + "' cannot transition from X state");
			}
			break;

		case X_TO_Y_STATE:

			if (yToYTransforms.containsKey(op)) { // REMAIN AT X-Y STATE
				Transform<Y, Y> transform = yToYTransforms.get(op);
				xToY = transform.compose(xToY);
			} else if (yToXTransforms.containsKey(op)) { // TRANSITION TO X-X STATE
				Transform<Y, X> transform = yToXTransforms.get(op);
				// stringIdentity = (s) -> s;
				xToX = xToY.andThen(transform);
				state = X_TO_X_STATE;
			} else {
				throw new ConfigurationException("Broken transform: '" + op + "' cannot transition from Y state");
			}
			break;
		}

	}

	if (state==X_TO_X_STATE) {	// we only care for the output state, not intermediate ones
		xToY = null;
	} else {
		xToX = null;
	}

	return new Pair<Transform<X, X>, Transform<X, Y>>(xToX, xToY);

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

