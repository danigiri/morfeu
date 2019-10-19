// FILTER MODULE . JAVA

package cat.calidos.morfeu.webapp.injection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class FilterModule {

protected final static Logger log = LoggerFactory.getLogger(FilterModule.class);

public static final int IDENTITY_INDEX = -1;

private boolean handled = false;


@Provides
boolean process(List<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> filterList,
				@Named("InputRequest") HttpServletRequest request,
				@Named("InputResponse") HttpServletResponse response) {

	boolean continue_= true;

	Iterator<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> iterator = filterList.iterator();
	while (continue_ && iterator.hasNext()) {
		continue_ = iterator.next().apply(request, response);
	}

	
	
	handled = true;	// now we can get the request and response objects, even if we stop handling this is valid

	return continue_;
}


@Provides
HttpServletRequest request(@Named("InputRequest") HttpServletRequest request) {

	if (!handled) {
		throw new IllegalStateException("Cannot get request of an unprocessed filter");
	}

	return request;

}


@Provides
HttpServletResponse response(@Named("InputResponse") HttpServletResponse response) {

	if (!handled) {
		throw new IllegalStateException("Cannot get response of an unprocessed filter");
	}

	return response;

}


@Provides
List<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> filterList(
		Map<Integer, BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> filters
		) {
	return filters.keySet().stream().sorted().skip(1).map(filters::get).collect(Collectors.toList());
}


// identity filter, it will be skipped, but this means we have a non-empty filter list
@Provides @IntoMap
@IntKey(IDENTITY_INDEX)
public static BiFunction<HttpServletRequest, HttpServletResponse, Boolean> identity() {
	return (req, resp) -> true;
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

