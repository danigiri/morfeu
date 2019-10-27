// SVG PREVIEW CONTROL MODULE . JAVA

package cat.calidos.morfeu.control.injection;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.MorfeuServlet;
import cat.calidos.morfeu.control.SVGPreviewGETControl;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import cat.calidos.morfeu.webapp.GenericHttpServlet;

/** Controller module to generatie dynamic previews (SVG and TXT supported)
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class PreviewControlModule {

protected final static Logger log = LoggerFactory.getLogger(PreviewControlModule.class);

private static final String HEADER_PARAM = "__header";	// this is used as the SVG header
private static final Pattern colorRegexp = Pattern.compile("^[0-9a-fA-F]{6}$");


@Provides @IntoMap @Named("GET")
@StringKey("/preview/svg/(.+)")
public static BiFunction<List<String>, Map<String, String>, String> getContentSVG() {

	return (pathElems, params) -> {

		String resourcesPrefix = params.get(MorfeuServlet.RESOURCES_PREFIX);
		String path = pathElems.get(1);		// normalised already
		Optional<String> header = PreviewControlModule.extractHeaderFrom(params);

		params = GenericHttpServlet.removeInternalHeaders(params);	// remove all __* we do not want as a param

		return new SVGPreviewGETControl(resourcesPrefix, path, header, params).processRequest();

	};

}


@Provides @IntoMap @Named("GET")
@StringKey("/preview/html/(.*)")
public static BiFunction<List<String>, Map<String, String>, String> getContentHTML() {

	return (pathElems, params) -> {

		String template = "<div class=\"card\">\n" + 
				"				<div class=\"card-body html-preview\" style=\"background-color: #{{v.color}}\">\n" + 
				"					<h4 class=\"card-title html-preview-title\">{{v.path}}</h4>\n" + 
				"				</div>\n" + 
				"			</div>";

		String path = pathElems.get(1);		// normalised already
		String color = params.get("color");
		color = color!=null && colorRegexp.matcher(color).matches() ? color : "ff0000";
		Map<String, String> values = MorfeuUtils.paramStringMap("path", path, "color", color);

		return DaggerViewComponent.builder().withTemplate(template).withValue(values).build().render();

	};

}

@Provides @IntoMap @Named("POST")
@StringKey("/preview/html/(.*)")
public static BiFunction<List<String>, Map<String, String>, String> getContentHTMLPOST() {
	return (pathElems, params) -> {
		return PreviewControlModule.getContentHTML().apply(pathElems, params);
	};
}

@Provides @IntoMap @Named("Content-Type")
@StringKey("/preview/svg/(.+)")
public static String contentTypeSVG(@Named("Path") String path) {
	return ControlComponent.SVG;
}


@Provides @IntoMap @Named("Content-Type")
@StringKey("/preview/html/(.*)")
public static String contentTypeHTML(@Named("Path") String path) {
	return ControlComponent.TEXT;
}


private static Optional<String> extractHeaderFrom(Map<String, String> params) {

	Optional<String> header =  Optional.empty();
	if (params.containsKey(HEADER_PARAM)) {
		String param = params.get(HEADER_PARAM);
		header = Optional.ofNullable(param);
	}

	return header;

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