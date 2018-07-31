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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.model.injection.DaggerDocumentComponent;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;

/** Document GET controller, outputs JSON
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DocumentGETControl extends Control {

protected final static Logger log = LoggerFactory.getLogger(DocumentGETControl.class);

private String prefix;
private String path;


public DocumentGETControl(String prefix, String path) {

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
									.document()
									.get();

}


@Override
protected void beforeProcess() {
	log.trace("Loading Document('{}', '{}')", prefix, path);
}


@Override
protected void afterProblem(String problem) {
	log.warn("Problem loading document('{}', '{}'): {}", prefix, path, problem);
}


@Override
protected Object problemInformation() {
	return path;	// we show the problematic path on the template
}


}
