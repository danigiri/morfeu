// CLIENT EVENT CONTROL MODULE . JAVA

package cat.calidos.morfeu.control.injection;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.inject.Named;

import cat.calidos.morfeu.webapp.injection.ControlComponent;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ClientEventControlModule {


@Provides @IntoMap @Named("GET")
@StringKey("/events/([^/\\?]+)")
public static BiFunction<List<String>, Map<String, String>, String> event() {
	return (pathElems, params) ->  "{\"result\":\""+pathElems.get(1)+"\", \"desc\":\""+paramsToString(params)+"\"}";
}


@Provides @IntoMap @Named("Content-Type")
@StringKey("/events/([^/\\?]+)")
public static String contentType() {
	return ControlComponent.JSON;
}


private static String paramsToString(Map<String, String> params) {

	StringBuilder out = new StringBuilder();
	params.keySet().stream().filter(k -> !k.startsWith("__")).forEach(k -> out.append(k+"="+params.get(k)+","));

	return out.toString();

}


}

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
