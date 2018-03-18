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

package cat.calidos.morfeu.model.transform.injection;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.ParsingException;
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
											@Named("TransformedContent") Producer<String> transformedContentProducer,
											@Named("Content") Producer<String> contentProducer) throws TransformException {
	
	String name = FilenameUtils.getName(uri.getPath());

	try {
		if (name.endsWith("yaml")) {

			return transformedContentProducer.get().get();

		} else {

			return contentProducer.get().get(); // no transformation required

		}
	} catch (InterruptedException | ExecutionException e) {
		log.error("Could not get effective content for '{}' ({})", uri, e);
		throw new TransformException("Problem when getting effective content to save '"+uri+"'", e);
	}	

}


@Produces @Named("TransformedContent")
public static String transformedYAMLToXML(Composite<Cell> contentRootCells, Model model) {
	
	Map<String, Object> values = new HashMap<String, Object>(2);
	values.put("cells", contentRootCells.asList());
	values.put("model", model);
	
	return DaggerViewComponent.builder()
			.withTemplate("templates/transform/content-to-yaml.twig")
			.withValue(values)
			.build()
			.render();
}



}
