/*
 * Copyright 2018 Daniel Giribet
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

package cat.calidos.morfeu.control.injection;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

import cat.calidos.morfeu.control.ContentGETControl;
import cat.calidos.morfeu.control.ContentSaveControl;
import cat.calidos.morfeu.control.MorfeuServlet;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import cat.calidos.morfeu.webapp.GenericHttpServlet;


/**
 * GET and POST for doucment content controller module
 * 
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ContentControlModule {

protected final static Logger log = LoggerFactory.getLogger(ContentControlModule.class);

@Provides @IntoMap @Named("GET") @StringKey("/content/(.+)")
public static BiFunction<List<String>, Map<String, String>, String> content() {

	return (pathElems,
			params) -> {

		String resourcesPrefix = params.get(MorfeuServlet.RESOURCES_PREFIX);
		String path = pathElems.get(1); // normalised already
		String modelPath = params.get("model");
		Optional<String> filters = Optional.ofNullable(params.get("filters"));
		log
				.trace(
						"ContentControlModule::content GET [{}]{}, model: {}",
						resourcesPrefix,
						path,
						modelPath);

		return new ContentGETControl(resourcesPrefix, path, filters, modelPath).processRequest();

	};

}


@Provides @IntoMap @Named("POST") @StringKey("/content/(.+)")
public static BiFunction<List<String>, Map<String, String>, String> postContent() {

	return (pathElems,
			params) -> {

		String resourcesPrefix = params.get(MorfeuServlet.RESOURCES_PREFIX);
		String path = pathElems.get(1); // normalised already
		String modelPath = params.get("model");
		String content = params.get(GenericHttpServlet.POST_VALUE);
		if (content == null || content.length() == 0) {
			log.warn("Empty POST content");
		}
		Optional<String> filters = Optional.ofNullable(params.get("filters"));
		log
				.trace(
						"ContentControlModule::content POST [{}]{}, model: {}",
						resourcesPrefix,
						path,
						modelPath);

		return new ContentSaveControl(resourcesPrefix, path, content, filters, modelPath)
				.processRequest();

	};

}


@Provides @IntoMap @Named("Content-Type") @StringKey("/content/(.+)")
public static String contentType() {
	return ControlComponent.JSON;
}

}
