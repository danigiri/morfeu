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

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.model.injection.DaggerURIComponent;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentControl extends Control {

protected final static Logger log = LoggerFactory.getLogger(ContentControl.class);
private String prefix;
private String path;
private String modelPath;
		

public ContentControl(String prefix, String path, @Nullable String modelPath) {
	
	super("content:"+path, "", "templates/content-problem.twig");
	
	this.prefix = prefix;
	this.path = path;
	this.modelPath = modelPath;
	
}


@Override
protected Object process()
		throws InterruptedException, ExecutionException, ValidationException, ParsingException, FetchingException {

	URI uri = DaggerURIComponent.builder().from(path).builder().uri().get();
	URI fetchableURI = DaggerURIComponent.builder().from(prefix+path).builder().uri().get();
	
	return null;
}


@Override
protected void logProcess() {
	log.trace("ContentControl::loadContent('{}', '{}', '{}')", prefix, path, modelPath);
}


@Override
protected void logProblem(String problem) {

	// TODO Auto-generated method stub
	
}


@Override
protected Object problemInformation() {

	// TODO Auto-generated method stub
	return null;
}



	
}
