package cat.calidos.morfeu.control;

import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.model.injection.DaggerModelComponent;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ValidationException;


/** Controller to get the model
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelGETControl extends JSONGETControl {

protected final static Logger log = LoggerFactory.getLogger(ModelGETControl.class);

private String path;	// relative path of the model
private String prefix;	// orefix, to build the full fetchable path


public ModelGETControl(String prefix, String path) {

	super("model:"+path, "templates/model.thy", "templates/model-problem.thy");

	this.prefix = prefix;
	this.path = path;

}


@Override
protected Object process() 
		throws InterruptedException, ExecutionException, ValidationException, FetchingException {

	URI uri = DaggerURIComponent.builder().from(path).build().uri().get();
	URI fetchableURI = DaggerURIComponent.builder().from(prefix+path).build().uri().get();

	return DaggerModelComponent.builder().identifiedBy(uri).fromFetchable(fetchableURI).build().model().get();

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
