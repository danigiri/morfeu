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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.injection.DaggerDocumentComponent;
import cat.calidos.morfeu.model.injection.URIModule;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DocumentControl {

protected final static Logger log = LoggerFactory.getLogger(DocumentControl.class);


public static String loadDocument(String prefix, String uri) {
	
	log.trace("DocumentControl::loadDocument('{}', '{}')", prefix, uri);
	
	Document document = null;
	String problem = "";
	try {
		
		// FIXME: URI module should be killed for a binds instance
		document = DaggerDocumentComponent.builder()
											.URIModule(new URIModule(prefix+uri))
											.withPrefix(prefix)
											.build()
											.produceDocument()
											.get();
		document.validate();
		
		
	} catch (InterruptedException e) {
		problem = "Interrupted processing document '"+uri+"' ("+e.getMessage()+")";		
	} catch (ExecutionException e) {
		e.printStackTrace();
		Throwable root = MorfeuUtils.findRootCauseFrom(e);
		problem = "Problem processing document '"+uri+"' ("+root.getMessage()+")";

	} catch (ValidationException e) {
		problem = "Problem validating document '"+uri+"' ("+e.getMessage()+")";
		log.error(problem);
	} catch (FetchingException e) {
		problem = "Problem fetching data for document '"+uri+"' ("+e.getMessage()+")";
	} catch (ParsingException e) {
		problem = "Problem parsing document '"+uri+"' ("+e.getMessage()+")";
	}

	if (document!=null) {
	
		return DaggerViewComponent.builder()
				.withTemplate("templates/document.twig")
				.withValue(document)
				.withProblem(problem)
				.build()
				.render();

	} else  {

		log.error(problem);
		return DaggerViewComponent.builder()
				.withTemplate("templates/document-problem.twig")
				.withValue(new Object())	//no data
				.withProblem(problem)
				.build()
				.render();
		
	}
}

}
