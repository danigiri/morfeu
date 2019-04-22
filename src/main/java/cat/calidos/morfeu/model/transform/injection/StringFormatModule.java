// STRING FORMAT MODULE . JAVA

package cat.calidos.morfeu.model.transform.injection;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import dagger.producers.Producer;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class StringFormatModule {

protected final static Logger log = LoggerFactory.getLogger(StringFormatModule.class);

@Produces @Named("EffectiveContent")
public static String produceEffectiveContent(@Named("DestinationContentURI") URI uri, 
											@Named("YAMLContent") Producer<String> yamlProducer,
											@Named("JSONContent") Producer<String> jsonProducer,
											@Named("Content") Producer<String> contentProducer) 
						throws TransformException {
	
	String name = FilenameUtils.getName(uri.getPath());
	String content;
	try {
		if (name.endsWith("yaml")) {
			content = yamlProducer.get().get();
		} else if (name.endsWith("json")) {
			content = jsonProducer.get().get();
		} else {
			content = contentProducer.get().get(); // no transformation required
		}
	} catch (InterruptedException | ExecutionException e) {
		log.error("Could not get effective content for '{}' ({})", uri, e);
		throw new TransformException("Problem when getting effective content to save '"+uri+"'", e);
	}

	return content;
	
}


@Produces @Named("YAMLContent")
public static String jsonProducer(Composite<Cell> contentRootCells, Model model) {

	Map<String, Object> values = new HashMap<String, Object>(2);
	values.put("cells", contentRootCells.child(0).asComplex().children().asList());	// skip virtual root
	values.put("model", model);

	return DaggerViewComponent.builder()
								.withTemplatePath("templates/transform/content-to-yaml.twig")
								.withValue(values)
								.build()
								.render();
}


@Produces @Named("JSONContent")
public static String yamlProducer(@Named("DestinationContentURI") URI uri, 
									@Named("YAMLContent") String yaml, 
									ObjectMapper jsonMapper, 
									YAMLMapper yamlMapper) 
						throws TransformException {

	try {

		// we are using the dirty trick of converting to YAML and then converting to JSON, works ^^
		return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(yamlMapper.readTree(yaml));

	} catch (IOException e) {
		throw new TransformException("Problem when transforming effective content to save '"+uri+"'", e);
	}
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
