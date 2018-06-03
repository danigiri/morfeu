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

package cat.calidos.morfeu.control.injection;

import java.util.concurrent.ExecutionException;

import cat.calidos.morfeu.control.Control;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.SavingException;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.problems.ValidationException;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SnippetGETControl extends Control {

public SnippetGETControl(String pref, String path, String modelPath, String cellModel) {

	super("GET snippet:"+path, "templates/snippet.twig", "templates/content-problem.twig");

		// TODO Auto-generated constructor stub
	}


@Override
protected Object process() throws InterruptedException, ExecutionException, ValidationException, ParsingException,
		FetchingException, ConfigurationException, SavingException, TransformException {

	// TODO Auto-generated method stub
	return null;
}


@Override
protected void beforeProcess() {

	// TODO Auto-generated method stub

}


@Override
protected void afterProblem(String problem) {

	// TODO Auto-generated method stub

}


@Override
protected Object problemInformation() {

	// TODO Auto-generated method stub
	return null;
}

}
