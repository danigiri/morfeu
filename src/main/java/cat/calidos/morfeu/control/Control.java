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

import java.util.concurrent.ExecutionException;

import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class Control {

protected String operation;
private String template;
private String problemTemplate;


public Control(String operation, String template, String problemTemplate) {
	
	this.operation = operation;
	this.template = template;
	this.problemTemplate = problemTemplate;

}


protected String render(String template, Object value, String problem) {
	return DaggerViewComponent.builder()
								.withTemplate(template)
								.withValue(value)
								.withProblem(problem)
								.build()
								.render();
}


public String processRequest() {
	
	beforeProcess();
	
	Object result = null;
	String problem = "";
	String parsedResult = null;
	
	try {
		result = process();
		
	} catch (InterruptedException e) {
		problem = "Interrupted processing '"+operation+"' ("+e.getMessage()+")";	
	} catch (ExecutionException e) {
		e.printStackTrace();
		Throwable root = MorfeuUtils.findRootCauseFrom(e);
		problem = "Problem processing '"+operation+"' ("+root.getMessage()+")";
	} catch (ValidationException e) {
		problem = "Problem validating '"+operation+"' ("+e.getMessage()+")";
	} catch (FetchingException e) {
		problem = "Problem fetching data for '"+operation+"' ("+e.getMessage()+")";
	} catch (ParsingException e) {
		problem = "Problem parsing for '"+operation+"' ("+e.getMessage()+")";
	} catch (ConfigurationException e) {
		problem = "Problem configuring for '"+operation+"' ("+e.getMessage()+")";
	}
		
	if (problem.length()==0) {
		parsedResult = render(template, result, problem);
	} else {
		afterProblem(problem);
		Object problemInformation = problemInformation(); 
		parsedResult = render(problemTemplate, problemInformation, problem);		
	}
		
	return parsedResult;
	
}


protected abstract Object process() 
		throws InterruptedException, ExecutionException, ValidationException, ParsingException, FetchingException, ConfigurationException;

protected abstract void beforeProcess();

protected abstract void afterProblem(String problem);

protected abstract Object problemInformation();

}
