package cat.calidos.morfeu.proxy;

import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIUtils;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MorfeuProxyServlet extends ProxyServlet {

protected final static Logger log = LoggerFactory.getLogger(MorfeuProxyServlet.class);

private static final String __PROXY_PREFIX = "__PROXY_PREFIX";


@Override
protected void initTarget() throws ServletException {

	try {
		super.initTarget();
	} catch (Exception e) {
		log.warn("Proxy init failed, we try to see if we can use variable substitution");
	}

	targetUri = getConfigParam(P_TARGET_URI);
	if (targetUri == null) {
		throw new ServletException(P_TARGET_URI + " is required.");
	}

	int varStart = targetUri.indexOf("${");
	int varEnd = targetUri.indexOf("}");
	if (varStart>=0 && varEnd>0 && varStart<varEnd) {
		String var = targetUri.substring(varStart, varEnd);
		String varValue = getServletContext().getInitParameter(__PROXY_PREFIX);
		log.info("** __PROXY_PREFIX='{}' (from init parameter)", varValue);
		varValue = System.getProperty(__PROXY_PREFIX, varValue);
		log.info("** __PROXY_PREFIX='{}' (after system property)", varValue);
		targetUri = targetUri.substring(0, varStart)+varValue+targetUri.substring(varEnd+1);
		log.info("** Using {} for proxy target uri", targetUri);
	}

	try {
		targetUriObj = new URI(targetUri);
	} catch (Exception e2) {
		throw new ServletException("Trying to process targetUri init parameter: " + e2);
	}
	targetHost = URIUtils.extractHost(targetUriObj);

}


/*
@Override
protected void copyResponseHeaders(HttpResponse proxyResponse, HttpServletRequest servletRequest,
		HttpServletResponse servletResponse) {

	super.copyResponseHeaders(proxyResponse, servletRequest, servletResponse);

	// to help with preview if needed
	servletResponse.addHeader("X-Content-Type-Options", "nosniff");

}

*/


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

