/*
 * Copyright 2017 Daniel Giribet
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

package cat.calidos.morfeu.utils.injection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class MapperModule {

protected final static Logger log = LoggerFactory.getLogger(MapperModule.class);

@Produces
public static ObjectMapper jsonMapper() {

	log.trace("[Producing ObjectMapper]");
	return new ObjectMapper(); // TODO: check if it is necessary to 'provide' default constructor
								// objects in Dagger2

}


@Produces
YAMLMapper yamlMapper() {
	return new YAMLMapper();
}

}
