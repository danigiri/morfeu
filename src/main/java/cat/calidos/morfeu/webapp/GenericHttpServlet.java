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
import java.io.PrintWriter;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import cat.calidos.morfeu.webapp.injection.DaggerServletConfigComponent;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class GenericHttpServlet extends HttpServlet {

protected final static Logger log = LoggerFactory.getLogger(GenericHttpServlet.class);

public static final String METHOD = "__METHOD";
public static final String POST_VALUE = "__POST";

protected Properties configuration;
protected String defaultContentType = "application/json";


@Override
public void init(ServletConfig config) throws ServletException {
	
	super.init(config);

	//TODO: check if there is a more dagger friendly way of doing this
	configuration = DaggerServletConfigComponent.builder()
													.servletConfig(this.getServletConfig())
													.build()
													.getProperties();

}


public abstract ControlComponent getControl(String path, Map<String, String> params);

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	String path = req.getPathInfo();
	log.trace("GenericMorfeuServlet::doGet {}", path);
	
	Map<String, String> params = normaliseParams(req.getParameterMap());
	params.put(METHOD, req.getMethod());
	params = processParams(params);

	ControlComponent controlComponent = getControl(path, params);
	
	handleResponse(resp, controlComponent);

}


public abstract ControlComponent putControl(String path, Map<String, String> params);


@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	String path = req.getPathInfo();
	log.trace("GenericMorfeuServlet::doPost {}", path);
	
	Map<String, String> params = normaliseParams(req.getParameterMap());
	params.put(METHOD, req.getMethod());
	String content = IOUtils.toString(req.getInputStream(), Config.DEFAULT_CHARSET);
	params.put(POST_VALUE, content);
	params = processParams(params);
	
	ControlComponent controlComponent = putControl(path, params);
	handleResponse(resp, controlComponent);

}


protected Map<String, String> processParams(Map<String, String> params) {
	return params;
}



/** @param req from the servlet
* 	@return normalised path that does not start with '/'
*/
protected String normalisedPathFrom(HttpServletRequest req) {

	//String path = req.getPathTranslated();
	String path = req.getPathInfo();	// internal model paths are not started with a slash (see tests)
	if (path.startsWith("/")) {
		path = path.substring(1);
	}

	return path;

}


protected void writeTo(String content, String contentType, HttpServletResponse resp) throws IOException {

	// to simulate slowness
//	try {
//		Thread.sleep(20000);
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	resp.setContentType(contentType);
	PrintWriter out = resp.getWriter();
	out.print(content);
	out.close();

}


protected void writeTo(String content, HttpServletResponse resp) throws IOException {
	writeTo(content, defaultContentType, resp);
}



protected void handleResponse(HttpServletResponse resp, ControlComponent controlComponent) throws IOException {

	if (controlComponent.matches()) {
		String result = controlComponent.process();
		writeTo(result, controlComponent.contentType(), resp);
	} else {
		log.trace("GenericMorfeuServlet::doPost {} NOT FOUND (not matched)");
		resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}
}


protected Map<String, String> normaliseParams(Map<String, String[]> parameterMap) {
	return parameterMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()[0]));
}


}
