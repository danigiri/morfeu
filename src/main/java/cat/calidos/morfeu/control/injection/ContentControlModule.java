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
import java.util.function.BiFunction;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

import cat.calidos.morfeu.control.ContentGETControl;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ContentControlModule {

@Provides @IntoMap
@StringKey("/content/(.+)")
public static BiFunction<List<String>, Map<String, String>, String> contentController() {

	return (pathElems, params) -> {

		String path = pathElems.get(0);
		String modelPath = params.get("model");
		String resourcesPrefix = params.get("__resourcesPrefix");

		return new ContentGETControl(resourcesPrefix, path, modelPath).processRequest();

	};

}

}
