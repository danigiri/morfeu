// GENERIC ASYNC HTTP SERVLET . JAVA

package cat.calidos.morfeu.webapp;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class GenericAsyncHttpServlet extends GenericHttpServlet {

protected final static Logger log = LoggerFactory.getLogger(GenericAsyncHttpServlet.class);

private static final String	ORIGINAL_PATH_INFO	= "ORIGINAL_PATH_INFO";	// async handling modifies
																		// the path info
private static final int	DEFAULT_TIMEOUT		= 5000;
public static final String	ASYNC_CONTEXT_QUEUE	= "ASYNC_CONTEXT_QUEUE";
public static final String	ASYNC_TIMEOUT		= "__ASYNC_TIMEOUT";

private LinkedBlockingQueue<AsyncContext>	queue;
private int									timeout;

@Override @SuppressWarnings("unchecked")
public void init(ServletConfig config) throws ServletException {

	super.init(config);

	queue = (LinkedBlockingQueue<AsyncContext>) context.getAttribute(ASYNC_CONTEXT_QUEUE);
	String timeoutStr = configuration.getProperty(ASYNC_TIMEOUT);
	try {
		timeout = Integer.parseInt(timeoutStr);
	} catch (Exception e) {
		log
				.warn(
						"Invalid timeout configuration ({} is not an integer), using {}",
						timeoutStr,
						DEFAULT_TIMEOUT);
		timeout = DEFAULT_TIMEOUT;
	}
	log.info("Using async servlet request handler timeout {}", timeout);

	final ExecutorService executorService = ListeningExecutorServiceModule.executor;
	log.info("Starting async handler in executor");
	executorService.execute(new AsyncRequestHandler(executorService, log));

}


@Override
protected void doGet(	HttpServletRequest req,
						HttpServletResponse resp)
		throws ServletException, IOException {

	AsyncContext asyncCtx = req.startAsync(req, resp);
	asyncCtx.setTimeout(timeout);
	// async context handling modifies the path info, and adds back the servlet prefix, which makes
	// matching to fail, this is weird behaviour so we store the original path info for retrieval
	// later
	req.setAttribute(ORIGINAL_PATH_INFO, req.getPathInfo());
	queue.add(asyncCtx);

}


@Override
protected void doPost(	HttpServletRequest req,
						HttpServletResponse resp)
		throws ServletException, IOException {
	doGet(req, resp);
}

private final class AsyncRequestHandler implements Runnable {

private final ExecutorService	executorService;
private final Logger			log;

private AsyncRequestHandler(ExecutorService executorService, Logger log) {

	this.executorService = executorService;
	this.log = log;

}


public void run() {
	while (true) {
		try {
			final AsyncContext asyncCtx = queue.take();
			executorService.execute(new Runnable() {

			public void run() {
				HttpServletRequest req = (HttpServletRequest) asyncCtx.getRequest();
				HttpServletResponse resp = (HttpServletResponse) asyncCtx.getResponse();

				// find out if we are get or post here to call the appropriate handler
				String method = req.getMethod();
				// we retrieve the original path info that async context has mangled and use that
				// instead
				String pathInfo = (String) req.getAttribute(ORIGINAL_PATH_INFO);
				ControlComponent controlComponent;
				log.trace("Handling async request {} ({})", pathInfo, req.getMethod());
				if (method.equalsIgnoreCase("POST")) {
					controlComponent = generatePostControlComponent(req, pathInfo);
				} else {
					controlComponent = generateGetControlComponent(req, pathInfo);
				}
				handleResponse(req, resp, controlComponent);
				asyncCtx.complete();
			}
			});

		} catch (InterruptedException e) {
			log.error("Runnabler servlet async handler interrupted {}", e.getMessage());
		}

	}
}

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
