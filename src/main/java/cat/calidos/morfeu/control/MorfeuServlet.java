// MORFEU SERVLET . JAVA

package cat.calidos.morfeu.control;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.injection.DaggerMorfeuControlComponent;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.injection.DaggerConfigPropertyComponent;
import cat.calidos.morfeu.webapp.GenericHttpServlet;
import cat.calidos.morfeu.webapp.injection.ControlComponent;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;


/**
 * Generic morfeu filter, will load the configuration and invoke the controller with the request
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MorfeuServlet extends GenericHttpServlet {

private static final long serialVersionUID = 7499640302122151409L;

protected final static Logger log = LoggerFactory.getLogger(MorfeuServlet.class);

public final static String		RESOURCES_PREFIX			= "__RESOURCES_PREFIX";
protected static final String	DEFAULT_RESOURCES_PREFIX	= "http://localhost:8980/";
protected String				resourcesPrefix;

@Override
public void init(ServletConfig config) throws ServletException {

	super.init(config); // this will add system and env vars to the configuration

	log.trace("Servlet config:" + configuration);
	// env vars etc should be in the configuration from the super class, but this
	// will not hurt
	// TODO: we could move this to a view so we have more flexibility, like var substitution
	resourcesPrefix = DaggerConfigPropertyComponent
			.builder()
			.forName(RESOURCES_PREFIX)
			.withProps(configuration)
			.allowEmpty(false)
			.andDefault(DEFAULT_RESOURCES_PREFIX)
			.build()
			.value()
			.get();
	log.info("Final RESOURCES_PREFIX='{}'", resourcesPrefix);
	if (!resourcesPrefix.endsWith("/")) {
		log
				.warn(
						"*** Used resources prefix does not end with '/', may have issues fetching content ***");
	}

}


public ControlComponent getControl(	String path,
									Map<String, String> params) {
	return DaggerMorfeuControlComponent
			.builder()
			.withPath(path)
			.method(ControlComponent.GET)
			.withParams(params)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();
}


public ControlComponent postControl(String path,
									Map<String, String> params) {
	return DaggerMorfeuControlComponent
			.builder()
			.withPath(path)
			.method(ControlComponent.POST)
			.withParams(params)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();
}


protected Map<String, String> processParams(Map<String, String> params) {

	params.put(RESOURCES_PREFIX, resourcesPrefix);

	return params;

}

}

/*
 * Copyright 2024 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
