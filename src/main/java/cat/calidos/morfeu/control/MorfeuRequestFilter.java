package cat.calidos.morfeu.control;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.webapp.injection.DaggerHttpFilterComponent;
import cat.calidos.morfeu.webapp.injection.HttpFilterComponent;

/** Placeholder for request filter if we need it
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MorfeuRequestFilter implements Filter {

protected final static Logger log = LoggerFactory.getLogger(MorfeuRequestFilter.class);


@Override
public void init(FilterConfig filterConfig) throws ServletException {

	log.info("------ Request filter initialized ({}) ------", filterConfig.getFilterName());

}


@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

	log.trace("---- REQUEST {} ----", request);

	HttpServletRequest httpRequest = (HttpServletRequest)request;
	HttpServletResponse httpResponse = (HttpServletResponse) response;
	try {
		HttpFilterComponent filterComponent = filterComponent(chain, httpRequest, httpResponse);
		filterComponent.process().get();
	} catch (Exception e) {
		log.error(e.getMessage());
	}

}


@Override
public void destroy() {}


protected HttpFilterComponent filterComponent(FilterChain chain, HttpServletRequest httpRequest,
		HttpServletResponse httpResponse) {
	return DaggerHttpFilterComponent.builder()
										.request(httpRequest)
										.response(httpResponse)
										.chain(chain)
										.build();
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

