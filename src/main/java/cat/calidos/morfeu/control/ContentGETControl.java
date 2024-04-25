// CONTENT GET CONTROL . JAVA

package cat.calidos.morfeu.control;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.injection.ContentParserComponent;
import cat.calidos.morfeu.model.injection.DaggerContentParserComponent;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.problems.ValidationException;

/** Controller that GETs, validates and transforms content into a cell list, from original XML or YAML, given a schema
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentGETControl extends JSONGETControl {

private final static Logger log = LoggerFactory.getLogger(ContentGETControl.class);

private String prefix;
private String path;
private String modelPath;
private String filters;

public ContentGETControl(String prefix, String path, Optional<String> filters, @Nullable String modelPath) {

	super("GET content:"+path, "templates/content.thy", "templates/content-problem.thy");

	this.prefix = prefix;
	this.path = path;
	this.modelPath = modelPath;
	this.filters = filters.orElse(null);
}


@Override
protected Object process() throws InterruptedException, ExecutionException, ValidationException, 
									ParsingException, FetchingException, ConfigurationException, TransformException {

	// we use the prefix to build fetchable content and models whenever needed
	URI uri = DaggerURIComponent.builder().from(path).build().uri().get();
	URI fetchableURI = DaggerURIComponent.builder().from(prefix+path).build().uri().get();
	URI modelURI = DaggerURIComponent.builder().from(modelPath).build().uri().get();
	URI fetchableModelPath = DaggerURIComponent.builder().from(prefix+modelPath).build().uri().get();

	ContentParserComponent contentComponent = DaggerContentParserComponent.builder()
																			.content(uri)
																			.fetchedContentFrom(fetchableURI)
																			.filters(filters)
																			.model(modelURI)
																			.withModelFetchedFrom(fetchableModelPath)
																			.build();
	// we first validate the content against the schema and then we get the cell list
	contentComponent.validator().get().validate();
	Composite<Cell> content = contentComponent.content().get();

	return content.asList();

}


@Override
protected void beforeProcess() {
	log.trace("About to load content '[{}]{}' given model '{}'", prefix, path, modelPath);
}


@Override
protected void afterProblem(String problem) {
	log.trace("Problem loading content('{}', '{}', '{}'): '{}'", prefix, path, modelPath, problem);
}


@Override
protected Object problemInformation() {
	return null;	// not adding any other extra error context yet
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
