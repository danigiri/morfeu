// FILTER MODULE . JAVA

package cat.calidos.morfeu.webapp.injection;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import cat.calidos.morfeu.problems.MorfeuRuntimeException;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class HttpFilterModule {

protected final static Logger log = LoggerFactory.getLogger(HttpFilterModule.class);

public static final int IDENTITY_INDEX = -1;

@Produces
public static boolean process(	@Named("PreFiltersList") List<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> prevFilter,
								@Named("PostFiltersList") List<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> postFilter,
								HttpServletRequest request,
								HttpServletResponse response,
								FilterChain chain)
		throws MorfeuRuntimeException {

	boolean completed_ = !prevFilter
			.stream()
			.filter(f -> !f.apply(request, response))
			.findFirst()
			.isPresent();

	if (completed_) {
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			throw new MorfeuRuntimeException("Had a problem running downstream filters", e);
		}
		completed_ = !postFilter
				.stream()
				.filter(f -> !f.apply(request, response))
				.findFirst()
				.isPresent();
	}

	return completed_; // we return true if we completed and never stopped in this local filter
						// chaing

}


@Produces @Named("PreFiltersList")
public static List<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> preFilters(@Named("PreFilters") Map<Integer, BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> filters) {
	return filters
			.keySet()
			.stream()
			.sorted()
			.skip(1)
			.map(filters::get)
			.collect(Collectors.toList());
}


@Produces @Named("PostFiltersList")
public static List<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> postFilters(@Named("PostFilters") Map<Integer, BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> filters) {
	return filters
			.keySet()
			.stream()
			.sorted()
			.skip(1)
			.map(filters::get)
			.collect(Collectors.toList());
}


// identity filter, it will be skipped, but this means we have a non-empty filter pre list
@Produces @IntoMap @Named("PreFilters") @IntKey(IDENTITY_INDEX)
public static BiFunction<HttpServletRequest, HttpServletResponse, Boolean> identity() {
	return (req,
			resp) -> true;
}


@Produces @IntoMap @Named("PostFilters") @IntKey(IDENTITY_INDEX)
public static BiFunction<HttpServletRequest, HttpServletResponse, Boolean> identityPost() {
	return HttpFilterModule.identity();
}

}

/*
 * Copyright 2019 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
