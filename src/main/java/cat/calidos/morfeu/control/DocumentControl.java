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
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.injection.DaggerDocumentComponent;
import cat.calidos.morfeu.model.injection.DaggerURIComponent;
import cat.calidos.morfeu.model.injection.URIModule;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.utils.MorfeuUtils;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DocumentControl extends Control {

protected final static Logger log = LoggerFactory.getLogger(DocumentControl.class);
private String prefix;
private String path;


public DocumentControl(String prefix, String path) {

	super("document", "templates/document.twig", "templates/document-problem.twig");

	this.prefix = prefix;
	this.path = path;
	
}


@Override
protected Object process() 
		throws InterruptedException, ExecutionException, ValidationException, ParsingException, FetchingException {

	URI uri = DaggerURIComponent.builder().from(prefix+path).builder().uri().get();
	return DaggerDocumentComponent.builder()
										.from(uri)
										.withPrefix(prefix)
										.build()
										.produceDocument()
										.get();
			
}


@Override
protected void logProcess() {
	log.trace("DocumentControl::loadDocument('{}', '{}')", prefix, path);
}


@Override
protected void logProblem(String problem) {
	log.trace("Problem loading document('{}', '{}'): {}", prefix, path, problem);
}


@Override
protected Object problemInformation() {
	return path;	// we show the problematic path on the template
}


}
