// SNIPPETS CONTROL MODULE . JAVA

package cat.calidos.morfeu.control.injection;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.MorfeuServlet;
import cat.calidos.morfeu.control.SnippetGETControl;
import cat.calidos.morfeu.problems.MorfeuException;
import cat.calidos.morfeu.webapp.injection.WebappControlComponent;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;


/**
 * Controller module that provides snippets, which are not validated as they may lack context
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class SnippetsControlModule {

protected final static Logger log = LoggerFactory.getLogger(SnippetsControlModule.class);

@Provides @IntoMap @Named("GET") @StringKey("/snippets/(.+)")
public static BiFunction<List<String>, Map<String, String>, String> snippet() {

	return (pathElems,
			params) -> {

		String pref = params.get(MorfeuServlet.RESOURCES_PREFIX);
		String path = pathElems.get(1); // normalised already
		String modelPath = params.get("model");
		log
				.trace(
						"SnippetsControlModule::snippet [{}]{}, model: {}, cell-model: {}",
						pref,
						path,
						modelPath);
		try {
			return new SnippetGETControl(pref, path, modelPath).doWork();
		} catch (MorfeuException e) {
			throw DaggerMorfeuExceptionTranslatorComponent
					.builder()
					.from(e)
					.build()
					.toWebappException();
		}
	};

}


@Provides @IntoMap @Named("Content-Type") @StringKey("/snippets/(.+)")
public static String contentType() {
	return WebappControlComponent.JSON;
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
