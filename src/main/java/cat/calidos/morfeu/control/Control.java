/*
 * Copyright 2018 Daniel Giribet
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

package cat.calidos.morfeu.control;

import java.util.concurrent.ExecutionException;

import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.SavingException;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;


/**
 * Main abstract controller that uses templates to generate output, has built-in error handling
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class Control {

protected String	operation;			// describes the operation we are doing with the controller
										// subclass
private String		template;			// template to generate the output
private String		problemTemplate;	// template to generate the output whenever there is a
										// problem

/**
 * Default constructor
 * 
 * @param operation operation we're doing
 * @param template template output for normal operations
 * @param problemTemplate template for problems
 */
public Control(String operation, String template, String problemTemplate) {

	this.operation = operation;
	this.template = template;
	this.problemTemplate = problemTemplate;

}


/**
 * Render the output using the supplied templates, should not need to override
 * 
 * @param template template path
 * @param value values from the specific controller, to be referenced from the template
 * @param problem problem object if nay
 * @return the string with the rendered output
 */
protected String render(String template,
						Object value,
						String problem) {
	return DaggerViewComponent
			.builder()
			.withValue(value)
			.withTemplatePath(template)
			.andProblem(problem)
			.build()
			.render();
}


/** @return process the request, handling any problems */
public String processRequest() {

	beforeProcess();

	Object result = null;
	String problem = "";
	String parsedResult = null;

	try {
		result = process();

	} catch (InterruptedException e) {
		problem = "Interrupted processing '" + operation + "' (" + e.getMessage() + ")";
	} catch (ExecutionException e) {
		Throwable root = MorfeuUtils.findRootCauseFrom(e);
		problem = "Problem processing '" + operation + "' (" + root.getMessage() + ", "
				+ e.getMessage() + ")";
		e.printStackTrace();
	} catch (ValidationException e) {
		problem = "Problem validating '" + operation + "' (" + e.getMessage() + ")";
	} catch (FetchingException e) {
		problem = "Problem fetching data for '" + operation + "' (" + e.getMessage() + ")";
	} catch (ParsingException e) {
		problem = "Problem parsing for '" + operation + "' (" + e.getMessage() + ")";
	} catch (ConfigurationException e) {
		problem = "Problem configuring for '" + operation + "' (" + e.getMessage() + ")";
	} catch (SavingException e) {
		problem = "Problem saving in '" + operation + "' (" + e.getMessage() + ")";
	} catch (TransformException e) {
		problem = "Problem transforming in '" + operation + "' (" + e.getMessage() + ")";
	}

	if (problem.length() == 0) {
		parsedResult = render(template, result, problem);
	} else {
		afterProblem(problem);
		Object problemInformation = problemInformation();
		parsedResult = render(problemTemplate, problemInformation, problem);
	}

	return parsedResult;

}


/**
 * @return process the request and return the value(s) that the template will use to show whatever
 * @throws InterruptedException should not happen, only if our threads are interrupetd
 * @throws ExecutionException generic problem
 * @throws ValidationException content or payload is not valid sermantically
 * @throws ParsingException content or payload is not syntactically valid
 * @throws FetchingException there was a problem when fetching needed content (I/O problems, etc.)
 * @throws ConfigurationException something was not configured right
 * @throws SavingException could not store the results of the operation
 * @throws TransformException the operation required some data trnasformation that could not happen
 */
protected abstract Object process()
		throws InterruptedException, ExecutionException, ValidationException, ParsingException,
		FetchingException, ConfigurationException, SavingException, TransformException;

/** Called before processing, to make any necessary preparations, log, etc. */
protected abstract void beforeProcess();

/**
 * There was a problem, do any cleanup, log
 * 
 * @param problem textual description of the problem is provided, to add it to the problem render
 */
protected abstract void afterProblem(String problem);

/** @return extra metadata to aid in the problem output */
protected abstract Object problemInformation();

}
