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

package cat.calidos.morfeu.control;

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.injection.DaggerMorfeuControlComponent;
import cat.calidos.morfeu.webapp.GenericHttpServlet;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import cat.calidos.morfeu.webapp.injection.DaggerControlComponent;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MorfeuServlet extends GenericHttpServlet {

protected final static Logger log = LoggerFactory.getLogger(MorfeuServlet.class);

public final static String RESOURCES_PREFIX = "__RESOURCES_PREFIX";
protected static final String DEFAULT_RESOURCES_PREFIX = "http://localhost:8080/morfeu/";
protected String resourcesPrefix;


@Override
public void init(ServletConfig config) throws ServletException {

	super.init(config);
	
	// the hierarchy is as follows:
	// 1) Read servlet configuration
	// 2) Add and override with java system properties
	// 3) Finally add an override with environment variables 
	log.trace("Servlet config:"+configuration);
	resourcesPrefix = configuration.getProperty(RESOURCES_PREFIX);
	// FIXME: this is being ignored so we hardcode
	if (resourcesPrefix==null) {
		log.info("Not getting anything on RESOURCES_PREFIX, setting default '"+DEFAULT_RESOURCES_PREFIX+"'");
		configuration.put(RESOURCES_PREFIX, DEFAULT_RESOURCES_PREFIX);
		resourcesPrefix = configuration.getProperty(RESOURCES_PREFIX);
	}
	log.info("Final RESOURCES_PREFIX='{}'", resourcesPrefix);
	if (!resourcesPrefix.endsWith("/")) {
		log.warn("*** Used resources prefix does not end with '/', may have issues fetching content ***");
	}

}




public ControlComponent getControl(String path, Map<String, String> params) {
	return DaggerMorfeuControlComponent.builder()
										.withPath(path)
										.method(DaggerControlComponent.GET)
										.withParams(params)
										.build();
}


public ControlComponent putControl(String path, Map<String, String> params) {
	return DaggerMorfeuControlComponent.builder()
										.withPath(path)
										.method(DaggerControlComponent.POST)
										.withParams(params)
										.build();
}



protected Map<String, String> processParams(Map<String, String> params) {

	params.put(RESOURCES_PREFIX, resourcesPrefix);
	
	return params;
}



}
