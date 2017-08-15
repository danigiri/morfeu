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


public static String loadDocument(String prefix, String path) {
	
	log.trace("DocumentControl::loadDocument('{}', '{}')", prefix, path);
	
	Document document = null;
	String problem = "";
	try {
		
		URI uri = DaggerURIComponent.builder().from(prefix+path).builder().uri().get();
		document = DaggerDocumentComponent.builder()
											.from(uri)
											.withPrefix(prefix)
											.build()
											.produceDocument()
											.get();
				
	} catch (InterruptedException e) {
		problem = "Interrupted processing document '"+path+"' ("+e.getMessage()+")";		
	} catch (ExecutionException e) {
		e.printStackTrace();
		Throwable root = MorfeuUtils.findRootCauseFrom(e);
		problem = "Problem processing document '"+path+"' ("+root.getMessage()+")";
	} catch (ValidationException e) {
		problem = "Problem validating document '"+path+"' ("+e.getMessage()+")";
	} catch (FetchingException e) {
		problem = "Problem fetching data for document '"+path+"' ("+e.getMessage()+")";
	} catch (ParsingException e) {
		problem = "Problem parsing document '"+path+"' ("+e.getMessage()+")";
	}

	if (problem.length()>0) {
		log.error(problem);
	}

	if (document!=null) {
	
		return render("templates/document.twig", document, problem);

	} else  {

		return render("templates/document-problem.twig", path, problem);	// path as data to help diagnostics
		
	}
	
}


}
