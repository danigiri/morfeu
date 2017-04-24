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


	protected Properties configuration;

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
		p.putAll(System.getenv());
		// FIXME:
		p.put(RESOURCES_PREFIX, "http://localhost:8080/morfeu");
		System.err.println(p);
		log.info("Using RESOURCE_PREFIX='{}'", p.getProperty(RESOURCES_PREFIX));
		configuration = p;

	}

}
