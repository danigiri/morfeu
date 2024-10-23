// CONTENT SAVE CONTROL . JAVA

package cat.calidos.morfeu.control;

import java.net.URI;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.injection.ContentSaverParserComponent;
import cat.calidos.morfeu.model.injection.DaggerContentSaverParserComponent;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.SavingException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.transform.injection.DaggerContentConverterComponent;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;


/**
 * Controller that validates the content and if valid, saves it
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentSaveControl extends Control {

private final static Logger log = LoggerFactory.getLogger(ContentSaveControl.class);

private String				prefix;			// prefix where things will be saved
private String				path;			// relative destination path
private String				destination;	// final destination: saveable prefix + relative path
											// contatenated
private Optional<String>	filters;		//
private String				modelPath;		// relative model path
private String				content;		// the content to be saved in string form

private String contentSnippet; // the first few characters of the content, for logging / diagnostics

private HashMap<String, String> resultMetadata;

public ContentSaveControl(	String prefix, String path, String content, Optional<String> filters,
							String modelPath) {

	super("POST content:" + path, "operation-ok.ftl", "operation-problem.ftl");

	this.prefix = prefix;
	this.path = path;
	this.destination = prefix + path;
	this.filters = filters;
	this.modelPath = modelPath;
	this.content = content;
	this.contentSnippet = content.substring(0, Math.min(10, content.length()));
	this.resultMetadata = new HashMap<String, String>(4);

	resultMetadata.put("target", path);
	resultMetadata.put("operation", "FileSaver");
	resultMetadata.put("filters", filters.orElse(""));
	resultMetadata.put("operationTime", "-1");
	resultMetadata.put("result", "Not set");

}


@Override
protected Object process() throws InterruptedException, ExecutionException, ValidationException,
		ParsingException, FetchingException, ConfigurationException, SavingException {

	URI uri = DaggerURIComponent.builder().from(path).build().uri().get();
	URI outputURI = DaggerURIComponent.builder().from(destination).build().uri().get();
	URI modelURI = DaggerURIComponent.builder().from(modelPath).build().uri().get();
	URI fullModelURI = DaggerURIComponent.builder().from(prefix + modelPath).build().uri().get();

	// log.trace(content);
	JsonNode json = DaggerJSONParserComponent.builder().from(content).build().json().get();
	String transformedContent = DaggerContentConverterComponent.builder().from(json).build().xml();
	// log.trace(transformedContent);
	log.info(">>> Saving content to '{}' (size:{}) <<<", outputURI, transformedContent.length());

	ContentSaverParserComponent component = DaggerContentSaverParserComponent
			.builder()
			.from(transformedContent)
			.to(outputURI)
			.filters(filters.orElse(""))
			.having(uri)
			.model(modelURI)
			.withModelFetchedFrom(fullModelURI)
			.build();

	// validate and save, measuring the time it takes to provide some diagnostics
	long before = System.currentTimeMillis();
	component.validator().get().validate();
	component.saver().get().save();
	long now = System.currentTimeMillis();
	resultMetadata.put("operationTime", Long.toString(now - before));
	resultMetadata.put("result", "Content saved successfully");

	return resultMetadata;

}


@Override
protected void beforeProcess() {
	log
			.trace(
					"Saving content '{}' to '[{}]{}' given model '{}'",
					contentSnippet,
					prefix,
					path,
					modelPath);
}


@Override
protected void afterProblem(String problem) {
	log
			.trace(
					"Problem saving content '{}' to '[{}]{}' given model '{}'",
					contentSnippet,
					prefix,
					path,
					modelPath);
}


@Override
protected Object problemInformation() {
	return resultMetadata;
}

}

/*
 * Copyright 2024 Daniel Giribet
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
