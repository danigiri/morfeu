// SNIPPET GET CONTROL . JAVA

package cat.calidos.morfeu.control;

import java.net.URI;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.injection.DaggerSnippetParserComponent;
import cat.calidos.morfeu.model.injection.SnippetParserComponent;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.SavingException;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;


/** Generates a list of cells coming from a snippet, does not do validation as snippets may not have all the context
*	however, it still creates a correct reference to the model from each cell
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SnippetGETControl extends JSONGETControl {

protected final static Logger log = LoggerFactory.getLogger(SnippetGETControl.class);

private String prefix;		// prefix of
private String path;		// relative path
private String modelPath;	// relative path of the model, not used to validate


public SnippetGETControl(String prefix, String path, @Nullable String modelPath) {

	super("GET snippet:"+path, "templates/content.thy", "templates/content-problem.thy");

	this.prefix = prefix;
	this.path = path;
	this.modelPath = modelPath;

}


@Override
protected Object process() throws InterruptedException, ExecutionException, ValidationException, ParsingException,
		FetchingException, ConfigurationException, SavingException, TransformException {

	URI uri = DaggerURIComponent.builder().from(path).build().uri().get();
	URI fetchableURI = DaggerURIComponent.builder().from(prefix+path).build().uri().get();
	URI modelURI = DaggerURIComponent.builder().from(modelPath).build().uri().get();
	URI fetchableModelPath = DaggerURIComponent.builder().from(prefix+modelPath).build().uri().get();
	SnippetParserComponent snippetComponent = DaggerSnippetParserComponent.builder()
																			.content(uri)
																			.fetchedContentFrom(fetchableURI)
																			.modelFiltered(modelURI)
																			.withModelFetchedFrom(fetchableModelPath)
																			.build();

	snippetComponent.validator().get().validate();	// may or may not do any kind of validation (actually none)
	Composite<Cell> content = snippetComponent.content().get();

	return content.asList();

}


@Override
protected void beforeProcess() {
	log.trace("About to load snippet '[{}]{}' given model '{}'", prefix, path, modelPath);
}


@Override
protected void afterProblem(String problem) {
	log.trace("Problem loading snippet('{}', '{}', '{}'): '{}'", prefix, path, modelPath, problem);
}


@Override
protected Object problemInformation() {
	return path;	// we show the problematic path on the template
}


}

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
