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


public static String loadDocument(String uri) {
	
	log.trace("DocumentControll::loadDocument('{}')", uri);
	
	Document document = null;
	String problemMessage = null;
	try {
		
		document = DaggerDocumentComponent.builder()
											.URIModule(new URIModule(uri))
											.build()
											.produceDocument()
											.get();
		document.validate();
		
		
	} catch (InterruptedException e) {
		problemMessage = "Interrupted processing document '"+uri+"' ("+e.getMessage()+")";		
	} catch (ExecutionException e) {
	
		Throwable root = MorfeuUtils.findRootCauseFrom(e);
		problemMessage = "Problem processing document '"+uri+"' ("+root.getMessage()+")";

	} catch (ValidationException e) {
		problemMessage = "Problem validating document '"+uri+"' ("+e.getMessage()+")";
	} catch (FetchingException e) {
		problemMessage = "Problem fetching data for document '"+uri+"' ("+e.getMessage()+")";
	} catch (ParsingException e) {
		problemMessage = "Problem parsing document '"+uri+"' ("+e.getMessage()+")";
	}

	if (problemMessage!=null) {
		log.error(problemMessage);
		return DaggerViewComponent.builder()
									.withTemplate("templates/document-problem.twig")
									.withValue(new Object())	//no data
									.withProblem(problemMessage)
									.build()
									.render();

	} else {
	
		return DaggerViewComponent.builder()
									.withTemplate("templages/document.twig")
									.withValue(document)
									.withProblem("")
									.build()
									.render();
	}
}

}
