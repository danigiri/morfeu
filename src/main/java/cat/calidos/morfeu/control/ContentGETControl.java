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

package cat.calidos.morfeu.control;

import java.net.URI;
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

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentGETControl extends Control {

private final static Logger log = LoggerFactory.getLogger(ContentGETControl.class);

private String prefix;
private String path;
private String modelPath;
		

public ContentGETControl(String prefix, String path, @Nullable String modelPath) {

	super("GET content:"+path, "templates/content.twig", "templates/content-problem.twig");

	this.prefix = prefix;
	this.path = path;
	this.modelPath = modelPath;

}


@Override
protected Object process() throws InterruptedException, ExecutionException, ValidationException, 
									ParsingException, FetchingException, ConfigurationException, TransformException {

	URI uri = DaggerURIComponent.builder().from(path).builder().uri().get();
	URI fetchableURI = DaggerURIComponent.builder().from(prefix+path).builder().uri().get();
	URI modelURI = DaggerURIComponent.builder().from(modelPath).builder().uri().get();
	URI fetchableModelPath = DaggerURIComponent.builder().from(prefix+modelPath).builder().uri().get();

	ContentParserComponent contentComponent = DaggerContentParserComponent.builder()
																		.content(uri)
																		.fetchedContentFrom(fetchableURI)
																		.model(modelURI)
																		.withModelFetchedFrom(fetchableModelPath)
																		.build();
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

	// TODO Auto-generated method stub
	return null;
}



	
}
