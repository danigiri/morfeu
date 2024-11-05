// PING CONTROL MODULE . JAVA

package cat.calidos.morfeu.control.injection;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.annotation.Nullable;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.webapp.GenericHttpServlet;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import jakarta.servlet.ServletContext;


/**
 * Controller module for pinging the API, mainly for monitoring and testing
 * 
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class PingControlModule {

protected final static Logger log = LoggerFactory.getLogger(PingControlModule.class);

@Provides @IntoMap @Named("GET") @StringKey("/ping/?(.+)?")
public static BiFunction<List<String>, Map<String, String>, String> ping() {
	return (pathElems,
			params) -> pathElems.size() > 1 ? "OK " + pathElems.get(1) : "OK";
}


@Provides @IntoMap @Named("GET") @StringKey("/counter/?(reset)?")
public static BiFunction<List<String>, Map<String, String>, String> counter(@Nullable ServletContext context) {
	return (pathElems,
			params) -> {
		Integer counter = context != null ? (Integer) context.getAttribute("counter") : 0;
		context.setAttribute("counter", ++counter);

		return counter.toString();
	};
}


@Provides @IntoMap @Named("POST") @StringKey("/ping/?(.+)?")
public static BiFunction<List<String>, Map<String, String>, String> pingPost() {
	return (pathElems,
			params) -> "OK " + GenericHttpServlet.removeInternalHeaders(params);
}

}

/*
 * Copyright 2024 Daniel Giribet
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
