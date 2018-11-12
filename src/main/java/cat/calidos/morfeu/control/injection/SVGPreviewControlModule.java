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

package cat.calidos.morfeu.control.injection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.MorfeuServlet;
import cat.calidos.morfeu.control.SVGPreviewGETControl;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import cat.calidos.morfeu.webapp.GenericHttpServlet;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

/** Controller module to generatie dynamic previews (SVG and TXT supported)
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class SVGPreviewControlModule {

protected final static Logger log = LoggerFactory.getLogger(SVGPreviewControlModule.class);

private static final String HEADER_PARAM = "__header";	// this is used as the SVG header
private static final String INTERNAL_PARAM_PREFIX = "__";	// internal params start with this


@Provides @IntoMap @Named("GET")
@StringKey("/preview/(.+)")
public static BiFunction<List<String>, Map<String, String>, String> getContent() {

	return (pathElems, params) -> {

		String resourcesPrefix = params.get(MorfeuServlet.RESOURCES_PREFIX);
		String path = pathElems.get(1);		// normalised already
		Optional<String> header = SVGPreviewControlModule.extractHeaderFrom(params);

		params = removeInternalHeaders(params);	// remove all __* we do not want as a param

		return new SVGPreviewGETControl(resourcesPrefix, path, header, params).processRequest();

	};
}


@Provides @IntoMap @Named("Content-Type")
@StringKey("/preview/(.+)")
public static String contentType(@Named("Path") String path) {
	return path.endsWith("svg") ? ControlComponent.SVG : ControlComponent.TEXT;
}


private static Optional<String> extractHeaderFrom(Map<String, String> params) {

	Optional<String> header =  Optional.empty();
	if (params.containsKey(HEADER_PARAM)) {
		String param = params.get(HEADER_PARAM);
		header = Optional.ofNullable(param);
	}

	return header;

}


private static Map<String, String> removeInternalHeaders(Map<String, String> params) {
	return params.entrySet()
					.stream()
					.filter(k -> !k.getKey().startsWith(INTERNAL_PARAM_PREFIX))
					.collect(Collectors.toMap(Map.Entry::getKey,  Map.Entry::getValue));
}



}
