package cat.calidos.morfeu.webapp.injection;

import java.net.URI;
import java.util.Properties;
import java.util.function.BiFunction;

import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;
import dagger.producers.Producer;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import cat.calidos.morfeu.problems.MorfeuRuntimeException;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.injection.DaggerSaverComponent;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;
import cat.calidos.morfeu.webapp.GenericHttpServlet;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class POSTFileSaverModule {

protected final static Logger log = LoggerFactory.getLogger(POSTFileSaverModule.class);

private static final String _POST = "POST";


@Produces @IntoMap @Named("PreFilters")
@IntKey(1000)
public static BiFunction<HttpServletRequest, HttpServletResponse, Boolean> postSaver(
															@Named("POSTFileSaverModule-POSTUri")Producer<String> uri) {


	return (request, response) -> {
		log.trace("------ Request filter request ({} {}) ------", request.getMethod(), request.getServletPath());

		if (!request.getMethod().equals(_POST)) {
			return true;	// continue filter chain
		}

		ServletOutputStream outputStream = null;
		try {

			URI destination = DaggerURIComponent.builder().from(uri.get().get()).build().uri().get();
			String content = request.getParameter("content");
			if (content==null) {
				content = request.getParameter(GenericHttpServlet.POST_VALUE);
			}
			//System.err.println(content);
			// we assume Morfeu is doing the validation for now
			DaggerSaverComponent.builder().toURI(destination).content(content).build().saver().get().save();

			// now we give a response back
			response.setStatus(HttpServletResponse.SC_OK);
			outputStream = response.getOutputStream();
			IOUtils.write("{\n" + 					//TODO: move this to an injectable parameter
					"	\"result\": \"OK\"\n" + 
					"	,\"target\": \""+destination+"\"\n" + 
					"	,\"operation\": \"FileSaver\"\n" + 
					"	,\"operationTime\": 1\n" + 
					"}\n" + 
					"", outputStream, Config.DEFAULT_CHARSET);
			outputStream.close();

		} catch (Exception e) {
			log.error("Had some kind of issue in the POST request filter '{}'", e.getMessage());
			throw new MorfeuRuntimeException("Could not save POST content", e);
		}

		return false;	// stop filter chain???

	};

}


@Produces @Named("POSTFileSaverModule-POSTUri")
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

