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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.injection.ControlComponent;
import cat.calidos.morfeu.control.injection.DaggerControlComponent;
import cat.calidos.morfeu.utils.Config;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class GenericMorfeuServlet extends MorfeuServlet {

protected final static Logger log = LoggerFactory.getLogger(GenericMorfeuServlet.class);

/* (non-Javadoc)
* @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	String path = req.getPathInfo();
	Map<String, String> params = normaliseParams(req.getParameterMap());
	params.put(RESOURCES_PREFIX, resourcesPrefix);
	params.put(METHOD, req.getMethod());
	log.trace("GenericMorfeuServlet::doGet {}", path);
	ControlComponent controlComponent = DaggerControlComponent.builder()
																.withPath(path)
																.withParams(params)
																.build();
	String result = controlComponent.process();
	writeTo(result, resp);

}


/* (non-Javadoc)
* @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
*//////////////////////////////////////////////////////////////////////////////
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	String path = "POST:"+req.getPathInfo();
	Map<String, String> params = normaliseParams(req.getParameterMap());
	params.put(RESOURCES_PREFIX, resourcesPrefix);
	params.put(METHOD, req.getMethod());
	String content = IOUtils.toString(req.getInputStream(), Config.DEFAULT_CHARSET);
	params.put(POST_VALUE, content);
	log.trace("GenericMorfeuServlet::doPost {}", path);
	ControlComponent controlComponent = DaggerControlComponent.builder()
																.withPath(path)
																.withParams(params)
																.build();
	String result = controlComponent.process();
	writeTo(result, resp);


}


private Map<String, String> normaliseParams(Map<String, String[]> parameterMap) {
	return parameterMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()[0]));
}

}
