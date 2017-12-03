/*
 *    Copyright 2016 Daniel Giribet
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

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.webapp.di.DaggerServletConfigComponent;


/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
public class MorfeuServlet extends HttpServlet {

protected final static Logger log = LoggerFactory.getLogger(MorfeuServlet.class);
protected final static String RESOURCES_PREFIX = "RESOURCES_PREFIX";
protected static final String DEFAULT_RESOURCE_PREFIX = "";

protected Properties configuration;
protected String resourcesPrefix;


/* (non-Javadoc)
* @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
*//////////////////////////////////////////////////////////////////////////////
@Override
public void init(ServletConfig config) throws ServletException {
	
	super.init(config);

	//TODO: check if there is a more dagger friendly way of doing this
	Properties p = DaggerServletConfigComponent.builder()
		.servletConfig(this.getServletConfig())
		.build()
		.getProperties();
	System.err.println("Servlet config:"+p);
	log.info("Configured RESOURCES_PREFIX='{}'", p.getProperty(RESOURCES_PREFIX));
	Map<String, String> env = System.getenv();
	System.err.println("ENV:"+env);
	log.info("Environment RESOURCES_PREFIX='{}'", env.get(RESOURCES_PREFIX));		
	p.putAll(env);
	log.info("After override RESOURCES_PREFIX='{}'", p.getProperty(RESOURCES_PREFIX));
	// FIXME: this is being ignored so we hardcode
	p.put(RESOURCES_PREFIX, "http://localhost:8080/morfeu/");
	log.info("Final RESOURCES_PREFIX='{}'", p.getProperty(RESOURCES_PREFIX));
	configuration = p;
	
	resourcesPrefix = configuration.getProperty(RESOURCES_PREFIX);
	resourcesPrefix = (resourcesPrefix!=null) ? resourcesPrefix : DEFAULT_RESOURCE_PREFIX; 

	if (!resourcesPrefix.endsWith("/")) {
		log.warn("Used resources prefix does not end with '/', may have issues fetching content");
	}
	
}


protected String normalisedPathFrom(HttpServletRequest req) {

	//String path = req.getPathTranslated();
	String path = req.getPathInfo();	// internal model paths are not started with a slash (see tests)
	if (path.startsWith("/")) {
		path = path.substring(1);
	}

	return path;
}


protected void writeTo(String content, HttpServletResponse resp) throws IOException {

	PrintWriter out = resp.getWriter();
	out.print(content);
	out.close();

}

}
