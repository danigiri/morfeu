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
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import cat.calidos.morfeu.webapp.injection.DaggerServletConfigComponent;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class GenericHttpServlet extends HttpServlet {


protected final static Logger log = LoggerFactory.getLogger(GenericHttpServlet.class);

public static final String URLENCODED = "application/x-www-form-urlencoded";
public static final String INTERNAL_PARAM_PREFIX = "__";	// internal params start with this
public static final String METHOD = "__METHOD";
public static final String POST_VALUE = "__POST";
public static final String __CONFIG = "__CONFIG";




protected Properties configuration;
protected ServletContext context;
protected String defaultContentType = "application/json";



@Override
public void init(ServletConfig config) throws ServletException {

	super.init(config);

	//TODO: add the servlet init params as part of the config so a proper merge can be done
	configuration = DaggerServletConfigComponent.builder().servletConfig(config).build().getProperties();
	context = config.getServletContext();
	addConfigurationToContext();

}


public abstract ControlComponent getControl(String path, Map<String, String> params);


public abstract ControlComponent postControl(String path, Map<String, String> params);


@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	ControlComponent controlComponent = generateGetControlComponent(req, req.getPathInfo());
	handleResponse(req, resp, controlComponent);

}


@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	ControlComponent controlComponent = generatePostControlComponent(req, req.getPathInfo());
	handleResponse(req, resp, controlComponent);

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


protected void writeTo(String content, String contentType, HttpServletResponse resp) {

	// to simulate slowness
//	try {
//		Thread.sleep(20000);
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	resp.setContentType(contentType);
	PrintWriter out;
	try {
		out = resp.getWriter();
		out.print(content);
		out.close();
	} catch (IOException e) {
		String msg = "Could not write response in servlet code, cannot recover from this";
		log.error(msg, e);
		throw new RuntimeException(msg, e);
	}

}


protected void writeTo(String content, HttpServletResponse resp) {
	writeTo(content, defaultContentType, resp);
}



public void handleResponse(HttpServletRequest req, HttpServletResponse resp, ControlComponent controlComponent) {

	if (controlComponent.matches()) {
		String result = controlComponent.process();
		writeTo(result, controlComponent.contentType(), resp);
	} else {
		log.error("GenericHttpServlet::handleeEsponse {} NOT FOUND (not matched)", req.getPathInfo());
		resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}

}


protected Map<String, String> normaliseParams(Map<String, String[]> parameterMap) {
	return parameterMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()[0]));
}


public static Map<String, String> removeInternalHeaders(Map<String, String> params) {
	return params.entrySet()
					.stream()
					.filter(k -> !k.getKey().startsWith(INTERNAL_PARAM_PREFIX))
					.collect(Collectors.toMap(Map.Entry::getKey,  Map.Entry::getValue));
}


public ControlComponent generateGetControlComponent(HttpServletRequest req, String pathInfo) {

	String path = pathInfo;
	log.trace("GenericHttpServlet::doGet {}", path);

	Map<String, String> params = normaliseParams(req.getParameterMap());
	params.put(METHOD, req.getMethod());
	params = processParams(params);

	ControlComponent controlComponent = getControl(path, params);

	return controlComponent;

}


public ControlComponent generatePostControlComponent(HttpServletRequest req, String pathInfo) {

	String path = pathInfo;
	log.trace("::doPost {}", path);

	Map<String, String> params = normaliseParams(req.getParameterMap());
	params.put(METHOD, req.getMethod());
	String content = "";
	try {
		content = IOUtils.toString(req.getInputStream(), Config.DEFAULT_CHARSET);
		log.trace("::doPost() size of input {}", content.length());
	} catch (IOException e) {
		log.error("Could not read input stream in POST servlet code, using empty input", e);
	}
	params.put(POST_VALUE, content);
	if (req.getContentType().equalsIgnoreCase(URLENCODED))	{
		try {
			// FIXME: this is an ugly hack, do we have to write our own parser?
			log.trace("About to parse the content as variables");
			URI tmpURI = DaggerURIComponent.builder().from("http://localhost/?"+content).build().uri().get();
			List<NameValuePair> contentAsVars = URLEncodedUtils.parse(tmpURI, Config.DEFAULT_NIO_CHARSET);
			log.trace("::doPost() number of vars in input {}", contentAsVars.size());
			for (NameValuePair v : contentAsVars) {
				params.put(v.getName(), v.getValue());
			}
		} catch (Exception e) {
			log.warn("Could not read input stream as variables in POST servlet code, no variables added", e);
		}
	}
	params = processParams(params);
	log.trace("POST param keys:", params.keySet().toString());

	ControlComponent controlComponent = postControl(path, params);

	return controlComponent;

}


public static Optional<Properties> getConfigurationFromContext(ServletContext context) {
	return Optional.ofNullable((Properties)context.getAttribute(__CONFIG));
}


private void addConfigurationToContext() {
	context.setAttribute(__CONFIG, configuration);
}


}
