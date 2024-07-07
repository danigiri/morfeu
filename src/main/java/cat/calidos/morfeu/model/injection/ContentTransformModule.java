// CONTENT TRANSFORM MODULE . JAVA

package cat.calidos.morfeu.model.injection;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;
import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import cat.calidos.morfeu.filter.injection.DaggerFilterComponent;
import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import dagger.producers.Producer;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/** Given in memory content (the model and cells) of a document, serialise to a specific format given the URI extension
* 	or the supplied filters
* 	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class ContentTransformModule {

protected final static Logger log = LoggerFactory.getLogger(ContentTransformModule.class);

@Produces @Named("EffectiveContent")
public static String effectiveContent(@Named("DestinationContentURI") URI uri,
										@Named("YAMLContent") Producer<String> yamlProducer,
										@Named("JSONContent") Producer<String> jsonProducer,
										@Named("FilterContent") Producer<String> filtersProducer,
										@Named("Content") Producer<String> contentProducer,
										@Nullable @Named("Filters") String filters)
						throws TransformException {

	String name = FilenameUtils.getName(uri.getPath());
	String content;
	try {
		if (filters==null || filters.isEmpty()) {
			if (name.endsWith("yaml")) {
				content = yamlProducer.get().get();
			} else if (name.endsWith("json")) {
				content = jsonProducer.get().get();
			} else {
				content = contentProducer.get().get(); // no transformation required
			}
		} else {
			content = filtersProducer.get().get();
		}
	} catch (InterruptedException | ExecutionException e) {
		log.error("Could not get effective content for '{}' ({})", uri, e);
		throw new TransformException("Problem when getting effective content to save '"+uri+"'", e);
	}

	return content;

}


// TODO: move to domain-specific filter module
@Produces @Named("YAMLContent")
public static String yamlContent(@Named("ValuesForTemplate") Map<String, Object> values) {
	return DaggerViewComponent.builder()
								.withTemplatePath("transform/content-to-yaml.ftl")
								.withValue(values)
								.build()
								.render();
}


//TODO: move to domain-specific filter module
@Produces @Named("JSONContent")
public static String jsonContent(@Named("DestinationContentURI") URI uri,
									@Named("YAMLContent") String yaml,
									ObjectMapper jsonMapper,
									YAMLMapper yamlMapper)
						throws TransformException {

	try {

		// we are using the dirty trick of converting to YAML and then converting to JSON, works ^^
		return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(yamlMapper.readTree(yaml));

	} catch (IOException e) {
		String message = "Problem when transforming effective content to save '"+uri+"'";
		log.error(message);
		throw new TransformException(message, e);
	}
}


@Produces @Named("FilterContent")
public static String filterContent(@Named("ValuesForTemplate") Map<String, Object> values,
									@Nullable @Named("Filters") String filters,
									@Named("DestinationContentURI") URI uri) throws TransformException {

	try {
		return DaggerFilterComponent.builder().filters(filters).build().objectToString().get().apply(values);
	} catch (Exception e) {

		String message = "Problem when filtering effective content to save '"+uri+"' with '"+filters+"'";
		log.error(message);
		throw new TransformException(message, e);

	}

}


@Produces @Named("ValuesForTemplate")
public static Map<String, Object> values(Composite<Cell> contentRootCells,
											Model model,
											@Named("Content") String content) {

	Map<String, Object> values = new HashMap<String, Object>(2);
	values.put("cells", contentRootCells);
	values.put("model", model);
	values.put("xml", content);

	return values;

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
