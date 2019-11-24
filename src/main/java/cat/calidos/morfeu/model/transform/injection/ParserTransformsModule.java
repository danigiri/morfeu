package cat.calidos.morfeu.model.transform.injection;

import java.io.IOException;

import javax.inject.Named;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import cat.calidos.morfeu.model.transform.Transform;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.utils.injection.MapperModule;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule(includes=MapperModule.class)
public class ParserTransformsModule {


@Produces @IntoMap @Named("stringToString")
@StringKey("yaml-to-json")
Transform<String, String> yamlToJSON(ObjectMapper jsonMapper, YAMLMapper yamlMapper) {
	return yaml -> {
		try {

			// we are using the dirty trick of converting to YAML and then converting to JSON, works ^^
			return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(yamlMapper.readTree(yaml));

		} catch (IOException e) {
			throw new TransformException("Problem when transforming yaml", e);
		}
	};
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

