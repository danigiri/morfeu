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
import cat.calidos.morfeu.view.injection.DaggerDocumentViewComponent;
import cat.calidos.morfeu.view.injection.DocumentViewModule;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DocumentControl {

protected final static Logger log = LoggerFactory.getLogger(DocumentControl.class);


public static String loadDocument(String uri) throws ParsingException {
	
	Document document = null;
	String message = null;
	String template = null;
	try {
		
		document = DaggerDocumentComponent.builder()
											.URIModule(new URIModule(uri))
											.build()
											.produceDocument()
											.get();
		template = "templages/document.twig";
		
		document.validate();
		
	} catch (InterruptedException e) {
		
		message = "Interrupted processing document '"+uri+"' ("+e.getMessage()+")";
		template = "templates/document-problem.twig";
		log.error(message);
		
	} catch (ExecutionException e) {
	
		Throwable problem = MorfeuUtils.findRootCauseFrom(e);
		//if () {}
		
		message = "Problem processing document '"+uri+"' ("+problem.getMessage()+")";
		template = "templates/document-problem.twig";
		log.error(message);

	} catch (ValidationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (FetchingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return DaggerDocumentViewComponent.builder()
										.withTemplate(template)
										.withValue(document)
										.withProblem(message)
										.build()
										.render();
	
}

}
