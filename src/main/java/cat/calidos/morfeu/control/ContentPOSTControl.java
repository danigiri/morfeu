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

package cat.calidos.morfeu.control;

import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

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
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentPOSTControl extends Control {

private final static Logger log = LoggerFactory.getLogger(ContentPOSTControl.class);

private String prefix;
private String path;
private String destination;
private String modelPath;
private String content;

private String contentSnippet;

private HashMap<String, String> resultMetadata;


public ContentPOSTControl(String prefix, String path, String content, @Nullable String modelPath) {

	super("POST content:"+path, "templates/content-save.twig", "templates/content-save-problem.twig");

	this.prefix = prefix;
	this.path = path;
	this.destination = prefix+path;
	this.modelPath = modelPath;
	this.content = content;
	this.contentSnippet = content.substring(0, Math.min(10, content.length()));
	this.resultMetadata = new HashMap<String, String>(4);

	resultMetadata.put("destination", destination);
	resultMetadata.put("uri", path);
	resultMetadata.put("saver", "FileSaver");
	resultMetadata.put("parsingTime", "-1");

}


/* (non-Javadoc)
* @see cat.calidos.morfeu.control.Control#process()
*//////////////////////////////////////////////////////////////////////////////
@Override
protected Object process() throws InterruptedException, ExecutionException, ValidationException, ParsingException, 
									FetchingException, ConfigurationException, SavingException {

	URI uri = DaggerURIComponent.builder().from(path).builder().uri().get();
	URI outputURI = DaggerURIComponent.builder().from(destination).builder().uri().get();
	URI modelURI = DaggerURIComponent.builder().from(modelPath).builder().uri().get();
	URI fullModelURI = DaggerURIComponent.builder().from(prefix+modelPath).builder().uri().get();
	
	JsonNode json = DaggerJSONParserComponent.builder().from(content).build().json().get();
	
	String transformedContent = DaggerViewComponent.builder()
													.withTemplate("templates/transform/content-json-to-xml.twig")
													.withValue(json)
													.build()
													.render();
 
	ContentSaverParserComponent component = DaggerContentSaverParserComponent.builder()
																			.from(transformedContent)
																			.to(outputURI)
																			.having(uri)
																			.model(modelURI)
																			.withModelFetchedFrom(fullModelURI)
																			.build();
	
	long before = System.currentTimeMillis();
	component.validator().get().validate();
	long now = System.currentTimeMillis();
	resultMetadata.put("parsingTime", Long.toString(now-before));

	component.saver().get().save();

	return resultMetadata;
	
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.control.Control#beforeProcess()
*//////////////////////////////////////////////////////////////////////////////
@Override
protected void beforeProcess() {
	log.trace("Saving content '{}' to '[{}]{}' given model '{}'", contentSnippet, prefix, path, modelPath);
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.control.Control#afterProblem(java.lang.String)
*//////////////////////////////////////////////////////////////////////////////
@Override
protected void afterProblem(String problem) {
	log.trace("Problem saving content '{}' to '[{}]{}' given model '{}'", contentSnippet, prefix, path, modelPath);
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.control.Control#problemInformation()
*//////////////////////////////////////////////////////////////////////////////
@Override
protected Object problemInformation() {
	return resultMetadata;
}

}
