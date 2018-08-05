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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ControlModule {

protected final static Logger log = LoggerFactory.getLogger(ControlModule.class);

// Given the input path, the path elements, the explicit parameters, look for the control and run it on the data
@Provides @Named("Content")
public static String process(@Named("Path") String path,
								@Named("Method") String method,
								Lazy<List<String>> pathElems,
								@Named("Params") Map<String, String> params,
								Optional<Pattern> matchedPath,
								@Named("GET") Map<String, BiFunction<List<String>, Map<String, String>, String>> get,
								@Named("POST") Map<String, BiFunction<List<String>, Map<String, String>, String>> post
								) {
	 Map<String, BiFunction<List<String>, Map<String, String>, String>> controls = method.equals(ControlComponent.GET) ? 
			 																		get : post;

	return controls.get(matchedPath.orElseThrow(() -> new UnsupportedOperationException("No matched "+path)).pattern())
				.apply(pathElems.get(), params);

}


// return the specified content type
@Provides @Named("Content-Type")
public static String contentType(@Named("Content-Type") Map<String, String> contentTypes, 
									Optional<Pattern> matchedPath) {
	return matchedPath.isPresent() ? contentTypes.get(matchedPath.get().pattern()) : ControlComponent.TEXT;
}


// Do we match the input path with any of the controls? 
@Provides
public static boolean matches(Optional<Pattern> matchedPathPattern) {
	return matchedPathPattern.isPresent();
}


// given the list of paths and controls, compile the regexp of the paths for the specified method
// TODO: avoid recompiling the regexps all the time
@Provides 
Map<Pattern, BiFunction<List<String>, Map<String, String>, String>> compiledControls(
						@Named("Method") String method,
						@Named("GET") Lazy<Map<String, BiFunction<List<String>, Map<String, String>, String>>> get,
						@Named("POST") Lazy<Map<String, BiFunction<List<String>, Map<String, String>, String>>> post
						) {

	// log.trace("Compiling path regexps for method {}", method);
	Map<String, BiFunction<List<String>, Map<String, String>, String>> controls =  method.equals(ControlComponent.GET) 
																					? get.get(): post.get();
	Map<Pattern, BiFunction<List<String>, Map<String, String>, String>> patternControls = 
			new HashMap<Pattern, BiFunction<List<String>, Map<String, String>, String>>(controls.size());
	controls.keySet().forEach(k -> patternControls.put(Pattern.compile(k), controls.get(k)));
	// log.trace("Compiled path regexps ({})", patternControls.size());

	return patternControls;

}


// given a map of compiled regexp patterns, return the first matched pattern 
@Provides
public static Optional<Pattern> matchedPathPattern(@Named("Path") String path, 
									Map<Pattern, BiFunction<List<String>, Map<String, String>, String>> controls) {
	return controls.keySet().stream().filter(p -> p.matcher(path).matches()).findFirst();
}


// 
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
