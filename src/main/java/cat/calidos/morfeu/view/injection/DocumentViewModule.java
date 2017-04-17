/*
 *    Copyright 2017 Daniel Giribet
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

package cat.calidos.morfeu.view.injection;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Provider;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;

import dagger.Module;
import dagger.Provides;
import dagger.producers.Producer;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class DocumentViewModule {


@Provides
public static String renderTemplate(JtwigTemplate template, JtwigModel model) {
	return template.render(model);
	
}


@Provides
public Map<String, Object> values(@Named("problem") String problem, @Named("value") Object v) {
	
	Map<String, Object> value = new HashMap<String, Object>(1);
	if (problem!=null) {
		value.put("problem", problem);
	} else {
		value.put("v", v);
	}
	
	return value;

}


@Provides
public static EnvironmentConfiguration defaultConfiguration() {
	
	return EnvironmentConfigurationBuilder.configuration()
			.parser()
			.syntax()
				.withStartCode("$%").withEndCode("%$")
				.withStartOutput("$[").withEndOutput("]$")
				.withStartComment("$#").withEndComment("#$")
			.and()
		.and()
		.build();
	
}


@Provides
public static JtwigTemplate produceTemplate(@Named("templatePath") String path, EnvironmentConfiguration config) {
	return JtwigTemplate.classpathTemplate(path, config);
}


@Provides
public static JtwigModel produceJTwigModel(Map<String, Object> values) {
	return JtwigModel.newModel(values);
}


}