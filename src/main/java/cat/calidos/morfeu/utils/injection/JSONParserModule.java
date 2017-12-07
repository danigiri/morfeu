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

package cat.calidos.morfeu.utils.injection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cat.calidos.morfeu.problems.ParsingException;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule(includes=JSONMapperModule.class)
public class JSONParserModule {

protected final static Logger log = LoggerFactory.getLogger(JSONParserModule.class);


@Produces
public static JsonNode produceJSONNode(String content, ObjectMapper mapper) throws ParsingException {

	try {

		return mapper.readTree(content);

	} catch (Exception e) {
		String snippet = content.substring(0,Math.min(10, content.length()));
		log.error("Cound not process input '{}', as valid JSON", snippet);
		throw new ParsingException("Cound not process input '"+snippet+"', as valid JSON", e);
	}

}

}
