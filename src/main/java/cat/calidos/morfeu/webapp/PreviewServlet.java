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
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class PreviewServlet extends MorfeuServlet {

protected final static Logger log = LoggerFactory.getLogger(PreviewServlet.class);

/* (non-Javadoc)
* @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	// http://localhost:8080/morfeu/preview/foo.svg?a=1&b=2

	String path = normalisedPathFrom(req);
	Map<String, String[]> params = req.getParameterMap();
	log.trace("PreviewServlet::doGet '[{}]{}' params:'{}'", resourcesPrefix, path, params);

	String content = new PreviewGETControl(resourcesPrefix, path, params).processRequest();

	if (path.endsWith("svg")) {
		writeTo(content, "image/svg+xml", resp);
	} else {
		writeTo(content, resp);
	}

}

}
