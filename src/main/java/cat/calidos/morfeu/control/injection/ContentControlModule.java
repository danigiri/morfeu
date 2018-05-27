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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

import cat.calidos.morfeu.control.ContentGETControl;
import cat.calidos.morfeu.control.ContentPOSTControl;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.webapp.MorfeuServlet;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ContentControlModule {

protected final static Logger log = LoggerFactory.getLogger(ContentControlModule.class);

@Provides @IntoMap 
@StringKey("/content/(.+)")
public static BiFunction<List<String>, Map<String, String>, String> getContentControl() {

	return (pathElems, params) -> {

				String resourcesPrefix = params.get(MorfeuServlet.RESOURCES_PREFIX);
				String path = pathElems.get(1);		// normalised already
				String modelPath = params.get("model");
				log.trace("ContentControlModule::contentControl [{}]{}, model: {}", resourcesPrefix, path, modelPath);

				return new ContentGETControl(resourcesPrefix, path, modelPath).processRequest();

	};
}


@Provides @IntoMap
@StringKey("POST:/content/(.+)")
public static BiFunction<List<String>, Map<String, String>, String> saveContentControl() {
	
	return (pathElems, params) -> {

		String resourcesPrefix = params.get(MorfeuServlet.RESOURCES_PREFIX);
		String path = pathElems.get(1);		// normalised already
		String modelPath = params.get("model");
		String content = params.get(MorfeuServlet.POST_VALUE);
		
		return new ContentPOSTControl(resourcesPrefix, path, content, Optional.empty(), modelPath).processRequest();
		
	};
}
}
