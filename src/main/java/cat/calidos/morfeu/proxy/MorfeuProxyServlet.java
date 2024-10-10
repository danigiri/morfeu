package cat.calidos.morfeu.proxy;

import java.net.URI;
import java.util.Properties;

import org.apache.http.client.utils.URIUtils;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.utils.injection.DaggerConfigPropertyComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import cat.calidos.morfeu.webapp.injection.DaggerServletConfigComponent;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MorfeuProxyServlet extends ProxyServlet {

private static final String TEMPLATE_SNIPPET = "${";

private static final long serialVersionUID = 9192081928916080216L;

public final static String __PROXY_PREFIX = "__PROXY_PREFIX";

protected final static Logger log = LoggerFactory.getLogger(MorfeuProxyServlet.class);

@Override
protected void initTarget() throws ServletException {

	try {
		super.initTarget();
	} catch (Exception e) {
		log.warn("Proxy init failed, we try to see if we can use variable substitution");
	}

	targetUri = getConfigParam(P_TARGET_URI);
	targetUri = MorfeuProxyServlet.getFinalTargetURI(targetUri, this.getServletConfig());

	try {
		targetUriObj = new URI(targetUri);
	} catch (Exception e2) {
		throw new ServletException("Trying to process targetUri init parameter: " + e2);
	}
	targetHost = URIUtils.extractHost(targetUriObj);

}


public static String getFinalTargetURI(	String targetUri,
										ServletConfig config)
		throws ServletException {
	targetUri = DaggerConfigPropertyComponent.builder()
			.forName(P_TARGET_URI)
			.allowEmpty(false)
			.andDefault(targetUri)
			.build()
			.value()
			.get();
	log.info("** Configured {} for proxy target uri", targetUri);
	if (targetUri == null) {
		throw new ServletException(P_TARGET_URI + " is required.");
	}

	if (targetUri.contains(TEMPLATE_SNIPPET)) {
		// template snippet in the param, so we populate the view with the servlet config, system
		// vars and env which
		// can be used in the param to provide advanced features (like variable substitution)
		Properties configuration = DaggerServletConfigComponent.builder()
				.with(config)
				.build()
				.getProperties();
		log.trace("(From configuration) __PROXY_PREFIX='{}'",
				configuration.getProperty(__PROXY_PREFIX, "not set!"));
		targetUri = DaggerViewComponent.builder()
				.withValues(configuration)
				.withTemplate(targetUri)
				.build()
				.render();
		log.info("** Using template-based {} for proxy target uri", targetUri);
	}
	return targetUri;
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
