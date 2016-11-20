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

package cat.calidos.partikle.webapp;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;

import cat.calidos.partikle.webapp.di.DaggerResourcesComponent;
import cat.calidos.partikle.webapp.di.DaggerServletConfigComponent;
import cat.calidos.partikle.webapp.di.ResourcesComponent;
import cat.calidos.partikle.webapp.di.ResourcesModule;
import cat.calidos.partikle.webapp.di.ServletConfigComponent;
import cat.calidos.partikle.webapp.di.ServletConfigModule;


/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
public class ResourcesServlet extends HttpServlet {


	@Inject
	Properties configuration;

	/* (non-Javadoc)
	* @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	*//////////////////////////////////////////////////////////////////////////////
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// TODO: refactor with a more Dagger2 friendly and less boilerplate
		ServletConfigComponent servletComponent = DaggerServletConfigComponent.builder()
			.servletConfigModule(new ServletConfigModule(config))
			.build();
		Properties p = servletComponent.getProperties();

		ResourcesComponent resourcesComponent = DaggerResourcesComponent.builder()
		.resourcesModule(new ResourcesModule(p.getProperty("")))
		.build();
		HttpClient httpClient = resourcesComponent.getHttpClient();
		
	}




	/* (non-Javadoc)
	* @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*//////////////////////////////////////////////////////////////////////////////
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}



}
