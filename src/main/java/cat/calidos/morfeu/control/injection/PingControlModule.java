// PING CONTROL MODULE . JAVA

package cat.calidos.morfeu.control.injection;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.inject.Named;
import javax.servlet.ServletContext;

import org.openjdk.tools.sjavac.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.webapp.GenericHttpServlet;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

/** Controller module for pinging the API, mainly for monitoring and testing
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class PingControlModule {

protected final static Logger log = LoggerFactory.getLogger(PingControlModule.class);


@Provides @IntoMap @Named("GET")
@StringKey("/ping/?(.+)?")
public static BiFunction<List<String>, Map<String, String>, String> ping() {
	return (pathElems, params) -> pathElems.size()>1 ? "OK "+pathElems.get(1) : "OK";
}


@Provides @IntoMap @Named("GET")
@StringKey("/counter/?(reset)?")
public static BiFunction<List<String>, Map<String, String>, String> counter(ServletContext context) {
	return (pathElems, params) -> {

		Integer counter = (Integer)context.getAttribute("counter");
		context.setAttribute("counter", ++counter);

		return counter.toString();

	};
}


@Provides @IntoMap @Named("POST")
@StringKey("/ping/?(.+)?")
public static BiFunction<List<String>, Map<String, String>, String> pingPost() {
	return (pathElems, params) -> "OK "+GenericHttpServlet.removeInternalHeaders(params);
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
