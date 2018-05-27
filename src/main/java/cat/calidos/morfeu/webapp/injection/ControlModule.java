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

package cat.calidos.morfeu.webapp.injection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Named;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ControlModule {

// Given the input path, the path elements, the explicit parameters, look for the control and run it on the data
@Provides
public static String process(@Named("Path") String path,
								Lazy<List<String>> pathElems,
								@Named("Params") Map<String, String> params,
								Optional<Pattern> matchedPathPattern,
								Map<String, BiFunction<List<String>, Map<String, String>, String>> controls) {
	return controls.get(matchedPathPattern.orElseThrow(() -> new UnsupportedOperationException("No matched "+path)).pattern())
					.apply(pathElems.get(), params);
}


// Do we match the input path with any of the controls? 
@Provides
public static boolean matches(Optional<Pattern> matchedPathPattern) {
	return matchedPathPattern.isPresent();
}


// given the list of paths and controls, compile the regexp of the paths
@Provides
Map<Pattern, BiFunction<List<String>, Map<String, String>, String>> compiledControls(
										Map<String, BiFunction<List<String>, Map<String, String>, String>> controls) {

	Map<Pattern, BiFunction<List<String>, Map<String, String>, String>> patternControls = 
			new HashMap<Pattern, BiFunction<List<String>, Map<String, String>, String>>(controls.size());
	controls.keySet().forEach(k -> patternControls.put(Pattern.compile(k), controls.get(k)));
	
	return patternControls;

}


@Provides
public static Optional<Pattern> matchedPathPattern(@Named("Path") String path, 
									Map<Pattern, BiFunction<List<String>, Map<String, String>, String>> controls) {
	return controls.keySet().stream().filter(p -> p.matcher(path).matches()).findFirst();
}


@Provides
List<String> pathElems(@Named("Path") String path, Optional<Pattern> matchedPathPattern) {

	ArrayList<String> pathElems = new ArrayList<String>();

	if (matchedPathPattern.isPresent()) {
		Matcher matcher = matchedPathPattern.get().matcher(path);
		matcher.matches();	// TODO: optimize so we only do the match once
		for (int i=0; i<=matcher.groupCount(); i++) {
			String elem = matcher.group(i);
			if (elem!=null) {
				pathElems.add(elem);
			}
		}
	}

	return pathElems;

}


}
