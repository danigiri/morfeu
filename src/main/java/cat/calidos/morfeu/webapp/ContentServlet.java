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

package cat.calidos.morfeu.webapp;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.ContentGETControl;
import cat.calidos.morfeu.control.ContentPOSTControl;
import cat.calidos.morfeu.utils.Config;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContentServlet extends MorfeuServlet {

protected final static Logger log = LoggerFactory.getLogger(ContentServlet.class);


/* (non-Javadoc)
* @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	// http://localhost:8080/morfeu/content/target/test-classes/test-resources/documents/document1.xml?model=target/test-classes/test-resources/models/test-model.xsd
	String path = normalisedPathFrom(req);
	String modelPath = req.getParameter("model");
	log.trace("ContentServlet::doGet '[{}]{}' model:'{}'", resourcesPrefix, path, modelPath);

	String content = new ContentGETControl(resourcesPrefix, path, modelPath).processRequest(); 

	writeTo(content, resp);

}


/* (non-Javadoc)
* @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
*//////////////////////////////////////////////////////////////////////////////
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	String path = normalisedPathFrom(req);
	String modelPath = req.getParameter("model");	//TODO: handle exception if not present
	Optional<String> transforms =  Optional.ofNullable(req.getParameter("t"));
	log.info("ContentServlet::doPost '[{}]{}' model:'{}'", resourcesPrefix, path, modelPath);
	
	if(!req.getContentType().equals("application/json")) {
		
	}
	
	String content = IOUtils.toString(req.getInputStream(), Config.DEFAULT_CHARSET);
	String result = new ContentPOSTControl(resourcesPrefix, path, content, transforms, modelPath).processRequest();
	
	writeTo(result, resp);

}

}
