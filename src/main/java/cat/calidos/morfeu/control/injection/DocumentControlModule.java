// DOCUMENT CONTROL MODULE . JAVA

package cat.calidos.morfeu.control.injection;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.DocumentGETControl;
import cat.calidos.morfeu.control.MorfeuServlet;
import cat.calidos.morfeu.webapp.injection.ControlComponent;

/** Controller module to show documents in JSON format
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class DocumentControlModule {

protected final static Logger log = LoggerFactory.getLogger(DocumentControlModule.class);


@Provides @IntoMap @Named("GET")
@StringKey("/documents/(.+)")
public static BiFunction<List<String>, Map<String, String>, String> getDocument() {

	return (pathElems, params) -> {

		String resourcesPrefix = params.get(MorfeuServlet.RESOURCES_PREFIX);
		String path = pathElems.get(1);		// normalised already
		log.trace("DocumentControlModule::getDocument '[{}]{}'", resourcesPrefix, path);

		return new DocumentGETControl(resourcesPrefix, path).processRequest();

	};

}


@Provides @IntoMap @Named("Content-Type")
@StringKey("/documents/(.+)")
public static String contentType() {
	return ControlComponent.JSON;
}


}

/*
 *    Copyright 2024 Daniel Giribet
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
