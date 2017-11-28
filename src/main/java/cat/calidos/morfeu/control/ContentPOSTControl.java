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

import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentPOSTControl extends Control {

private String prefix;
private String path;
private String modelPath;
private String content;


public ContentPOSTControl(String prefix, String path, String content, @Nullable String modelPath) {

	super("POST content:"+path, "templates/xx.twig", "templates/xxx-problem.twig");

	this.prefix = prefix;
	this.path = path;
	this.modelPath = modelPath;
	this.content = content;
	
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.control.Control#process()
*//////////////////////////////////////////////////////////////////////////////
@Override
protected Object process()
		throws InterruptedException, ExecutionException, ValidationException, ParsingException, FetchingException {

	URI uri = DaggerURIComponent.builder().from(path).builder().uri().get();
	URI fetchableURI = DaggerURIComponent.builder().from(prefix+path).builder().uri().get();
	URI modelURI = DaggerURIComponent.builder().from(modelPath).builder().uri().get();
	URI fetchableModelPath = DaggerURIComponent.builder().from(prefix+modelPath).builder().uri().get();
	
	// TODO Auto-generated method stub
	return null;
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.control.Control#beforeProcess()
*//////////////////////////////////////////////////////////////////////////////
@Override
protected void beforeProcess() {

	// TODO Auto-generated method stub

}


/* (non-Javadoc)
* @see cat.calidos.morfeu.control.Control#afterProblem(java.lang.String)
*//////////////////////////////////////////////////////////////////////////////
@Override
protected void afterProblem(String problem) {

	// TODO Auto-generated method stub

}


/* (non-Javadoc)
* @see cat.calidos.morfeu.control.Control#problemInformation()
*//////////////////////////////////////////////////////////////////////////////
@Override
protected Object problemInformation() {

	// TODO Auto-generated method stub
	return null;
}

}
