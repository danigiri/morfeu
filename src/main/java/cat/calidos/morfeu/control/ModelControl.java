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

import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.injection.DaggerModelComponent;
import cat.calidos.morfeu.model.injection.DaggerURIComponent;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.utils.MorfeuUtils;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelControl extends Control{


protected final static Logger log = LoggerFactory.getLogger(ModelControl.class);

public static String loadModel(String prefix, String path) {

	log.trace("ModelControl::loadModel('{}', '{}')", prefix, path);
	
	Model model = null;
	String problem = "";
	
	try {
		
		URI uri = DaggerURIComponent.builder().from(path).builder().uri().get();
		URI fetchableURI = DaggerURIComponent.builder().from(prefix+path).builder().uri().get();
		model = DaggerModelComponent.builder()
										.identifiedBy(uri)
										.fromFetchable(fetchableURI)
										.build()
										.model()
										.get();
		
	} catch (InterruptedException e) {
		problem = "Interrupted processing model '"+path+"' ("+e.getMessage()+")";	
	} catch (ExecutionException e) {
		e.printStackTrace();
		Throwable root = MorfeuUtils.findRootCauseFrom(e);
		problem = "Problem processing model '"+path+"' ("+root.getMessage()+")";
	} catch (ValidationException e) {
		problem = "Problem validating model '"+path+"' ("+e.getMessage()+")";
	} catch (FetchingException e) {
		problem = "Problem fetching data for model '"+path+"' ("+e.getMessage()+")";
	}
	
	if (problem.length()>0) {
		log.error(problem);
	}

	if (model!=null) {
		
		return render("templates/model.twig", model, problem);
		
	} else {
		return render("templates/model-problem.twig", new Object(), problem);
	}
	
}


}
