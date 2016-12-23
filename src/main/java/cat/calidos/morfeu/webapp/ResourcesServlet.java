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

import cat.calidos.morfeu.webapp.di.DaggerServletConfigComponent;


/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
public class ResourcesServlet extends HttpServlet {

	// TODO: doublecheck visibility is needed (it probably is)
	@Inject
	public Properties configuration;

	/* (non-Javadoc)
	* @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	*//////////////////////////////////////////////////////////////////////////////
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		//TODO: think of refactoring to a servlet module?
		Properties p = DaggerServletConfigComponent.builder()
			.servletConfig(this.getServletConfig())
			.build()
			.getProperties();

		//TODO: refactor into more dagger2 friendliness
//		RemoteResourcesComponent resourcesComponent = DaggerRemoteResourcesComponent.builder()
//		.remoteResourcesModule(new RemoteResourcesModule(p.getProperty("")))
//		.build();
//		HttpClient httpClient = resourcesComponent.getHttpClient();
		
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
