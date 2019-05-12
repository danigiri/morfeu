// GENERIC ASYNC HTTP SERVLET . JAVA

package cat.calidos.morfeu.webapp;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.MorfeuServletListener;
import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class GenericAsyncHttpServlet extends GenericHttpServlet {


protected final static Logger log = LoggerFactory.getLogger(GenericAsyncHttpServlet.class);

private static final int DEFAULT_TIMEOUT = 5000;

private LinkedBlockingQueue<AsyncContext> queue;
private int timeout;

@Override @SuppressWarnings("unchecked")
public void init(ServletConfig config) throws ServletException {

	super.init(config);
	
	queue = (LinkedBlockingQueue<AsyncContext>)context.getAttribute(MorfeuServletListener.ASYNC_CONTEXT_QUEUE);
	String timeoutStr = (String)context.getAttribute(MorfeuServletListener.ASYNC_TIMEOUT);

	try {
		this.timeout = Integer.parseInt(timeoutStr);
	} catch (Exception e) {
		log.warn("Invalid timeout configuration ({} is not an integer), using {}", timeoutStr, DEFAULT_TIMEOUT);
		this.timeout = DEFAULT_TIMEOUT;
	}

	final ExecutorService executorService = ListeningExecutorServiceModule.executor;
	executorService.execute(new Runnable() {

			public void run() {
				while (true) {
					try {
						final AsyncContext asyncCtx = queue.take();
						executorService.execute(new Runnable() {

							public void run() {
								ServletRequest req = asyncCtx.getRequest();
								HttpServletResponse resp = (HttpServletResponse)asyncCtx.getResponse();

								// find out if we are get or post here
								
								asyncCtx.complete();
							}
						});

					} catch (InterruptedException e) {
					}

				}
			}
		});
	
}

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	AsyncContext asyncCtx = req.startAsync(req, resp); 
	asyncCtx.setTimeout(timeout);
	queue.add(asyncCtx);
}

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	doGet(req, resp);
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

