package cat.calidos.morfeu.control.injection;

import java.net.URI;
import java.util.Properties;

import javax.inject.Provider;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.Saver;
import cat.calidos.morfeu.utils.injection.DaggerSaverComponent;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;
import cat.calidos.morfeu.webapp.GenericHttpServlet;
import dagger.Module;
import dagger.Provides;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class MorfeuPOSTFilterModule {

protected final static Logger log = LoggerFactory.getLogger(MorfeuPOSTFilterModule.class);

private static final String _POST = "POST";


@Provides
public static boolean handle(HttpServletRequest request, Provider<HttpServletResponse> response, Provider<String> uri) {

	log.trace("------ Request filter request ({} {}) ------", request.getMethod(), request.getServletPath());

	if (!request.getMethod().equals(_POST)) {
		return false;
	}

	ServletOutputStream outputStream = null;
	try {
		URI destination = DaggerURIComponent.builder().from(uri.get()).builder().uri().get();
		String content = request.getParameter("content");
		System.err.println(content);
		// we assume Morfeu is doing the validation for now
		Saver saver = DaggerSaverComponent.builder()
											.toURI(destination)
											.content(content)
											.build()
											.saver()
											.get();
		saver.save();
		// now we give a response back
		HttpServletResponse res = response.get();
		res.setStatus(HttpServletResponse.SC_OK);
		outputStream = res.getOutputStream();
		IOUtils.write("{\n" + 					//TODO: move this to a template parameter
				"	\"result\": \"OK\"\n" + 
				"	,\"target\": \""+destination+"\"\n" + 
				"	,\"operation\": \"FileSaver\"\n" + 
				"	,\"operationTime\": 1\n" + 
				"}\n" + 
				"", outputStream, Config.DEFAULT_CHARSET);
		outputStream.close();

	} catch (Exception e) {
		log.error("Had some kind of issue in the POST request filter '{}'", e.getMessage());
		e.printStackTrace();
	}

	return true;

}


@Provides
public static HttpServletRequest request(ServletRequest request) {
	return (HttpServletRequest) request;
}


@Provides
public static HttpServletResponse response(ServletResponse response) {
	return (HttpServletResponse) response;
}


@Provides
public static String uri(HttpServletRequest request) {

	ServletContext ctxt = request.getServletContext();
	String prefix = ctxt.getInitParameter("__RESOURCES_PREFIX");
	prefix = (String)((Properties)ctxt.getAttribute(GenericHttpServlet.__CONFIG)).get("__RESOURCES_PREFIX");
	String uri = prefix+request.getServletPath();

	return uri;

}


}

/*
 *    Copyright 2019 Daniel Giribet
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

