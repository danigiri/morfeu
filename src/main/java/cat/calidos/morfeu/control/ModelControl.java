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
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.model.injection.DaggerModelComponent;
import cat.calidos.morfeu.model.injection.DaggerURIComponent;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ValidationException;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelControl extends Control {

protected final static Logger log = LoggerFactory.getLogger(ModelControl.class);

private String path;
private String prefix;


public ModelControl(String prefix, String path) {

	super("model:"+path, "templates/model.twig", "templates/model-problem.twig");
		
	this.prefix = prefix;
	this.path = path;
	
}


@Override
protected Object process() 
		throws InterruptedException, ExecutionException, ValidationException, FetchingException {

	URI uri = DaggerURIComponent.builder().from(path).builder().uri().get();
	URI fetchableURI = DaggerURIComponent.builder().from(prefix+path).builder().uri().get();
	return DaggerModelComponent.builder()
									.identifiedBy(uri)
									.fromFetchable(fetchableURI)
									.build()
									.model()
									.get();

}


@Override
protected void beforeProcess() {
	log.trace("ModelControl::loadModel('{}', '{}')", prefix, path);
}


@Override
protected void afterProblem(String problem) {
	log.trace("Problem loading model ('{}', '{}'): '{}'", prefix, path, problem);
}


@Override
protected Object problemInformation() {
	return path;	// we show the problematic path on the template
}


}
